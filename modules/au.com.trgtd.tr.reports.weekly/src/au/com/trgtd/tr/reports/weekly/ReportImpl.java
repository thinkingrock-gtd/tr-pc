/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.reports.weekly;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import net.sf.jasperreports.view.JasperViewer;
import org.w3c.dom.Document;
import au.com.trgtd.tr.extract.ExtractActions;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamsDialog;
import tr.model.Data;
import au.com.trgtd.tr.util.DateUtils;
import tr.extract.reports.PaperSize;

/**
 * Extract implementation.
 * 
 * @author Jeremy Moore
 */
public class ReportImpl extends au.com.trgtd.tr.extract.Extract {

    private static final String ID = "tr.reports.weekly";
    private ParamList paramPaper;
    private ParamList paramStart;
    private ParamBoolean paramSuccess;
    private ParamBoolean paramCriteria;
    private ParamBoolean paramProject;
    private ParamBoolean paramNotes;
    private ParamBoolean paramOverdue;

    /** Overridden to return the report ID. */
    public String getID() {
        return ID;
    }

    /** Overridden to return the report name. */
    public String getName() {
        return getString("CTL_ReportAction");
    }

    /** Overridden to return report parameters. */
    public List<Param> getParams() {
        List<Param> params = new Vector<Param>();
        List<Item> formatItems = new Vector<Item>();
        formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
        formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
        paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
        params.add(paramPaper);
        List<Item> dateItems = new Vector<Item>();
        dateItems.add(new Item(getString("today"), "today"));
        dateItems.add(new Item(getString("tomorrow"), "tomorrow"));
        paramStart = new ParamList("paramStart", getString("param-start"), dateItems);
        params.add(paramStart);
        paramOverdue = new ParamBoolean("paramOverdue", getString("param-overdue"));
        params.add(paramOverdue);
        paramCriteria = new ParamBoolean("paramCriteria", getString("param-criteria"));
        params.add(paramCriteria);
        paramSuccess = new ParamBoolean("paramSuccess", getString("param-success"));
        params.add(paramSuccess);
        paramProject = new ParamBoolean("paramProject", getString("param-project"));
        params.add(paramProject);
        paramNotes = new ParamBoolean("paramNotes", getString("param-notes"));
        params.add(paramNotes);
        return params;
    }

    /** Overriden to process the report. */
    public void process(Data data) throws Exception {
        String title = getDialogTitleReport(getName());
        ParamsDialog dlg = new ParamsDialog(title, getID(), getParams());
        if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
            return;
        }
        Map<String, Object> rparams = new HashMap<String, Object>();
                
        boolean today = paramStart.getValue().equals("today");
        Date todayDate = today ? getDate(0) : getDate(1);
        Date weekStartDate = today ? getDate(1) : getDate(2);
        Date weekEndDate = today ? getDate(7) : getDate(8);      
        rparams.put("paramDate", todayDate);                    
        rparams.put("paramTodayStart", getStart(todayDate));            
        rparams.put("paramTodayEnd", getEnd(todayDate));            
        rparams.put("paramWeekStart", getStart(weekStartDate));            
        rparams.put("paramWeekEnd", getEnd(weekEndDate));                    
        rparams.put("paramOverdue", Boolean.parseBoolean(paramOverdue.getValue()));
        rparams.put("paramCriteria", Boolean.parseBoolean(paramCriteria.getValue()));        
        rparams.put("paramSuccess", Boolean.parseBoolean(paramSuccess.getValue()));
        rparams.put("paramProject", Boolean.parseBoolean(paramProject.getValue()));
        rparams.put("paramNotes", Boolean.parseBoolean(paramNotes.getValue()));
        File resourcePath = Resources.FILE_RPT_WEEKLY.getParentFile();
        rparams.put("SUBREPORT_DIR", resourcePath.getPath() + File.separator);

        File xmlfile = getTmpFile("Actions.xml");
        ExtractActions.process(data, xmlfile);

        Document document = JRXmlUtils.parse(JRLoader.getURLInputStream(xmlfile.toURI().toString()));
        rparams.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);

        PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;        
        File rptfile = (paper == PaperSize.Letter) ? Resources.FILE_RPT_WEEKLY_LTR : Resources.FILE_RPT_WEEKLY;
        InputStream reportStream = new FileInputStream(rptfile);

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams);

        JasperViewer.viewReport(jasperPrint, false);
    }
    
    private Long getStart(Date date) {
        return DateUtils.getStart(date).getTime();
    }

    private Long getEnd(Date date) {
        return DateUtils.getEnd(date).getTime();
    }
    
    private Date getDate(int addDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, addDays);
        return calendar.getTime();
    }

}

/*
 * ThinkingRock, a project management tool for Personal Computers. 
 * Copyright (C) 2006 Avente Pty Ltd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
        List<Param> params = new Vector<>();
        List<Item> formatItems = new Vector<>();
        formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
        formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
        paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
        params.add(paramPaper);
        List<Item> dateItems = new Vector<>();
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
        Map<String, Object> rparams = new HashMap<>();
                
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

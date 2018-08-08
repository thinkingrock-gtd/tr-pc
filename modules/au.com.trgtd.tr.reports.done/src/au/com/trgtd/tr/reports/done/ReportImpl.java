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
package au.com.trgtd.tr.reports.done;

import au.com.trgtd.tr.appl.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
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
import au.com.trgtd.tr.extract.ParamContext;
import au.com.trgtd.tr.extract.ParamDateList;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamTopic;
import au.com.trgtd.tr.extract.ParamsDialog;
import tr.model.Data;
import au.com.trgtd.tr.swing.date.combo.DateItem;
import au.com.trgtd.tr.util.DateUtils;
import tr.extract.reports.PaperSize;

/**
 * Extract implementation.
 * 
 * @author Jeremy Moore
 */
public class ReportImpl extends au.com.trgtd.tr.extract.Extract {

    private static final String ID = "tr.reports.actions.done";
    private static final DateFormat DF = Constants.DATE_FORMAT_FIXED;
    private ParamList paramPaper;
    private ParamDateList paramDateFrom;
    private ParamDateList paramDateUpTo;
    private ParamBoolean paramSuccess;
    private ParamBoolean paramCriteria;
    private ParamBoolean paramProject;
    private ParamBoolean paramNotes;
    private ParamTopic paramTopic;
    private ParamContext paramContext;

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
        paramTopic = new ParamTopic("paramTopic", getString("param-topic"), FormatType.XML);
        params.add(paramTopic);
        paramContext = new ParamContext("paramContext", getString("param-context"), FormatType.XML);
        params.add(paramContext);
        List<DateItem> fromDateItems = new Vector<DateItem>();
        fromDateItems.add(DateItem.DATE_CHOOSER);
        fromDateItems.add(DateItem.TOMORROW);
        fromDateItems.add(DateItem.TODAY);
        fromDateItems.add(DateItem.YESTERDAY);
        fromDateItems.add(DateItem.WEEKS_AGO_1);
        fromDateItems.add(DateItem.WEEKS_AGO_2);
        fromDateItems.add(DateItem.WEEKS_AGO_3);
        fromDateItems.add(DateItem.WEEKS_AGO_4);
        fromDateItems.add(DateItem.EARLIEST);
        paramDateFrom = new ParamDateList("from", getString("param-from"), fromDateItems);
        params.add(paramDateFrom);
        List<DateItem> toDateItems = new Vector<DateItem>();
        toDateItems.add(DateItem.DATE_CHOOSER);
        toDateItems.add(DateItem.YESTERDAY);
        toDateItems.add(DateItem.TODAY);
        toDateItems.add(DateItem.TOMORROW);
        toDateItems.add(DateItem.WEEKS_1);
        toDateItems.add(DateItem.WEEKS_2);
        toDateItems.add(DateItem.WEEKS_3);
        toDateItems.add(DateItem.WEEKS_4);
        toDateItems.add(DateItem.LATEST);
        paramDateUpTo = new ParamDateList("to", getString("param-to"), toDateItems);
        params.add(paramDateUpTo);
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
        
        Date dateFrom = null;
        String dateFromStr = paramDateFrom.getValue();
        if (dateFromStr == null) {
            dateFrom = new Date(DateItem.EARLIEST.value);
        } else {
            long n = Long.parseLong(paramDateFrom.getValue());
            if (n == DateItem.EARLIEST.value) {
                dateFrom = DateItem.EARLIEST.getDate();
            } else if (n == DateItem.LATEST.value) {
                dateFrom = DateItem.LATEST.getDate();
            } else if (n < 1000) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_YEAR, (int) n);
                dateFrom = DateUtils.getStart(c.getTime());
            } else {
                dateFrom = DateUtils.getStart(new Date(n));
            }
        }
        Date dateUpTo = null;
        String dateUpToStr = paramDateUpTo.getValue();
        if (dateUpToStr == null) {
            dateUpTo = new Date(DateItem.LATEST.value);
        } else {
            long n = Long.parseLong(paramDateUpTo.getValue());
            if (n == DateItem.EARLIEST.value) {
                dateUpTo = DateItem.EARLIEST.getDate();
            } else if (n == DateItem.LATEST.value) {
                dateUpTo = DateItem.LATEST.getDate();
            } else if (n < 1000) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_YEAR, (int) n);
                dateUpTo = DateUtils.getEnd(c.getTime());
            } else {
                dateUpTo = DateUtils.getEnd(new Date(n));
            }
        }
        if (DateUtils.isSameDay(dateFrom, dateUpTo)) {
            rparams.put("paramDateRangeText", " For: " + DF.format(dateFrom));
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("From: ");
            sb.append(dateFrom.getTime() == DateItem.EARLIEST.value ? "Earliest" : DF.format(dateFrom));
            sb.append(" To: ");
            sb.append(dateUpTo.getTime() == DateItem.LATEST.value ? "Latest" : DF.format(dateUpTo));
            rparams.put("paramDateRangeText", sb.toString());
        }
        rparams.put("paramFrom", dateFrom.getTime());
        rparams.put("paramTo", dateUpTo.getTime());
        rparams.put("paramCriteria", Boolean.parseBoolean(paramCriteria.getValue()));
        rparams.put("paramSuccess", Boolean.parseBoolean(paramSuccess.getValue()));
        rparams.put("paramProject", Boolean.parseBoolean(paramProject.getValue()));
        rparams.put("paramNotes", Boolean.parseBoolean(paramNotes.getValue()));

        File xmlfile = getTmpFile("Actions.xml");
        ExtractActions.process(data, xmlfile, paramTopic.getValue(), paramContext.getValue());

        Document document = JRXmlUtils.parse(JRLoader.getURLInputStream(xmlfile.toURI().toString()));
        rparams.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);

        File resourcePath = Resources.FILE_RPT_ACTIONS_DONE.getParentFile();
        rparams.put("SUBREPORT_DIR", resourcePath.getPath() + File.separator);

        PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
        File rptfile = (paper == PaperSize.Letter) ? Resources.FILE_RPT_ACTIONS_DONE_LTR : Resources.FILE_RPT_ACTIONS_DONE;
        InputStream reportStream = new FileInputStream(rptfile);

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams);

        JasperViewer.viewReport(jasperPrint, false);
    }
}

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
package au.com.trgtd.tr.reports.actions.delegated;

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
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JasperViewer;
import au.com.trgtd.tr.extract.ExtractActions;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamDateList;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamsDialog;
import tr.model.Data;
import au.com.trgtd.tr.swing.date.combo.DateItem;
import au.com.trgtd.tr.util.DateUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import tr.extract.reports.PaperSize;

/**
 * Extract implementation.
 * 
 * @author Jeremy Moore
 */
public class ReportImpl extends au.com.trgtd.tr.extract.Extract {

    private static final Logger LOG = Logger.getLogger("au.com.trgtd.tr.reports");
    private static final String ID = "tr.reports.actions.delegated";
    private static final DateFormat DF = Constants.DATE_FORMAT_FIXED;
    private ParamList paramPaper;
    private ParamDateList paramDateFrom;
    private ParamDateList paramDateUpTo;
    private ParamBoolean paramGroup;
    private ParamBoolean paramSuccess;
    private ParamBoolean paramCriteria;
    private ParamBoolean paramProject;
    private ParamBoolean paramNotes;

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
        paramGroup = new ParamBoolean("group", getString("param-group"));
        params.add(paramGroup);
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

    /** Overridden to process the report. */
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
            sb.append("From: " + (dateFrom.getTime() == DateItem.EARLIEST.value ? "Earliest" : DF.format(dateFrom)));
            sb.append("  To: " + (dateUpTo.getTime() == DateItem.LATEST.value ? "Latest" : DF.format(dateUpTo)));
            rparams.put("paramDateRangeText", sb.toString());
        }
        rparams.put("paramFrom", dateFrom.getTime());
        rparams.put("paramTo", dateUpTo.getTime());
        rparams.put("paramCriteria", Boolean.parseBoolean(paramCriteria.getValue()));
        rparams.put("paramSuccess", Boolean.parseBoolean(paramSuccess.getValue()));
        rparams.put("paramProject", Boolean.parseBoolean(paramProject.getValue()));
        rparams.put("paramNotes", Boolean.parseBoolean(paramNotes.getValue()));

        PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;

        InputStream reportStream;
        if (paramGroup.getValue().equals("true")) {
            if (paper == PaperSize.Letter) {
                reportStream = new FileInputStream(Resources.FILE_RPT_DELEGATED_GROUPED_LTR);
            } else {
                reportStream = new FileInputStream(Resources.FILE_RPT_DELEGATED_GROUPED);
            }
        } else {
            if (paper == PaperSize.Letter) {
                reportStream = new FileInputStream(Resources.FILE_RPT_DELEGATED_LTR);
            } else {
                reportStream = new FileInputStream(Resources.FILE_RPT_DELEGATED);
            }
        }

        File xmlfile = getTmpFile("Actions.xml");
        ExtractActions.process(data, xmlfile);

        LOG.log(Level.INFO, "==============================================================");
        LOG.log(Level.INFO, "Extract file : {0}", xmlfile.getPath());
        LOG.log(Level.INFO, "paramFrom    : {0}", dateFrom.getTime());
        LOG.log(Level.INFO, "paramTo      : {0}", dateUpTo.getTime());
        LOG.log(Level.INFO, "paramCriteria: {0}", Boolean.parseBoolean(paramCriteria.getValue()));
        LOG.log(Level.INFO, "paramSuccess : {0}", Boolean.parseBoolean(paramSuccess.getValue()));
        LOG.log(Level.INFO, "paramProject : {0}", Boolean.parseBoolean(paramProject.getValue()));
        LOG.log(Level.INFO, "paramNotes   : {0}", Boolean.parseBoolean(paramNotes.getValue()));
        LOG.log(Level.INFO, "==============================================================");


        JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlfile, "/data/actions/action");
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams, xmlDataSource);
        JasperViewer.viewReport(jasperPrint, false);
    }
}

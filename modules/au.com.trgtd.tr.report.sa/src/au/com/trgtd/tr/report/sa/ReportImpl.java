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
package au.com.trgtd.tr.report.sa;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JasperViewer;
import au.com.trgtd.tr.extract.ExtractSingleActions;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamContext;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamTopic;
import au.com.trgtd.tr.extract.ParamsDialog;
import tr.extract.reports.PaperSize;
import tr.model.Data;

/**
 * Extract implementation.
 * 
 * @author Jeremy Moore
 */
public class ReportImpl extends au.com.trgtd.tr.extract.Extract {

//  private static final Logger LOG = Logger.getLogger("tr.reports.sa");
    private ParamList paramPaper;
    private ParamList paramSort;
    private ParamList paramDone;
    private ParamTopic paramTopic;
    private ParamContext paramContext;
    private ParamBoolean paramThought;
    private ParamBoolean paramSuccess;
    private ParamBoolean paramCriteria;
    private ParamBoolean paramNotes;

    /** Overridden to return the report ID. */
    public String getID() {
        return "au.com.trgtd.tr.report.sa";
    }

    /** Overridden to return the report name. */
    public String getName() {
        return getString("CTL_ReportAction");
    }

    /** Overridden to return report parameters. */
    public List<Param> getParams() {
        List<Item> formatItems = new Vector<Item>();
        formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
        formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
        paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
        paramContext = new ParamContext("paramContext", getString("param-context"), FormatType.XML);
        paramTopic = new ParamTopic("paramTopic", getString("param-topic"), FormatType.XML);
        List<Item> doneItems = new Vector<Item>();
        doneItems.add(new Item(getString("param-done-all"), "all"));
        doneItems.add(new Item(getString("param-done-done"), "done"));
        doneItems.add(new Item(getString("param-done-todo"), "todo"));
        paramDone = new ParamList("paramDone", getString("param-done"), doneItems);
        List<Item> sortItems = new Vector<Item>();
        sortItems.add(new Item(getString("param-sort-none"), "none"));
        sortItems.add(new Item(getString("param-sort-descr"), "descr"));
        sortItems.add(new Item(getString("param-sort-priority"), "priority"));
        sortItems.add(new Item(getString("param-sort-date"), "date"));
        paramSort = new ParamList("paramSort", getString("param-sort"), sortItems);
        paramThought = new ParamBoolean("paramThought", getString("param-thought"));
        paramCriteria = new ParamBoolean("paramCriteria", getString("param-criteria"));
        paramSuccess = new ParamBoolean("paramSuccess", getString("param-success"));
        paramNotes = new ParamBoolean("paramNotes", getString("param-notes"));
        List<Param> params = new Vector<Param>();
        params.add(paramPaper);
        params.add(paramTopic);
        params.add(paramContext);
        params.add(paramDone);
        params.add(paramThought);
        params.add(paramSuccess);
        params.add(paramCriteria);
        params.add(paramNotes);
        params.add(paramSort);
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
        rparams.put("paramThought", Boolean.parseBoolean(paramThought.getValue()));
        rparams.put("paramSuccess", Boolean.parseBoolean(paramSuccess.getValue()));
        rparams.put("paramCriteria", Boolean.parseBoolean(paramCriteria.getValue()));
        rparams.put("paramNotes", Boolean.parseBoolean(paramNotes.getValue()));

        PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;

        File rptfile = null;
        if (paramSort.getValue().equals("none")) {
            rptfile = (paper == PaperSize.Letter) ? Resources.FILE_REPORT_LTR : Resources.FILE_REPORT;
        } else if (paramSort.getValue().equals("descr")) {
            rptfile = (paper == PaperSize.Letter) ? Resources.FILE_REPORT_BY_DESCR_LTR : Resources.FILE_REPORT_BY_DESCR;
        } else if (paramSort.getValue().equals("priority")) {
            rptfile = (paper == PaperSize.Letter) ? Resources.FILE_REPORT_BY_PRIORITY_LTR : Resources.FILE_REPORT_BY_PRIORITY;
        } else if (paramSort.getValue().equals("date")) {
            rptfile = (paper == PaperSize.Letter) ? Resources.FILE_REPORT_BY_DATE_LTR : Resources.FILE_REPORT_BY_DATE;
        }
        InputStream reportStream = new FileInputStream(rptfile);

        File xmlfile = getTmpFile("SingleActions.xml");
        ExtractSingleActions.process(xmlfile, data, paramTopic.getValue(), paramContext.getValue(), paramDone.getValue());

        JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlfile, "/data/action");

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams, xmlDataSource);

        JasperViewer.viewReport(jasperPrint, false);
    }
}

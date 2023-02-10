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
        List<Item> formatItems = new Vector<>();
        formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
        formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
        paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
        paramContext = new ParamContext("paramContext", getString("param-context"), FormatType.XML);
        paramTopic = new ParamTopic("paramTopic", getString("param-topic"), FormatType.XML);
        List<Item> doneItems = new Vector<>();
        doneItems.add(new Item(getString("param-done-all"), "all"));
        doneItems.add(new Item(getString("param-done-done"), "done"));
        doneItems.add(new Item(getString("param-done-todo"), "todo"));
        paramDone = new ParamList("paramDone", getString("param-done"), doneItems);
        List<Item> sortItems = new Vector<>();
        sortItems.add(new Item(getString("param-sort-none"), "none"));
        sortItems.add(new Item(getString("param-sort-descr"), "descr"));
        sortItems.add(new Item(getString("param-sort-priority"), "priority"));
        sortItems.add(new Item(getString("param-sort-date"), "date"));
        paramSort = new ParamList("paramSort", getString("param-sort"), sortItems);
        paramThought = new ParamBoolean("paramThought", getString("param-thought"));
        paramCriteria = new ParamBoolean("paramCriteria", getString("param-criteria"));
        paramSuccess = new ParamBoolean("paramSuccess", getString("param-success"));
        paramNotes = new ParamBoolean("paramNotes", getString("param-notes"));
        List<Param> params = new Vector<>();
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
        Map<String, Object> rparams = new HashMap<>();
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

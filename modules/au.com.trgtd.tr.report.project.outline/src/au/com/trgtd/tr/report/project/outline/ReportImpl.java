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
package au.com.trgtd.tr.report.project.outline;

import au.com.trgtd.tr.extract.ExtractProjectOutline;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamTopic;
import au.com.trgtd.tr.extract.ParamsDialog;
import au.com.trgtd.tr.prefs.projects.ProjectsPrefs;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JasperViewer;
import tr.extract.reports.PaperSize;
import tr.model.Data;
import tr.model.project.Project;

/**
 * Extract implementation.
 *
 * @author Jeremy Moore
 */
public class ReportImpl extends au.com.trgtd.tr.extract.Extract {

//  private static final Logger LOG = Logger.getLogger("au.com.trgtd.tr.reports");

    private ParamList paramPaper;
    private ParamList paramSort;
    private ParamTopic paramTopic;
    private ParamList paramDoneTop;
    private ParamList paramDoneSub;
    private ParamBoolean paramActions;

    /**
     * Overridden to return the report ID.
     * @return The report ID.
     */
    @Override
    public String getID() {
        return "tr.reports.project.outline";
    }

    /**
     * Overridden to return the report name.
     * @return The report name.
     */
    @Override
    public String getName() {
        return getString("CTL_ReportAction");
    }

    /**
     * Overridden to return report parameters.
     * @return The report parameters.
     */
    @Override
    public List<Param> getParams() {
        List<Item> formatItems = new ArrayList<>();
        formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
        formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
        paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
        
        paramTopic = new ParamTopic("paramTopic", getString("param-topic"), FormatType.XML);
        
        List<Item> doneItems = new ArrayList<>();
        doneItems.add(new Item(getString("param-done-all"), "all"));
        doneItems.add(new Item(getString("param-done-done"), "done"));
        doneItems.add(new Item(getString("param-done-todo"), "todo"));
        paramDoneTop = new ParamList("paramDoneTop", getString("param-done-top"), doneItems);
        paramDoneSub = new ParamList("paramDoneSub", getString("param-done-sub"), doneItems);
        
        paramActions = new ParamBoolean("paramActions", getString("param-actions"));
        
        List<Item> sortItems = new ArrayList<>();
        sortItems.add(new Item(getString("param-sort-none"), "none"));
        sortItems.add(new Item(getString("param-sort-descr"), "descr"));
        sortItems.add(new Item(getString("param-sort-priority"), "priority"));
        sortItems.add(new Item(getString("param-sort-date"), "date"));
        sortItems.add(new Item(getString("param-sort-topic"), "topic"));
        paramSort = new ParamList("paramSort", getString("param-sort"), sortItems);
        
        List<Param> params = new ArrayList<>();
        params.add(paramPaper);
        params.add(paramTopic);
        params.add(paramDoneTop);
        params.add(paramDoneSub);
        params.add(paramActions);
        params.add(paramSort);
        return params;
    }

    /**
     * Overridden to process the report.
     * @param data
     * @throws java.lang.Exception
     */
    @Override
    public void process(Data data) throws Exception {
        String title = getDialogTitleReport(getName());
        ParamsDialog dlg = new ParamsDialog(title, getID(), getParams());
        if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
            return;
        }
        Map<String, Object> rparams = new HashMap<>();
        rparams.put("paramTopic", paramTopic.getValue());
        if (!paramSort.getValue().equals("none")) {
            rparams.put("paramSortText", getString("param-sort-" + paramSort.getValue()));
        }

//      InputStream reportStream = new FileInputStream(Resources.FILE_REPORT);
        PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
        File rptfile = (paper == PaperSize.Letter) ? Resources.FILE_REPORT_LTR : Resources.FILE_REPORT;
        InputStream reportStream = new FileInputStream(rptfile);

        File xmlfile = getTmpFile("ProjectOutline.xml");

        List<Project> projects = data.getRootProjects().getChildren(Project.class);

        boolean includeActions = Boolean.parseBoolean(paramActions.getValue());

        ExtractProjectOutline.process(
                xmlfile, 
                projects, 
                paramTopic.getValue(),
                paramDoneTop.getValue(), 
                paramDoneSub.getValue(),
                includeActions, 
                paramSort.getValue());

        JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlfile, "/data/item");

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams, xmlDataSource);

        JasperViewer.viewReport(jasperPrint, false);
    }

    public void process(List<Project> projects, boolean showDone) throws Exception {
        Map<String, Object> rparams = new HashMap<>();
        rparams.put("paramTopic", "all");

        PaperSize paper;
        if (ProjectsPrefs.getPageSizeChoiceName().equals("Prompt")) {
            List<Item> formatItems = new ArrayList<>();
            formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
            formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
            paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
            List<Param> params = new ArrayList<>();
            params.add(paramPaper);
            String title = getDialogTitleReport(getName());
            ParamsDialog dlg = new ParamsDialog(title, getID(), params);
            if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
                return;
            }
            paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
        } else {
            paper = ProjectsPrefs.getPageSizeChoiceName().equals("A4") ? PaperSize.A4 : PaperSize.Letter;
        }

        File rptfile = (paper == PaperSize.Letter) ? Resources.FILE_REPORT_LTR : Resources.FILE_REPORT;
        InputStream reportStream = new FileInputStream(rptfile);

        File xmlfile = getTmpFile("ProjectOutline.xml");

        String topTopic = "all";
        String topDone = "all";
        String subDone = showDone ? "all" : "todo";

        ExtractProjectOutline.process(xmlfile, projects, topTopic, topDone, subDone, true, "none");

        JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlfile, "/data/item");

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams, xmlDataSource);

        JasperViewer.viewReport(jasperPrint, false);
    }

}

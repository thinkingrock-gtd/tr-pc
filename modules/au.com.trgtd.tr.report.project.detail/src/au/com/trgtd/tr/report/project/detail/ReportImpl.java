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
package au.com.trgtd.tr.report.project.detail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JasperViewer;
import au.com.trgtd.tr.extract.ExtractProjectDetail;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamsDialog;
import au.com.trgtd.tr.prefs.projects.ProjectsPrefs;
import javax.swing.JOptionPane;
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
    
    /** Overridden to return the report ID. */
    @Override
    public String getID() {
        return "tr.reports.project.detail";
    }

    /** Overridden to return the report name. */
    @Override
    public String getName() {
        return getString("CTL_ReportAction");
    }

    /** Overridden to return report parameters. */
    @Override
    public List<Param> getParams() {        
        List<Param> params = new Vector<>();
        List<Item> formatItems = new Vector<>();
        formatItems.add(new Item(PaperSize.A4.toString(), "a4"));
        formatItems.add(new Item(PaperSize.Letter.toString(), "letter"));
        paramPaper = new ParamList("paper", PaperSize.getLabel(), formatItems);
        params.add(paramPaper);
        return params;
    }

    /** Overridden to process the report. */
    @Override
    public void process(Data data) throws Exception {
    }

    public void process(Project project, boolean showDone) throws Exception {
        PaperSize paper;
        if (ProjectsPrefs.getPageSizeChoiceName().equals("Prompt")) {
            String title = getDialogTitleReport(getName());
            ParamsDialog dlg = new ParamsDialog(title, getID(), getParams());
            if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
                return;
            }
            paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
        } else {
            paper = ProjectsPrefs.getPageSizeChoiceName().equals("A4") ? PaperSize.A4 : PaperSize.Letter;
        }

        Map<String, Object> rparams = new HashMap<>();

        File rptfile = (paper == PaperSize.Letter) ? Resources.FILE_REPORT_LTR : Resources.FILE_REPORT_A4;

        InputStream reportStream = new FileInputStream(rptfile);
        
        File xmlfile = getTmpFile("ProjectDetail.xml");

        ExtractProjectDetail.process(xmlfile, project, (showDone ? "all" : "todo"));
                            
        JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlfile, "/data/item");        
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams, xmlDataSource);

        JasperViewer.viewReport(jasperPrint, false);
    }    
    
}

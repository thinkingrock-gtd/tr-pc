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
    public String getID() {
        return "tr.reports.project.detail";
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
        return params;
    }

    /** Overridden to process the report. */
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

        Map<String, Object> rparams = new HashMap<String, Object>();

        File rptfile = (paper == PaperSize.Letter) ? Resources.FILE_REPORT_LTR : Resources.FILE_REPORT_A4;

        InputStream reportStream = new FileInputStream(rptfile);
        
        File xmlfile = getTmpFile("ProjectDetail.xml");

        ExtractProjectDetail.process(xmlfile, project, (showDone ? "all" : "todo"));
                            
        JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlfile, "/data/item");        
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams, xmlDataSource);

        JasperViewer.viewReport(jasperPrint, false);
    }    
    
}

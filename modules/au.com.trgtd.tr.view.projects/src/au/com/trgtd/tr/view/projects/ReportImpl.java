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
package au.com.trgtd.tr.view.projects;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.sf.jasperreports.engine.JasperFillManager; 
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import net.sf.jasperreports.view.JasperViewer;
import org.w3c.dom.Document;
import au.com.trgtd.tr.extract.ExtractAction;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamsDialog;
import au.com.trgtd.tr.prefs.projects.ProjectsPrefs;
import javax.swing.JOptionPane;
import tr.extract.reports.PaperSize;
import tr.model.Data;
import tr.model.action.Action;

/**
 * Extract implementation.
 * 
 * @author Jeremy Moore
 */
public class ReportImpl extends au.com.trgtd.tr.extract.Extract {

    private static final String ID = "tr.reports.action";
    private ParamList paramPaper;

    /** Overridden to return the report ID. */
    public String getID() {
        return ID;
    }

    /** Overridden to return the report name. */
    public String getName() {
        return "";
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

    /** Overriden to process the report. */
    public void process(Data data) throws Exception {        
    }
    
    
    /** Overriden to process the report. */
    public void process(Action action) throws Exception {

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

        File xmlfile = getTmpFile("Action.xml");

        ExtractAction.process(action, xmlfile);

        Document document = JRXmlUtils.parse(JRLoader.getURLInputStream(xmlfile.toURI().toString()));
        rparams.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);

        File rptfile = (paper == PaperSize.Letter) ? Resources.FILE_RPT_ACTION_LETTER : Resources.FILE_RPT_ACTION_A4;

//      InputStream reportStream = new FileInputStream(Resources.FILE_RPT_ACTION);
        InputStream reportStream = new FileInputStream(rptfile);

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams);

        JasperViewer.viewReport(jasperPrint, false);
    }
}

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
package au.com.trgtd.tr.reports.someday;

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
import au.com.trgtd.tr.extract.ExtractSomedayMaybe;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamBoolean;
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

    private ParamList paramPaper;
    private ParamTopic paramTopic;
    private ParamBoolean paramGroup;
    private ParamBoolean paramNotes;

    /** Overridden to return the report ID. */
    public String getID() {
        return "au.com.trgtd.tr.reports.someday";
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
        paramTopic = new ParamTopic("topic", getString("param-topic"), FormatType.XML);
        paramGroup = new ParamBoolean("group", getString("param-group"));
        paramNotes = new ParamBoolean("notes", getString("param-notes"));
        List<Param> params = new Vector<Param>();
        params.add(paramPaper);
        params.add(paramTopic);
        params.add(paramGroup);
        params.add(paramNotes);
        return params;
    }

    /** Overridden to process the report. */
    public void process(Data data) throws Exception {

        List<Param> extractParams = getParams();

        String title = getDialogTitleReport(getName());
        ParamsDialog dlg = new ParamsDialog(title, getID(), extractParams);
        if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
            return;
        }

        Map<String, Object> rparams = new HashMap<String, Object>();        
        rparams.put("param-notes", Boolean.parseBoolean(paramNotes.getValue()));                
        rparams.put("param-topic", paramTopic.getValue());                

        File rptfile;
        if (paramGroup.getValue().equals("true")) {
            PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
            rptfile = (paper == PaperSize.Letter) ? Resources.FILE_RPT_SOMEDAY_GROUP_LTR : Resources.FILE_RPT_SOMEDAY_GROUP_A4;
        } else {
            PaperSize paper = paramPaper.getValue().equals("letter") ? PaperSize.Letter : PaperSize.A4;
            rptfile = (paper == PaperSize.Letter) ? Resources.FILE_RPT_SOMEDAY_LTR : Resources.FILE_RPT_SOMEDAY_A4;
        }

        InputStream reportStream = new FileInputStream(rptfile);

        File xmlfile = getTmpFile("SomedayMaybe.xml");
        ExtractSomedayMaybe.process(data, xmlfile);
        
        JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlfile, "/somedays/someday");        
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, rparams, xmlDataSource);
        JasperViewer.viewReport(jasperPrint, false);
    }

}

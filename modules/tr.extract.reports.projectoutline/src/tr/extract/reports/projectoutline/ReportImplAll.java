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
package tr.extract.reports.projectoutline;

import java.io.File;
import java.net.URL;
import java.util.List;
import javax.swing.JOptionPane;
import au.com.trgtd.tr.extract.Extract.FormatType;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamFont;
import au.com.trgtd.tr.extract.ParamProject;
import au.com.trgtd.tr.extract.ParamsDialog;
import java.util.ArrayList;
import tr.model.Data;

/**
 * Run the report.
 *
 * @author Jeremy Moore
 */
public class ReportImplAll extends au.com.trgtd.tr.extract.Extract {
    
    /* Overridden to return the report ID. */
    @Override
    public String getID() {
        return "report-project-outline-all";
    }
    
    /* Overridden to return the report name. */
    @Override
    public String getName() {
        return getString("CTL_ReportActionAll");
    }
    
    /* Overridden to return the URL of the XSLT-FO report file. */
    public URL getReportURL() {
        return this.getClass().getResource("project-outline-all.fo.xml");
    }
    
    /* Overridden to return report parameters. */
    @Override
    public List<Param> getParams() {
        List<Param> params = new ArrayList<Param>(3);
        params.add(new ParamProject("project-key", getString("param-project"), getString("param-project-all"), true));
        params.add(new ParamBoolean("striked-done", getString("param-striked-done")));
        params.add(new ParamBoolean("colour", getString("param-use-colour")));
        params.add(new ParamFont("font", getString("param-font")));
        return params;
    }
    
    /* Overridden to process the report. */
    @Override
    public void process(Data data) throws Exception {
        
        List<Param> params = getParams();
        
        // show dialog for parameters
        String title = getDialogTitleReport(getName());
        ParamsDialog dlg = new ParamsDialog(title, getID(), params);
        if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
            return;
        }
        
        File xmlfile = getTmpFile("data.xml");
        extractData(data, xmlfile, FormatType.XML);
        
        URL xslfoURL = getClass().getResource("project-outline-all.fo.xml");
        File outfile = getOutFile(getID() + "-" + getTimeStamp() + ".pdf");
        transformXSLFO(xmlfile, xslfoURL, params, outfile);
        
        openFile(outfile);
    }
    
}

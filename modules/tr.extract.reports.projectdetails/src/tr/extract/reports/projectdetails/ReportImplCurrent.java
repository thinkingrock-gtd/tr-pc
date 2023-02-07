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
package tr.extract.reports.projectdetails;

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
public class ReportImplCurrent extends au.com.trgtd.tr.extract.Extract {
    
    /* Overridden to return the report ID. */
    @Override
    public String getID() {
        return "report-project-details-current";
    }
    
    /* Overridden to return the report name. */
    @Override
    public String getName() {
        return getString("CTL_ReportActionCurrent");
    }
    
    /* Overridden to return report parameters. */
    @Override
    public List<Param> getParams() {
        List<Param> params = new ArrayList<>(6);
        params.add(new ParamProject("project-key", getString("param-project"), getString("param-project-current"), false));
        params.add(new ParamBoolean("subprojects", getString("param-subprojects")));
        params.add(new ParamBoolean("include-done", getString("param-include-done")));
        params.add(new ParamBoolean("striked-done", getString("param-striked-done")));
        params.add(new ParamBoolean("show-notes", getString("param-show-notes")));
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
        
        URL xslfoURL = getClass().getResource("project-details-current.fo.xml");
        File outfile = getOutFile(getID() + "-" + getTimeStamp() + ".pdf");
        transformXSLFO(xmlfile, xslfoURL, params, outfile);
        
        openFile(outfile);
    }
    
}

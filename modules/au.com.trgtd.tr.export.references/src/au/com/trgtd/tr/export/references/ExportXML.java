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
package au.com.trgtd.tr.export.references;

import java.io.File;
import java.net.URL;
import java.util.List;
import javax.swing.JOptionPane;
import au.com.trgtd.tr.extract.Extract;
import au.com.trgtd.tr.extract.Extract.FormatType;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamsDialog;
import au.com.trgtd.tr.extract.prefs.ExtractPrefs;
import java.util.ArrayList;
import tr.model.Data;

/**
 * Export to an XML file extract implementation.
 *
 * @author Jeremy Moore
 */
public class ExportXML extends Extract {
    
    @Override
    public String getID() {
        return "information-xml";
    }
    
    @Override
    public String getName() {
        return getString("CTL_ExportXMLAction");
    }
    
    @Override
    public List<Param> getParams() {
        List<Param> params = new ArrayList<>(2);
        params.add(new ParamBoolean("include-topic", getString("param-include-topic")));
        params.add(new ParamBoolean("include-notes", getString("param-include-notes")));
        return params;
    }
    
    @Override
    public void process(Data data) throws Exception {
        
        List<Param> params = getParams();
        
        // show dialog for parameters
        String title = getDialogTitleExport(getName());
        ParamsDialog dlg = new ParamsDialog(title, getID(), params);
        if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
            return;
        }
        
        File xmlfile = getTmpFile("data.xml");
        extractData(data, xmlfile, FormatType.XML);
        
        URL xslURL = getClass().getResource("reference-xml.xsl");
        File outfile = getOutFile("reference-" + getTimeStamp() + ".xml");
        String encoding = ExtractPrefs.getEncoding();
        
        transformXSL(xmlfile, xslURL, params, outfile, encoding, true);
        
        openTextFile(outfile);
    }
    
}

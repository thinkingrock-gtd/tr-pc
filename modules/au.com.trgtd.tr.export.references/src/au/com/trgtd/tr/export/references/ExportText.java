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
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamsDialog;
import au.com.trgtd.tr.extract.prefs.ExtractPrefs;
import java.util.ArrayList;
import tr.model.Data;

/**
 * Export to a text file extract implementation.
 *
 * @author Jeremy Moore
 */
public class ExportText extends Extract {

    @Override
    public String getID() {
        return "information-text";
    }
    
    @Override
    public String getName() {
        return getString("CTL_ExportTextAction");
    }
    
    @Override
    public List<Param> getParams() {
        List<Param> params = new ArrayList<>(2);
        params.add(new ParamBoolean("include-topic", getString("param-include-topic")));
        params.add(new ParamBoolean("include-notes", getString("param-include-notes")));
        List<Item> separatorItems = new ArrayList<>(3);
        separatorItems.add(new Item(getString("comma"), "comma"));
        separatorItems.add(new Item(getString("semicolon"), "semicolon"));
        separatorItems.add(new Item(getString("tab"), "tab"));
        params.add(new ParamList("separator", getString("param-separator"), separatorItems));
        params.add(new ParamBoolean("output-headings", getString("output-headings")));
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
        
        params.add(new Param("heading-descr", getString("heading-descr")));
        params.add(new Param("heading-topic", getString("heading-topic")));
        params.add(new Param("heading-notes", getString("heading-notes")));
        
        File xmlfile = getTmpFile("data.xml");
        extractData(data, xmlfile, FormatType.CSV);
        
        URL xslURL = getClass().getResource("reference-text.xsl");
        File outfile = getOutFile("reference-" + getTimeStamp() + ".txt");
        String encoding = ExtractPrefs.getEncoding();
        
        transformXSL(xmlfile, xslURL, params, outfile, encoding, false);
        
        openTextFile(outfile);
    }
    
}

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
package au.com.trgtd.tr.export.actions;

import java.awt.Component;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.openide.windows.WindowManager;
import au.com.trgtd.tr.extract.Extract;
import au.com.trgtd.tr.extract.Extract.FormatType;
import au.com.trgtd.tr.extract.Param;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamBoolean;
import au.com.trgtd.tr.extract.ParamContext;
import au.com.trgtd.tr.extract.ParamList;
import au.com.trgtd.tr.extract.ParamTopic;
import au.com.trgtd.tr.extract.ParamsDialog;
import au.com.trgtd.tr.extract.prefs.ExtractPrefs;
import tr.model.Data;

/**
 * Export to a text file extract implementation.
 *
 * @author Jeremy Moore
 */
public class ExportText extends Extract {
    
    /** Overridden to return the extract ID. */
    public String getID() {
        return "actions-text";
    }
    
    /** Overridden to return the export name. */
    public String getName() {
        return getString("CTL_ExportTextAction");
    }
    
    /** Overridden to return report parameters. */
    public List<Param> getParams() {
        List<Param> params = new Vector<>();
        params.add(new ParamTopic("topic", getString("param-topic"), FormatType.CSV));
        params.add(new ParamContext("context", getString("param-context"), FormatType.CSV, false));
        List<Item> uptoItems = new Vector<>();
        uptoItems.add(new Item(getString("today"), "today"));
        uptoItems.add(new Item(getString("tomorrow"), "tomorrow"));
        uptoItems.add(new Item(getString("one-week"), "one-week"));
        uptoItems.add(new Item(getString("two-weeks"), "two-weeks"));
        uptoItems.add(new Item(getString("three-weeks"), "three-weeks"));
        uptoItems.add(new Item(getString("four-weeks"), "four-weeks"));        
        uptoItems.add(new Item(getString("latest"), "latest"));
        params.add(new ParamList("upto", getString("param-upto"), uptoItems));
        params.add(new ParamBoolean("include-done", getString("param-include-done")));
        params.add(new ParamBoolean("include-inactive", getString("param-include-inactive")));
        params.add(new ParamBoolean("include-doasap", getString("param-include-doasap")));
        params.add(new ParamBoolean("include-scheduled", getString("param-include-scheduled")));
        params.add(new ParamBoolean("include-delegated", getString("param-include-delegated")));
        List<Item> separatorItems = new Vector<>();
        separatorItems.add(new Item(getString("comma"), "comma"));
        separatorItems.add(new Item(getString("semicolon"), "semicolon"));
        separatorItems.add(new Item(getString("tab"), "tab"));
        params.add(new ParamList("separator", getString("param-separator"), separatorItems));
        List<Item> dateFormatItems = new Vector<>();
        dateFormatItems.add(new Item("YYYYMMDDhhmmss", "f1"));
        dateFormatItems.add(new Item("DAY DD MMM YYYY hh:mm:ss", "f2"));
        params.add(new ParamList("date-format", getString("param-date-format"), dateFormatItems));
        List<Item> fieldItems = new Vector<>();
        fieldItems.add(new Item("", "none"));
        fieldItems.add(new Item(getString("field-key"), "field-key"));
        fieldItems.add(new Item(getString("field-desc"), "field-desc"));
        fieldItems.add(new Item(getString("field-notes"), "field-notes"));
        fieldItems.add(new Item(getString("field-created"), "field-created"));
        fieldItems.add(new Item(getString("field-done"), "field-done"));
        fieldItems.add(new Item(getString("field-done-date"), "field-done-date"));
        fieldItems.add(new Item(getString("field-thought-key"), "field-thought-key"));
        fieldItems.add(new Item(getString("field-thought-desc"), "field-thought-desc"));
        fieldItems.add(new Item(getString("field-parent-key"), "field-parent-key"));
        fieldItems.add(new Item(getString("field-parent-desc"), "field-parent-desc"));
        fieldItems.add(new Item(getString("field-topic-key"), "field-topic-key"));
        fieldItems.add(new Item(getString("field-topic-desc"), "field-topic-desc"));
        fieldItems.add(new Item(getString("field-context-key"), "field-context-key"));
        fieldItems.add(new Item(getString("field-context-desc"), "field-context-desc"));
        fieldItems.add(new Item(getString("field-state"), "field-state"));
        fieldItems.add(new Item(getString("field-action-date"), "field-action-date"));
        fieldItems.add(new Item(getString("field-scheduled-datetime"), "field-scheduled-datetime"));
        fieldItems.add(new Item(getString("field-scheduled-duration"), "field-scheduled-duration"));
        fieldItems.add(new Item(getString("field-delegated-to"), "field-delegated-to"));
        fieldItems.add(new Item(getString("field-success"), "field-success"));
        fieldItems.add(new Item(getString("field-start-date"), "field-start-date"));
        fieldItems.add(new Item(getString("field-due-date"), "field-due-date"));
        fieldItems.add(new Item(getString("field-time"), "field-time"));
        fieldItems.add(new Item(getString("field-energy"), "field-energy"));
        fieldItems.add(new Item(getString("field-priority"), "field-priority"));
        fieldItems.add(new Item(getString("field-project-path"), "field-project-path"));
        params.add(new ParamList("field-1", getString("param-field-1"), fieldItems));
        params.add(new ParamList("field-2", getString("param-field-2"), fieldItems));
        params.add(new ParamList("field-3", getString("param-field-3"), fieldItems));
        params.add(new ParamList("field-4", getString("param-field-4"), fieldItems));
        params.add(new ParamList("field-5", getString("param-field-5"), fieldItems));
        params.add(new ParamList("field-6", getString("param-field-6"), fieldItems));
        params.add(new ParamList("field-7", getString("param-field-7"), fieldItems));
        params.add(new ParamList("field-8", getString("param-field-8"), fieldItems));
        params.add(new ParamList("field-9", getString("param-field-9"), fieldItems));
        params.add(new ParamList("field-10", getString("param-field-10"), fieldItems));
        params.add(new ParamList("field-11", getString("param-field-11"), fieldItems));
        params.add(new ParamList("field-12", getString("param-field-12"), fieldItems));
        params.add(new ParamList("field-13", getString("param-field-13"), fieldItems));
        params.add(new ParamList("field-14", getString("param-field-14"), fieldItems));
        params.add(new ParamList("field-15", getString("param-field-15"), fieldItems));
        params.add(new ParamList("field-16", getString("param-field-16"), fieldItems));
        params.add(new ParamList("field-17", getString("param-field-17"), fieldItems));
        params.add(new ParamList("field-18", getString("param-field-18"), fieldItems));
        params.add(new ParamList("field-19", getString("param-field-19"), fieldItems));
        params.add(new ParamList("field-20", getString("param-field-20"), fieldItems));
        
        params.add(outputHeadings);        
        
        return params;
    }
    
    private ParamBoolean outputHeadings = new ParamBoolean("output-headings", getString("output-headings")); 
    
    /** Overriden to process the report. */
    public void process(Data data) throws Exception {
        
        List<Param> params = getParams();

        // show dialog for parameters
        String title = getDialogTitleExport(getName());
        ParamsDialog dlg = new ParamsDialog(title, getID(), params);
        Component p = WindowManager.getDefault().getMainWindow();
        if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
            return;
        }
        
        List<Param> headingParams = new Vector<>();
        if (Boolean.parseBoolean(outputHeadings.getValue())) {
            for (Param param : params) {
//              if (param.id.startsWith("field") && !param.getValue().equals("")) {
                if (param.id.startsWith("field")) {
                    String value = param.getValue();
                    if (value != null && !value.equals("")) {
                        headingParams.add(new Param("h-" + param.id, getString(value)));                                            
                    }
                }
            }
        }
        params.addAll(headingParams);
        
        File xmlfile = getTmpFile("xxxdata.xml");
        extractData(data, xmlfile, FormatType.CSV);
        
        URL xslURL = getClass().getResource("actions-text.xsl");
        File outfile = getOutFile("actions-" + getTimeStamp() + ".txt");
        String encoding = ExtractPrefs.getEncoding();
        
        transformXSL(xmlfile, xslURL, params, outfile, encoding, false);
        
        openTextFile(outfile);
    }
    
}

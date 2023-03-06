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
package au.com.trgtd.tr.view.actns.screens;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Vector;
import au.com.trgtd.tr.extract.Extract;
import au.com.trgtd.tr.extract.Extract.FormatType;
import tr.model.Data;
import au.com.trgtd.tr.extract.Param;

/**
 * Actions screen report.
 *
 * @author Jeremy Moore
 */
public class ActionsPrint extends Extract {
    
    /** Context: "all" or context name. */
    public String context;
    /** Topic: "all" or topic name. */
    public String topic;
    /** Include done. */
    public boolean done;
    /** Include inactive. */
    public boolean inactive;
    /** Include do ASAP actions. */
    public boolean doasap;
    /** Include delegated actions. */
    public boolean delegated;
    /** Include scheduled actions. */
    public boolean scheduled;
    /** Up to: 0=today, 1=tomorrow, 7=one week, 14=two weeks, 21=three weeks, 28=four weeks. */
    public int upto;
    /** Available fields */
    public static enum Field {
        ACTION_ICON("icon"),
        ACTION_DONE("done"),
        ACTION_DESC("desc"),
        ACTION_DATE("action-date"),
        ACTION_DELEGATE("delegate"),
        ACTION_CONTEXT("context-name"),
        ACTION_TOPIC("topic-name"),
        FROM_ICON("from-icon"),
        FROM_DESC("from-desc"),
        FROM_TOPIC("from-topic-name"),
        CREATED_DATE("created-date"),
        DONE_DATE("done-date");
        private final String value;
        Field(String value) {
            this.value = value;
        }
        public String value() {
            return value;
        }
    }
    
    /** Field list to print. */
    public List<Field> fields;
    
    /** Field to sort on. */
    public List<Field> sortfields;
    
    /** Sort order for sort fields. */
    public List<String> sortorders;
    
    /** Overridden to get the report ID. */
    @Override
    public String getID() {
        return "actions";
    }
    
    /** Overridden to get the report name */
    @Override
    public String getName() {
        return "";
    }
    
    /** Overridden to get the report parameters. */
    @Override
    public List<Param> getParams() {
        // get the parameters
        List<Param> params = new Vector<>();
        params.add(new Param("context", context));
        params.add(new Param("topic", topic));
        params.add(new Param("done", Boolean.toString(done)));
        params.add(new Param("inactive", Boolean.toString(inactive)));
        params.add(new Param("doasap", Boolean.toString(doasap)));
        params.add(new Param("delegated", Boolean.toString(delegated)));
        params.add(new Param("scheduled", Boolean.toString(scheduled)));
        params.add(new Param("upto", Integer.toString(upto)));
////////params.add(new Param("colour", Boolean.toString(ActionsPrefs.isReportUseColour())));
        params.add(new Param("colour", "false"));
        for (int i = 0; i < fields.size(); i++) {
            Field f = fields.get(i);
            params.add(new Param("field-" + i, f.value));
        }
        for (int i = 0; i < sortfields.size(); i++) {
            Field f = sortfields.get(i);
            params.add(new Param("sort-field-" + i, f.value));
            String o = sortorders.get(i);
            params.add(new Param("sort-order-" + i, o));
        }
        return params;
    }
    
    @Override
    public void process(Data data) throws Exception {
        File xmlfile = getTmpFile("data.xml");
        URL xslfoURL = getClass().getResource("actions.fo.xml");
        File outfile = getOutFile(getID() + "-" + getTimeStamp() + ".pdf");
        
        extractData(data, xmlfile, FormatType.XML);
        transformXSLFO(xmlfile, xslfoURL, getParams(), outfile);
        openFile(outfile);
    }
    
}

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
package au.com.trgtd.tr.extract;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import java.io.File;
import java.net.URL; 
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.extract.prefs.ExtractPrefs;
import tr.model.Data;
import au.com.trgtd.tr.runtime.Open;
import au.com.trgtd.tr.util.UtilsFile;

/**
 * Abstract base class for producing exports and reports.
 *
 * @author Jeremy Moore
 */
public abstract class Extract {

    public enum FormatType {

        /** Character separated value format. */
        CSV(),
        /** XML format. */
        XML();

        public String escape(String string) {
            if (string == null) {
                return "";
            }
            if (this == XML) {
                string = string.replace("&", "&#38;");
                return StringEscapeUtils.escapeXml(string);
            }
            if (this == CSV) {
                /* Replace quotation marks (") by double quotation marks ("")
                 * and surround with quotation marks. */
//              return "\"" + string.replace("\"", "\"\"") + "\"";
                string = "\"" + string.replace("\"", "\"\"") + "\"";
                return StringEscapeUtils.escapeXml(string);
            }
            return string;
        }
    }

    /** Constructs a new instance. */
    public Extract() {
    }

    /** Override to return the extract ID. */
    public abstract String getID();

    /** Override to return the extract name. */
    public abstract String getName();

    /** Override to return extract parameters. */
    public abstract List<Param> getParams();

    /** Override to process the extract. */
    public abstract void process(Data data) throws Exception;

    /** Gets the appropriate parameter dialog title for a report. */
    public String getDialogTitleReport(String reportName) {
        return NbBundle.getMessage(Extract.class, "param-dialog-title-report", reportName);
    }

    /** Gets the appropriate parameter dialog title for an export. */
    public String getDialogTitleExport(String exportName) {
        return NbBundle.getMessage(Extract.class, "param-dialog-title-export", exportName);
    }

    /* Gets a string for the given key from the resource bundle. */
    public final String getString(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    /** Gets a new timestamp. */
    public static final String getTimeStamp() {
        return Constants.DF_TIMESTAMP.format(Calendar.getInstance().getTime());
    }

    /** Gets a temporary file for the given filename. */
    public static final File getTmpFile(String filename) throws Exception {
        return new File(UtilsFile.getTempDir(), filename);
    }

    /** Gets an output file for the given filename. */
    public static final File getOutFile(String filename) throws Exception {        
        String path = ExtractPrefs.getPath();
        if (path.trim().length() > 0) {         
            // user preference extract path
            return new File(path, filename);            
        }
        // no user preference path so use path of data file
        DataStore ds = (DataStore) DataStoreLookup.instance().lookup(DataStore.class);
        if (ds != null) {
            File dataFile = new File(ds.getPath());
            if (dataFile.isFile()) {
                return new File(dataFile.getParent(), filename);            
            }        
        }
        // data file error
        throw new Exception("Data file not found.");            
    }

    /**
     * ExtractData data to the extract file.
     */
    public final void extractData(Data data, File extractFile, FormatType type) {
        ExtractData.process(data, extractFile, type);
    }

    /** Open a file using the system runtime. */
    public static final void openFile(File file) {
        Open.openFile(file);
    }

    /** Open a text file using the system runtime. */
    public final void openTextFile(File file) {
        Open.openTextFile(file);
    }

    /**
     * Transform using the given XML input file, XSL-FO file URL, XSL parameters
     * and output file.
     */
    public final void transformXSLFO(File xml, URL xslfo, List<Param> params, File out) throws Exception {
        XSLFO.transform(xml, xslfo.openStream(), params, out);
    }

    /**
     * Transform using the given XML input file, XSL file URL, XSL parameters
     * and output file.
     */
    public final void transformXSL(File xml, URL xsl, List<Param> params,
            File out, String encoding, boolean isXML) throws Exception {

        XSL.transform(xml, xsl.openStream(), params, out, encoding, isXML);
    }
}

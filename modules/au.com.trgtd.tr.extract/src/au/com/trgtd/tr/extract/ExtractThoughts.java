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
package au.com.trgtd.tr.extract;

import au.com.trgtd.tr.appl.Constants;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.logging.Logger;
import au.com.trgtd.tr.extract.Extract.FormatType;
import java.util.logging.Level;
import tr.model.Data;
import tr.model.thought.Thought;

/**
 * Extract thoughts data as XML.
 *
 * @author Jeremy Moore
 */
public class ExtractThoughts {

    private static final Logger LOG = Logger.getLogger("tr.extract");
    private static final DateFormat df = Constants.DATE_FORMAT_FIXED;

    /**
     * Extract ThinkingRock thoughts to an XML file.
     * @param data The data.
     * @param file The extract file.
     */
    public static void process(Data data, File xmlfile) {
        try {
            Writer out = initialise(xmlfile);
            process(data, out);
            finalise(out);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Extracting data failed: {0}", ex.getMessage());
        }
    }

    /* Initialise the output XML file stream, etc. */
    private static Writer initialise(File xmlfile) throws Exception {
        if (xmlfile.exists()) {
            xmlfile.delete();
        }
        OutputStream fout = new FileOutputStream(xmlfile);
        OutputStream bout = new BufferedOutputStream(fout);
        OutputStreamWriter out = new OutputStreamWriter(bout, "UTF-8");
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        out.write("<data>");
        return out;
    }

    /* Finalise the output XML file stream, etc. */
    private static void finalise(Writer out) throws Exception {
        out.write("</data>");
        out.flush();
        out.close();
    }

    /**
     * Extract ThinkingRock thoughts using a writer.
     * @param data The data.
     * @param out The writer.
     */
    public static void process(Data data, Writer out) {
        try {
            LOG.info("Extracting thoughts ... ");
            writeThoughts(data, out);
            LOG.info("Extracting thoughts ... done");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Extracting thoughts failed: {0}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    /* Write thoughts. */
    private static void writeThoughts(Data data, Writer out) throws Exception {
        out.write("<thoughts>\r\n");
        for (Thought thought : data.getThoughtManager().list()) {
            if (!thought.isProcessed()) {
                out.write("<thought>\r\n");
                out.write("<created>" + df.format(thought.getCreated()) + "</created>\r\n");
                out.write("<descr>" + escape(thought.getDescription()) + "</descr>\r\n");
                out.write("<topic>" + escape(thought.getTopic().getName()) + "</topic>\r\n");
                out.write("<notes>" + escape(thought.getNotes().trim()) + "</notes>\r\n");
                out.write("</thought>\r\n");                
            }
        }
        out.write("</thoughts>\r\n");
    }

    /*
     * Escapes the characters in a String using XML entities.  For example:
     * "bread" & "butter" => &quot;bread&quot; &amp; &quot;butter&quot;.
     * @param string The string.
     * @return The escaped string.
     */
    private static String escape(String string) {
        return FormatType.XML.escape(string);
    }
}

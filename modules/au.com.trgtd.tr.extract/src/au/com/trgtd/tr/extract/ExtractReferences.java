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
import tr.model.information.Information;

/**
 * Extract references data as XML.
 *
 * @author Jeremy Moore
 */
public class ExtractReferences {

    private static final Logger LOG = Logger.getLogger("tr.extract");
    private static final DateFormat df = Constants.DATE_FORMAT_FIXED;

    /**
     * Extract ThinkingRock reference items to an XML file.
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
        return out;
    }

    /* Finalise the output XML file stream, etc. */
    private static void finalise(Writer out) throws Exception {
        out.flush();
        out.close();
    }

    /**
     * Extract ThinkingRock reference items using a writer.
     * @param data The data.
     * @param out The writer.
     */
    public static void process(Data data, Writer out) {
        try {
            LOG.info("Extracting references ... ");
            writeReferences(data, out);
            LOG.info("Extracting references ... done");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Extracting references failed: {0}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    /* Write information items. */
    private static void writeReferences(Data data, Writer out) throws Exception {
        out.write("<references>\r\n");
        for (Information reference : data.getInformationManager().list()) {
            out.write("<reference>\r\n");
            out.write("<created>" + df.format(reference.getCreated()) + "</created>\r\n");
            out.write("<descr>" + escape(reference.getDescription()) + "</descr>\r\n");
            out.write("<topic>" + escape(reference.getTopic().getName()) + "</topic>\r\n");
            out.write("<notes>" + escape(reference.getNotes().trim()) + "</notes>\r\n");
            out.write("</reference>\r\n");
        }
        out.write("</references>\r\n");
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

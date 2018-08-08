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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.logging.Logger;
import au.com.trgtd.tr.extract.Extract.FormatType;
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
            LOG.severe("Extracting data failed: " + ex.getMessage());
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
            LOG.severe("Extracting references failed: " + ex.getMessage());
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

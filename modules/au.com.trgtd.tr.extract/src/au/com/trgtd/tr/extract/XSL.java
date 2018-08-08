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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Static methods for XSL transformations.
 *
 * @author Jeremy Moore
 */
public class XSL {

    private static final Logger LOG = Logger.getLogger("tr.extract");

    /**
     * Transform the XML file using the XSL file and output file.
     * @param xml The XML file.
     * @param xsl The XSL file.
     * @param params The List of XSLParam values to set or null if none.
     * @param out The output file.
     */
    public static void transform(File xml, InputStream xsl, List<Param> params,
            File out, String encoding, boolean isXML) throws Exception {

        StreamSource xmlStream = new StreamSource(xml);
        StreamSource xslStream = new StreamSource(xsl);
        FileOutputStream outStream = new FileOutputStream(out);
        OutputStreamWriter outStreamWriter = null;
        if (encoding == null) {
            outStreamWriter = new OutputStreamWriter(outStream);
        } else {
            try {
                outStreamWriter = new OutputStreamWriter(outStream, encoding);
            } catch (Exception ex) {
                LOG.warning("Encoding " + encoding + " is not supported.");
                encoding = null;
                outStreamWriter = new OutputStreamWriter(outStream);
            }
        }
        StreamResult streamRslt = new StreamResult(new BufferedWriter(outStreamWriter));
        transform(xmlStream, xslStream, params, streamRslt, encoding, isXML);
    }

    /* Do the XSL transformation. */
    private static void transform(StreamSource xml, StreamSource xslt,
            List<Param> params, StreamResult out, String encoding, boolean isXML)
            throws Exception {

        LOG.info("Started XSLT processing");

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xslt);
            for (Param param : params) {
                if (param != null && param.id != null && !param.id.trim().equals("")) {
                    String value = param.getValue();
                    if (value != null && !value.trim().equals("")) {
                        
                        LOG.info("****** ID: |" + param.id + "| Value: |" + value + "| ******");
                        
                        transformer.setParameter(param.id, value);                        
                    }
                }                
            }
            if (isXML) {                
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");                
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                if (encoding != null) {
                    transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
                }                
            } else {
                transformer.setOutputProperty(OutputKeys.METHOD, "text");
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");                
            }

            transformer.transform(xml, out);

            LOG.info("Finished XSLT processing");
        } finally {
            try {
                Writer writer = out.getWriter();
                if (writer != null) {
                    writer.close();
                }
                OutputStream outStream = out.getOutputStream();
                if (outStream != null) {
                    outStream.close();
                }
            } catch (Exception ex) {
            }
        }
    }
}

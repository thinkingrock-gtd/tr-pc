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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;

import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;
import org.xml.sax.SAXException;

/**
 * Static methods for XSL-FO transformations.
 *
 * @author Jeremy Moore
 */
public class XSLFO {

    /**
     * Transform the XML file to PDF using JAXP (XSLT) and FOP (XSL-FO).
     *
     * @param xmlfile The XML file.
     * @param xsltfile The XSL-FO file.
     * @param params The List of Parameter values to set or null if none.
     * @param pdffile The output file.
     */
    public static void transform(File xmlfile, InputStream xsltfile, List<Param> params, File pdffile) { 
        System.out.print("Processing XSL-FO ... ");
        try {
            // configure fopFactory as desired
            FopFactory fopFactory = FopFactory.newInstance();

            // load configuration
            File configFile = Resources.FILE_FOP_CONFIG;
            if (configFile != null) {
                DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
                Configuration cfg = cfgBuilder.buildFromFile(configFile);
                fopFactory.setUserConfig(cfg);
            }

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(pdffile))) {
                // Construct fop with desired output format

                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

                // Set the values of parameters in the stylesheet
                for (Param param : params) {
                    transformer.setParameter(param.id, param.getValue());
                }

                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile);

                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());

                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
            }
            System.out.print("done");
        } catch (SAXException | IOException | ConfigurationException | TransformerException e) {
            e.printStackTrace(System.err);
        } finally {
            System.out.println();
        }
    }

}

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
package au.com.trgtd.tr.datastore.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.XppReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Logger;
import tr.model.Data;

/**
 * A wrapper for using XStream (http://xstream.codehaus.org) to persist TR data
 * models.
 *
 * @author Jeremy Moore
 */
public final class XStreamWrapper {

    private static final Logger LOG = Logger.getLogger("tr.datastore");    
    
    /* XStream aliases for TR data model classes. */
    private static final Object[][] aliases = {
        {"data", tr.model.Data.class},
        {"action", tr.model.action.Action.class},
        {"actionStateASAP", tr.model.action.ActionStateASAP.class},
        {"actionStateScheduled", tr.model.action.ActionStateScheduled.class},
        {"actionStateDelegated", tr.model.action.ActionStateDelegated.class},
        {"actionStateInactive", tr.model.action.ActionStateInactive.class},
        {"topic", tr.model.topic.Topic.class},
        {"context", tr.model.context.Context.class},
        {"project", tr.model.project.Project.class},
        {"projects", tr.model.project.ProjectProjects.class},
        {"actions", tr.model.project.ProjectSingleActions.class},
        {"info", tr.model.information.Information.class},
        {"future", tr.model.future.Future.class},
        {"thought", tr.model.thought.Thought.class},
        {"list", tr.model.util.Manager.class},
        {"criterion", tr.model.criteria.Criterion.class},
        {"value", tr.model.criteria.Value.class}};
    /* Singleton instance. */
    private static XStreamWrapper instance;
    private final XStream xstream;

    /* Singleton constructor. */
    private XStreamWrapper() {
        super();
        xstream = new XStream();
        
//      xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);        
        xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);        
        
        for (Object[] alias : aliases) {
            xstream.alias((String)alias[0], (Class)alias[1]);
        }    
    }

    /**
     * Gets the singleton instance.
     * @return the singleton instance.
     */
    public static XStreamWrapper instance() {
        if (instance == null) {
            instance = new XStreamWrapper();
        }
        return instance;
    }

    /**
     * Attempt to load a TR data file using XStream.
     * @param file a TR data file.
     * @return The TR data model.
     * @throws Exception if the data could not be loaded.
     */
    public Data load(File file) throws Exception {
        LOG.info("Loading data started... ");

        Reader fr = getReader(file);

        Data data = (Data) xstream.unmarshal(new XppReader(fr));

        // if version 0, read in with default encoding
        if (data.getVersion() == 0) {
            fr.close();
            fr = new FileReader(file);
            data = (Data) xstream.unmarshal(new XppReader(fr));
        }

        fr.close();

        data.checkVersion();

        LOG.info("Loading data finished.");

        return data;
    }

    /**
     * Stores data to a file using XStream.
     * @param data The data.
     * @param file The file.
     * @throws Exception if an exception occurs while storing the data.
     */
    public synchronized void store(Data data, File file) throws Exception {
        LOG.info("Storing data started... ");

        Writer fw = getWriter(file);
        xstream.marshal(data, new CompactWriter(fw));
        fw.close();

        LOG.info("Storing data finished.");
    }

    private Reader getReader(File file) throws Exception {
        return new InputStreamReader(new FileInputStream(file), "UTF-8");
    }

    private Writer getWriter(File file) throws Exception {
        return new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
    }
}

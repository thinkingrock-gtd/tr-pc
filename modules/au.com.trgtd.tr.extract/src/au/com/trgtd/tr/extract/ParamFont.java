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

import java.io.File;
import java.util.List;
import java.util.Vector;

import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.Param.Type;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Font parameter.
 *
 * @author Jeremy Moore
 */
public class ParamFont extends Param {
    
    /** Constructs a new instance. */
    public ParamFont(String id, String display) {
        super(Type.COMBOBOX, id, display);
    }
    
    /** Gets selection items of fonts for PDF documents including FOP fonts. */
    @Override
    public List<Item> getItems() {
        if (items == null) {
            initItems();
        }
        return items;
    }
    
    private void initItems() {
        items = new Vector<Item>();
        items.add(new Item("Courier","Courier"));
        items.add(new Item("Helvetica","Helvetica"));
        items.add(new Item("Times","Times"));
        
        getFOPFonts(items);
        
//      Collections.sort(items);
    }
    
    /* Parse the FOP configuration file for added fonts. */
    private static final void getFOPFonts(List<Item> items) {        
        File fopConfigFile = Resources.FILE_FOP_CONFIG;
        if (fopConfigFile == null) return;
        
        Document dom = parseXmlFile(fopConfigFile);
        parseDocument(dom, items);        
    }
    
    // Parse the xml file and get the dom object
    private static final Document parseXmlFile(File file) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(file);
        } catch (Exception ex) {
            return null;
        }
    }
    
    // Parse the document and get the features.
    private static void parseDocument(Document dom, List<Item> items) {
        Element fop = dom.getDocumentElement();
        NodeList renderers = fop.getElementsByTagName("renderer");
        if (renderers == null) {
            return;
        }
        for (int i = 0; i < renderers.getLength(); i++) {
            Element renderer = (Element) renderers.item(i);
            String mime = renderer.getAttribute("mime");
            if (!mime.equalsIgnoreCase("application/pdf")) {
                continue;
            }
            NodeList fontTriplets = renderer.getElementsByTagName("font-triplet");
            if (fontTriplets == null) {
                continue;
            }
            for (int j = 0; j < fontTriplets.getLength(); j++) {
                Element fontTriplet = (Element) fontTriplets.item(j);
                String style = fontTriplet.getAttribute("style");
                if ( ! style.equalsIgnoreCase("normal")) {
                    continue;
                }
                String weight = fontTriplet.getAttribute("weight");
                if ( ! weight.equalsIgnoreCase("normal")) {
                    continue;
                }
                String name = fontTriplet.getAttribute("name");
                items.add(new Item(name, name));
            }
        }
    }
    
}

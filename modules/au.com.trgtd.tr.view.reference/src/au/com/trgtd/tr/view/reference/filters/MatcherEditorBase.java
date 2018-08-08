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

package au.com.trgtd.tr.view.reference.filters;

import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import java.awt.Component;
import java.io.Serializable;
import tr.model.information.Information;

/**
 * Base class for reference matcher editors.
 *
 * @author Jeremy Moore
 */
public abstract class MatcherEditorBase extends AbstractMatcherEditor<Information> {
    
    /** Default constructor. */
    public MatcherEditorBase() {}
    
    /** Gets the view label. */
    public abstract String getLabel();
    
    /** Gets the view component. */
    public abstract Component getComponent();
    
    /** Gets the serializable value. */
    public abstract Serializable getSerializable();
    
    /** Sets the serializable value. */
    public abstract void setSerializable(Serializable serializable);
    
    /** Resets the filter. */
    public abstract void reset();

}

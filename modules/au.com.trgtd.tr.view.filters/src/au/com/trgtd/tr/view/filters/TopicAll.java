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
 * Portions Copyright 2006-2009 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view.filters;

import au.com.trgtd.tr.appl.Constants;
import java.io.Serializable;
import org.openide.util.NbBundle;
import tr.model.topic.Topic;
 
/**
 * Topic for selection of all topics.
 *
 * @author Jeremy Moore
 */

public final class TopicAll extends Topic implements Serializable {
    
    private static final long serialVersionUID = 7738438725287L;

    public static final String ID = ChoiceAll.ID;
    
    public TopicAll() {
        super(Constants.ID_FILTER_TOPICS_ALL);
        super.setName(NbBundle.getMessage(TopicAll.class, "filter-all"));
    }
    
    public boolean equals(Object object) {
        return object instanceof TopicAll;
    }
}



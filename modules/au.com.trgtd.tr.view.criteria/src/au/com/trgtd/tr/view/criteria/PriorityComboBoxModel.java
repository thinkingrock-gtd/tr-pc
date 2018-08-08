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
package au.com.trgtd.tr.view.criteria;

import javax.swing.DefaultComboBoxModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.criteria.Criterion;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;

/**
 * Combo box model for priority critera.
 */
public class PriorityComboBoxModel extends DefaultComboBoxModel implements Observer {

    private Criterion criterion;
    private Lookup.Result result;

    /**
     * Constructs a new instance.
     */
    public PriorityComboBoxModel() {
        super();
        initialise();
    }

    private void initialise() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) {
            criterion = null;
            return;
        }

        if (criterion != null) {
            criterion.removeObserver(this);
        }

        criterion = data.getPriorityCriterion();

        criterion.addObserver(this);

        if (result == null) {
            result = DataLookup.instance().lookup(new Lookup.Template(Data.class));
            result.addLookupListener(new LookupListener() {

                public void resultChanged(LookupEvent lookupEvent) {
                    update(null, null);
                }
            });
        }
    }

    /**
     * Implement ListModel.getElementAt(int index).
     */
    @Override
    public Object getElementAt(int index) {
        return criterion == null || !criterion.isUse() ? null : criterion.values.get(index);
    }

    /**
     * Implement ListModel.getSize().
     */
    @Override
    public int getSize() {
        return criterion == null || !criterion.isUse() ? 0 : criterion.values.size();
    }

    /**
     * Implement Observer to fire contents changed.
     */
    public void update(Observable obs, Object arg) {
        initialise();
        fireContentsChanged(this, 0, getSize());
    }
}

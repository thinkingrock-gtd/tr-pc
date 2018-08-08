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
package au.com.trgtd.tr.view;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import tr.model.actor.Actor;

/**
 * List cell renderer for topics combo box.
 *
 * @author Jeremy Moore
 */
public class ActorsListCellRenderer extends JLabel implements ListCellRenderer {

//    public TopicsListCellRenderer() {
//        setOpaque(true);
//    }
//
//    public Component getListCellRendererComponent(JList list, Object value,
//            int index, boolean isSelected, boolean cellHasFocus) {
//
//        if (value instanceof Topic) {
//            Topic topic = (Topic) value;
//            setText(topic.getName());
//            setFont(list.getFont());
//            if (isSelected) {
//                setBackground(list.getSelectionBackground());
//                setForeground(list.getSelectionForeground());
//            } else {
//                setBackground(topic.getBackground());
//                setForeground(topic.getForeground());
//            }
//        } else {
//            setText(value.toString());
//            setFont(list.getFont());
//            if (isSelected) {
//                setBackground(list.getSelectionBackground());
//                setForeground(list.getSelectionForeground());
//            } else {
//                setBackground(list.getBackground());
//                setForeground(list.getForeground());
//            }
//        }
//        return this;
//    }
    private final ListCellRenderer std;

    public ActorsListCellRenderer(ListCellRenderer std) {
        if (std == null) {
            throw new NullPointerException("Standard renderer is null.");
        }
        this.std = std;
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        JLabel lbl = (JLabel)std.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

//        if (!isSelected) {
            if (value instanceof Actor) {
                Actor actor = (Actor) value;
                if (actor.isInactive()) {
                    lbl.setText("<HTML><STRIKE>" + actor.getName() + "</STRIKE></HTML>");
                } else {
                    lbl.setText(actor.getName());
                }

//                component.setBackground(actor.getBackground());
//                component.setForeground(actor.getForeground());
            }
//        }
        
        return lbl;
    }
}





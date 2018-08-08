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

package au.com.trgtd.tr.view.topics.screen;

import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Cookie;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.topic.Topic;
import tr.model.topic.TopicUtils;
import tr.model.util.Manager;
import au.com.trgtd.tr.view.topics.DeleteTopicCookie;
import au.com.trgtd.tr.view.topics.EditTopicCookie;
import au.com.trgtd.tr.view.topics.TopicsComboBox;
import au.com.trgtd.tr.view.topics.TopicsComboBoxModel;
import au.com.trgtd.tr.view.topics.dialog.TopicDialog;

/**
 * Node for a topic.
 *
 * @author Jeremy Moore
 */
public class TopicNode extends AbstractNode implements EditTopicCookie, DeleteTopicCookie {
    
    public final Manager<Topic> manager;
    public final Topic topic;
    
    /** Constructs a new instance. */
    public TopicNode(Manager<Topic>manager, Topic topic) {
        super(Children.LEAF, Lookups.singleton(topic));
        this.manager = manager;
        this.topic = topic;
    }
    
    public String getName() {
        return (topic == null) ? "" : topic.getName();
    }
    
    public Cookie getCookie(Class clazz) {
        if (clazz == EditTopicCookie.class) return this;
        if (clazz == DeleteTopicCookie.class) return this;
        return super.getCookie(clazz);
    }
    
    public void editTopic() {
        if (topic == null) return;
        
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        // The default topic can not be edited.
        if (topic.equals(Topic.getDefault())) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        
        new TopicDialog(data).showModifyDialog(topic);
    }
    
    public void deleteTopic() {
        if (topic == null) return;
        
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        // The default topic can not be deleted.
        if (topic.equals(Topic.getDefault())) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        
        if (TopicUtils.instance().isUsed(data, topic)) {
            // dialog for in use message and option to replace
            TopicsComboBox ccb = new TopicsComboBox(new TopicsComboBoxModel(false));
            ccb.setSelectedIndex(0);
            
            JPanel panel = new JPanel();
            panel.add(new JLabel(NbBundle.getMessage(getClass(), "MSG_ReplaceTopic")));
            panel.add(ccb);
            
            String title = NbBundle.getMessage(TopicNode.class, "delete.topic", topic.getName());
            
            int r = JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.CANCEL_OPTION) {
                return;
            }
            Topic replacement = (Topic)ccb.getSelectedItem();
            if (!TopicUtils.instance().replace(data, topic, replacement)) {
                String msg = NbBundle.getMessage(TopicNode.class, "can.not.replace.topic", topic.getName(), replacement.getName());
                JOptionPane.showMessageDialog(null, msg);
                return;
            }
        }
        
        // topic should not be in use now so remove it
        data.getTopicManager().remove(topic);
    }
    
}

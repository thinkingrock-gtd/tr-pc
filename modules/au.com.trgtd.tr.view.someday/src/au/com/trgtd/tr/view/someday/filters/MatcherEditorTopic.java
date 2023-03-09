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
package au.com.trgtd.tr.view.someday.filters;

import ca.odell.glazedlists.matchers.Matcher;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.future.Future;
import tr.model.topic.Topic;
import tr.model.util.Manager;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.filters.FilterComboAbstract;
import au.com.trgtd.tr.view.filters.MultiChoiceDialog;
import au.com.trgtd.tr.view.filters.TopicAll;
import au.com.trgtd.tr.view.filters.TopicMultiple;
import au.com.trgtd.tr.view.filters.TopicMultipleEdit;

/**
 * MatcherEditor the matches a topic selection.
 *
 * @author Jeremy Moore
 */
public class MatcherEditorTopic extends MatcherEditorBase implements PropertyChangeListener {
    
    private final TopicsComboBoxModel comboModel;
    private final TopicsComboBox comboBox;
    
    /** Constructs a new instance. */
    public MatcherEditorTopic() {
        comboModel = new TopicsComboBoxModel();
        comboBox = new TopicsComboBox(comboModel);
        comboBox.addValueChangeListener(this);
    }
    
    public Component getComponent() {
        return comboBox;
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        Topic topic = (Topic)comboBox.getSelectedItem();
        if (topic == null) {
            fireMatchAll();
        } else if (topic instanceof TopicAll) {
            fireMatchAll();
        } else if (topic instanceof TopicMultiple multiple) {
            fireChanged(new TopicMatcher(multiple.getChosen()));
        } else {
            fireChanged(new TopicMatcher(topic));
        }
    }
    
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-topic");
    }
    
    public Serializable getSerializable() {
        return (Serializable)comboBox.getSelectedItem();
    }
    
    public void setSerializable(Serializable serializable) {
        comboBox.stopChangeEvents();
        if (serializable instanceof TopicAll topicAll) {
            comboBox.setSelectedItem(topicAll);
            fireMatchAll();
        } else if (serializable instanceof TopicMultiple tm) {
//            TopicMultiple multiple = (TopicMultiple)serializable;
//            comboBox.setSelectedItem(multiple);
//            fireChanged(new TopicMatcher(multiple.getChosen()));
            comboModel.multiple.setChosen(tm.getChosen());
            comboBox.setSelectedItem(comboModel.multiple);
            fireChanged(new TopicMatcher(comboModel.multiple.getChosen()));
        } else if (serializable instanceof Topic topic) {
            comboBox.setSelectedItem(topic);
            fireChanged(new TopicMatcher(topic));
        }
        comboBox.startChangeEvents();
    }
    
    private static class TopicMatcher implements Matcher<Future> {
        private final boolean all;
        private final List<Topic> topics;
        
        public TopicMatcher() {
            this.all = true;
            this.topics = null;
        }
        
        public TopicMatcher(Topic topic) {
            this.all = false;
            this.topics = new Vector<>();
            this.topics.add(topic);
        }
        
        public TopicMatcher(List<Topic> topics) {
            this.all = false;
            this.topics = topics;
        }
        
        public boolean matches(Future future) {
            if (all) {
                return true;
            }
            for (Topic topic : topics) {
                if (topic.equals(future.getTopic())) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private class TopicsComboBoxModel extends DefaultComboBoxModel<Topic> implements Observer {
        private final TopicAll all;
        private final TopicMultiple multiple;
        private final TopicMultipleEdit multipleEdit;
        private Manager<Topic> topicManager;
        private List<Topic> topics;
        private Lookup.Result result;
        
        /**
         * Creates a new instance for the given data model.
         * @param data The data model.
         */
        public TopicsComboBoxModel() {
            super();
            all = new TopicAll();
            multiple = new TopicMultiple();
            multipleEdit = new TopicMultipleEdit();
            initialise();
        }
        
        private void initialise() {
            if (topicManager != null) {
                topicManager.removeObserver(this);
            }
            Data data = DataLookup.instance().lookup(Data.class);
            if (data == null) {
                topicManager = null;
                topics = new Vector<>();
            } else {
                topicManager = data.getTopicManager();
                topicManager.addObserver(this);
                
                topics = topicManager.list();
                Collections.sort(topics);
            }
            topics.add(0, all);
            topics.add(1, multiple);
            if (Utilities.isWindows()) {
                topics.add(2, multipleEdit);
            }
            // data lookup listener to force panel initialisation if data changes
            if (result == null) {
                result = DataLookup.instance().lookupResult(Data.class);
                result.addLookupListener((LookupEvent lookupEvent) -> {
                    update(null, null);
                });
            }
        }
        
        /** Implement ListModel.getElementAt(int index). */
        public Topic getElementAt(int index) {
            return topics.get(index);
        }
        
        /** Implement ListModel.getSize(). */
        public int getSize() {
            return topics.size();
        }
        
        /** Implement Observer to fire contents changed. */
        public void update(Observable o, Object arg) {
            initialise();
            fireContentsChanged(this, 0, getSize());
        }
    }

    @Override
    public void reset() {
        comboBox.setSelectedItem(null);
        comboBox.fireValueChange();
    }
    
    public class TopicsComboBox extends FilterComboAbstract<Topic> {
        
        private final ActionListener listener;
        
        public TopicsComboBox(TopicsComboBoxModel model) {
            super(model);
//          setPreferredSize(new Dimension(120, 24));
            if (Utilities.isWindows()) {
                listener = new WindowsActionListener();
            } else {
                listener = new RealActionListener();
            }
            addActionListener(listener);
        }
        
        public void stopChangeEvents() {
            removeActionListener(listener);
        }
        
        public void startChangeEvents() {
            addActionListener(listener);
            // for MS-Windows:
            lastSelectedItem = getSelectedItem();
        }
        
        private final class RealActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object object = getSelectedItem();
                if (object instanceof TopicMultiple tm) {
                    Vector<Topic> all;
                    Data data = DataLookup.instance().lookup(Data.class);
                    if (data == null) {
                        all = new Vector<>();
                    } else {
                        all = data.getTopicManager().list();
                    }
                    MultiChoiceDialog<Topic> d = new MultiChoiceDialog<>(TopicsComboBox.this, all, tm.getChosen(), true);
                    d.setTitle(NbBundle.getMessage(getClass(), "filter-topic"));
                    d.setLocationRelativeTo(TopicsComboBox.this);
                    d.setVisible(true);
                    if (d.isOkay()) {
                        tm.setChosen(d.getChosen());
                    }
                }
                fireValueChange();
            }
        };
        
        private Object lastSelectedItem;
        
        private final class WindowsActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object object = getSelectedItem();
                if (object instanceof TopicMultipleEdit) {
                    TopicMultiple tm = comboModel.multiple;
                    Vector<Topic> all;
                    Data data = DataLookup.instance().lookup(Data.class);
                    if (data == null) {
                        all = new Vector<>();
                    } else {
                        all = data.getTopicManager().list();
                    }
                    MultiChoiceDialog<Topic> d = new MultiChoiceDialog<>(TopicsComboBox.this, all, tm.getChosen(), true);
                    d.setTitle(NbBundle.getMessage(getClass(), "filter-topic"));
                    d.setLocationRelativeTo(TopicsComboBox.this);
                    d.setVisible(true);
                    if (d.isOkay()) {
                        tm.setChosen(d.getChosen());
                        setSelectedItem(tm);
                        lastSelectedItem = tm;
                    } else {
                        setSelectedItem(lastSelectedItem);
                    }
                } else {
                    lastSelectedItem = object;
                }
                fireValueChange();
            }
        }
    }
    
}


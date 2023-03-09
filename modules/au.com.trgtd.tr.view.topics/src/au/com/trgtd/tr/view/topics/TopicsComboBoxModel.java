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
package au.com.trgtd.tr.view.topics;

import au.com.trgtd.tr.appl.Constants;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.topic.Topic;

import tr.model.util.Manager;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;

/**
 * Topics ComboBoxModel.
 *
 * @author Jeremy Moore
 */
public class TopicsComboBoxModel extends DefaultComboBoxModel<Topic> implements Observer {
    
    private Manager<Topic> topicManager;
    private List<Topic> topics;
    private boolean all;
    private transient Lookup.Result result;

    /**
     * Creates a new default instance.
     */
    public TopicsComboBoxModel() {
        this(false);
    }    
    
    /**
     * Creates a new instance.
     * @param all Whether or not to include an item representing all topics.
     */
    public TopicsComboBoxModel(boolean all) {
        super();
        this.all = all;
        initialise();
    }
    
    private void initialise() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            topics = new Vector<>();
            return;
        }
        
        if (topicManager != null) {
            topicManager.removeObserver(this);
        }
        topicManager = data.getTopicManager();
        topicManager.addObserver(this);
        
        topics = topicManager.list();
        Collections.sort(topics);

        if (all) {
            Topic allTopic = new Topic(Constants.ID_FILTER_TOPICS_ALL);
            allTopic.setName(NbBundle.getMessage(TopicsComboBoxModel.class, "all"));
            topics.add(0, allTopic);
        }
        
        // if the data model changes we need to re-initialise
        if (result == null) {
            result = DataLookup.instance().lookupResult(Data.class);
            result.addLookupListener((LookupEvent lookupEvent) -> {
                update(null, null);
            });
        }
    }
    
    /**
     * Implement ListModel.getElementAt(int index).
     */
    @Override
    public Topic getElementAt(int index) {
        return topics.get(index);
    }
    
    /**
     * Implement ListModel.getSize().
     */
    @Override
    public int getSize() {
        return topics.size();
    }
    
    /**
     * Implement Observer to fire contents changed.
     */
    public void update(Observable o, Object arg) {
        initialise();
        fireContentsChanged(this, 0, getSize());
    }
    
}

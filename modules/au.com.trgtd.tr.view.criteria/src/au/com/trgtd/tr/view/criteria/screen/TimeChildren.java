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
package au.com.trgtd.tr.view.criteria.screen;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import java.util.Collections;
import java.util.List;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.criteria.Value;
import tr.model.util.Manager;

public class TimeChildren extends Children.Keys<Value> implements Observer {

    private final static List<Value> EMPTY = Collections.emptyList();
    private final ExplorerManager em;
    private final Manager<Value> values;
    private final Index index;
    private boolean reordering;

    public TimeChildren(ExplorerManager em) {
        this.em = em;
        Data data = DataLookup.instance().lookup(Data.class);
        this.values = (data == null) ? null : data.getTimeCriterion().values;
        this.index = new IndexImpl();
    }

    @Override
    protected void addNotify() {
        if (values != null) {
            values.addObserver(this);
        }
        this.setKeys();
        super.addNotify();
    }

    @Override
    protected void removeNotify() {
        if (values != null) {
            values.removeObserver(this);
        }
        super.setKeys(EMPTY);
        super.removeNotify();
    }

    private void setKeys() {
        super.setKeys(null == values ? EMPTY : values.list());
    }

    @Override
    protected Node[] createNodes(Value value) {
        return new Node[]{new TimeNode(value)};
    }

    @Override
    public void update(Observable observable, Object arguement) {
        if (reordering) {
            return;
        }
        this.setKeys();
        super.refresh();
    }

    public Index getIndex() {
        return index;
    }

    private class IndexImpl extends Index.Support {

        @Override
        public Node[] getNodes() {
            return TimeChildren.this.getNodes();
        }

        @Override
        public int getNodesCount() {
            return getNodes().length;
        }

        @Override
        public void moveDown(final int i) {
            if (null == values) {
                return;
            }
            if (i < 0 || i > values.size() - 2) {
                return;
            }

            Node[] nodes = em.getSelectedNodes();
                        
            reordering = true;
            
            Value value = values.get(i);
            values.set(i, values.get(i + 1));
            values.set(i + 1, value);

            setKeys();
            
            reordering = false;
            
            try {
                em.setSelectedNodes(nodes);
            } catch (Exception ex) {
            }
        }

        @Override
        public void moveUp(final int i) {
            if (values == null) {
                return;
            }
            if (i < 1 || i > values.size() - 1) {
                return;
            }

            Node[] nodes = em.getSelectedNodes();
            
            reordering = true;

            Value value = values.get(i);
            values.set(i, values.get(i - 1));
            values.set(i - 1, value);

            setKeys();
            
            reordering = false;
            
            try {
                em.setSelectedNodes(nodes);
            } catch (Exception ex) {
            }
        }

        @Override
        public void reorder(int[] perm) {
        }
    };
    
}

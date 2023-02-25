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
package au.com.trgtd.tr.find.ui;

import au.com.trgtd.tr.find.FindPanel;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

public class FoundNodeRoot extends AbstractNode implements Observer {

    private static final String ICON_PATH = "au/com/trgtd/tr/find/Find.png";
    private static final Image icon = ImageUtilities.loadImage(ICON_PATH, true);

    /**
     * Constructs a new instance for the given found items.
     * @param items The found items.
     */
    public FoundNodeRoot(FoundItems items) {
        this(new FoundChildren(items));
        setName(items);
        items.addObserver(this);
    }

    /**
     * Constructs a new instance for a given children.
     * @param children The children.
     */
    private FoundNodeRoot(FoundChildren children) {
        super(children);
    }
    
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        sheet.put(Sheet.createPropertiesSet());
        return sheet;
    }
    
    @Override
    public Image getIcon(int type) {
        return icon;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return icon;
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public void update(Observable obs, Object arg) {
        if (obs instanceof FoundItems foundItems) {
            setName(foundItems);
        }
    }

    private void setName(FoundItems items) {
        String find = NbBundle.getMessage(FindPanel.class, "find");
        setName(find + ": " + items.getText() + " (" + items.size() + ")");
    }
    
}

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
package au.com.trgtd.tr.view.reference.linker;

import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.view.notes.NotesLink;
import au.com.trgtd.tr.view.notes.spi.NotesLinker;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import tr.model.information.Information;

/**
 * Reference notes linker.
 *
 * @author Jeremy Moore
 */
public class RefNotesLinker implements NotesLinker {

    private final static String SCHEME = "ref:";
    private final String prefix;

    public RefNotesLinker() {
        String msg = NbBundle.getMessage(getClass(), "ref.link.prefix");
        this.prefix = msg.trim().length() > 0 ? msg.trim() + " " : "";
    }

    @Override
    public String getScheme() {
        return SCHEME;
    }

    @Override
    public ImageIcon getIcon() {
        return ImageUtilities.loadImageIcon("au/com/trgtd/tr/view/reference/linker/reflink.png", false);
    }

    @Override
    public String getToolTip() {
        return NbBundle.getMessage(getClass(), "ref.link.tooltip");
    }

    @Override
    public NotesLink getUserLink() {
        Information ref = RefChooser.getDefault().getUserChoice();
        if (ref == null) {
            return null;
        }
        return new NotesLink(SCHEME + ref.getID(), prefix + ref.getDescription());
    }

    @Override
    public void openUserLink(String url) {
        if (url == null || !url.startsWith(SCHEME)) {
            return;
        }
        int id;
        try {
            id = Integer.valueOf(url.substring(SCHEME.length()));
        } catch (NumberFormatException x) {
            String msg = NbBundle.getMessage(getClass(), "ref.not.known");
            int msgType = NotifyDescriptor.ERROR_MESSAGE;
            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(msg, msgType));
            return;
        }
        for (Information ref : Services.instance.getReferences()) {
            if (ref.getID() == id) {
                RefViewer.getDefault().view(ref);
                return;
            }
        }
        String msg = NbBundle.getMessage(getClass(), "ref.not.found");
        int type = NotifyDescriptor.ERROR_MESSAGE;
        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(msg, type));
    }

    @Override
    public int getShortcutMask() {
        return KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;
    }

    @Override
    public int getShortcutKey() {
        return KeyEvent.VK_R;
    }

}

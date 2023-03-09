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
package au.com.trgtd.tr.view.actors;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.prefs.actions.ActionPrefs;
import au.com.trgtd.tr.swing.TRComboBox;
import au.com.trgtd.tr.swing.document.LazyDocumentListener;
import au.com.trgtd.tr.view.actors.ActorDialog.Result;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.JTextComponent;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.actor.Actor;

/**
 * Combo box for actors.
 *
 * @author Jeremy Moore
 */
public class ActorsComboBox extends TRComboBox<Actor> implements ActionListener {

    public final static String PROP_SELECTED = "selected";

    private final ActorsComboBoxModel model;
    private final JTextComponent textfield;

    /** Constructs a new instance. */
    public ActorsComboBox() {
        this(new ActorsComboBoxModel());
    }

    /**
     * Constructs a new instance for the given data model.
     * @param model The actors combo box model.
     */
    public ActorsComboBox(ActorsComboBoxModel model) {
        super(model);
        this.model = model;
        setRenderer(new ActorsListCellRenderer(getRenderer()));
        setFont(getFont().deriveFont(Font.PLAIN));
        setMaximumRowCount(Constants.COMBO_MAX_ROWS);
        setToolTipText(NbBundle.getMessage(getClass(), "TTT_ActorsComboBox"));
        setEditable(true);
        addActionListener((ActionEvent e) -> {
            Object selection = getSelectedItem();
            if (selection == null) {
                firePropertyChange(PROP_SELECTED, null, null);
            } else if ((selection instanceof String) && (e.getActionCommand().equals("comboBoxEdited"))) {
                handleEdit((String) selection);
            } else if (selection instanceof Actor actor) {
                handleSelection(actor);
            }
        });
        textfield = (JTextComponent)getEditor().getEditorComponent();
        textfield.getDocument().addDocumentListener(new LazyDocumentListener() {
            @Override
            public void update() {
                firePropertyChange(PROP_SELECTED, null, textfield.getText());
            }
        });
        textfield.setSize(new Dimension(200, 20));
    }

    private Data getData() {
        return DataLookup.instance().lookup(Data.class);
    }

    private void handleEdit(String str) {
        firePropertyChange(PROP_SELECTED, null, str);
    }

    private void handleSelection(Actor actor) {
        if (actor.equals(ActorsComboBoxModel.ACTOR_CLR)) {
            setSelectedItem(null);
            firePropertyChange(PROP_SELECTED, null, null);
        } else if (actor.equals(ActorsComboBoxModel.ACTOR_ADD)) {
            Actor tmpActor = new Actor(0);
            ActorDialog dlg = new ActorDialog(getData());
            Result rslt = dlg.showModifyDialog(tmpActor);
            if (rslt == Result.CANCEL) {
                setSelectedItem(null);
                firePropertyChange(PROP_SELECTED, null, null);
            } else { // Result.OK
                stopObserving();
                Actor newActor = new Actor(getData().getNextID());
                newActor.setName(tmpActor.getName());
                newActor.setEmail(tmpActor.getEmail());
                newActor.setTeam(tmpActor.isTeam());
                newActor.setInactive(tmpActor.isInactive());
                getData().getActorManager().add(newActor);
                updateModel();
                setSelectedItem(newActor);
                firePropertyChange(PROP_SELECTED, null, newActor);
            }
        } else { // selection of an actual actor
            firePropertyChange(PROP_SELECTED, null, actor);
            setEditable(false);
            model.addClearItem();
        }
    }

    public void setIncludeInactive(boolean inactive) {
        model.setIncludeInactive(inactive);
    }

    public void updateModel() {
        model.update();
    }

    public void stopObserving() {
        model.stopObserving();
    }

    public void startObserving() {
        model.startObserving();
    }

    @Override
    public synchronized void setSelectedItem(Object object) {
        
        if (object == null || object instanceof String) {
            setEditable(true);
            model.removeClearItem();
        } else {
            setEditable(false);
            model.addClearItem();
        }
        
        super.setSelectedItem(object);
    }

    public synchronized void selectDefault() {
        if (ActionPrefs.isDelegateModeCombo()) {
            model.addClearItem();
            setEditable(false);
            super.setSelectedItem(null);
        } else { // Free text mode
            model.removeClearItem();
            setEditable(true);
            setSelectedItem("");
        }
    }

}

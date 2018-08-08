package au.com.trgtd.tr.view.delegates.screen;

import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.view.actors.ActorDialog;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Cookie;
import org.openide.util.lookup.Lookups;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.actor.Actor;
import tr.model.util.Manager;
import au.com.trgtd.tr.view.delegates.DeleteActorCookie;
import au.com.trgtd.tr.view.delegates.EditActorCookie;
import javax.swing.JOptionPane;
import org.openide.util.NbBundle;

/**
 * Node for a actor.
 *
 * @author Jeremy Moore
 */
public class ActorNode extends AbstractNode implements EditActorCookie, DeleteActorCookie {

    public final Manager<Actor> manager;
    public final Actor actor;

    /** 
     * Constructs a new instance.
     * @param manager
     * @param actor 
     */
    public ActorNode(Manager<Actor> manager, Actor actor) {
        super(Children.LEAF, Lookups.singleton(actor));
        this.manager = manager;
        this.actor = actor;
    }

    @Override
    public String getName() {
        return (actor == null) ? "" : actor.getName();
    }

    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == EditActorCookie.class) {
            return this;
        }
        if (clazz == DeleteActorCookie.class) {
            return this;
        }
        return super.getCookie(clazz);
    }

    @Override
    public void editActor() {
        if (actor == null) {
            return;
        }

        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }

        new ActorDialog(data).showModifyDialog(actor);
    }

    @Override
    public void deleteActor() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        if (Services.instance.isUsed(actor, true)) {
            String m = NbBundle.getMessage(getClass(), "delete.actor.in.use.message", actor.getName());
            String t = NbBundle.getMessage(getClass(), "delete.actor.in.use.title");
            JOptionPane.showMessageDialog(null, m, t, JOptionPane.INFORMATION_MESSAGE);
        } else {
            data.getActorManager().remove(actor);
        }
    }

}

package au.com.trgtd.tr.view.delegates.screen;

import java.util.Collection;

/**
 * A provider of actor nodes.
 *
 * @author Jeremy Moore
 */
public interface ActorNodeProvider {
    
    /**
     * Provide the given actor nodes.
     * @param actorNodes the actor nodes.
     */
    public void provide(Collection<ActorNode> actorNodes);
    
}

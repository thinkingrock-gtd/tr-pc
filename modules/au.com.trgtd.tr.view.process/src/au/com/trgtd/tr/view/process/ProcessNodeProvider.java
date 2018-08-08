package au.com.trgtd.tr.view.process;

/**
 * A provider of a process node.
 *
 * @author Jeremy Moore
 */
public interface ProcessNodeProvider {

    /**
     * Provide a thought node.
     *
     * @param processNode
     */
    public void provide(ProcessNode processNode);

}

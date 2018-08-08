package au.com.trgtd.tr.sync.device;

/**
 * Sync handler interface.
 *
 * @author Jeremy Moore
 */
public interface ISyncHandler {

    public void sync() throws Exception;

    public void cancel();

}


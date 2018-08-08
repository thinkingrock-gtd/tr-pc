package au.com.trgtd.tr.email.task;

import au.com.trgtd.tr.email.Email;
import org.openide.util.NbBundle;

/**
 * Thread for fetching email.
 *
 * @author Jeremy Moore
 */
public final class FetchEmailThread extends Thread {

    private static final Object SYNCHRONIZE_OBJECT = new Object();

    private static final String NAME = NbBundle.getMessage(FetchEmailThread.class, "fetch.email.task");

    /**
     * Constructs a new instance.
     */
    public FetchEmailThread() {
        super(NAME);
    }

    /**
     * Run the task.
     */
    @Override
    public void run() {
//      LOG.info("Starting email fetch at: " + new Date());

        synchronized (SYNCHRONIZE_OBJECT) {
            Email.retrieve();
        }

//      LOG.info("Finished email fetch at: " + new Date());
    }
}

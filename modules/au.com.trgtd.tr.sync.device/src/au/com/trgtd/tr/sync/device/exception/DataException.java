package au.com.trgtd.tr.sync.device.exception;

/**
 * Exception for data manager.
 *
 * @author Jeremy Moore
 */
public class DataException extends Exception {

    public DataException(String msg) {
        super(msg);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }


}

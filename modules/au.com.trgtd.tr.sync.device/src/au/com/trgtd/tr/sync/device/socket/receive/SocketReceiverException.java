package au.com.trgtd.tr.sync.device.socket.receive;

/**
 * Exception for receiver problems.
 *
 * @author Jeremy Moore
 */
public class SocketReceiverException extends Exception {

    public SocketReceiverException(String msg) {
        super(msg);
    }

    public SocketReceiverException(String message, Throwable cause) {
        super(message, cause);
    }

}

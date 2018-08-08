package au.com.trgtd.tr.sync.device.socket.send;

/**
 * Exception for sender problems.
 *
 * @author Jeremy Moore
 */
public class SocketSenderException extends Exception {

    public SocketSenderException(String msg) {
        super(msg);
    }

    public SocketSenderException(String message, Throwable cause) {
        super(message, cause);
    }

}

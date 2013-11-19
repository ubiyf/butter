package butter.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-19
 * Time: 下午5:13
 */
public class CommandTimeoutException extends RuntimeException {

    public CommandTimeoutException() {
    }

    public CommandTimeoutException(String message) {
        super(message);
    }

    public CommandTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandTimeoutException(Throwable cause) {
        super(cause);
    }
}

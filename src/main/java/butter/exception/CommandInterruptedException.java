package butter.exception;

/**
 * Created with IntelliJ IDEA.
 * User: lizhongyuan
 * Date: 13-11-16
 * Time: 上午2:12
 */
public class CommandInterruptedException extends RuntimeException {

    public CommandInterruptedException(String message) {
        super(message);
    }

    public CommandInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}

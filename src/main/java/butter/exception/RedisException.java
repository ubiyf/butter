package butter.exception;

/**
 * Created with IntelliJ IDEA.
 * User: lizhongyuan
 * Date: 13-11-16
 * Time: 上午2:22
 */
public class RedisException extends RuntimeException {
    public RedisException(String message) {
        super(message);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }
}

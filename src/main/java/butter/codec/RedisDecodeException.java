package butter.codec;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 上午10:25
 */
public class RedisDecodeException extends RuntimeException
{
    public RedisDecodeException(String message)
    {
        super(message);
    }

    public RedisDecodeException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

package butter.protocol.replies;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 下午1:49
 */
public class IntegerReply {
    private final long integer;

    public IntegerReply(long integer) {
        this.integer = integer;
    }

    public long getInteger() {
        return integer;
    }
}

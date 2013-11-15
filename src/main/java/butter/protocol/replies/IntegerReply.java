package butter.protocol.replies;

import butter.protocol.Reply;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 下午1:49
 */
public class IntegerReply extends Reply
{
    private final long integer;

    public IntegerReply(long integer)
    {
        this.integer = integer;
    }

    public long getInteger()
    {
        return integer;
    }
}

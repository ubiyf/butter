package butter.protocol.replies;

import butter.protocol.Reply;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 下午1:40
 */
public class StatusReply extends Reply
{
    private final String status;

    public StatusReply(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
}

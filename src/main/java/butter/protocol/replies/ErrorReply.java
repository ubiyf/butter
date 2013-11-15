package butter.protocol.replies;

import butter.protocol.Reply;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 下午1:45
 */
public class ErrorReply extends Reply {
    private final String error;

    public ErrorReply(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}

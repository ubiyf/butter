package butter.protocol.replies;

import butter.protocol.Reply;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-19
 * Time: 下午5:55
 */
public class BulkReply extends Reply {
    private final byte[] data;

    public BulkReply(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}

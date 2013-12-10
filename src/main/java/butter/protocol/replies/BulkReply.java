package butter.protocol.replies;

import javax.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-19
 * Time: 下午5:55
 */
public class BulkReply {
    private final byte[] data;

    public BulkReply(@Nullable byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}

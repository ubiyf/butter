package butter.protocol;

import com.google.common.base.Charsets;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-17
 * Time: 上午11:08
 */
public enum InsertPos {
    BEFORE,
    AFTER;

    public final byte[] bytes;

    InsertPos() {
        bytes = name().getBytes(Charsets.US_ASCII);
    }
}

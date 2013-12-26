package butter.connection.protocol;

import com.google.common.base.Charsets;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-13
 * Time: 下午2:42
 */
public enum BitOPs {
    AND,
    OR,
    XOR,
    NOT;
    public final byte[] bytes;

    BitOPs() {
        this.bytes = name().getBytes(Charsets.US_ASCII);
    }
}

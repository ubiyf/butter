package butter.protocol;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-19
 * Time: 下午2:01
 */
public enum Commands {
    SET;

    public final byte[] bytes;

    private Commands() {
        this.bytes = name().getBytes(Charsets.ASCII);
    }
}

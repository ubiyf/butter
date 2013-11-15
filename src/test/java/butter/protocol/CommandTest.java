package butter.protocol;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-15
 * Time: 下午6:45
 */
public class CommandTest {

    @Test
    public void testEncode() {
        Command cmd = new Command();
        cmd.addArg("SET".getBytes());
        cmd.addArg("mykey".getBytes());
        cmd.addArg("myvalue".getBytes());
        cmd.encode();
    }
}

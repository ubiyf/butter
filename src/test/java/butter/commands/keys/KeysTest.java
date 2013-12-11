package butter.commands.keys;

import butter.commands.RedisTest;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-11
 * Time: 下午5:43
 */
public class KeysTest extends RedisTest {

    @Test
    public void delTest() {
        final byte[] key1 = "key1".getBytes();
        final byte[] key2 = "key2".getBytes();
        final byte[] key3 = "key3".getBytes();

        connection.set(key1, "Hello".getBytes());
        connection.set(key2, "World".getBytes());

        Assert.assertEquals(2, connection.del(key1, key2, key3));
    }

    @Test
    public void dumpTest() {
        final byte[] key1 = "key1".getBytes();
        final byte[] hello = "Hello".getBytes();
        connection.set(key1, hello);
        byte[] dump = connection.dump(key1);
        long delNum = connection.del(key1);
        connection.restore(key1, 0, dump);
        Assert.assertEquals(1, delNum);
        byte[] value = connection.get(key1);
        for (int i = 0; i < value.length; i++)
            Assert.assertEquals(value[i], hello[i]);
    }
}

package butter.connection.codec;

import butter.connection.protocol.Command;
import io.netty.util.AttributeKey;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created with IntelliJ IDEA.
 * User: lizhongyuan
 * Date: 13-11-16
 * Time: 下午4:29
 */
public interface Attribute {
    AttributeKey<ConcurrentLinkedQueue<Command>> CMD_QUEUE = AttributeKey.valueOf("cmdQueue");
}

package butter.codec;

import butter.exception.RedisDecodeException;
import butter.exception.RedisException;
import butter.protocol.Command;
import butter.protocol.Reply;
import butter.protocol.replies.BulkReply;
import butter.protocol.replies.IntegerReply;
import butter.protocol.replies.StatusReply;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static butter.codec.Attribute.CMD_QUEUE;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-14
 * Time: 下午8:21
 */
public class ReplyDecoder extends ByteToMessageDecoder {
    private STATE state = STATE.PREFIX;
    private ConcurrentLinkedQueue<Command> cmdQueue;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        cmdQueue = new ConcurrentLinkedQueue<>();
        ctx.channel().attr(CMD_QUEUE).set(cmdQueue);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf frame, List<Object> objects) throws Exception {
        if (!frame.isReadable())
            return;

        switch (state) {
            case PREFIX:
                switch (frame.readByte()) {
                    case '+':
                        state = STATE.STATUS;
                        break;
                    case '-':
                        state = STATE.ERROR;
                        break;
                    case ':':
                        state = STATE.INTEGER;
                        break;
                    case '$':
                        state = STATE.BULK;
                        break;
                    case '*':
                        state = STATE.MULTI_BULK;
                        break;
                    default:
                        throw new RedisDecodeException("Invalid first byte");
                }
            case STATUS: {
                byte[] statusData = new byte[frame.readableBytes()];
                frame.readBytes(statusData);
                String status = new String(statusData, Charset.forName("US-ASCII"));
                Reply reply = new StatusReply(status);
                cmdQueue.poll().set(reply);
                break;
            }
            case ERROR: {
                byte[] errorData = new byte[frame.readableBytes()];
                frame.writeBytes(errorData);
                String error = new String(errorData, Charset.forName("US-ASCII"));
                cmdQueue.poll().setException(new RedisException(error));
                break;
            }
            case INTEGER: {
                byte[] integerData = new byte[frame.readableBytes()];
                frame.writeBytes(integerData);
                Reply reply = new IntegerReply(bytesToLong(integerData));
                cmdQueue.poll().set(reply);
                break;
            }
            case BULK: {
                if (objects.size() == 0) {
                    byte[] integerData = new byte[frame.readableBytes()];
                    frame.writeBytes(integerData);
                    objects.add(bytesToInt(integerData));
                    return;
                } else {
                    int dataLength = (int) objects.get(0);
                    byte[] data = new byte[dataLength];
                    frame.writeBytes(data);
                    Reply reply = new BulkReply(data);
                    cmdQueue.poll().set(reply);
                }
                break;
            }
            case MULTI_BULK: {

                break;
            }
        }

        resetState();
    }

    /**
     * only for positive int
     *
     * @param b
     */
    private int bytesToInt(byte[] b) {
        if (b == null || b.length <= 0)
            return 0;
        int result = 0;
        byte byteZero = '0';
        int mulMax = Integer.MAX_VALUE / 10;
        for (int i = 0; i < b.length; i++) {
            if (result > mulMax) {
                throw new NumberFormatException("For input data:" + new String(b) + "!");
            }
            result *= 10;
            int digit = b[i] - byteZero;
            if (result > Integer.MAX_VALUE - digit) {
                throw new NumberFormatException("For input data:" + new String(b) + "!");
            }
            result += digit;
        }
        return result;
    }

    private long bytesToLong(byte[] b) {
        if (b == null || b.length <= 0)
            return 0;
        long result = 0;
        byte byteZero = '0';
        long mulMax = Long.MAX_VALUE / 10;
        for (int i = 0; i < b.length; i++) {
            if (result > mulMax) {
                throw new NumberFormatException("For input data:" + new String(b) + "!");
            }
            result *= 10;
            int digit = b[i] - byteZero;
            if (result > Long.MAX_VALUE - digit) {
                throw new NumberFormatException("For input data:" + new String(b) + "!");
            }
            result += digit;
        }
        return result;
    }

    private void resetState() {
        state = STATE.PREFIX;
    }

    enum STATE {
        PREFIX,
        STATUS,
        ERROR,
        INTEGER,
        BULK,
        MULTI_BULK
    }
}

package butter.codec;

import butter.exception.RedisDecodeException;
import butter.exception.RedisException;
import butter.protocol.Command;
import butter.protocol.Reply;
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
                String integer = new String(integerData);
                Reply reply = new IntegerReply(Long.parseLong(integer));
                cmdQueue.poll().set(reply);
                break;
            }
            case BULK:

                break;
            case MULTI_BULK:

                break;
        }

        resetState();
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

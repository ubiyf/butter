package butter.codec;

import butter.exception.RedisDecodeException;
import butter.protocol.Reply;
import butter.protocol.replies.ErrorReply;
import butter.protocol.replies.IntegerReply;
import butter.protocol.replies.StatusReply;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-14
 * Time: 下午8:21
 */
public class ReplyDecoder extends ByteToMessageDecoder {
    private STATE state = STATE.PREFIX;

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
                objects.add(reply);
                break;
            }
            case ERROR: {
                byte[] errorData = new byte[frame.readableBytes()];
                frame.writeBytes(errorData);
                String status = new String(errorData);
                Reply reply = new ErrorReply(status);
                objects.add(reply);
                break;
            }
            case INTEGER: {
                byte[] integerData = new byte[frame.readableBytes()];
                frame.writeBytes(integerData);
                String integer = new String(integerData);
                Reply reply = new IntegerReply(Long.parseLong(integer));
                objects.add(reply);
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

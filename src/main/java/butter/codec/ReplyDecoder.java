package butter.codec;

import butter.exception.RedisDecodeException;
import butter.exception.RedisException;
import butter.protocol.Command;
import butter.protocol.replies.BulkReply;
import butter.protocol.replies.IntegerReply;
import butter.protocol.replies.MultiBulkReply;
import butter.protocol.replies.StatusReply;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.ArrayList;
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
    private STATE bulkState;
    private List<Object> multiBulkReplies;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        cmdQueue = new ConcurrentLinkedQueue<>();
        ctx.channel().attr(CMD_QUEUE).set(cmdQueue);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf frame, List<Object> objects) throws Exception {
        if (!frame.isReadable())
            return;

        if (decoder(frame, objects))
            resetState();
    }

    /**
     * @return if decode is finished
     */
    @SuppressWarnings("unchecked")
    private boolean decoder(ByteBuf frame, List<Object> tmpObjects) {
        int originTmpNum = tmpObjects.size();
        char firstChar;
        switch (state) {
            case PREFIX:
                firstChar = (char) frame.readByte();
                switch (firstChar) {
                    case '+': {
                        state = STATE.STATUS;
                        String status = parseStringData(frame);
                        StatusReply reply = new StatusReply(status);
                        cmdQueue.poll().set(reply);
                        break;
                    }
                    case '-': {
                        state = STATE.ERROR;
                        String error = parseStringData(frame);
                        cmdQueue.poll().setException(new RedisException(error));
                        break;
                    }
                    case ':': {
                        state = STATE.INTEGER;
                        long number = parseNumberData(frame);
                        IntegerReply reply = new IntegerReply(number);
                        cmdQueue.poll().set(reply);
                        break;
                    }
                    case '$': {
                        state = STATE.BULK;
                        long dataLen = parseNumberData(frame);
                        if (dataLen > 0) {
                            tmpObjects.add(dataLen);
                        } else {
                            BulkReply reply = new BulkReply(null);
                            cmdQueue.poll().set(reply);
                        }
                        break;
                    }
                    case '*': {
                        state = STATE.MULTI_BULK;
                        long replyNum = parseNumberData(frame);
                        if (replyNum > 0) {
                            tmpObjects.add(replyNum);
                            multiBulkReplies = new ArrayList<>((int) replyNum);
                            bulkState = STATE.PREFIX;
                        } else {
                            MultiBulkReply reply = new MultiBulkReply(null);
                            cmdQueue.poll().set(reply);
                        }
                        break;
                    }
                    default:
                        throw new RedisDecodeException("Invalid first byte: " + firstChar);
                }
                break;
            case BULK: {
                int dataLength = (int) tmpObjects.get(0);
                byte[] data = new byte[dataLength];
                frame.writeBytes(data);
                BulkReply reply = new BulkReply(data);
                cmdQueue.poll().set(reply);
                break;
            }
            case MULTI_BULK: {

                break;
            }
            default:
                throw new RedisDecodeException("should not be here!");
        }

        return originTmpNum == tmpObjects.size();
    }

    private boolean multiBulkDecoder(ByteBuf frame, int total) {
        char firstChar = (char) frame.readByte();
        switch (bulkState) {
            case PREFIX:
                switch (firstChar) {
                    case '+': {
                        bulkState = STATE.STATUS;
                        String status = parseStringData(frame);
                        multiBulkReplies.add(status);
                        break;
                    }
                    case ':': {
                        state = STATE.INTEGER;
                        long number = parseNumberData(frame);
                        multiBulkReplies.add(number);
                        break;
                    }
                    case '$': {
                        bulkState = STATE.BULK;
                        long dataLen = parseNumberData(frame);
                        if (dataLen > 0) {
                            tmpObjects.add(dataLen);
                        } else {
                            multiBulkReplies.add(null);
                        }
                        break;
                    }
                    case '*': {
                        break;
                    }
                    default:
                        throw new RedisDecodeException("Invalid first byte: " + firstChar);
                }
                break;
            case BULK:

                break;
            case MULTI_BULK:
                break;
        }


        return multiBulkReplies.size() == total;
    }

    private String parseStringData(ByteBuf frame) {
        byte[] stringData = new byte[frame.readableBytes()];
        frame.readBytes(stringData);
        return new String(stringData, Charset.forName("US-ASCII"));
    }

    private long parseNumberData(ByteBuf frame) {
        byte[] integerData = new byte[frame.readableBytes()];
        frame.writeBytes(integerData);
        return bytesToLong(integerData);
    }

    /**
     * only for positive long
     *
     * @param b
     */
    private long bytesToLong(byte[] b) {
        if (b == null) {
            throw new NumberFormatException("null");
        }

        if (b.length == 0) {
            throw new NumberFormatException("zero length data");
        }

        int radix = 10;
        long result = 0;
        boolean negative = false;
        int i = 0, len = b.length;
        long limit = -Long.MAX_VALUE;
        long multmin;
        int digit;

        if (len > 0) {
            char firstChar = (char) b[0];
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+') {
                    throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
                }

                if (len == 1) {// Cannot have lone "+" or "-"
                    throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
                }
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit((char) b[i++], radix);
                if (digit < 0) {
                    throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
                }
                if (result < multmin) {
                    throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
                }
                result *= radix;
                if (result < limit + digit) {
                    throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
                }
                result -= digit;
            }
        } else {
            throw new NumberFormatException("For input string: \"" + new String(b) + "\"");
        }
        return negative ? result : -result;
    }

    private void resetState() {
        state = STATE.PREFIX;
        bulkState = null;
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

package butter.codec;

import butter.exception.RedisDecodeException;
import butter.exception.RedisException;
import butter.protocol.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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
    private Stack<MultiBulkState> multiBulkStates = new Stack<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        cmdQueue = new ConcurrentLinkedQueue<>();
        ctx.channel().attr(CMD_QUEUE).set(cmdQueue);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf frame, List<Object> objects) throws Exception {
        if (!frame.isReadable())
            return;

        final boolean needMoreFrames = true;
        if (!decoder(frame)) {
            objects.add(needMoreFrames);
        }
    }

    /**
     * @return if decode is finished
     */
    @SuppressWarnings("unchecked")
    private boolean decoder(ByteBuf frame) {
        switch (state) {
            case PREFIX:
                char firstChar = (char) frame.readByte();
                switch (firstChar) {
                    case '+': {
                        String status = parseStringData(frame);
                        cmdQueue.poll().set(status);
                        break;
                    }
                    case '-': {
                        String error = parseStringData(frame);
                        cmdQueue.poll().setException(new RedisException(error));
                        break;
                    }
                    case ':': {
                        long number = parseNumberData(frame);
                        cmdQueue.poll().set(number);
                        break;
                    }
                    case '$': {
                        long dataLen = parseNumberData(frame);
                        if (dataLen > 0) {
                            state = STATE.BULK;
                            return false;
                        } else {
                            cmdQueue.poll().set(null);
                        }
                        break;
                    }
                    case '*': {
                        long replyNum = parseNumberData(frame);
                        if (replyNum > 0) {
                            state = STATE.MULTI_BULK;
                            MultiBulkState mbs = new MultiBulkState((int) replyNum);
                            multiBulkStates.push(mbs);
                            return false;
                        } else {
                            cmdQueue.poll().set(null);
                        }
                        break;
                    }
                    default:
                        throw new RedisDecodeException("Invalid first byte: " + firstChar);
                }
                break;
            case BULK: {
                byte[] data = new byte[frame.readableBytes()];
                frame.readBytes(data);
                cmdQueue.poll().set(data);
                state = STATE.PREFIX;
                break;
            }
            case MULTI_BULK: {
                if (multiBulkDecoder(frame)) {
                    MultiBulkState mbs = multiBulkStates.pop();
                    cmdQueue.poll().set(mbs.getReplies());
                    state = STATE.PREFIX;
                } else {
                    return false;
                }
                break;
            }
            default:
                throw new RedisDecodeException("should not be here!");
        }

        return true;
    }

    private boolean multiBulkDecoder(ByteBuf frame) {
        MultiBulkState curState = multiBulkStates.peek();
        switch (curState.getState()) {
            case PREFIX:
                char firstChar = (char) frame.readByte();
                switch (firstChar) {
                    case '+': {
                        String status = parseStringData(frame);
                        curState.getReplies().add(status);
                        break;
                    }
                    case ':': {
                        long number = parseNumberData(frame);
                        curState.getReplies().add(number);
                        break;
                    }
                    case '$': {
                        long dataLen = parseNumberData(frame);
                        if (dataLen > 0) {
                            curState.setState(STATE.BULK);
                        } else {
                            curState.getReplies().add(null);
                        }
                        break;
                    }
                    case '*': {
                        long replyNum = parseNumberData(frame);
                        if (replyNum > 0) {
                            curState.setState(STATE.MULTI_BULK);
                            MultiBulkState mbs = new MultiBulkState((int) replyNum);
                            multiBulkStates.push(mbs);
                        } else {
                            curState.getReplies().add(null);
                        }
                        break;
                    }
                    default:
                        throw new RedisDecodeException("Invalid first byte: " + firstChar);
                }
                break;
            case BULK:
                byte[] data = new byte[frame.readableBytes()];
                frame.readBytes(data);
                curState.getReplies().add(data);
                curState.setState(STATE.PREFIX);
                break;
            case MULTI_BULK:
                if (multiBulkDecoder(frame)) {
                    MultiBulkState innerState = multiBulkStates.pop();
                    curState.getReplies().add(innerState.getReplies());
                    curState.setState(STATE.PREFIX);
                }
                break;
        }

        return curState.isDecodeFinished();
    }

    private String parseStringData(ByteBuf frame) {
        byte[] stringData = new byte[frame.readableBytes()];
        frame.readBytes(stringData);
        return new String(stringData, Charset.forName("US-ASCII"));
    }

    private long parseNumberData(ByteBuf frame) {
        byte[] integerData = new byte[frame.readableBytes()];
        frame.readBytes(integerData);
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
    }

    enum STATE {
        PREFIX,
        STATUS,
        ERROR,
        INTEGER,
        BULK,
        MULTI_BULK
    }

    class MultiBulkState {
        private STATE state = STATE.PREFIX;
        private List<Object> replies;
        private int replyNum;

        MultiBulkState(int replyNum) {
            this.replyNum = replyNum;
            replies = new ArrayList<>(replyNum);
        }

        public STATE getState() {
            return state;
        }

        public void setState(STATE state) {
            this.state = state;
        }

        public List<Object> getReplies() {
            return replies;
        }

        public void setReplies(List<Object> replies) {
            this.replies = replies;
        }

        public boolean isDecodeFinished() {
            return replyNum == replies.size();
        }
    }
}

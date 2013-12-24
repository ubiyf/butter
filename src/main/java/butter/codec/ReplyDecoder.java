package butter.codec;

import butter.exception.RedisDecodeException;
import butter.exception.RedisException;
import butter.protocol.Command;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

import static butter.codec.Attribute.CMD_QUEUE;
import static butter.util.NumberUtil.bytesToInteger;

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
        return new String(stringData, Charsets.US_ASCII);
    }

    private long parseNumberData(ByteBuf frame) {
        byte[] integerData = new byte[frame.readableBytes()];
        frame.readBytes(integerData);
        return bytesToInteger(integerData);
    }

    enum STATE {
        PREFIX,
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

        public boolean isDecodeFinished() {
            if (replyNum == replies.size()) {
                if (multiBulkStates.size() > 1) {

                }
            }
            return false;
        }
    }
}

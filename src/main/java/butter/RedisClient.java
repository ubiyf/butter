package butter;

import butter.codec.CommandEncoder;
import butter.codec.ReplyDecoder;
import butter.codec.ReplyHandler;
import butter.connection.RedisConnection;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-14
 * Time: 下午8:09
 */
public class RedisClient {
    private final String host;
    private final int port;
    private Bootstrap bootstrap;

    public RedisClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws Exception {
        // Configure the client.
        final EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new LoggingHandler(LogLevel.INFO),
                                new CommandEncoder(),
                                new DelimiterBasedFrameDecoder(512 * 1024 * 1024, Unpooled.wrappedBuffer(new byte[]{'\r', '\n'})),
                                new ReplyDecoder(),
                                new ReplyHandler()
                        );
                    }
                });
    }

    public RedisConnection connect() throws InterruptedException {
        Channel channel = bootstrap.connect(host, port).sync().channel();
        return new RedisConnection(channel);
    }

    public void shutdown() {
        bootstrap.group().shutdownGracefully();
    }
}

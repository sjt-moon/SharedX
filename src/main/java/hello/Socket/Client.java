package hello.Socket;

import hello.Utils.MetaUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public final class Client implements Runnable {

    private final ClientHandler clientHandler = new ClientHandler();

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    //new LoggingHandler(LogLevel.TRACE),
                                    new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()),

                                    // if i remove string en/de-coder, no message appears
                                    new StringEncoder(),
                                    new StringDecoder(),
                                    clientHandler);
                        }
                    });

            // Start the connection attempt.
            bootstrap.connect(MetaUtils.HOST, MetaUtils.PORT).sync().channel().closeFuture().sync();
            System.out.println("Message sent successfully.");
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getStackTrace());
        }
        finally {
            group.shutdownGracefully();
        }

    }

    /**
     * send message to server
     * @param message message to be sent
     */
    public void sendMessage(String message) {
        clientHandler.sendMessage(message);
    }
}
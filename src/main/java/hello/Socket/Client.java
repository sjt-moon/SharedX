package hello.Socket;

import hello.Utils.Message;
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
import com.vaadin.ui.*;

public final class Client implements Runnable {
    /**
     * the unique id for this client
     */
    private final String urlId;

    private String origin = "";

    /**
     * previous text read from text editor
     */
    private String prevText = "";

    /**
     * current text read from text editor
     */
    private String currText = "";

    /**
     * UI text area
     */
    TextArea textArea;

    private final ClientHandler clientHandler;

    public Client(String urlId, TextArea textArea) {
        super();
        this.urlId = urlId;
        clientHandler = new ClientHandler(urlId);
    }

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
     * send message (Message.class, a {urlId, text} json) to server
     * @param message Message structure to be sent
     */
    public void sendMessage(Message message) {
        clientHandler.sendMessage(message.toString());
    }

    public String getCurrText() {
        return currText;
    }

    public String getPrevText() {
        return prevText;
    }

    public String getOrigin() {
        return origin;
    }

    public void setCurrText(String currText) {
        this.currText = currText;
    }

    public void setPrevText(String prevText) {
        this.prevText = prevText;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }
}
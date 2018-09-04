package hello.Socket;

import hello.Utils.Message;
import hello.Utils.ThreadSafeQueue;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Stack;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * the unique id for this client
     */
    private final String urlId;

    private ChannelHandlerContext ctx;

    public ThreadSafeQueue<Message> receivedMessageQueue = new ThreadSafeQueue<>();

    ClientHandler(String urlId) {
        super();
        this.urlId = urlId;
    }

    void sendMessage(String message) {
        if (ctx != null) {
            ctx.writeAndFlush(message);

            System.out.println("Send: \n" + message);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("I am activated.");

        this.ctx = ctx;

        String message = "message from client.";
        // ctx.writeAndFlush(message + MetaUtils.LINE_SEPARATOR);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Error caught in the communication service: " + cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        System.out.println("Received message: " + message);

        Message recv = Message.getMessage(message);
        /*
        * if recv is not parsed successfully, recv is null
        * if recv is sent from this client, ignore it. only care for other clients messages */
        if (recv == null || recv.getUrlId().equalsIgnoreCase(urlId)) {
            return;
        }
        receivedMessageQueue.add(recv);
    }
}
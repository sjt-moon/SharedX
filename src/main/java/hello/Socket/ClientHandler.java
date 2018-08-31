package hello.Socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private ChannelHandlerContext ctx;

    public void sendMessage(String message) {
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
        // ctx.writeAndFlush(message + MetaUtils.LINE_SEPARATOR);
    }
}
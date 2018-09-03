package hello.Socket;

import hello.Utils.MetaUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private final ChannelGroup clients;

    ServerHandler(ChannelGroup clients) {
        super();
        this.clients = clients;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String message = "message from server.";

        clients.add(ctx.channel());
        ctx.writeAndFlush(message + MetaUtils.LINE_SEPARATOR);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Error in receiving message.");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        System.out.println("Received message: " + message);

        /*
        * broadcast to all connected clients */
        clients.forEach(c -> c.writeAndFlush(message + MetaUtils.LINE_SEPARATOR));
    }
}
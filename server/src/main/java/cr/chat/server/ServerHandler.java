package cr.chat.server;

import cr.chat.common.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.HashMap;

public class ServerHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " Say : " + new String(msg.getBody()));
//        ctx.writeAndFlush(new ChatMessage(new HashMap<>(), msg.getBody()));
        ctx.writeAndFlush(new ChatMessage(new HashMap<>(), "Received your message !\n".getBytes()));
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
        String message = "Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n";
        ctx.writeAndFlush(new ChatMessage(new HashMap<>(), message.getBytes()));

        super.channelActive(ctx);
    }
}

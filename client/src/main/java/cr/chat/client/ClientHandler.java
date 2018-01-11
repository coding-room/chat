package cr.chat.client;

import cr.chat.common.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<ChatMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {

        System.out.println("Server say : " + new String(msg.getBody()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active ");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close ");
        super.channelInactive(ctx);
    }
}

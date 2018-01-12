package cr.chat.server;

import com.alibaba.fastjson.JSON;
import cr.chat.common.ChatMessage;
import cr.chat.common.MessageConstant;
import cr.chat.common.message.CreateRoom;
import cr.chat.server.client.ClientManager;
import cr.chat.server.client.ClientDomain;
import cr.chat.server.common.util.SpringUtils;
import cr.chat.server.room.ChatRoomExistException;
import cr.chat.server.room.ChatRoomManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static cr.chat.common.MessageConstant.*;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Autowired
    private ClientManager clientManager;
    @Autowired
    private ChatRoomManager chatRoomManager;

    private ClientDomain clientDomain;

    @Autowired
    private MessageManager messageManager;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        messageManager.handle(clientDomain, msg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
//        String message = "Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n";
//        ctx.writeAndFlush(new ChatMessage(new HashMap<>(), message.getBytes()));
        log.info("RemoteAddress : " + ctx.channel().remoteAddress() + " active !");
        clientDomain = SpringUtils.getBean(ClientDomain.class);
        clientDomain.setCtx(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}

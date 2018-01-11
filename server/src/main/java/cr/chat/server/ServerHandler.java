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
public class ServerHandler extends SimpleChannelInboundHandler<ChatMessage> {

    @Autowired
    private ClientManager clientManager;
    @Autowired
    private ChatRoomManager chatRoomManager;

    private ClientDomain clientDomain;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        Map<String, String> headers = msg.getHeaders();
        if (!headers.containsKey(MessageConstant.ACTION_HEADER_KEY)) {
            ctx.writeAndFlush(new ChatMessage(new HashMap<>(), "No message!".getBytes()));
            return;
        }
        Map<String, String> respHeaders = new HashMap<>();
        if (StringUtils.isEmpty(clientDomain.getName())) {
            respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.CREATE_NAME);
            ctx.writeAndFlush(new ChatMessage(respHeaders, "require name".getBytes()));
            return;
        }

        String actionKey = headers.get(MessageConstant.ACTION_HEADER_KEY);
        switch (actionKey) {
            case CREATE_ROOM:
                try {
                    String json = new String(msg.getBody());
                    CreateRoom createRoom = JSON.parseObject(json, CreateRoom.class);
                    clientDomain.createRoom(createRoom.getCode(), createRoom.getName());
                } catch (ChatRoomExistException existException) {
                    respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                    ctx.writeAndFlush(new ChatMessage(respHeaders, "房间编号已存在".getBytes()));
                } catch (Exception e) {
                    respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                    ctx.writeAndFlush(new ChatMessage(respHeaders, e.getMessage().getBytes()));
                }
                break;
            case JOIN_ROOM:
                break;
            case LEAVE_ROOM:
                break;
            default:
                ctx.writeAndFlush(new ChatMessage(new HashMap<>(), "No action!".getBytes()));
        }
        System.out.println(ctx.channel().remoteAddress() + " Say : " + new String(msg.getBody()));
//        ctx.writeAndFlush(new ChatMessage(new HashMap<>(), msg.getBody()));
        ctx.writeAndFlush(new ChatMessage(new HashMap<>(), "Received your message !\n".getBytes()));
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
//        String message = "Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n";
//        ctx.writeAndFlush(new ChatMessage(new HashMap<>(), message.getBytes()));
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

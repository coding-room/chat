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
        Map<String, String> headers = msg.getHeaders();
        if (!headers.containsKey(MessageConstant.ACTION_HEADER_KEY)) {
            ctx.writeAndFlush(new ChatMessage(new HashMap<>(), "No message!".getBytes()));
            return;
        }
        Map<String, String> respHeaders = new HashMap<>();
        if (StringUtils.isEmpty(clientDomain.getName())) {
            respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.CREATE_NAME);
//            ctx.writeAndFlush(new ChatMessage(respHeaders, "require name".getBytes()));
            ctx.channel().writeAndFlush(new ChatMessage(respHeaders, "require name".getBytes()));
            return;
        }

        String actionKey = headers.get(MessageConstant.ACTION_HEADER_KEY);
        log.info("actionKey:{}", actionKey);
        log.info("body:{}",new String(msg.getBody()));
        switch (actionKey) {
            case CREATE_ROOM:
                try {
                    String json = new String(msg.getBody());
                    CreateRoom createRoom = JSON.parseObject(json, CreateRoom.class);
                    clientDomain.createRoom(createRoom.getCode(), createRoom.getName());
                } catch (ChatRoomExistException existException) {
                    respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                    messageManager.sendMessage(ctx, "房间编号已存在", headers);
                } catch (Exception e) {
                    respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                    ctx.writeAndFlush(new ChatMessage(respHeaders, e.getMessage().getBytes()));
                }
                break;
            case JOIN_ROOM:
                try {
                    String roomCode = new String(msg.getBody());
                    boolean result = clientDomain.joinRoom(roomCode);
                    if (result) {
                        //发信息
                        messageManager.sendMessage(ctx, "成功加入该房间", headers);
                    } else {
                        respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                        messageManager.sendMessage(ctx, "房间编号不存在", headers);
                    }
                } catch (Exception e) {
                    respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                    ctx.writeAndFlush(new ChatMessage(respHeaders, e.getMessage().getBytes()));
                }
                break;
            case LEAVE_ROOM:
                try {
                    String roomCode = new String(msg.getBody());
                    boolean result = clientDomain.leaveRoom(roomCode);
                    if (result) {
                        //发信息
                    } else {
                        respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                        messageManager.sendMessage(ctx, "房间编号不存在", headers);
                    }
                } catch (Exception e) {
                    respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                    ctx.writeAndFlush(new ChatMessage(respHeaders, e.getMessage().getBytes()));
                }
                break;
            case SEND_MESSAGE:
                String roomCode = headers.get(ROME_CODE_HEAD_KEY);
                if (chatRoomManager.checkExist(roomCode)) {
                    try {
                        boolean result = clientDomain.sendMessage(roomCode, new String(msg.getBody()));
                        if (!result) {
                            respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                            messageManager.sendMessage(ctx, "您还没加入改房间", headers);
                        }
                    } catch (Exception e) {
                        respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                        ctx.writeAndFlush(new ChatMessage(respHeaders, e.getMessage().getBytes()));
                    }
                } else {
                    respHeaders.put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
                    messageManager.sendMessage(ctx, "房间编号不存在", headers);
                }

                break;

            default:
                messageManager.sendMessage(ctx, "No action!", headers);
        }
//        System.out.println(ctx.channel().remoteAddress() + " Say : " + new String(msg.getBody()));
//        ctx.writeAndFlush(new ChatMessage(new HashMap<>(), msg.getBody()));
//        ctx.writeAndFlush(new ChatMessage(new HashMap<>(), "Received your message !\n".getBytes()));
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

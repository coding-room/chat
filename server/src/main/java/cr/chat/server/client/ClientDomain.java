package cr.chat.server.client;

import cr.chat.server.room.ChatRoom;
import cr.chat.server.room.ChatRoomExistException;
import cr.chat.server.room.ChatRoomManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ClientDomain {

    @Autowired
    private ChatRoomManager chatRoomManager;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名字
     */
    private String name;

    private ChannelHandlerContext ctx;
    /**
     * 所在房间
     */
    private List<ChatRoom> chatRooms = new ArrayList<>();
    private Map<String, ChatRoom> chatRoomMap = new HashMap<>();


    public void createRoom(String code, String name) throws ChatRoomExistException {
        ChatRoom chatRoom = chatRoomManager.createChatRoom(this, code, name);
        chatRooms.add(chatRoom);
        chatRoomMap.put(code, chatRoom);
        log.info("成功加入房间，'{}'->'{}'", code, name);
    }

    public boolean leaveRoom(String code) {
        ChatRoom chatRoom = chatRoomManager.getChatRoom(code);
        if (chatRoom != null && chatRooms.contains(chatRoom)) {
            chatRooms.remove(chatRoom);
            chatRoomMap.remove(code);
            chatRoom.leave(this);
            log.info("成功离开房间，'{}'", code);
            return true;
        }
        log.info("房间编号'{}'不存在", code);
        return false;
    }

    public boolean joinRoom(String code) {
        ChatRoom chatRoom = chatRoomManager.getChatRoom(code);
        if (chatRoom != null) {
            chatRoom.join(this);
            chatRooms.add(chatRoom);
            chatRoomMap.put(code, chatRoom);
            log.info("成功加入房间，'{}'", code);
            return true;
        }
        log.info("房间编号'{}'不存在", code);
        return false;
    }


    /**
     * 发送消息
     * @param code
     * @param message
     * @return
     */
    public boolean sendMessage(String code, String message) {
        if (chatRoomMap.containsKey(code)) {
            ChatRoom chatRoom = chatRoomMap.get(code);
            chatRoom.sendMessage(this, message);
            return true;
        }
        return false;
    }

}

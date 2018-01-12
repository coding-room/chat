package cr.chat.server.room;

import cr.chat.server.client.ClientDomain;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatRoomManager {

    private Map<String, ChatRoom> chatRoomMap = new HashMap<>();

    /**
     * 创建房间
     *
     * @param master 房主
     * @return
     */
    public ChatRoom createChatRoom(ClientDomain master, String code, String name) throws ChatRoomExistException {
        if (chatRoomMap.containsKey(code)) {
            throw new ChatRoomExistException("房间号已存在");
        }
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(generateId());
        chatRoom.setMaster(master);
        chatRoom.getMembers().add(master);
        chatRoom.setName(name);
        chatRoom.setCode(code);
        chatRoomMap.put(code, chatRoom);
        return chatRoom;
    }

    public ChatRoom getChatRoom(String code) {
        return chatRoomMap.get(code);
    }

    public List<ChatRoom> getALl() {
        List<ChatRoom> all = new ArrayList<>();
        chatRoomMap.forEach((k,v)-> all.add(v));
        return all;
    }

    public boolean checkExist(String code) {
        return chatRoomMap.containsKey(code);
    }

    public void removeRoom(String code) {
        chatRoomMap.remove(code);
    }


    private String generateId() {
        return UUID.randomUUID().toString();
    }

}

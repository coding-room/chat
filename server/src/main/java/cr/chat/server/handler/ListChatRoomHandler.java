package cr.chat.server.handler;

import cr.chat.common.message.ListChatRoomMessage;
import cr.chat.common.message.bean.ChatRoomBean;
import cr.chat.server.MessageManager;
import cr.chat.server.client.ClientDomain;
import cr.chat.server.room.ChatRoom;
import cr.chat.server.room.ChatRoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beldon
 * @create 2018-01-12 下午2:38
 */
@Service
public class ListChatRoomHandler implements MessageHandler<ListChatRoomMessage> {
    @Autowired
    private MessageManager messageManager;

    @Autowired
    private ChatRoomManager chatRoomManager;

    @Override
    public void handle(ClientDomain client, ListChatRoomMessage msg) {
        List<ChatRoomBean> chatRooms = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomManager.getALl()) {
            ChatRoomBean roomBean = new ChatRoomBean();
            roomBean.setCode(chatRoom.getCode());
            roomBean.setName(chatRoom.getName());
            chatRooms.add(roomBean);
        }
        msg.setChatRooms(chatRooms);
        messageManager.sendMessage(client, msg);
    }
}

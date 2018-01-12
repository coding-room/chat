package cr.chat.server.handler;

import cr.chat.common.MessageConstant;
import cr.chat.common.message.TextMessage;
import cr.chat.server.MessageManager;
import cr.chat.server.client.ClientDomain;
import cr.chat.server.room.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Beldon
 * @create 2018-01-12 下午2:54
 */
@Service
public class TextMessageHandler implements MessageHandler<TextMessage> {

    @Autowired
    private MessageManager messageManager;

    @Override
    public void handle(ClientDomain client, TextMessage msg) {
        String roomCode = msg.getRoomCode();
        if (client.getChatRoomMap().containsKey(roomCode)) {
            ChatRoom chatRoom = client.getChatRoomMap().get(roomCode);
            chatRoom.sendMessage(client, msg.getMessage());
        } else {
            TextMessage textMessage = new TextMessage();
            textMessage.setMessage("您还没加入该房间");
            textMessage.getHeaders().put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.CREATE_ROOM);
            messageManager.sendMessage(client, textMessage);
        }

    }
}

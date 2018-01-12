package cr.chat.server.handler;

import cr.chat.common.MessageConstant;
import cr.chat.common.message.CreateRoom;
import cr.chat.common.message.TextMessage;
import cr.chat.server.MessageManager;
import cr.chat.server.client.ClientDomain;
import cr.chat.server.room.ChatRoom;
import cr.chat.server.room.ChatRoomExistException;
import cr.chat.server.room.ChatRoomManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Beldon
 * @create 2018-01-12 下午1:25
 */
@Service
@Slf4j
public class CreateRoomHandler implements MessageHandler<CreateRoom> {

    @Autowired
    private MessageManager messageManager;

    @Override
    public void handle(ClientDomain client, CreateRoom msg) {
        try {
            client.createRoom(msg.getCode(), msg.getName());
            TextMessage textMessage = new TextMessage();
            textMessage.setMessage("创建房间成功");
            textMessage.getHeaders().put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.CREATE_ROOM);
            messageManager.sendMessage(client, textMessage);
        } catch (ChatRoomExistException e) {
            log.error("创建房间失败，{}，{}", msg.getCode(), msg.getName());
            TextMessage textMessage = new TextMessage();
            textMessage.setMessage("创建房间失败,改房间已存在");
            textMessage.getHeaders().put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
            messageManager.sendMessage(client, textMessage);
        }
    }
}

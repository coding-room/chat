package cr.chat.server.handler;

import cr.chat.common.MessageConstant;
import cr.chat.common.message.JoinRoom;
import cr.chat.common.message.TextMessage;
import cr.chat.server.MessageManager;
import cr.chat.server.client.ClientDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Beldon
 * @create 2018-01-12 下午1:25
 */
@Service
public class JoinRoomHandler implements MessageHandler<JoinRoom> {
    @Autowired
    private MessageManager messageManager;

    @Override
    public void handle(ClientDomain client, JoinRoom msg) {
        boolean result = client.joinRoom(msg.getCode());
        if (result) {
            TextMessage textMessage = new TextMessage();
            textMessage.setMessage("加入房间成功");
            textMessage.getHeaders().put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.CREATE_ROOM);
            messageManager.sendMessage(client, textMessage);
        } else {
            TextMessage textMessage = new TextMessage();
            textMessage.setMessage("加入房间失败，房间号" + msg.getCode() + "不存在");
            textMessage.getHeaders().put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
            messageManager.sendMessage(client, textMessage);
        }
    }
}

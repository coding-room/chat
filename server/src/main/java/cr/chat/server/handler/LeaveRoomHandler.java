package cr.chat.server.handler;

import cr.chat.common.MessageConstant;
import cr.chat.common.message.LeaveRoom;
import cr.chat.common.message.TextMessage;
import cr.chat.server.MessageManager;
import cr.chat.server.client.ClientDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Beldon
 * @create 2018-01-12 下午1:26
 */
@Service
public class LeaveRoomHandler implements MessageHandler<LeaveRoom> {
    @Autowired
    private MessageManager messageManager;

    @Override
    public void handle(ClientDomain client, LeaveRoom msg) {
        boolean result = client.leaveRoom(msg.getRoomCode());
        TextMessage textMessage = new TextMessage();
        if (result) {
            textMessage.setMessage("离开房间成功");
            textMessage.getHeaders().put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.CREATE_ROOM);
        } else {
            textMessage.setMessage("离开房间失败，房间号" + msg.getRoomCode() + "不存在");
            textMessage.getHeaders().put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.ERROR);
        }
        messageManager.sendMessage(client, textMessage);
    }
}

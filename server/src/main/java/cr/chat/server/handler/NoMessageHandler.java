package cr.chat.server.handler;

import cr.chat.server.MessageManager;
import cr.chat.server.client.ClientDomain;
import cr.chat.server.room.ChatRoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Beldon
 * @create 2018-01-12 上午11:05
 */
@Component
public class NoMessageHandler implements MessageHandler {

    @Autowired
    private MessageManager messageManager;
    @Override
    public void handle(ClientDomain client, Object msg) {
    }
}

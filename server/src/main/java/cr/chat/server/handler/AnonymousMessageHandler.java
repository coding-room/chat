package cr.chat.server.handler;

import cr.chat.common.MessageConstant;
import cr.chat.common.message.AnonymousMessage;
import cr.chat.common.message.TextMessage;
import cr.chat.server.MessageManager;
import cr.chat.server.client.ClientDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Beldon
 * @create 2018-01-12 下午1:24
 */
@Service
public class AnonymousMessageHandler implements MessageHandler<AnonymousMessage> {

    @Autowired
    private MessageManager messageManager;

    @Override
    public void handle(ClientDomain client, AnonymousMessage msg) {
        TextMessage textMessage = new TextMessage();
        textMessage.setMessage("require name");
        textMessage.getHeaders().put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.CREATE_NAME);
        messageManager.sendMessage(client, textMessage);
    }
}

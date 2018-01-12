package cr.chat.server.handler;

import cr.chat.common.MessageConstant;
import cr.chat.common.message.CreateName;
import cr.chat.common.message.TextMessage;
import cr.chat.server.MessageManager;
import cr.chat.server.client.ClientDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Beldon
 * @create 2018-01-12 上午11:05
 */
@Service
@Slf4j
public class CreateNameHandler implements MessageHandler<CreateName> {

    @Autowired
    private MessageManager messageManager;

    @Override
    public void handle(ClientDomain client, CreateName msg) {
        log.info("修改名字，{}", msg.getName());
        client.setName(msg.getName());
        TextMessage textMessage = new TextMessage();
        textMessage.setMessage("名字修改成功");
        textMessage.getHeaders().put(MessageConstant.ACTION_HEADER_KEY, MessageConstant.CREATE_NAME);
        messageManager.sendMessage(client, textMessage);
    }
}

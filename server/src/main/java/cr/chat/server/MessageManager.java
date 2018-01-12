package cr.chat.server;

import com.alibaba.fastjson.JSON;
import cr.chat.common.ChatMessage;
import cr.chat.common.MessageConstant;
import cr.chat.common.message.*;
import cr.chat.server.client.ClientDomain;
import cr.chat.server.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cr.chat.common.MessageConstant.*;

/**
 * @author Beldon
 * @create 2018-01-12 上午8:57
 */
@Service
@Slf4j
public class MessageManager implements ApplicationContextAware {
    private ApplicationContext context;

    private Map<Class, List<MessageHandler>> handlers = new HashMap<>();
    private List<MessageHandler> noneHandlers = new ArrayList<>();

    @PostConstruct
    public void initHandler() {
        Map<String, MessageHandler> handlerMap = context.getBeansOfType(MessageHandler.class);
        System.out.println(handlerMap);
        handlerMap.forEach((k, v) -> {
            Type[] types = v.getClass().getGenericInterfaces();
            Type type = types[0];
            if (type instanceof ParameterizedType) {
                Type[] params = ((ParameterizedType) type).getActualTypeArguments();
                Class messageClass = (Class) params[0];
                List<MessageHandler> messageHandlers;
                if (handlers.containsKey(messageClass)) {
                    messageHandlers = handlers.get(messageClass);
                } else {
                    messageHandlers = new ArrayList<>();
                    handlers.put(messageClass, messageHandlers);
                }
                messageHandlers.add(v);
            } else {
                noneHandlers.add(v);
            }
        });
    }


    public void handle(ClientDomain client, ChatMessage msg) {
        Map<String, String> headers = msg.getHeaders();
        String action = headers.get(MessageConstant.ACTION_HEADER_KEY);
        if (StringUtils.isEmpty(action)) {
            return;
        }

        String actionKey = headers.get(MessageConstant.ACTION_HEADER_KEY);
        log.info("actionKey:{}", actionKey);
        log.info("body:{}", new String(msg.getBody()));
        BaseMessage message;

        if (CREATE_NAME.equals(action)) {
            String json = new String(msg.getBody());
            log.info(json);
            message = JSON.parseObject(json, CreateName.class);
        } else {
            if (StringUtils.isEmpty(client.getName())) {
                message = new AnonymousMessage();
            } else {
                switch (action) {
                    case CREATE_ROOM:
                        message = JSON.parseObject(new String(msg.getBody()), CreateRoom.class);
                        break;
                    case JOIN_ROOM:
                        message = JSON.parseObject(new String(msg.getBody()), JoinRoom.class);
                        break;
                    case LEAVE_ROOM:
                        message = JSON.parseObject(new String(msg.getBody()), LeaveRoom.class);
                        break;
                    case LIST_ROOM:
                        message = new ListChatRoomMessage();
                        break;
                    case SEND_MESSAGE:
                        message = JSON.parseObject(new String(msg.getBody()), TextMessage.class);
                        break;
                    default:
                        message = new NoMessage();
                }
            }
        }

        handle(client, message);
    }


    private void handle(ClientDomain client, BaseMessage message) {
        Class messageClass = message.getClass();
        if (handlers.containsKey(messageClass)) {
            List<MessageHandler> messageHandlers = handlers.get(messageClass);
            for (MessageHandler messageHandler : messageHandlers) {
                messageHandler.handle(client, message);
            }
        }
        for (MessageHandler noneHandler : noneHandlers) {
            noneHandler.handle(client, message);
        }

    }


    public void sendMessage(ClientDomain client, BaseMessage message) {
        sendMessage(client, message, SendMessage.TYPE_COMMON);
    }

    public void sendMessage(ClientDomain client, BaseMessage message, String type) {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(message.getHeaders());
        message.setType(type);
        client.getCtx().writeAndFlush(new ChatMessage(message.getHeaders(), JSON.toJSONString(message).getBytes()));
    }

    public void sendSysMessage(ClientDomain client, BaseMessage message) {
        sendMessage(client, message, SendMessage.TYPE_SYSTEM);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public static void main(String[] args) {
        String message = "{\"headers\":{\"action\":\"CREATE_NAME\"},\"name\":\"Beldon\"}";
        System.out.println(message);
        CreateName createName = JSON.parseObject(message, CreateName.class);
        System.out.println(createName.getName());
    }
}

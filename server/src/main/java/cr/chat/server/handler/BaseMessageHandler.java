package cr.chat.server.handler;

import com.alibaba.fastjson.JSON;
import cr.chat.common.ChatMessage;
import cr.chat.common.message.SendMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * @author Beldon
 * @create 2018-01-12 下午1:30
 */
public abstract class BaseMessageHandler<T> implements MessageHandler<T> {

}

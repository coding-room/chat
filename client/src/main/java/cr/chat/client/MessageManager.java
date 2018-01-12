package cr.chat.client;

import com.alibaba.fastjson.JSON;
import cr.chat.common.ChatMessage;
import cr.chat.common.message.SendMessage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Beldon
 * @create 2018-01-12 上午9:11
 */
@Service
public class MessageManager {
    public void sendMessage(ChannelHandlerContext ctx, String message, Map<String, String> headers) {
        SendMessage sendMessage = new SendMessage(SendMessage.TYPE_COMMON, message);
        ctx.writeAndFlush(new ChatMessage(headers, JSON.toJSONString(sendMessage).getBytes()));
    }
}

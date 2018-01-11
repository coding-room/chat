package cr.chat.common;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class ChatMessageTest {

    @Test
    public void test() {
        String message = "hello";
        ChatMessage chatMessage = new ChatMessage(new HashMap<>(), message.getBytes());
        String json = JSON.toJSONString(chatMessage);
        ChatMessage temp = JSON.parseObject(json, ChatMessage.class);
        Assert.assertEquals(message, new String(temp.getBody()));
    }
}
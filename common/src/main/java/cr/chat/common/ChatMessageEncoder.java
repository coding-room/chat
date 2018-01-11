package cr.chat.common;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatMessageEncoder extends MessageToByteEncoder<ChatMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, ByteBuf byteBuf) throws Exception {
        String json = JSON.toJSONString(chatMessage);
        log.info("json:{}", json);
        log.info("数据长度：{}", json.length());
        byteBuf.writeBytes(json.getBytes());
    }
}

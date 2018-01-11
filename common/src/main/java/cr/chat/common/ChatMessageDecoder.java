package cr.chat.common;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ChatMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int dataLength = byteBuf.readableBytes();
        log.info("数据长度：{}", dataLength);
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        String body = new String(data);
        log.info("json:{}", body);
        ChatMessage chatMessage = JSON.parseObject(body, ChatMessage.class);
        log.info("body:{}", new String(chatMessage.getBody()));
        list.add(chatMessage);
    }
}

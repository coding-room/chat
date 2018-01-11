package cr.chat.client;


import cr.chat.common.ChatMessage;
import cr.chat.common.ChatMessageDecoder;
import cr.chat.common.ChatMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

@Component
@Slf4j
public class ChatClient implements ApplicationListener<ContextRefreshedEvent> {

    public static String host = "127.0.0.1";
    public static int port = 9090;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", new ChatMessageDecoder());
                            pipeline.addLast("encoder", new ChatMessageEncoder());
                            // 客户端的逻辑
                            pipeline.addLast("handler", new ClientHandler());
                        }
                    });

            Channel ch = b.connect(host, port).sync().channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    continue;
                }

                ch.writeAndFlush(new ChatMessage(new HashMap<>(),(line + "\r\n").getBytes()));
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }
}

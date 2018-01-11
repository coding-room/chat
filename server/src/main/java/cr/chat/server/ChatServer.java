package cr.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ChatServer implements ApplicationListener<ContextRefreshedEvent> {

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Value("${chat.host:127.0.0.1}")
    String host;

    @Value("${chat.thread:5}")
    int ioThreadNum;

    @Value("${chat.backlog:1024}")
    int backlog;

    @Value("${chat.port:9090}")
    int port;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("begin to start rpc server");
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(10);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, backlog)
                //注意是childOption
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new LengthFieldPrepender(2));
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ServerHandler());
                    }
                });

        try {
            channel = serverBootstrap.bind(host, port).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("server listening on port " + port + " and ready for connections...");
    }

    @PreDestroy
    public void destroy() {
        if (channel != null) {
            channel.close();
        }
    }
}

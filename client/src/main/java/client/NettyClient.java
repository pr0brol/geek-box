package client;

import common.AbstractMessage;
import handlers.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.*;

public class NettyClient {

    private static final String host = "localhost";
    private static final int port = 8888;
    private static Channel currentChannel;

    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 1000;  //1 gb

    public void run(Window window) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            currentChannel = socketChannel;
                            socketChannel.pipeline().addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                                    new ClientHandler(window));
                        }
                    });

            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }

    public static boolean sendMsg(AbstractMessage msg) {
        try {
            currentChannel.writeAndFlush(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

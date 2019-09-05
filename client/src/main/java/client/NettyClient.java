package client;

import common.AbstractMessage;
import handlers.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.*;

import java.io.IOException;
import java.net.Socket;

public class NettyClient {

    private static final String host = "localhost";
    private static final int port = 8888;
    private static Socket socket;
    private static ObjectEncoderOutputStream ObjEncOutStream;
    private static ObjectDecoderInputStream ObjDecInStream;
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // 10 mb


    public NettyClient() {
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        socket = new Socket(host, port);
        ObjEncOutStream = new ObjectEncoderOutputStream(socket.getOutputStream());
        ObjDecInStream = new ObjectDecoderInputStream(socket.getInputStream());
        try{
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
//                                    new ClientHandler(),
                                    new ObjectEncoder(),
                                    new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null))
                                    );
                        }
                    });

            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully();
        }
    }

    public static AbstractMessage readObject() throws ClassNotFoundException, IOException {
        Object obj = ObjDecInStream.readObject();
        return (AbstractMessage) obj;
    }

    public static boolean sendMsg(AbstractMessage msg) {
        try {
            ObjEncOutStream.writeObject(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

package client;

import common.AbstractMessage;
import common.FileMessage;
import common.FileRequest;
import common.MyMessage;
import server.handlers.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NettyClient {

    private static final String host = "localhost";
    private static final int port = 8888;
    private static Socket socket;
    private static ObjectEncoderOutputStream oeos;
    private static ObjectDecoderInputStream odis;


    public NettyClient() throws IOException {
    }


    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        socket = new Socket(host, port);
        oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
        odis = new ObjectDecoderInputStream(socket.getInputStream());
        try{
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });

            ChannelFuture f = bootstrap.connect(host, port).sync();

            MyMessage msg = new MyMessage("hello server");
            oeos.writeObject(msg.getText());
            Path path = Paths.get("client/client_storage/1.mp3"); //delete
            FileMessage fm = new FileMessage(path);  //delete
            oeos.writeObject(fm);            //delete
            MyMessage msgFromServer = (MyMessage) odis.readObject();
            System.out.println(msgFromServer.getText());
            f.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully();
        }
    }

    public static AbstractMessage readObject() throws ClassNotFoundException, IOException {
        Object obj = odis.readObject();
        return (AbstractMessage) obj;
    }

    public static boolean sendMsg(AbstractMessage msg) {
        try {
            oeos.writeObject(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

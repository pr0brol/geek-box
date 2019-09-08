package handlers;

import client.Window;
import common.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final String PATH = "client/client_storage/";
    private String userName;
    private Window window;

    public ClientHandler(Window window) {
        this.window = window;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("connected is success");
        MyMessage msg = new MyMessage("hello server");
        ctx.write(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileMessage) {
                fileMessage(msg);
            }
            if (msg instanceof MyMessage) {
                System.out.println(((MyMessage) msg).getText());
            }
            if (msg instanceof FileRequest) {
                fileRequest(msg);
            }
            if (msg instanceof AuthMessage) {
                authMessage(msg);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    public void fileMessage(Object msg) throws IOException {
        FileMessage fm = (FileMessage) msg;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(PATH + userName + "/" + fm.getFilename(), true);
            fos.write(fm.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
    }

    public void fileRequest(Object msg) {
        FileRequest fr = (FileRequest) msg;
        window.getAreaServer().setItems(null);
        ObservableList<String> table = FXCollections.observableArrayList(fr.getFilesName());
        window.getAreaServer().setItems(table);
    }

    public void authMessage(Object msg) {
        AuthMessage am = (AuthMessage) msg;
        userName = am.getLogin();
//        window.closeModalWindow();
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

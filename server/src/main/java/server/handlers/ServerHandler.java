package server.handlers;

import common.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import server.auth.AuthServiceImpl;
import server.network.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static common.CommandMessage.*;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final Data data = new Data();
    private final String PATH = data.getPATH();
    private final AuthServiceImpl dataBase = new AuthServiceImpl();
    private String userName;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("client connected...");
        MyMessage msg = new MyMessage("client connected. Hello");
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
            if (msg instanceof CommandMessage) {
                commandMessage(ctx, msg);
            }
            if (msg instanceof AuthMessage) {
                authMessage(ctx, msg);
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

    public void commandMessage(ChannelHandlerContext ctx, Object msg) {
        CommandMessage cmMsg = (CommandMessage) msg;
        String command = cmMsg.getText();
        if (command.equals(DELETE)) {
            File file = new File(PATH + userName + "/" + cmMsg.getPath());
            file.delete();
        }
        if (command.equals(COPY)) {
            Path path = Paths.get(PATH + userName + "/" + cmMsg.getPath());
            try {
                FileMessage fm = new FileMessage(path);
                ctx.writeAndFlush(fm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (command.equals(REFRESH)) {
            FileRequest fr = new FileRequest(data.getFilesNames(userName));
            ctx.writeAndFlush(fr);
        }
    }

    public void authMessage(ChannelHandlerContext ctx, Object msg) {
        AuthMessage am = (AuthMessage) msg;
        if(dataBase.authUser(am)){
            userName = am.getLogin();
            ctx.writeAndFlush(am);
        }
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

package server.handlers;

import common.FileMessage;
import common.FileRequest;
import common.MyMessage;
import common.CommandMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import server.Data;

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

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("client connected...");
        MyMessage msg = new MyMessage("client connected. Hello");
        ctx.write(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        try{
            if(msg == null){
                return;
            }
            if(msg instanceof FileMessage){
                fileMessage(msg);
            }
            if(msg instanceof MyMessage){
                System.out.println(((MyMessage) msg).getText());
            }
            if(msg instanceof CommandMessage){
                commandMessage(ctx, msg);
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    public void fileMessage(Object msg) throws IOException {
        FileMessage fm = (FileMessage) msg;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(PATH + fm.getFilename(), true);
            fos.write(fm.getData());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
    }

    public void commandMessage(ChannelHandlerContext ctx, Object msg){
        CommandMessage cmMsg = (CommandMessage) msg;
        String command = cmMsg.getText();
        if(command.equals(DELETE)){
            File file = new File(PATH + cmMsg.getPath());
            file.delete();
        }
        if(command.equals(COPY)){
            Path path = Paths.get(PATH + cmMsg.getPath());
            try {
                FileMessage fm = new FileMessage(path);
                ctx.write(fm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(command.equals(REFRESH)){
            FileRequest fr = new FileRequest(data.getFilesNames());
            ctx.write(fr);
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

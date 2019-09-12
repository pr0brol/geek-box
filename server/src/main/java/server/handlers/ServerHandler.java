package server.handlers;

import common.FileMessage;
import common.MyMessage;
import common.CommandMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final String PATH = "server/server_storage/";

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("client connected...");
        MyMessage msg = new MyMessage("client connected. Hello");
        ctx.write(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        if(msg == null){
            return;
        }
        if(msg instanceof FileMessage){
            fileMessage(msg);
        }
        if(msg instanceof MyMessage){
            myMessage(msg);
        }
        if(msg instanceof CommandMessage){

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

    public void myMessage(Object msg) {
        MyMessage mm =  (MyMessage)msg;
        System.out.println(mm.getText());
    }

    public void CommandMessage(Object msg){
        CommandMessage cm = (CommandMessage) msg;

    }

    public File[] fileRequest(File[] files){
        return files;
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

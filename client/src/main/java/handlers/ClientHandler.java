package handlers;

import common.FileMessage;
import common.MyMessage;
import common.FileRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final String PATH = "client/client_storage/";

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("connected is success");
        MyMessage msg = new MyMessage("hello server");
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
        if(msg instanceof FileRequest){
            fileRequest(msg);
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

    }

    public String[] fileRequest(Object msg){
        FileRequest fr = (FileRequest) msg;
        return fr.getFilesname();
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

package handlers;

import common.AbstractMessage;
import common.FileMessage;
import common.MyMessage;
import common.FileRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

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
            if(msg instanceof FileRequest){
                fileRequest(msg);
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

    public String[] fileRequest(Object msg){
        FileRequest fr = (FileRequest) msg;
        return fr.getFilesName();
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

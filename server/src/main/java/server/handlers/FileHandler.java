package server.handlers;

import common.FileMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileOutputStream;

public class FileHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FileMessage fm = (FileMessage) msg;
        FileOutputStream fos = new FileOutputStream("server/server_storage/" + fm.getFilename(), true);
        fos.write(fm.getData());
    }
}

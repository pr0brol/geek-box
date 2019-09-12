package server.handlers;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ClientHandler extends ChannelInboundHandlerAdapter {

//    private final ByteBuf firstMessage;
//
//    public ClientHandler() {
//        firstMessage = Unpooled.buffer(256);
//        for (int i = 0; i < firstMessage.capacity(); i++) {
//            firstMessage.writeByte((byte) i);
//        }
//
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        StringBuffer helloMsg = new StringBuffer("hello, server");
//
//        ByteBuf msg = Unpooled.buffer();
//        for (int i=0; i< helloMsg.length(); i++ ){
//            msg.writeByte((byte) helloMsg.charAt(i));
//        }

//        MyMessage message = new MyMessage("hello server");
//        ctx.write(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        StringBuffer helloMsg = new StringBuffer("hello, server");
//
//        ByteBuf ms = Unpooled.buffer();
//        for (int i=0; i< helloMsg.length(); i++ ){
//            ms.writeByte((byte) helloMsg.charAt(i));
//        }
//        MyMessage ms = new MyMessage("hello");
//        ctx.write(ms);
//        ByteBuf in = (ByteBuf) msg;
//        try {
//            while (in.isReadable()) {
//                System.out.print((char) in.readByte() + " ");
//                System.out.flush();
//            }
//            ctx.write("goodbuy");
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

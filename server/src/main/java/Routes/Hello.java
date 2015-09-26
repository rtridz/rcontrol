package Routes;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 * Created by vadim on 26.09.15.
 */
public class Hello implements Handler {

    public void handle(ChannelHandlerContext ctx) {
        sendHello(ctx);
    }

    private void sendHello(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer();
        byteBuf.writeBytes("Hello".getBytes());
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/txt");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(byteBuf.readableBytes()));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}

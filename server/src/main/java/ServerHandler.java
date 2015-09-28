import Routes.Handler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * Created by vadim on 18.09.15.
 */
public class ServerHandler extends SimpleChannelInboundHandler<HttpRequest>{
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        String path = decoder.path();
        Handler handler = new Router().getHandler(path);
        if (handler != null) {
            handler.handle(ctx);
        }
        else {
            sendNotFound(ctx);
        }
    }

    private void sendNotFound(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer();
        byteBuf.writeBytes("Not found".getBytes());
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, byteBuf
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/txt");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(byteBuf.readableBytes()));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}

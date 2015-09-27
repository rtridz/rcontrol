import Routes.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * Created by vadim on 27.09.15.
 */
public class ResponseHandler extends SimpleChannelInboundHandler<Protocol> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Protocol protocol) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", protocol.getCode());
        jsonObject.put("message", protocol.getMessage());
        jsonObject.put("response", protocol.getResponse());

        HttpResponse response = createResponse(jsonObject);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private HttpResponse createResponse(JSONObject jsonObject) {
        ByteBuf content = PooledByteBufAllocator.DEFAULT.directBuffer();
        content.writeBytes(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        HttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(content.readableBytes()));
        response.headers().set(HttpHeaderNames.CONNECTION, "close");
        return response;
    }

}

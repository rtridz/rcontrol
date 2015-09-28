package Routes;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by vadim on 26.09.15.
 */
public class Category implements Handler {
    @Override
    public void handle(ChannelHandlerContext ctx) {
        String response = "Category";
        Protocol protocol = new Protocol(200, "OK", response);
        ctx.fireChannelRead(protocol);
    }
}

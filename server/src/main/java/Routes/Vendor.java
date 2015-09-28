package Routes;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by vadim on 27.09.15.
 */
public class Vendor implements Handler {
    @Override
    public void handle(ChannelHandlerContext ctx) {
        String response = "Vendor";
        Protocol protocol = new Protocol(200, "OK", response);
        ctx.fireChannelRead(protocol);
    }
}

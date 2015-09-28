package Routes;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by vadim on 26.09.15.
 */
public interface Handler {
    public void handle(ChannelHandlerContext ctx);
}

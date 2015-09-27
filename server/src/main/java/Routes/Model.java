package Routes;

import Database.DBServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONArray;

/**
 * Created by vadim on 27.09.15.
 */
public class Model implements Handler {
    @Override
    public void handle(ChannelHandlerContext ctx) {
        DBServiceImpl dbService = new DBServiceImpl();
        JSONArray response = dbService.find();
        Protocol protocol = new Protocol(200, "OK", response);
        ctx.fireChannelRead(protocol);
    }
}

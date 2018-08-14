package game.core.nettyCore.session;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class ChannelContextManager {

    public static final ChannelContextManager instance = new ChannelContextManager();

    private Map<String, ChannelHandlerContext> context = new HashMap<>();

    private ChannelContextManager() {

    }

    public static ChannelContextManager getInstance() {
        return instance;
    }

    public void put(String key, ChannelHandlerContext ctx) {
        context.put(key, ctx);
    }

    public void remove(String key) {
        context.remove(key);
    }


}

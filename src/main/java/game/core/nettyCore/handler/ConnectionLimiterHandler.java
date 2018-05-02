package game.core.nettyCore.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接数判断
 *
 * @author nullzZ
 */
@Sharable
public class ConnectionLimiterHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ConnectionLimiterHandler.class);
    private final AtomicInteger numConnections;
    /**
     * 小于0无上限
     */
    private final int maxConnections;

    public ConnectionLimiterHandler(int maxConnections) {
        this.maxConnections = maxConnections;
        this.numConnections = new AtomicInteger(0);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int curr = numConnections.incrementAndGet();
        if (maxConnections > 0 && curr > maxConnections) {
            log.error("[服务器连接上限][当前：" + curr + "|" + maxConnections + "]");
            ctx.close();
        }
        log.debug("[当前连接数][" + curr + "]");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.close();
        ctx.fireChannelInactive();
    }

    private void close() {
        int curr = numConnections.decrementAndGet();
        if (maxConnections > 0 && curr < 0) {
            log.error("BUG in ConnectionLimiter");
        }
        log.debug("[当前连接数][" + curr + "]");
    }

}

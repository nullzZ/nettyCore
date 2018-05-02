package game.core.nettyCore.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 接收频率限制
 * 
 * @author nullzZ
 *
 */
@Sharable
public class RecvRateLimiterHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(RecvRateLimiterHandler.class);
	private Map<Integer, Long> rate = new HashMap<>();// 这里以后加入次数过多 封IP
	/**
	 * 小于等于0无上限(ms)
	 */
	private final int maxRate;

	/**
	 * 设置频率上限，单位毫秒
	 * 
	 * @param maxRate
	 */
	public RecvRateLimiterHandler(int maxRate) {
		this.maxRate = maxRate * 1000000;
	}

	public RecvRateLimiterHandler() {
		this.maxRate = -1;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelActive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (maxRate > 0) {
			Long time = rate.get(ctx.hashCode());
			long now = System.nanoTime();
			if (time != null && (now - time) < maxRate) {
				log.error("发送频率过高");
				ctx.close();
			} else {
				rate.put(ctx.hashCode(), now);
			}
		}
		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		rate.remove(ctx.hashCode());
		ctx.fireChannelInactive();
	}

}

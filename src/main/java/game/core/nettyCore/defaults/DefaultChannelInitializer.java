package game.core.nettyCore.defaults;

import java.util.concurrent.TimeUnit;

import game.core.nettyCore.serverDef.ServerDef;
import game.core.nettyCore.coder.socket.MessageDecoder;
import game.core.nettyCore.coder.socket.MessageEncoder;
import game.core.nettyCore.handler.ConnectionLimiterHandler;
import game.core.nettyCore.handler.RecvRateLimiterHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author nullzZ
 *
 */
public class DefaultChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final int NO_WRITER_IDLE_TIMEOUT = 0;
	private final int NO_ALL_IDLE_TIMEOUT = 0;

	private ConnectionLimiterHandler connectionLimiter;
	private RecvRateLimiterHandler recvRateLimiter;
	private ServerDef serverDef;

	public DefaultChannelInitializer(ServerDef serverDef) {
		connectionLimiter = new ConnectionLimiterHandler(serverDef.maxConnections);
		recvRateLimiter = new RecvRateLimiterHandler();
		this.serverDef = serverDef;
	}

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		// channel.config().setReceiveBufferSize(receiveBufferSize);
		// channel.config().setSendBufferSize(sendBufferSize);
		pipeline.addLast("ConnectionLimiter", connectionLimiter);
		pipeline.addLast("RecvRateLimiter", recvRateLimiter);
		if (serverDef.clientIdleTimeout > 0) {
			pipeline.addLast("Heart", new IdleStateHandler(serverDef.clientIdleTimeout, NO_WRITER_IDLE_TIMEOUT,
					NO_ALL_IDLE_TIMEOUT, TimeUnit.SECONDS));

		}

		pipeline.addLast("Decoder", new MessageDecoder(serverDef));
		pipeline.addLast("Encoder", new MessageEncoder(serverDef));
		pipeline.addLast("Handler", new MessageRecvHandlerBase(serverDef));
	}

}

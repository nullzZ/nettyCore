package game.core.nettyCore.http.bootstrap;

import game.core.nettyCore.http.HttpServerDef;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerBootstrap extends HttpCommonServer implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(HttpServerBootstrap.class);

	private final ChannelGroup allChannels;
	private final HttpServerDef serverDef;

	public HttpServerBootstrap(HttpServerDef serverDef) {
		this(serverDef, new DefaultChannelGroup(new DefaultEventLoop()));
	}

	public HttpServerBootstrap(HttpServerDef serverDef, ChannelGroup allChannels) {
		this.serverDef = serverDef;
		this.allChannels = allChannels;
		Runtime.getRuntime().addShutdownHook(new Thread(this));
	}

	public ChannelGroup getAllChannels() {
		return allChannels;
	}

	@Override
	public void run() {
		logger.info("server closing...|channels:" + allChannels.size());
		close();
	}

	@Override
	public void close() {
		allChannels.close().awaitUninterruptibly();
		super.close();
	}

	public void start() throws Exception {
		super.start(serverDef.port, serverDef.channelInitializer);
	}

	public void start(int bossThreads, int workThreads) throws Exception {
		start(serverDef.port, serverDef.channelInitializer, bossThreads, workThreads);
	}

}

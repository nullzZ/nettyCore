package game.core.nettyCore.bootstrap;

import game.core.nettyCore.ServerDef;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerBootstrap extends CommonServer implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(ServerBootstrap.class);

	private final ChannelGroup allChannels;
	private final ServerDef serverDef;

	public ServerBootstrap(ServerDef serverDef) {
		this(serverDef, new DefaultChannelGroup(new DefaultEventLoop()));
	}

	public ServerBootstrap(ServerDef serverDef, ChannelGroup allChannels) {
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

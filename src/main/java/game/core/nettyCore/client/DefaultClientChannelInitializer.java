package game.core.nettyCore.client;

import game.core.nettyCore.client.corder.ClientMessageDecoder;
import game.core.nettyCore.client.corder.ClientMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author nullzZ
 */
public class DefaultClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final int NO_READ_IDLE_TIMEOUT = 0;
    private final int NO_ALL_IDLE_TIMEOUT = 0;

    private ClientDef clientDef;

    public DefaultClientChannelInitializer(ClientDef clientDef) {
        this.clientDef = clientDef;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
		if (clientDef.clientIdleTimeout > 0) {
			pipeline.addLast("heart", new IdleStateHandler(NO_READ_IDLE_TIMEOUT, clientDef.clientIdleTimeout,
					NO_ALL_IDLE_TIMEOUT, TimeUnit.SECONDS));

		}
        pipeline.addLast("Decoder", new ClientMessageDecoder(clientDef));
        pipeline.addLast("Encoder", new ClientMessageEncoder(clientDef));
        pipeline.addLast("handler", new ClientMessageRecvHandlerBase(clientDef));
    }

}

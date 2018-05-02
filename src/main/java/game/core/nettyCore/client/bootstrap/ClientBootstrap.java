package game.core.nettyCore.client.bootstrap;

import game.core.nettyCore.client.ClientDef;
import game.core.nettyCore.client.ClientListener;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * cmd=1 是保留指
 *
 * @author nullzZ
 */
public class ClientBootstrap extends CommonClient implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ClientBootstrap.class);
    private ChannelFutureListener channelFutureListener;
    private final ChannelGroup allChannels;
    private final ClientDef clientDef;
    public Channel channel;

    public ClientBootstrap(ClientDef clientDef) {
        this(clientDef, new DefaultChannelGroup(new DefaultEventLoop()));
    }

    public ClientBootstrap(ClientDef clientDef, ChannelGroup allChannels) {
        this.clientDef = clientDef;
        this.allChannels = allChannels;
        this.clientDef.listener = new ClientListener() {

            @Override
            public void onConnect(ChannelHandlerContext ctx) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onClose(ChannelHandlerContext ctx) {
                ctx.channel().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            logger.debug("服务器断开,重新连接");
                            connect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 3, TimeUnit.SECONDS);

            }

        };

        channelFutureListener = new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    logger.info("连接服务器成功");
                    channel = f.channel();
                } else {
                    logger.error("重新连接服务器失败");
                    f.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                connect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 3, TimeUnit.SECONDS);

                }
            }
        };
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

    public void connect() throws Exception {
        super.connect(clientDef.host, clientDef.port, clientDef.channelInitializer, channelFutureListener);
    }

    public void connect(int workThreads) throws Exception {
        super.connect(clientDef.host, clientDef.port, clientDef.channelInitializer, workThreads, channelFutureListener);
    }
}

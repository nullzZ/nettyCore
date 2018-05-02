package game.core.nettyCore.client;

import game.core.nettyCore.AbstractMessageLogicExecutorBase;
import game.core.nettyCore.HandlerManager;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.defaults.DefaultProtocolFactorySelectorFactory;
import game.core.nettyCore.proto.ProtocolFactorySelectorFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ClientDefBuilderBase<T extends ClientDefBuilderBase<T>> {
    private static final AtomicInteger ID = new AtomicInteger(1);

    private String name = "netty-" + ID.getAndIncrement();
    private String host = "localhost";
    private int port = 8081;
    private int maxFrameSize = MAX_FRAME_SIZE;
    private ChannelInitializer<SocketChannel> channelInitializer;// hasDefault
    private long clientIdleTimeout;// hasDefault
    private ProtocolFactorySelectorFactory protocolFactorySelectorFactory;//
    // hasDefault
    private ProtocolType protocolType;
    private AbstractMessageLogicExecutorBase messageLogicExecutor;// hasDefault
    private HandlerManager handlerManager;
    private String hanlderPackageName;


    // private HttpResourceHandler httpResourceHandler;// hasDefault
    // private HttpHandlerFactory httpHandlerFactory;// hasDefault

    /**
     * The default maximum allowable size for a single incoming thrift request
     * or outgoing thrift response. A server can configure the actual maximum to
     * be much higher (up to 0x7FFFFFFF or almost 2 GB). This default could also
     * be safely bumped up, but 64MB is chosen simply because it seems
     * reasonable that if you are sending requests or responses larger than
     * that, it should be a conscious decision (something you must manually
     * configure).
     */
    private static final int MAX_FRAME_SIZE = 64 * 1024 * 1024;

    /**
     * Create a ThriftServerDefBuilder with common defaults
     */
    public ClientDefBuilderBase() {
    }

    /**
     * Give the endpoint a more meaningful name.
     */
    @SuppressWarnings("unchecked")
    public T name(String name) {
        this.name = name;
        return (T) this;
    }

    /**
     * Listen to this port.
     */
    @SuppressWarnings("unchecked")
    public T connect(String host, int port) {
        this.host = host;
        this.port = port;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T protocolFactorySelectorFactory(ProtocolFactorySelectorFactory protocolFactorySelectorFactory) {
        this.protocolFactorySelectorFactory = protocolFactorySelectorFactory;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T messageLogicExecutor(AbstractMessageLogicExecutorBase messageLogicExecutor) {
        this.messageLogicExecutor = messageLogicExecutor;
        return (T) this;
    }

    /**
     * Set frame size limit. Default is MAX_FRAME_SIZE
     */
    @SuppressWarnings("unchecked")
    public T limitFrameSizeTo(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
        return (T) this;
    }

    /**
     * 是否开启默认心跳 大于0开始
     * @param clientIdleTimeout
     * @return
     */
    @SuppressWarnings("unchecked")
    public T clientIdleTimeout(long clientIdleTimeout) {
        this.clientIdleTimeout = clientIdleTimeout;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T contextHandlerInstaller(ChannelInitializer<SocketChannel> channelInitializer) {
        this.channelInitializer = channelInitializer;
        return (T) this;
    }

    /**
     * hanlder存放的包名
     *
     * @param hanlderPackageName
     * @return
     */
    @SuppressWarnings("unchecked")
    public T handlerPackage(String hanlderPackageName) {
        this.hanlderPackageName = hanlderPackageName;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T protocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
        return (T) this;
    }


    public ClientDef build() {
        checkState(hanlderPackageName != null, "hanlderPackageName not defined!");

        checkState(protocolType != null, "potocolType not defined!");


        if (protocolFactorySelectorFactory == null) {
            protocolFactorySelectorFactory = new DefaultProtocolFactorySelectorFactory();
        }

        try {
            this.handlerManager = new HandlerManager();
            this.handlerManager.init(hanlderPackageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ClientDef(name, host, port, maxFrameSize, channelInitializer, clientIdleTimeout,
                messageLogicExecutor, protocolFactorySelectorFactory.createProtocolFactorySelector(), protocolType,
                handlerManager, clientIdleTimeout > 0);
    }

    /**
     * Ensures the truth of an expression involving the state of the calling
     * instance, but not involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    /**
     * Ensures the truth of an expression involving the state of the calling
     * instance, but not involving any parameters to the calling method.
     *
     * @param expression   a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be
     *                     converted to a string using {@link String#valueOf(Object)}
     * @throws IllegalStateException if {@code expression} is false
     */
    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }
}
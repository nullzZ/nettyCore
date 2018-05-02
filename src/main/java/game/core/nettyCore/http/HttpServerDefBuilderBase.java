package game.core.nettyCore.http;

import game.core.nettyCore.IExecutorCallBack;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.defaults.DefaultProtocolFactorySelectorFactory;
import game.core.nettyCore.proto.ProtocolFactorySelectorFactory;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class HttpServerDefBuilderBase<T extends HttpServerDefBuilderBase<T>> {
    private static final AtomicInteger ID = new AtomicInteger(1);

    private String name = "netty-" + ID.getAndIncrement();
    private int serverPort = 8081;
    private int maxFrameSize = MAX_FRAME_SIZE;
    private int maxConnections;
    //    private ChannelInitializer<SocketChannel> channelInitializer;// hasDefault
    private long clientIdleTimeout;// hasDefault
    private ProtocolFactorySelectorFactory protocolFactorySelectorFactory;//
    // hasDefault
    private ProtocolType protocolType;
    private AbstractHttpMessageLogicExecutorBase messageLogicExecutor;// hasDefault
    private HttpHandlerManager handlerManager;
    private String hanlderPackageName;
    private IExecutorCallBack executorCallBack;

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


    public HttpServerDefBuilderBase() {
    }


    @SuppressWarnings("unchecked")
    public T name(String name) {
        this.name = name;
        return (T) this;
    }

    /**
     * Listen to this port.
     */
    @SuppressWarnings("unchecked")
    public T listen(int serverPort) {
        this.serverPort = serverPort;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T protocolFactorySelectorFactory(ProtocolFactorySelectorFactory protocolFactorySelectorFactory) {
        this.protocolFactorySelectorFactory = protocolFactorySelectorFactory;
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


    public T messageLogicExecutor(AbstractHttpMessageLogicExecutorBase messageLogicExecutor) {
        this.messageLogicExecutor = messageLogicExecutor;
        return (T) this;
    }

    public T executorCallBack(IExecutorCallBack executorCallBack) {
        this.executorCallBack = executorCallBack;
        return (T) this;
    }


    public HttpServerDef build() {
        checkState(hanlderPackageName != null, "hanlderPackageName not defined!");

//        checkState(protocolType != null, "potocolType not defined!");
        if (protocolType == null) {
            protocolType = ProtocolType.PROTOSTUFF;
        }
        // checkState(maxConnections >= 0, "maxConnections should be 0 (for
        // unlimited) or positive");

        if (protocolFactorySelectorFactory == null) {
            protocolFactorySelectorFactory = new DefaultProtocolFactorySelectorFactory();
        }

        if (messageLogicExecutor == null) {
            this.messageLogicExecutor = new HttpMessageLogicExecutorBase(executorCallBack);
        } else {
            this.messageLogicExecutor = messageLogicExecutor;
        }

        try {
            this.handlerManager = new HttpHandlerManager();
            this.handlerManager.init(hanlderPackageName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HttpServerDef(name, serverPort, maxFrameSize, maxConnections, clientIdleTimeout,
                messageLogicExecutor, handlerManager, executorCallBack);
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

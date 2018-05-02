/*
 * Copyright (C) 2012-2013 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package game.core.nettyCore;

import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.defaults.DefaultProtocolFactorySelectorFactory;
import game.core.nettyCore.defaults.MessageLogicExecutorBase;
import game.core.nettyCore.proto.ProtocolFactorySelectorFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ServerDefBuilderBase<T extends ServerDefBuilderBase<T>> {
    private static final AtomicInteger ID = new AtomicInteger(1);

    private String name = "netty-" + ID.getAndIncrement();
    private int serverPort = 8081;
    private int maxFrameSize = MAX_FRAME_SIZE;
    private int maxConnections;
    private ChannelInitializer<SocketChannel> channelInitializer;// hasDefault
    private long clientIdleTimeout;// hasDefault
    private ProtocolFactorySelectorFactory protocolFactorySelectorFactory;//hasDefault
    private ProtocolType protocolType;
    private AbstractMessageLogicExecutorBase messageLogicExecutor;// hasDefault
    private HandlerManager handlerManager;
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


    public ServerDefBuilderBase() {
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

    @SuppressWarnings("unchecked")
    public T messageLogicExecutor(AbstractMessageLogicExecutorBase messageLogicExecutor) {
        this.messageLogicExecutor = messageLogicExecutor;
        return (T) this;
    }


    @SuppressWarnings("unchecked")
    public T limitFrameSizeTo(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T limitConnectionsTo(int maxConnections) {
        this.maxConnections = maxConnections;
        return (T) this;
    }


    /**
     * @param clientIdleTimeout 秒
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

    public T executorCallBack(IExecutorCallBack executorCallBack) {
        this.executorCallBack = executorCallBack;
        return (T) this;
    }


    public ServerDef build() throws Exception {
        try {
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


            this.handlerManager = new HandlerManager();
            this.handlerManager.init(hanlderPackageName);

            if (messageLogicExecutor == null) {
                this.messageLogicExecutor = new MessageLogicExecutorBase(executorCallBack);
            } else {
                this.messageLogicExecutor = messageLogicExecutor;
            }

            return new ServerDef(name, serverPort, maxFrameSize, maxConnections, channelInitializer, clientIdleTimeout,
                    messageLogicExecutor, protocolFactorySelectorFactory.createProtocolFactorySelector(), protocolType,
                    handlerManager, executorCallBack);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }
}
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
package game.core.nettyCore.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class HttpServerDef {
    protected final String name;
    public final int port;
    public final ChannelInitializer<SocketChannel> channelInitializer;
    public final AbstractHttpMessageLogicExecutorBase messageLogicExecutor;// logic
    public final HttpHandlerManager handlerManager;
    public final int maxFrameSize;
    public final int maxConnections;
    public final long clientIdleTimeout;


    public HttpServerDef(String name, int port, int maxFrameSize, int maxConnections, long clientIdleTimeout,
                         AbstractHttpMessageLogicExecutorBase messageLogicExecutor,
                         HttpHandlerManager handlerManager) {
        this.name = name;
        this.port = port;

        this.channelInitializer = new DefaultWebSocketChannelInstaller(this);

        this.messageLogicExecutor = messageLogicExecutor;
        this.handlerManager = handlerManager;
        this.maxFrameSize = maxFrameSize;
        this.maxConnections = maxConnections;
        this.clientIdleTimeout = clientIdleTimeout;

    }

    public static HttpServerDefBuilder newBuilder() {
        return new HttpServerDefBuilder();
    }

}

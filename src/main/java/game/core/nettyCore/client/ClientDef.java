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
package game.core.nettyCore.client;

import game.core.nettyCore.AbstractMessageLogicExecutorBase;
import game.core.nettyCore.HandlerManager;
import game.core.nettyCore.coder.ProtocolType;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientDef {
    public final String name;
    public final String host;
    public final int port;
    public final int maxFrameSize;
    public final ChannelInitializer<SocketChannel> channelInitializer;
    public final long clientIdleTimeout;
    public final ProtocolType protocolType;
    public final AbstractMessageLogicExecutorBase messageLogicExecutor;// logic
    public final HandlerManager handlerManager;
    public ClientListener listener;
    /**
     * 是否开始默认心跳
     */
    public final boolean isSendHeart;

    public ClientDef(String name, String host, int port, int maxFrameSize,
                     ChannelInitializer<SocketChannel> channelInitializer, long clientIdleTimeout,
                     AbstractMessageLogicExecutorBase messageLogicExecutor,
                     ProtocolType protocolType, HandlerManager handlerManager, boolean isSendHeart) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.maxFrameSize = maxFrameSize;
        this.clientIdleTimeout = clientIdleTimeout;

        if (channelInitializer == null) {
            this.channelInitializer = new DefaultClientChannelInitializer(this);
        } else {
            this.channelInitializer = channelInitializer;
        }
        if (messageLogicExecutor == null) {
            this.messageLogicExecutor = new ClientMessageLogicExecutorBase();
        } else {
            this.messageLogicExecutor = messageLogicExecutor;
        }


        this.protocolType = protocolType;
        this.handlerManager = handlerManager;
        this.isSendHeart = isSendHeart;
    }

    public static ClientDefBuilder newBuilder() {
        return new ClientDefBuilder();
    }

}

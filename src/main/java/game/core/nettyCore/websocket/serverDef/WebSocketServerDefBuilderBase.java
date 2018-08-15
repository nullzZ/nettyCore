package game.core.nettyCore.websocket.serverDef;

import game.core.nettyCore.HandlerManager;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.defaults.DefaultProtocolFactorySelectorFactory;
import game.core.nettyCore.defaults.MessageLogicExecutorBase;
import game.core.nettyCore.serverDef.ServerDef;
import game.core.nettyCore.serverDef.ServerDefBuilderBase;
import game.core.nettyCore.websocket.DefaultWebSocketChannelInstaller;
import game.core.nettyCore.websocket.WebSocketLogicExecutorBase;

public abstract class WebSocketServerDefBuilderBase<T extends WebSocketServerDefBuilderBase> extends ServerDefBuilderBase {

    @Override
    public ServerDef build() throws Exception {
        try {
            checkState(hanlderPackageName != null, "hanlderPackageName not defined!");

            checkState(protocolType != null, "potocolType not defined!");

            this.messageLogicExecutor = new WebSocketLogicExecutorBase(executorCallBack);

            return new ServerDef(name, serverPort, channelInitializer, maxFrameSize, maxConnections, clientIdleTimeout,
                    messageLogicExecutor, protocolFactorySelectorFactory.createProtocolFactorySelector(), protocolType,
                    handlerManager, executorCallBack);
        } catch (Exception e) {
            throw e;
        }
    }

}

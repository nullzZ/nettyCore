package game.core.nettyCore.serverDef.websocket;

import game.core.nettyCore.HandlerManager;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.defaults.DefaultProtocolFactorySelectorFactory;
import game.core.nettyCore.defaults.MessageLogicExecutorBase;
import game.core.nettyCore.serverDef.ServerDef;
import game.core.nettyCore.serverDef.ServerDefBuilderBase;

public abstract class WebSocketServerDefBuilderBase<T extends WebSocketServerDefBuilderBase> extends ServerDefBuilderBase {

    public ServerDef build() throws Exception {
        this.maxFrameSize = 0;
        return super.build();
    }

}

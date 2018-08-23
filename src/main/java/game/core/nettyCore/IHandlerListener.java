package game.core.nettyCore;

import game.core.nettyCore.session.Session;

public interface IHandlerListener {
    void onConnect(Session session);

    void onClose(Session session);

}

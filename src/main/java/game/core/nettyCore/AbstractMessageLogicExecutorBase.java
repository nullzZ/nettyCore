package game.core.nettyCore;

import game.core.nettyCore.model.Message;
import game.core.nettyCore.session.Session;

/**
 * @author nullzZ
 */
public interface AbstractMessageLogicExecutorBase {


    void execute(IHandler handler, Session session, Message msg);

}

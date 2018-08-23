package game.core.nettyCore.websocket;

import game.core.nettyCore.IHandler;
import game.core.nettyCore.IMessageLogicExecutorBase;
import game.core.nettyCore.model.Message;
import game.core.nettyCore.session.Session;
import game.core.nettyCore.util.WebSocketMessageUtil;

public class LogicExecutor implements IMessageLogicExecutorBase {
    //private static final Logger logger = LogManager.getLogger(LogicExecutor.class);

    @Override
    public void execute(IHandler handler, Session session, Message msg) {
        try {
            long now = System.currentTimeMillis();
            short cmd = msg.getCmd();
//            if (cmd != CMD.LOGIN && session.getAttribute().get("isLogin") != null) {//登陆请求
//                WebSocketMessageUtil.sendByJPB(session.getCtx(), CMD.RELOGIN);
//                return;
//            }

            Object r = handler.execute(session, msg.getContent());
            if (r != null && !(r instanceof Void)) {
                WebSocketMessageUtil.sendByJPB(session.getCtx(), cmd, r);
            }

            long time = System.currentTimeMillis() - now;
            if (time > 300) {
                //logger.error("[业务逻辑处理时间] handler:" + cmd + "|time:" + time);
            }
        } catch (Throwable e) {
//            logger.error("logic 异常-", e);
//            try {
//                WebSocketMessageUtil.sendByJPB(session.getCtx(), CMD.ERROR);
//            } catch (Exception e1) {
//                logger.error("logic 异常-", e);
//            }
        }
    }

}

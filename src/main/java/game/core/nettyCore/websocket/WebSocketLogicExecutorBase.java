package game.core.nettyCore.websocket;

import game.core.nettyCore.AbstractMessageLogicExecutorBase;
import game.core.nettyCore.CMD;
import game.core.nettyCore.IExecutorCallBack;
import game.core.nettyCore.IHandler;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.model.Message;
import game.core.nettyCore.session.Session;
import game.core.nettyCore.session.SessionManager;
import game.core.nettyCore.util.WebSocketMessageUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author nullzZ
 */
public class WebSocketLogicExecutorBase implements AbstractMessageLogicExecutorBase {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketLogicExecutorBase.class);
    private ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1, new ThreadFactory() {
        private AtomicInteger id = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "WebSocketLogicExecutor" + id.getAndAdd(1));
        }
    });
    public IExecutorCallBack executorCallBack;
    private ProtocolType protocolType;

    public WebSocketLogicExecutorBase(ProtocolType protocolType, IExecutorCallBack executorCallBack) {
        this.executorCallBack = executorCallBack;
        this.protocolType = protocolType;
    }

    @Override
    public void execute(IHandler handler, Session session, Message msg) {
        if (handler != null) {
            es.execute(() -> {
                        try {
                            long now = System.currentTimeMillis();
                            if (executorCallBack != null) {
                                executorCallBack.onHandleBefor(session.getCtx(), msg);
                            }
                            Object r = null;
                            short cmd = msg.getCmd();
                            if (cmd == 100) {//登陆请求

                            } else {
                                r = handler.execute(session, msg.getContent());
                            }

                            if (r != null && !(r instanceof Void)) {
//                                Message m = Message.newBuilder().cmd((short) 10001).message(new Object()).build();
                                WebSocketMessageUtil.sendByProtocolType(session.getCtx(), cmd, r, protocolType);
                            }
                            if (executorCallBack != null) {
                                executorCallBack.onHandleAfer(session.getCtx(), msg);
                            }
                            long time = System.currentTimeMillis() - now;
                            if (time > 300) {
                                logger.error("[业务逻辑处理时间] handler:" + cmd + "|time:" + time);
                            }
                        } catch (Throwable e) {
                            logger.error("logic 异常-", e);
                            try {
                                WebSocketMessageUtil.sendByProtocolType(session.getCtx(), CMD.ERROR, protocolType);
                            } catch (Exception e1) {
                                logger.error("logic 异常-", e);
                            }
                        }
                    }
            );
        } else {
            logger.error("logic 异常-handler is null|cmd=" + msg.getCmd());
        }
    }

}

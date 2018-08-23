package game.core.nettyCore.defaults;

import game.core.nettyCore.AbstractMessageLogicExecutorBase;
import game.core.nettyCore.IExecutorCallBack;
import game.core.nettyCore.IHandler;
import game.core.nettyCore.model.Message;
import game.core.nettyCore.session.Session;
import game.core.nettyCore.util.MessageUtil;
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
public class MessageLogicExecutorBase implements AbstractMessageLogicExecutorBase {

    private static final Logger logger = LoggerFactory.getLogger(MessageLogicExecutorBase.class);
    private ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1, new ThreadFactory() {
        private AtomicInteger id = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "MessageLogicExecutor" + id.getAndAdd(1));
        }
    });
    public IExecutorCallBack executorCallBack;

    public MessageLogicExecutorBase(IExecutorCallBack executorCallBack) {
        this.executorCallBack = executorCallBack;
    }

    @Override
    public void execute(IHandler handler, Session session, Message msg) {
        if (handler != null) {
            es.execute(() -> {
                        try {
                            if (executorCallBack != null) {
                                executorCallBack.onHandleBefor(session.getCtx(), msg);
                            }
                            Object r = handler.execute(session, msg.getContent());
                            if (r == null || r instanceof Void) {

                            } else {
                                //Message res = Message.newBuilder().cmd(msg.getCmd()).message(r).build();
                                MessageUtil.sendTcpMsg(session.getCtx(), msg.getCmd(), r);
                            }
                            if (executorCallBack != null) {
                                executorCallBack.onHandleAfer(session.getCtx(), msg);
                            }
                        } catch (Throwable e) {
                            logger.error("logic 异常-", e);
                        }
                    }
            );
        } else {
            logger.error("logic 异常-handler is null|cmd=" + msg.getCmd());
        }

    }

}

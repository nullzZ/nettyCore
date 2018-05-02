package game.core.nettyCore.defaults;

import game.core.nettyCore.AbstractMessageLogicExecutorBase;
import game.core.nettyCore.IExecutorCallBack;
import game.core.nettyCore.IHandler;
import game.core.nettyCore.model.Message;
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
    public void execute(IHandler handler, ChannelHandlerContext ctx, Message msg) {
        if (handler != null) {
            es.execute(() -> {
                        try {
                            if (executorCallBack != null) {
                                executorCallBack.onHandleBefor(ctx, msg);
                            }
                            Object r = handler.execute(ctx, msg.getContent());
                            if (r == null || r instanceof Void) {

                            } else {
                                //Message res = Message.newBuilder().cmd(msg.getCmd()).message(r).build();
                                MessageUtil.sendTcpMsg(ctx, msg.getCmd(), r);
                            }
                            if (executorCallBack != null) {
                                executorCallBack.onHandleAfer(ctx, msg);
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

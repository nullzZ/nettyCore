package game.core.nettyCore.websocket;

import game.core.nettyCore.AbstractMessageLogicExecutorBase;
import game.core.nettyCore.IExecutorCallBack;
import game.core.nettyCore.IHandler;
import game.core.nettyCore.http.HttpResponse;
import game.core.nettyCore.http.HttpResponseMessage;
import game.core.nettyCore.model.Message;
import game.core.nettyCore.util.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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

    public WebSocketLogicExecutorBase(IExecutorCallBack executorCallBack) {
        this.executorCallBack = executorCallBack;
    }

    @Override
    public void execute(IHandler handler, ChannelHandlerContext ctx, Message msg) {
        if (handler != null) {
            es.execute(() -> {
                        try {
                            long now = System.currentTimeMillis();
                            if (executorCallBack != null) {
                                executorCallBack.onHandleBefor(ctx, msg);
                            }
                            Object r = null;
                            int cmd = msg.getCmd();
                            if (cmd == 100) {//登陆请求

                            } else {
                                r = handler.execute(ctx, msg.getContent());
                            }

                            if (r == null || r instanceof Void) {

                            } else {
                                ByteBuf bb = Unpooled.wrappedBuffer("sfdsfsd".getBytes("utf-8"));
                                MessageUtil.sendWebSocketResponse(ctx, bb);
                            }
                            if (executorCallBack != null) {
                                executorCallBack.onHandleAfer(ctx, msg);
                            }
                            long time = System.currentTimeMillis() - now;
                            if (time > 300) {
                                logger.error("[业务逻辑处理时间] handler:" + cmd + "|time:" + time);
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

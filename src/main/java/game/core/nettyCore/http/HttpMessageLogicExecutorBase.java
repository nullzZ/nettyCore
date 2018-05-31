package game.core.nettyCore.http;

import game.core.nettyCore.IExecutorCallBack;
import game.core.nettyCore.util.MessageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author nullzZ
 */
public class HttpMessageLogicExecutorBase implements AbstractHttpMessageLogicExecutorBase {

    private static final Logger logger = LoggerFactory.getLogger(HttpMessageLogicExecutorBase.class);
    private ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1, new ThreadFactory() {
        private AtomicInteger id = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "HttpLogicExecutor" + id.getAndAdd(1));
        }
    });
    public IExecutorCallBack executorCallBack;

    public HttpMessageLogicExecutorBase(IExecutorCallBack executorCallBack) {
        this.executorCallBack = executorCallBack;
    }

    @Override
    public void execute(IHttpHandler handler, ChannelHandlerContext ctx, FullHttpRequest req, int cmd, String token, Object msg) {
        es.execute(() -> {
                    try {
                        long now = System.currentTimeMillis();
                        if (executorCallBack != null) {
                            executorCallBack.onHandleBefor(ctx, msg);
                        }
                        Object r = handler.execute(token, msg);
                        if (r == null || r instanceof Void) {

                        } else {
                            HttpResponse rr = (HttpResponse) r;
                            HttpResponseMessage res = new HttpResponseMessage();
                            res.setCmd(cmd);
                            res.setRet(rr.getRet());
                            res.setData(r);
                            MessageUtil.sendHttpResponse(ctx, req, res);
                        }
                        if (executorCallBack != null) {
                            executorCallBack.onHandleAfer(ctx, msg);
                        }
                        long time = System.currentTimeMillis() - now;
                        if (time > 300) {
                            logger.error("[业务逻辑处理时间] handler:" + cmd + "|time:" + time);
                        }
                    } catch (Throwable e) {
                        HttpResponseMessage res = new HttpResponseMessage();
                        res.setCmd(cmd);
                        res.setRet(-1);
                        try {
                            MessageUtil.sendHttpResponse(ctx, req, res);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        logger.error("logic 异常-", e);
                    }
                }
        );
    }

}

package game.core.nettyCore.client;

import game.core.nettyCore.IMessageLogicExecutorBase;
import game.core.nettyCore.IHandler;
import game.core.nettyCore.model.Message;
import game.core.nettyCore.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author nullzZ
 */
public class ClientMessageLogicExecutorBase implements IMessageLogicExecutorBase {

    private static final Logger logger = LoggerFactory.getLogger(ClientMessageLogicExecutorBase.class);
    private ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);


    public ClientMessageLogicExecutorBase() {

    }

    @Override
    public void execute(IHandler handler, Session session, Message msg) {
        es.execute(new Runnable() {

            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                try {
                    handler.execute(session, msg.getContent());
                } catch (Throwable e) {
                    logger.error("logic 异常-", e);
                }

            }
        });
    }


}

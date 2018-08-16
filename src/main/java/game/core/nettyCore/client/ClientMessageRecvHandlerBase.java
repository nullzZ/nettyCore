package game.core.nettyCore.client;

import game.core.nettyCore.AbstractMessageLogicExecutorBase;
import game.core.nettyCore.IHandler;
import game.core.nettyCore.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nullzZ
 */
public class ClientMessageRecvHandlerBase extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientMessageRecvHandlerBase.class);
    private ClientDef clientDef;
    private AbstractMessageLogicExecutorBase messageLogicExecutor;

    public ClientMessageRecvHandlerBase(ClientDef clientDef) {
        this.clientDef = clientDef;
        this.messageLogicExecutor = clientDef.messageLogicExecutor;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        messageLogicExecutor.onConnect(ctx);
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            Message mes = (Message) msg;
            if(mes.getCmd()==1){
                return;
            }
            IHandler handler = clientDef.handlerManager.getHandler(mes.getCmd());
            if (handler != null) {
                messageLogicExecutor.execute(handler, ctx, mes);
            } else {
                logger.error("logic 异常-handler is null|cmd=" + mes.getCmd());
            }
        } catch (Exception e) {
            logger.error("", e);
            this.close(ctx);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.close(ctx);
        ctx.fireChannelInactive();
        this.clientDef.listener.onClose(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (clientDef.isSendHeart) {
            try {
                if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
                    IdleStateEvent event = (IdleStateEvent) evt;
                    if (event.state() == IdleState.WRITER_IDLE) {
                        Message heartMsg = Message.newBuilder().cmd((short) 1).build();
                        ctx.writeAndFlush(heartMsg);
                    }
                }
            } catch (Exception e) {
                logger.error("ping异常", e);
                ctx.close();
            }
        }

    }

    private void close(ChannelHandlerContext ctx) {
        ctx.close();
//        messageLogicExecutor.onClose(ctx);
        logger.debug("客户端断开连接！！");
    }
}

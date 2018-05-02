package game.core.nettyCore.defaults;

import game.core.nettyCore.AbstractMessageLogicExecutorBase;
import game.core.nettyCore.IHandler;
import game.core.nettyCore.ServerDef;
import game.core.nettyCore.model.Message;
import game.core.nettyCore.util.MessageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author nullzZ
 */
public class MessageRecvHandlerBase extends ChannelInboundHandlerAdapter {

    private static final Log logger = LogFactory.getLog(MessageRecvHandlerBase.class);
    private final AbstractMessageLogicExecutorBase messageLogicExecutor;
    private final ServerDef serverDef;

    public MessageRecvHandlerBase(ServerDef serverDef) {
        this.serverDef = serverDef;
        this.messageLogicExecutor = serverDef.messageLogicExecutor;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (serverDef.executorCallBack != null) {
            serverDef.executorCallBack.onConnect(ctx);
        }
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message mes = (Message) msg;
        if (mes.getCmd() == 1) {
            MessageUtil.sendTcpMsg(ctx,1,Message.newBuilder().cmd(1).build());
            return;
        }
        IHandler handler = serverDef.handlerManager.getHandler(mes.getCmd());
        if (handler != null) {
            messageLogicExecutor.execute(handler, ctx, mes);
        } else {
            logger.error("logic 异常-handler is null|cmd=" + mes.getCmd());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.close(ctx);
        ctx.fireChannelInactive();
    }

    private void close(ChannelHandlerContext ctx) {
        ctx.close();
        if (serverDef.executorCallBack != null) {
            serverDef.executorCallBack.onClose(ctx);
        }
        logger.debug("客户端断开连接！！");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                logger.debug("[客户端心跳超时]");
                ctx.close();
            }
            // else if (event.state() == IdleState.WRITER_IDLE) {
            // }
            // System.out.println("write idle");
            // else if (event.state() == IdleState.ALL_IDLE) {
            // }
            // System.out.println("all idle");
        }
    }
}

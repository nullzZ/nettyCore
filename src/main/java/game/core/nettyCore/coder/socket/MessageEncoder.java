package game.core.nettyCore.coder.socket;

import game.core.nettyCore.serverDef.ServerDef;
import game.core.nettyCore.coder.IMessageProtocol;
import game.core.nettyCore.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nullzZ
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

    private static final Logger logger = LoggerFactory.getLogger(MessageEncoder.class);
    private final ServerDef serverDef;
    private final int MESSAGE_FRAME_SIZE = 4;


    public MessageEncoder(ServerDef serverDef) {
        this.serverDef = serverDef;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        IMessageProtocol protocol = serverDef.protocolFactorySelector.getProtocol(serverDef.protocolType);
        if (protocol == null) {
            logger.error("protocol is null");
            return;
        }
        byte[] bb = protocol.encode(msg.getContent(),msg.getContent().getClass());
        int len = bb.length + MESSAGE_FRAME_SIZE;
        out.writeShort(len);
        out.writeShort(msg.getCmd());
        out.writeBytes(bb);
        ctx.flush();
    }

}

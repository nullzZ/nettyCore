package game.core.nettyCore.websocket;

import game.core.nettyCore.coder.IMessageProtocol;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.model.Message;
import game.core.nettyCore.proto.IProtocolFactorySelector;
import game.core.nettyCore.serverDef.ServerDef;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author nullzZ
 */
public class WebSocketMessageEncoder extends MessageToMessageEncoder<Message> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketMessageEncoder.class);
    public final ProtocolType protocolType;
    public final IProtocolFactorySelector protocolFactorySelector;


    public WebSocketMessageEncoder(ProtocolType protocolType, IProtocolFactorySelector protocolFactorySelector) {
        this.protocolType = protocolType;
        this.protocolFactorySelector = protocolFactorySelector;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {

        IMessageProtocol protocol = protocolFactorySelector.getProtocol(protocolType);
        if (protocol == null) {
            logger.error("protocol is null");
            return;
        }
        byte[] bb = protocol.encode(msg.getContent());

        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeShort(msg.getCmd());
        byteBuf.writeBytes(bb);
        BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(byteBuf);
        out.add(binaryWebSocketFrame);
    }
}

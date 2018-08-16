package game.core.nettyCore.client.corder;

import game.core.nettyCore.client.ClientDef;
import game.core.nettyCore.coder.IMessageProtocol;
import game.core.nettyCore.model.Message;
import game.core.nettyCore.proto.ProtocolFactorySelector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nullzZ
 *
 */
public class ClientMessageEncoder extends MessageToByteEncoder<Message> {

	private static final Logger logger = LoggerFactory.getLogger(ClientMessageEncoder.class);
	private final ClientDef clientDef;

	public ClientMessageEncoder(ClientDef clientDef) {
		this.clientDef = clientDef;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		IMessageProtocol protocol = ProtocolFactorySelector.getInstance().getProtocol(clientDef.protocolType);
		if (protocol == null) {
			logger.error("protocol is null");
			return;
		}
		byte[] bb = protocol.encode(msg.getContent());
		int len = bb.length + 4;
		out.writeShort(len);
		out.writeShort(msg.getCmd());
		out.writeBytes(bb);
		ctx.flush();
	}

}

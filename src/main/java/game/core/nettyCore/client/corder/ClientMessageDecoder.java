package game.core.nettyCore.client.corder;

import game.core.nettyCore.client.ClientDef;
import game.core.nettyCore.coder.IMessageProtocol;
import game.core.nettyCore.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author nullzZ
 *
 */
public class ClientMessageDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LoggerFactory.getLogger(ClientMessageDecoder.class);
	private final int MESSAGE_FRAME_SIZE = 4;
	private final ClientDef clientDef;
	private final int maxFrameSize;

	public ClientMessageDecoder(ClientDef clientDef) {
		this.clientDef = clientDef;
		maxFrameSize = clientDef.maxFrameSize;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			if (in.readableBytes() < MESSAGE_FRAME_SIZE) {
				in.resetReaderIndex();
				return;
			}
			short dataLength = in.readShort();
			if (dataLength < 0) {
				ctx.close();
				return;
			}

			int messageContentsLength = dataLength;
			if (messageContentsLength > maxFrameSize) {
				throw new TooLongFrameException(
						String.format("Frame size exceeded on encode: frame was %d bytes, maximum allowed is %d bytes",
								messageContentsLength, maxFrameSize));
			}

			short cmd = in.readShort();

			int contentLen = dataLength - MESSAGE_FRAME_SIZE;
			if (contentLen < 0) {
				return;
			}
			if (in.readableBytes() < contentLen) {
				in.resetReaderIndex();
				return;
			}

			byte[] body = new byte[contentLen];
			in.readBytes(body);

			IMessageProtocol protocol = clientDef.protocolFactorySelector.getProtocol(clientDef.protocolType);
			if (protocol == null) {
				logger.error("protocol is null");
				return;
			}
			Message mes = Message.newBuilder().cmd(cmd)
					.message(protocol.decode(body, clientDef.handlerManager.getMessageClazz(cmd))).build();
			out.add(mes);

		} catch (Exception ex) {
			logger.error("decode exception", ex);
			return;
		}

	}

}

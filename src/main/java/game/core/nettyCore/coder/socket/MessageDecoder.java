package game.core.nettyCore.coder.socket;

import game.core.nettyCore.serverDef.ServerDef;
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
 */
public class MessageDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);
    private final int MESSAGE_FRAME_SIZE = 4;
    private final ServerDef serverDef;
    private final int maxFrameSize;

    public MessageDecoder(ServerDef serverDef) {
        this.serverDef = serverDef;
        maxFrameSize = serverDef.maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            // if (buf.readableBytes() < IMessageCodecUtil.MOSHU) {
            // return;
            // }
            // buf.markReaderIndex();
            // byte magic1 = buf.readByte();// 魔数
            // byte magic2 = buf.readByte();
            // if (magic1 != magic1Check || magic2 != magic2Check) {
            // ctx.close();
            // log.error("魔数异常|" + magic1 + "," + magic2);
            // return;
            // }
            if (in.readableBytes() < MESSAGE_FRAME_SIZE) {
                in.resetReaderIndex();
                return;
            }
            short dataLength = in.readShort();
            if (dataLength < 0) {
                ctx.close();
                return;
            }

//            int messageContentsLength = MESSAGE_FRAME_SIZE + dataLength;
            int messageContentsLength = dataLength;
            if (messageContentsLength > maxFrameSize) {
                throw new TooLongFrameException(
                        String.format("Frame size exceeded on encode: frame was %d bytes, maximum allowed is %d bytes",
                                messageContentsLength, maxFrameSize));
            }

            int contentLen = dataLength - MESSAGE_FRAME_SIZE;
            if (contentLen < 0) {
                return;
            }
            if (in.readableBytes() < contentLen) {
                in.resetReaderIndex();
                return;
            }

            // byte version = in.readByte();// 版本
            // byte serializeType = in.readByte();// 序列化类型
            int cmd = in.readShort();
            byte[] body = new byte[contentLen];
            in.readBytes(body);

            IMessageProtocol protocol = serverDef.protocolFactorySelector.getProtocol(serverDef.protocolType);
            if (protocol == null) {
                logger.error("protocol is null");
                return;
            }
            Message mes = Message.newBuilder().cmd(cmd)
                    .message(protocol.decode(body, serverDef.handlerManager.getMessageClazz(cmd))).build();
            out.add(mes);

        } catch (Exception ex) {
            logger.error("decode exception", ex);
            return;
        }

    }

}

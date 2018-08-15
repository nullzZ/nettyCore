package game.core.nettyCore.proto.jprotobuff;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import game.core.nettyCore.coder.IMessageProtocol;

public class JProtoBuffProtocol implements IMessageProtocol {
    @Override
    public <T> byte[] encode(T message, Class<T> clazz) throws Exception {
        Codec<T> simpleTypeCodec = ProtobufProxy
                .create(clazz);
        return simpleTypeCodec.encode(message);
    }


    @Override
    public <T> T decode(byte[] body, Class<T> clazz) throws Exception {
        Codec<T> simpleTypeCodec = ProtobufProxy
                .create(clazz);
        return simpleTypeCodec.decode(body);
    }
}

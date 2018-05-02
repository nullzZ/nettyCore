package game.core.nettyCore.proto.json;


import com.alibaba.fastjson.JSON;
import game.core.nettyCore.coder.IMessageProtocol;

public class JsonProtocol implements IMessageProtocol {

    @Override
    public byte[] encode(Object message) throws Exception {
        return JSON.toJSONBytes(message);
    }

    @Override
    public <T> T decode(byte[] body, Class<T> clazz) throws Exception {
        if (body == null || body.length == 0) {
            return null;
        }
        return JSON.parseObject(body, clazz);
    }
}

/**
 *
 */
package game.core.nettyCore.proto;

import java.util.HashMap;

import game.core.nettyCore.coder.IMessageProtocol;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.proto.json.JsonProtocol;
import game.core.nettyCore.proto.protostuff.ProtostuffProtocol;

/**
 * @author nullzZ
 */
public class ProtocolFactorySelector implements IProtocolFactorySelector {
    private final HashMap<Integer, IMessageProtocol> protocolFactoryMap = new HashMap<>(8);

//    private static final ProtocolFactorySelector instance = new ProtocolFactorySelector();
//
//    public static ProtocolFactorySelector getInstance() {
//        return instance;
//    }

    public ProtocolFactorySelector() {
        // protocolFactoryMap.put((short) -32767, new
        // TBinaryProtocol.Factory());
        // protocolFactoryMap.put((short) -32223, new
        // TCompactProtocol.Factory());
        // protocolFactoryMap.put((short) 23345, new TJSONProtocol.Factory());
        registProtocolFactory(ProtocolType.PROTOSTUFF, new ProtostuffProtocol());
        registProtocolFactory(ProtocolType.JSON, new JsonProtocol());
    }

    protected void registProtocolFactory(ProtocolType type, IMessageProtocol protocol) {
        protocolFactoryMap.put(type.type(), protocol);
    }

    @Override
    public IMessageProtocol getProtocol(ProtocolType type) {
        IMessageProtocol fac = protocolFactoryMap.get(type.type());
        return fac;
    }
}

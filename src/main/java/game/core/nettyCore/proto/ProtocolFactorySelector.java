/**
 *
 */
package game.core.nettyCore.proto;

import java.util.HashMap;

import game.core.nettyCore.coder.IMessageProtocol;
import game.core.nettyCore.coder.ProtocolType;
import game.core.nettyCore.proto.jprotobuff.JProtoBuffProtocol;
import game.core.nettyCore.proto.json.JsonProtocol;
import game.core.nettyCore.proto.protostuff.ProtostuffProtocol;

/**
 * @author nullzZ
 */
public class ProtocolFactorySelector implements IProtocolFactorySelector {
    private final HashMap<Integer, IMessageProtocol> protocolFactoryMap = new HashMap<>(8);

    private static final ProtocolFactorySelector instance = new ProtocolFactorySelector();

    public static ProtocolFactorySelector getInstance() {
        return instance;
    }


    private ProtocolFactorySelector() {
        registProtocol(ProtocolType.JPROTOBUFF, new JProtoBuffProtocol());
        registProtocol(ProtocolType.PROTOSTUFF, new ProtostuffProtocol());
        registProtocol(ProtocolType.JSON, new JsonProtocol());
    }

    protected void registProtocol(ProtocolType type, IMessageProtocol protocol) {
        protocolFactoryMap.put(type.type(), protocol);
    }

    @Override
    public IMessageProtocol getProtocol(ProtocolType type) {
        IMessageProtocol fac = protocolFactoryMap.get(type.type());
        return fac;
    }
}

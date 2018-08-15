package game.core.nettyCore.proto;

import game.core.nettyCore.coder.IMessageProtocol;
import game.core.nettyCore.coder.ProtocolType;

/**
 * @author nullzZ
 */
public interface IProtocolFactorySelector {
    IMessageProtocol getProtocol(ProtocolType type);
}

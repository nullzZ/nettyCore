/**
 * 
 */
package game.core.nettyCore.proto;

import game.core.nettyCore.coder.ProtocolType;

/**
 * @author nullzZ
 *
 */
public interface ProtocolFactorySelectorFactory {

	ProtocolFactorySelector createProtocolFactorySelector();
}

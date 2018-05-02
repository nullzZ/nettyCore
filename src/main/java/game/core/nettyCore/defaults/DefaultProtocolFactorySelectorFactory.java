/**
 * 
 */
package game.core.nettyCore.defaults;

import game.core.nettyCore.proto.ProtocolFactorySelector;
import game.core.nettyCore.proto.ProtocolFactorySelectorFactory;

/**
 * @author nullzZ
 *
 */
public class DefaultProtocolFactorySelectorFactory implements ProtocolFactorySelectorFactory {

	@Override
	public ProtocolFactorySelector createProtocolFactorySelector() {
		return new ProtocolFactorySelector();
	}

}

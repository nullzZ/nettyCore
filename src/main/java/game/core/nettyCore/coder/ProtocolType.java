package game.core.nettyCore.coder;

/**
 * @author nullzZ
 *
 */
public enum ProtocolType {

	PROTOSTUFF(1), JSON(2);
	private int type;

	private ProtocolType(int type) {
		this.type = type;
	}

	public int type() {
		return type;
	}

}

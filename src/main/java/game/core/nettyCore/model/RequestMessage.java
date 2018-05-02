package game.core.nettyCore.model;

/**
 * @author nullzZ
 *
 */
public abstract class RequestMessage {

	protected short cmd;

	public RequestMessage(short cmd) {
		this.cmd = cmd;
	}

	public short getCmd() {
		return cmd;
	}

	public void setCmd(short cmd) {
		this.cmd = cmd;
	}

}

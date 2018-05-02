package game.core.nettyCore.coder;

/**
 * @author nullzZ
 *
 */
public interface IMessageProtocol {
	public  byte[] encode(final Object message) throws Exception;

	public <T> T decode(byte[] body, Class<T> clazz) throws Exception;
}

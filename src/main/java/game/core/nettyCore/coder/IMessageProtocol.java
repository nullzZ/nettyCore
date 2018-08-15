package game.core.nettyCore.coder;

/**
 * @author nullzZ
 */
public interface IMessageProtocol {
    <T> byte[] encode(final T message, Class<T> clazz) throws Exception;

    <T> T decode(byte[] body, Class<T> clazz) throws Exception;
}

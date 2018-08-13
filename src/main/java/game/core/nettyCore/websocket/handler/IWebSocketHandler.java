package game.core.nettyCore.websocket.handler;

/**
 * @author nullzZ
 */
public interface IWebSocketHandler<R, M, S> {

    S execute(R r, M m) throws Throwable;
}

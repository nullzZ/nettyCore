package game.core.nettyCore.http.webSocket;

/**
 * @author nullzZ
 */
public interface IWebSocketHandler<R, M, S> {

    S execute(R r, M m) throws Throwable;
}

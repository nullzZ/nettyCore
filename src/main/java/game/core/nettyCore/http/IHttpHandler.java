package game.core.nettyCore.http;

/**
 * @author nullzZ
 */
public interface IHttpHandler<R, M, S> {

    S execute(R r, String token, M m) throws Throwable;
}

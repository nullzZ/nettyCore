package game.core.nettyCore.http;

/**
 * @author nullzZ
 */
public interface IHttpHandler<M, S> {

    S execute(String token, M m) throws Throwable;
}

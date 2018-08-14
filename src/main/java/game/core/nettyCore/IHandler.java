package game.core.nettyCore;

/**
 * @author nullzZ
 */
public interface IHandler<R, M, S> {

    S execute(R r, M m) throws Throwable;
}

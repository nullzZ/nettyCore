package game.core.nettyCore.bootstrap;

public interface IServerBootstrap extends Runnable {

    void start() throws Exception;

    void start(int bossThreads, int workThreads) throws Exception;
}

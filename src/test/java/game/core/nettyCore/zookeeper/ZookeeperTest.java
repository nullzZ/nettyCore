package game.core.nettyCore.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class ZookeeperTest {
    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 2000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("已经触发了" + watchedEvent.getType() + "事件！");
                }
            });
            Stat stat = zooKeeper.exists("/testRootPath",true);
            
            zooKeeper.create("/testRootPath","ttt".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
            zooKeeper.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
            zooKeeper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

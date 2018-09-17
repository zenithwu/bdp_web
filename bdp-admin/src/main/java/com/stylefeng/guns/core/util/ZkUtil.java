package com.stylefeng.guns.core.util;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ZkUtil {

    private static Logger logger=LoggerFactory.getLogger(ZkUtil.class);
    private int timeout=30000;
    private ZooKeeper zooKeeper=null;

    public ZkUtil(String connectStr) {
        try {
            this.zooKeeper= new ZooKeeper(connectStr,timeout, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {

                }
            });
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }



    public byte[] getValue(String path) throws Exception {
        return zooKeeper.getData(path,false,new Stat());
    }

    public void close() throws InterruptedException {
        zooKeeper.close();

    }

}

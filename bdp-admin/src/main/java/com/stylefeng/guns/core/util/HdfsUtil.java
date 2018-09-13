package com.stylefeng.guns.core.util;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.server.namenode.ha.proto.HAZKInfoProtos;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * hdfs工具类
 */
public class HdfsUtil {

    private static Logger logger = Logger.getLogger(HdfsUtil.class);
    private String FS_URL;
    public HdfsUtil(String zkUrl) throws Exception {
        ZkUtil zk=new ZkUtil(zkUrl);
        HAZKInfoProtos.ActiveNodeInfo activeNodeInfo=HAZKInfoProtos.ActiveNodeInfo.parseFrom(zk.getValue("/hadoop-ha/bdpns/ActiveStandbyElectorLock"));
        FS_URL=String.format("hdfs://%s",activeNodeInfo.getHostname());
        zk.close();
    }


    /**
     * 上传方法
     * @param fileContent  内容
     * @param dst  目标路径
     * @return 成功还是失败
     */
    public boolean writeFile(String fileContent, String dst) {
        Configuration conf=new Configuration();
        Path dstPath = new Path(FS_URL+dst);
        try {
            FileSystem fs  = dstPath.getFileSystem(conf);
            if(fs.exists(dstPath)){
                fs.delete(dstPath,true);
            }
            //Init output stream
            FSDataOutputStream outputStream=fs.create(new Path(dst));
            //Cassical output stream usage
            outputStream.writeBytes(fileContent);
            outputStream.close();


        } catch (IOException ie) {
            logger.error(ie.getMessage());
            return false;
        }
        return true;
    }


    public static void main(String[] args) throws Exception {

//        HdfsUtil util=new HdfsUtil("bdata001:2181");
//        util.writeFile("{\"id\":1,\"name\":\"zenith\"}","/bdp/jobconfig/test.json");

    }




}

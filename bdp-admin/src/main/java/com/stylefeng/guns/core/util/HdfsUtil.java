package com.stylefeng.guns.core.util;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;

/**
 * hdfs工具类
 */
public class HdfsUtil {

    private static Logger logger = Logger.getLogger(HdfsUtil.class);
    public DistributedFileSystem dfs;

    public HdfsUtil(String nameNodeStr) throws Exception {
        Configuration conf=new Configuration(false);
        String ns = "bdpns";
        String[] nameNodesAddr = nameNodeStr.split(",");
        String[] nameNodes = {"nn1","nn2"};
        conf.set("fs.defaultFS", "hdfs://" + ns);
        conf.set("dfs.nameservices",ns);
        conf.set("dfs.ha.namenodes." + ns, nameNodes[0]+","+nameNodes[1]);
        conf.set("dfs.namenode.rpc-address." + ns + "." + nameNodes[0], nameNodesAddr[0]);
        conf.set("dfs.namenode.rpc-address." + ns + "." + nameNodes[1], nameNodesAddr[1]);
        conf.set("dfs.client.failover.proxy.provider." + ns,"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        String hdfsRPCUrl = "hdfs://" + ns + ":" + 8020;
        dfs = new DistributedFileSystem();
        dfs.initialize(URI.create(hdfsRPCUrl),conf);
    }


    /**
     * 上传方法
     * @param fileContent  内容
     * @param dst  目标路径
     * @return 成功还是失败
     */
    public boolean writeFile(byte[] fileContent, String dst) {
        Path dstPath = new Path(dst);
        try {
            if(dfs.exists(dstPath)){
                dfs.delete(dstPath,true);
            }
            if(!dfs.exists(dstPath.getParent())){
                dfs.mkdirs(dstPath.getParent());
            }
            //Init output stream
            FSDataOutputStream outputStream=dfs.create(dstPath);
            //Cassical output stream usage
            outputStream.write(fileContent);
            outputStream.close();


        } catch (IOException ie) {
            logger.error(ie.getMessage());
            return false;
        }
        return true;
    }
    public boolean delete(String dst) {
        Path dstPath = new Path(dst);
        try {
            if(dfs.exists(dstPath)){
                dfs.delete(dstPath,true);
            }
        } catch (IOException ie) {
            logger.error(ie.getMessage());
            return false;
        }
        return true;
}


    public static void main(String[] args) throws Exception {

//        HdfsUtil util=new HdfsUtil("bdata001:8020,bdata002:8020");
//        util.writeFile("{\"id\":1,\"name\":\"zenithnw\"}","/bdp/jobconfig/test.json");
//        util.dfs.close();
    }




}

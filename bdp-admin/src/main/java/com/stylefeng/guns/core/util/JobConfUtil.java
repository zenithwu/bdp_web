package com.stylefeng.guns.core.util;

import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.config.properties.BdpJobConfig;
import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.core.constant.conTypeType;
import com.stylefeng.guns.modular.bdp.service.IConfConnectService;
import com.stylefeng.guns.modular.bdp.service.IConfConnectTypeService;
import com.stylefeng.guns.modular.system.model.ConfConnect;
import com.stylefeng.guns.modular.system.model.ConfConnectType;
import com.stylefeng.guns.modular.system.model.JobConfig;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobConfUtil {

    public static String wrapShell(String shell, JenkinsConfig jenkinsConfig) {
        String begin = "source /etc/profile\n" +
                "param=${time_hour}\n" +
                "stat_date=`date +%Y-%m-%d`\n" +
                "if [ -z '%param' ]; then\n" +
                "param=`date +%Y-%m-%d %H:%M:%S`\n" +
                "fi\n" +
                "curl -d \"jobName=${JOB_NAME}&number=${BUILD_NUMBER}&stat_date=${stat_date}&params=${param}\" \"" + jenkinsConfig.getRest_url() + "/rest/job_begin\" &\n" +
                "function run(){\n";
        String end = "\n}\n" +
                "run && (curl -d \"jobName=${JOB_NAME}&number=${BUILD_NUMBER}&stat_date=${stat_date}\" \"" + jenkinsConfig.getRest_url() + "/rest/job_end\" &) " +
                "|| (curl -d \"jobName=${JOB_NAME}&number=${BUILD_NUMBER}&stat_date=${stat_date}\" \"" + jenkinsConfig.getRest_url() + "/rest/job_end\" & exit 1)";

        return begin + shell + end;
    }


    public static String gentProcExeShell(JobConfig jobConfig, JenkinsConfig jenkinsConfig, BdpJobConfig bdpJobConfig) {
        String shell = String.format("spark2-submit --class %s \\\n" +
                        "--master yarn \\\n" +
                        "--deploy-mode cluster \\\n" +
                        "--driver-memory %s \\\n" +
                        "--driver-cores %s \\\n" +
                        "--executor-memory %s \\\n" +
                        "--executor-cores %s \\\n" +
                        "--queue default \\\n" +
                        "hdfs://%s/%s", jobConfig.getBase_proc_main_class()
                , jobConfig.getResource_dm()
                , jobConfig.getResource_dc()
                , jobConfig.getResource_em()
                , jobConfig.getResource_ec()
                , bdpJobConfig.getJoblib()
                , jobConfig.getBase_proc_main_file());
        if (StringUtils.isNotEmpty(jobConfig.getBase_proc_main_in())) {
            shell = shell + " " + jobConfig.getBase_proc_main_in();
        }
        if (StringUtils.isNotEmpty(jobConfig.getBase_proc_main_out())) {
            shell = shell + " " + jobConfig.getBase_proc_main_out();
        }
        if (StringUtils.isNotEmpty(jobConfig.getBase_proc_main_args())) {
            shell = shell + " " + jobConfig.getBase_proc_main_args();
        }
        return wrapShell(shell, jenkinsConfig);


    }


    public static Map<String, String> createJdbcUrl(JobConfig jobConfig, IConfConnectService confConnectService, IConfConnectTypeService confConnectTypeService) {
        ConfConnect conf = confConnectService.selectById(jobConfig.getOutput_connect_id());

        Map<String, String> urlParms = new HashMap<String, String>();

        ConfConnectType confConnectType = confConnectTypeService.selConfConnectTypeById(jobConfig.getOutput_connect_type());
        String url = null;
        if (confConnectType.getType().equals(0)) { // rdbms
            // 此处不能判断到数据库的类型, 只支持mysql
            url = String.format("jdbc:mysql://%s:%s/%s", conf.getHost(), conf.getPort(), conf.getDbname());
        } else { // ftp

        }
        urlParms.put("url", url);
        urlParms.put("user", conf.getUsername());
        urlParms.put("password", conf.getPassword());

        return urlParms;
    }


    public static String genConfigFile(JobConfig jobConfig, ConfConnect conf, ConfConnectType confConnectType) {
        String config = null;
        // sql查询, jobConfig.getInput_input_type().equals("sql")
        String tabAndLoc = genTableLocation(jobConfig);
        if (StringUtils.isEmpty(jobConfig.getHight_thread())) {
            jobConfig.setHight_thread("1");
        }
        if (StringUtils.isEmpty(jobConfig.getHight_file_num())) {
            jobConfig.setHight_file_num("1");
        }
        //关系型数据的实现
        if (confConnectType.getType().equals(conTypeType.RDBMS.getCode())) {
            config = String.format("exec: {max_threads: %s, min_output_tasks: %s}\n" +
                            "in:\n" +
                            "  type: %s\n" +
                            "  host: %s\n" +
                            "  port: %s\n" +
                            "  user: %s\n" +
                            "  password: %s\n" +
                            "  database: %s\n" +
                            "  query: %s\n" +
                            "  use_raw_query_with_incremental: false\n" +
                            "  options: {useLegacyDatetimeCode: false, serverTimezone: CST}\n" +
                            "out:\n" +
                            "  type: hdfs\n" +
                            "  config_files: [/etc/hadoop/conf/core-site.xml, /etc/hadoop/conf/hdfs-site.xml]\n" +
                            "  config: {fs.defaultFS: 'hdfs://bdpns', fs.hdfs.impl: org.apache.hadoop.hdfs.DistributedFileSystem,\n" +
                            "    fs.file.impl: org.apache.hadoop.fs.LocalFileSystem}\n" +
                            "  path_prefix: /user/hive/warehouse/%s/\n" +
                            "  file_ext: text\n" +
                            "  mode: %s\n" +
                            "  formatter: {type: jsonl, encoding: UTF-8}",
                    jobConfig.getHight_thread(),
                    jobConfig.getHight_file_num(),
                    confConnectType.getName(),
                    conf.getHost(),
                    conf.getPort(),
                    conf.getUsername(),
                    conf.getPassword(),
                    conf.getDbname(),
                    jobConfig.getInput_input_content(),
                    tabAndLoc,
                    jobConfig.getOutput_write_model()
            );
        } else {
            config = String.format("exec: {max_threads: %s, min_output_tasks: %s}\n" +
                            "in:\n" +
                            "  type: ftp\n" +
                            "  host: %s\n" +
                            "  port: %s\n" +
                            "  user: %s\n" +
                            "  password: \"%s\"\n" +
                            "  path_prefix: %s\n" +
                            "\n" +
                            "  ssl: true\n" +
                            "  ssl_verify: false\n" +
                            "out:\n" +
                            "  type: hdfs\n" +
                            "  config_files: [/etc/hadoop/conf/core-site.xml, /etc/hadoop/conf/hdfs-site.xml]\n" +
                            "  config: {fs.defaultFS: 'hdfs://bdpns', fs.hdfs.impl: org.apache.hadoop.hdfs.DistributedFileSystem,\n" +
                            "    fs.file.impl: org.apache.hadoop.fs.LocalFileSystem}\n" +
                            "  path_prefix: /user/hive/warehouse/%s/\n" +
                            "  file_ext: text\n" +
                            "  mode: %s\n" +
                            "  formatter: {type: jsonl, encoding: UTF-8}",
                    jobConfig.getHight_thread(),
                    jobConfig.getHight_file_num(),
                    conf.getHost(),
                    conf.getPort(),
                    conf.getUsername(),
                    conf.getPassword(),
                    jobConfig.getInput_file_position(),
                    tabAndLoc,
                    jobConfig.getOutput_write_model()
            );
        }
        return config;
    }

    private static String genTableLocation(JobConfig jobConfig) {
        String partition = null;
        String tabName = "default".equals(jobConfig.getOutput_db_name()) ? jobConfig.getOutput_table_name() : jobConfig.getOutput_db_name() + ".db/" + jobConfig.getOutput_table_name();

        // partition
        if (jobConfig.getOutput_data_partition() != null) {
            ArrayList<String> arrayList = new ArrayList<String>();

            for (String item : StringUtils.split(jobConfig.getOutput_data_partition(), ",")) {
                StringUtils.trimToNull(item);
                arrayList.add(item);
            }
            partition = StringUtils.join(arrayList.toArray(), "/");
        }
        return (partition == null) ? tabName : tabName + "/" + partition;
    }


    public static String genOutPutShell(JobConfig jobConfig, ConfConnect conf, ConfConnectType confConnectType, BdpJobConfig bdpJobConfig) {
        String shell;
        Map<String, String> jdbcUrl = JDBCUtil.createJDBCUrl(conf, confConnectType.getType());
        String sql_statement = null;

        // 构造SQL statement
        sql_statement = jobConfig.getInput_input_content();
        List<String> cmdList = new ArrayList<>();
        // 构造命令
        cmdList.add("/bin/spark2-submit");
        cmdList.add("--class com.it863.common.ExportHiveToRDB");
        cmdList.add("--jars /opt/cm-5.15.0/share/cmf/lib/mysql-connector-java-5.1.46.jar,/opt/cloudera/parcels/CDH/jars/libthrift-0.9.3.jar");
        cmdList.add(String.format("hdfs://%s/bdp-common-1.0.0.jar",bdpJobConfig.getJoblib()));
        cmdList.add(jobConfig.getJobId().toString());
        cmdList.add("${param}");

        shell = String.join(" ", cmdList.toArray(new String[cmdList.size()]));
        return shell;
    }

    public static void upLoadJobConf(JobConfig jobConfig, ConfConnect conf, ConfConnectType confConnectType, String zkUrl, String conUrl) throws Exception {


        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(jobConfig);
        jsonObject.put("type", confConnectType.getName());
        jsonObject.put("host", conf.getHost());
        jsonObject.put("port", conf.getPort());
        jsonObject.put("username", conf.getUsername());
        jsonObject.put("password", conf.getPassword());
        jsonObject.put("dbname", conf.getDbname());
        jsonObject.put("driverClass", confConnectType.getDriverClass());

        HdfsUtil util = new HdfsUtil(zkUrl);
        util.writeFile(jsonObject.toJSONString(), String.format("%s/jobid_%s.json", conUrl, jobConfig.getJobId()));


    }


}

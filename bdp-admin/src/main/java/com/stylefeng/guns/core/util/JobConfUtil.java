package com.stylefeng.guns.core.util;

import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.modular.bdp.service.IConfConnectService;
import com.stylefeng.guns.modular.bdp.service.IConfConnectTypeService;
import com.stylefeng.guns.modular.system.model.ConfConnect;
import com.stylefeng.guns.modular.system.model.ConfConnectType;
import com.stylefeng.guns.modular.system.model.JobConfig;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
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



    public static String gentProcExeShell(JobConfig jobConfig,JenkinsConfig jenkinsConfig){
        String shell=String.format("spark2-submit --class %s \\\n" +
                "--master yarn \\\n" +
                "--deploy-mode cluster \\\n" +
                "--driver-memory %s \\\n" +
                "--driver-cores %s \\\n" +
                "--executor-memory %s \\\n" +
                "--executor-cores %s \\\n" +
                "--queue default \\\n" +
                "hdfs:///spark-lib/%s",jobConfig.getBase_proc_main_class()
                ,jobConfig.getResource_dm()
                ,jobConfig.getResource_dc()
                ,jobConfig.getResource_em()
                ,jobConfig.getResource_ec()
                ,jobConfig.getBase_proc_main_file());
        if (StringUtils.isNotEmpty(jobConfig.getBase_proc_main_in())){
                shell=shell+" "+jobConfig.getBase_proc_main_in();
        }
        if (StringUtils.isNotEmpty(jobConfig.getBase_proc_main_out())){
            shell=shell+" "+jobConfig.getBase_proc_main_out();
        }
        if (StringUtils.isNotEmpty(jobConfig.getBase_proc_main_args())){
            shell=shell+" "+jobConfig.getBase_proc_main_args();
        }
      return wrapShell(shell,jenkinsConfig);



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


    public static String genConfigFile(JobConfig jobConfig,IConfConnectService confConnectService) {
        ConfConnect conf = confConnectService.selectById(jobConfig.getInput_connect_id());
        String mysql2HDFS = null;

        // sql查询, jobConfig.getInput_input_type().equals("sql")
        String tabAndLoc = genTableLocation(jobConfig);
        mysql2HDFS = String.format("exec: {max_threads: 8, min_output_tasks: 1}\n" +
                        "in:\n" +
                        "  type: mysql\n" +
                        "  driver_path: /opt/cm-5.15.0/share/cmf/lib/mysql-connector-java-5.1.46.jar\n" +
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
                conf.getHost(),
                conf.getPort(),
                conf.getUsername(),
                conf.getPassword(),
                conf.getDbname(),
                jobConfig.getInput_input_content(),
                tabAndLoc,
                jobConfig.getOutput_write_model()
        );

        return mysql2HDFS;
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

}

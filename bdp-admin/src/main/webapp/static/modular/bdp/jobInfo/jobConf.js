var JobConf = {
    JobConfData : {}
};

/**
 * 清除数据
 */
JobConf.clearData = function() {
    this.JobConfData = {};
}
/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobConf.set = function(key, val) {
    this.JobConfData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}
/**
 * 收集数据
 */
JobConf.collectData = function() {
    this
        .set('jobId')
        .set('input_connect_type')
        .set('input_connect_id')
        .set('input_input_type')
        .set('input_input_content')
        .set('input_file_position')
        .set('input_db_name')
        .set('input_table_name')
        .set('input_data_partition')
        .set('output_connect_type')
        .set('output_connect_id')
        .set('output_db_name')
        .set('output_table_name')
        .set('output_write_model')
        .set('output_data_partition')
        .set('output_pre_statment')
        .set('schedule_crontab')
        .set('schedule_depend')
        .set('hight_resource')
        .set('hight_thread')
        .set('hight_file_num')
        .set('sql_statment')
        .set('base_proc_main_class')
        .set('base_proc_main_in')
        .set('base_proc_main_out')
        .set('base_proc_main_args')
        .set('base_proc_main_file')
        .set('resource_dm')
        .set('resource_sc')
        .set('resource_em')
        .set('resource_ec');
}

/**
 * 关闭此对话框
 */
JobConf.close = function() {
    parent.layer.close(window.parent.JobInfo.layerIndex);
}

/**
 * 保存配置信息
 */
JobConf.save = function () {
    //提交信息
    this.clearData();
    this.collectData();
    var ajax = new $ax(Feng.ctxPath + "/jobInfo/saveData", function(data){
        Feng.success("修改成功!");
        window.parent.JobInfo.table.refresh();
        JobConf.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.JobConfData);
    ajax.start();
};




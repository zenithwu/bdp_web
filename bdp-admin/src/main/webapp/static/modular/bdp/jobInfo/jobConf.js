var JobInputDataConf = {
    JobConfData : {}
};

/**
 * 清除数据
 */
JobInputDataConf.clearData = function() {
    this.JobConfData = {};
}
/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobInputDataConf.set = function(key, val) {
    this.JobConfData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}
/**
 * 收集数据
 */
JobInputDataConf.collectData = function() {
    this
        .set('jobId')
        .set('input_connect_type')
        .set('input_connect_id')
        .set('input_input_type')
        .set('input_input_content')
        .set('input_file_position')
        .set('input_db_name')
        .set('input_table_name')
        .set('output_write_model')
        .set('output_data_partition')
        .set('schedule_crontab')
        .set('schedule_depend')
        .set('hight_resource')
        .set('hight_thread')
        .set('hight_file_num');
}

/**
 * 保存配置信息
 */
JobInputDataConf.save = function () {
    //提交信息
    this.clearData();
    this.collectData();
    var ajax = new $ax(Feng.ctxPath + "/jobInfo/saveInputData", function(data){
        Feng.success("修改成功!");
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.JobConfData);
    ajax.start();
};




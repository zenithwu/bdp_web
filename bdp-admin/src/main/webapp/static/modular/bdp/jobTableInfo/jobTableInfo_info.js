/**
 * 初始化任务数据表详情对话框
 */
var JobTableInfoInfoDlg = {
    jobTableInfoInfoData : {}
};

/**
 * 清除数据
 */
JobTableInfoInfoDlg.clearData = function() {
    this.jobTableInfoInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobTableInfoInfoDlg.set = function(key, val) {
    this.jobTableInfoInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobTableInfoInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
JobTableInfoInfoDlg.close = function() {
    parent.layer.close(window.parent.JobTableInfo.layerIndex);
}

/**
 * 收集数据
 */
JobTableInfoInfoDlg.collectData = function() {
    this
    .set('dbName')
    .set('tbaleName')
    .set('desc');
}

/**
 * 提交修改
 */
JobTableInfoInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobTableInfo/update", function(data){
        Feng.success("修改成功!");
        window.parent.JobTableInfo.table.refresh();
        JobTableInfoInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobTableInfoInfoData);
    ajax.start();
}

$(function() {

});

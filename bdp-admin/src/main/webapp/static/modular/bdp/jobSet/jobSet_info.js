/**
 * 初始化任务集详情对话框
 */
var JobSetInfoDlg = {
    jobSetInfoData : {}
};

/**
 * 清除数据
 */
JobSetInfoDlg.clearData = function() {
    this.jobSetInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobSetInfoDlg.set = function(key, val) {
    this.jobSetInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobSetInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
JobSetInfoDlg.close = function() {
    parent.layer.close(window.parent.JobSet.layerIndex);
}

/**
 * 收集数据
 */
JobSetInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('desc')
    .set('createTime')
    .set('createPer')
    .set('modTime')
    .set('modPer');
}

/**
 * 提交添加
 */
JobSetInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobSet/add", function(data){
        Feng.success("添加成功!");
        window.parent.JobSet.table.refresh();
        JobSetInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobSetInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
JobSetInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobSet/update", function(data){
        Feng.success("修改成功!");
        window.parent.JobSet.table.refresh();
        JobSetInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobSetInfoData);
    ajax.start();
}

$(function() {

});

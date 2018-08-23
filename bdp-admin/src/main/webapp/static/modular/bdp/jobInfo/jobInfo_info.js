/**
 * 初始化任务信息详情对话框
 */
var JobInfoInfoDlg = {
    jobInfoInfoData : {}
};

/**
 * 清除数据
 */
JobInfoInfoDlg.clearData = function() {
    this.jobInfoInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobInfoInfoDlg.set = function(key, val) {
    this.jobInfoInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobInfoInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
JobInfoInfoDlg.close = function() {
    parent.layer.close(window.parent.JobInfo.layerIndex);
}

/**
 * 收集数据
 */
JobInfoInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('typeId')
    .set('desc')
    .set('enable')
    .set('createTime')
    .set('createPer')
    .set('modTime')
    .set('modPer')
    .set('lastRunState')
    .set('lastRunTime')
    .set('lastRunCost')
    .set('userInfoId')
    .set('jobSetId');
}

/**
 * 提交添加
 */
JobInfoInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobInfo/add", function(data){
        Feng.success("添加成功!");
        window.parent.JobInfo.table.refresh();
        JobInfoInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobInfoInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
JobInfoInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobInfo/update", function(data){
        Feng.success("修改成功!");
        window.parent.JobInfo.table.refresh();
        JobInfoInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobInfoInfoData);
    ajax.start();
}

$(function() {

});

/**
 * 初始化配置连接详情对话框
 */
var ConfConnectInfoDlg = {
    confConnectInfoData : {}
};

/**
 * 清除数据
 */
ConfConnectInfoDlg.clearData = function() {
    this.confConnectInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ConfConnectInfoDlg.set = function(key, val) {
    this.confConnectInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ConfConnectInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ConfConnectInfoDlg.close = function() {
    parent.layer.close(window.parent.ConfConnect.layerIndex);
}

/**
 * 收集数据
 */
ConfConnectInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('typeId')
    .set('host')
    .set('port')
    .set('username')
    .set('password')
    .set('dbname')
    .set('desc')
    .set('createTime')
    .set('createPer')
    .set('modTime')
    .set('modPer');
}

/**
 * 提交添加
 */
ConfConnectInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/confConnect/add", function(data){
        Feng.success("添加成功!");
        window.parent.ConfConnect.table.refresh();
        ConfConnectInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.confConnectInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ConfConnectInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/confConnect/update", function(data){
        Feng.success("修改成功!");
        window.parent.ConfConnect.table.refresh();
        ConfConnectInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.confConnectInfoData);
    ajax.start();
}

$(function() {

});

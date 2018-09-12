/**
 * 初始化配置连接类型详情对话框
 */
var ConfConnectTypeInfoDlg = {
    confConnectTypeInfoData : {}
};

/**
 * 清除数据
 */
ConfConnectTypeInfoDlg.clearData = function() {
    this.confConnectTypeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ConfConnectTypeInfoDlg.set = function(key, val) {
    this.confConnectTypeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ConfConnectTypeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ConfConnectTypeInfoDlg.close = function() {
    parent.layer.close(window.parent.ConfConnectType.layerIndex);
}

/**
 * 收集数据
 */
ConfConnectTypeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('desc').set('driverClass')
    .set('createTime')
    .set('createPer')
    .set('modTime')
    .set('modPer');
}

/**
 * 提交添加
 */
ConfConnectTypeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/confConnectType/add", function(data){
        Feng.success("添加成功!");
        window.parent.ConfConnectType.table.refresh();
        ConfConnectTypeInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.confConnectTypeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ConfConnectTypeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/confConnectType/update", function(data){
        Feng.success("修改成功!");
        window.parent.ConfConnectType.table.refresh();
        ConfConnectTypeInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.confConnectTypeInfoData);
    ajax.start();
}

$(function() {

});

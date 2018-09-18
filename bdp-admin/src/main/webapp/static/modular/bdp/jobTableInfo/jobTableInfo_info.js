/**
 * 初始化数据表信息详情对话框
 */
var JobTableInfoInfoDlg = {
    count: $("#itemSize").val(),
    dbName: '',
    tableName: '',
    format: '',
    desc: '',
    mutiString: '',
    itemTemplate: $("#itemTemplate").html()
};

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
    this.clearNullDom();
    var mutiString = "";
    $("[name='dictItem']").each(function(){
        var code = $(this).find("[name='itemName']").val();
        var name = $(this).find("[name='itemType']").val();
        var num = $(this).find("[name='itemDesc']").val();
        mutiString = mutiString + (code + ":" + name + ":"+ num+";");
    });
    this.dbName = $("#dbName").val();
    this.tableName = $("#tableName").val();
    this.desc = $("#desc").val();
    this.format = $("#format").val();
    this.mutiString = mutiString;

}

/**
 * 清除为空的item Dom
 */
JobTableInfoInfoDlg.clearNullDom = function(){
    $("[name='dictItem']").each(function(){
        var num = $(this).find("[name='itemName']").val();
        var name = $(this).find("[name='itemType']").val();
        if(num == '' || name == ''){
            $(this).remove();
        }
    });
};

/**
 * 提交添加
 */
JobTableInfoInfoDlg.addSubmit = function() {

    this.clearNullDom();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobTableInfo/add", function(data){
        Feng.success("添加成功!");
        window.parent.JobTableInfo.table.refresh();
        JobTableInfoInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set('dbName',this.dbName);
    ajax.set('tableName',this.tableName);
    ajax.set('format',this.format);
    ajax.set('desc',this.desc)
    ajax.set('dictValues',this.mutiString);
    ajax.start();
}

/**
 * 提交修改
 */
JobTableInfoInfoDlg.editSubmit = function() {

    this.clearNullDom();
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

/**
 * 添加条目
 */
JobTableInfoInfoDlg.addItem = function () {
    $("#itemsArea").append(this.itemTemplate);
    $("#dictItem").attr("id", this.newId());
};

/**
 * 删除item
 */
JobTableInfoInfoDlg.deleteItem = function (event) {
    var obj = Feng.eventParseObject(event);
    obj = obj.is('button') ? obj : obj.parent();
    obj.parent().parent().remove();
};

/**
 * item获取新的id
 */
JobTableInfoInfoDlg.newId = function () {
    if(this.count == undefined){
        this.count = 0;
    }
    this.count = this.count + 1;
    return "dictItem" + this.count;
};

$(function() {

});

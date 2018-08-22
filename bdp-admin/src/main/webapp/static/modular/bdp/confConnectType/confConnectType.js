/**
 * 配置连接类型管理初始化
 */
var ConfConnectType = {
    id: "ConfConnectTypeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ConfConnectType.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '连接类型名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '连接类型描述', field: 'desc', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '创建用户', field: 'createPer', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'modTime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改用户', field: 'modPer', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ConfConnectType.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ConfConnectType.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加配置连接类型
 */
ConfConnectType.openAddConfConnectType = function () {
    var index = layer.open({
        type: 2,
        title: '添加配置连接类型',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/confConnectType/confConnectType_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看配置连接类型详情
 */
ConfConnectType.openConfConnectTypeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '配置连接类型详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/confConnectType/confConnectType_update/' + ConfConnectType.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除配置连接类型
 */
ConfConnectType.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/confConnectType/delete", function (data) {
            Feng.success("删除成功!");
            ConfConnectType.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("confConnectTypeId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询配置连接类型列表
 */
ConfConnectType.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ConfConnectType.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ConfConnectType.initColumn();
    var table = new BSTable(ConfConnectType.id, "/confConnectType/list", defaultColunms);
    table.setPaginationType("client");
    ConfConnectType.table = table.init();
});

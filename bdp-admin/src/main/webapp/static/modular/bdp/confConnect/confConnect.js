/**
 * 配置连接管理初始化
 */
var ConfConnect = {
    id: "ConfConnectTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ConfConnect.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '连接名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '连接类型', field: 'typeIdName', visible: true, align: 'center', valign: 'middle'},
            {title: '主机名', field: 'host', visible: true, align: 'center', valign: 'middle'},
            {title: '端口', field: 'port', visible: true, align: 'center', valign: 'middle'},
            {title: '用户名称', field: 'username', visible: true, align: 'center', valign: 'middle'},
            {title: '数据库名称', field: 'dbname', visible: true, align: 'center', valign: 'middle'},
            {title: '描述', field: 'desc', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '创建用户', field: 'createPerName', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'modTime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改用户', field: 'modPerName', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ConfConnect.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ConfConnect.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加配置连接
 */
ConfConnect.openAddConfConnect = function () {
    var index = layer.open({
        type: 2,
        title: '添加配置连接',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/confConnect/confConnect_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看配置连接详情
 */
ConfConnect.openConfConnectDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '配置连接详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/confConnect/confConnect_update/' + ConfConnect.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除配置连接
 */
ConfConnect.delete = function () {
    if (this.check()) {
        var queren=function(){
        	var ajax = new $ax(Feng.ctxPath + "/confConnect/delete", function (data) {
                Feng.success("删除成功!");
                ConfConnect.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("confConnectId",ConfConnect.seItem.id);
            ajax.start();
        }
        Feng.confirm("确认删除"+ConfConnect.seItem.createPerName+"吗?",queren);
    }
};

/**
 * 查询配置连接列表
 */
ConfConnect.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ConfConnect.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ConfConnect.initColumn();
    var table = new BSTable(ConfConnect.id, "/confConnect/list", defaultColunms);
    table.setPaginationType("client");
    ConfConnect.table = table.init();
});

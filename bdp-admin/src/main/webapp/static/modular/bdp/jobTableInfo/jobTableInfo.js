/**
 * 任务数据表管理初始化
 */
var JobTableInfo = {
    id: "JobTableInfoTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
JobTableInfo.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '数据库名称', field: 'dbName', visible: true, align: 'center', valign: 'middle'},
            {title: '表名称', field: 'tableName', visible: true, align: 'center', valign: 'middle'},
            {title: '详情信息', field: 'desc', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
JobTableInfo.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        JobTableInfo.seItem = selected[0];
        return true;
    }
};

/**
 * 打开查看任务数据表详情
 */
JobTableInfo.openJobTableInfoDetail = function () {
    var index = layer.open({
        type: 2,
        title: '任务数据表详情',
        area: ['1000px', '600px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/jobTableInfo/jobTableInfo_update/'
    });
    this.layerIndex = index;
};



JobTableInfo.detail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '运行任务参数信息',
            area: ['800px', '600px'], //宽高
            fix: false, //不固定
            maxmin: false,
            content: Feng.ctxPath + '/jobTableInfo/detail/' + JobTableInfo.seItem.dbName+"/"+JobTableInfo.seItem.tableName
        });
        this.layerIndex = index;
    }
};


/**
 * 查询任务数据表列表
 */
JobTableInfo.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    JobTableInfo.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = JobTableInfo.initColumn();
    var table = new BSTable(JobTableInfo.id, "/jobTableInfo/list", defaultColunms);
    table.setPaginationType("client");
    JobTableInfo.table = table.init();
});

/**
 * 管理初始化
 */
var JobRunHistory = {
    id: "JobRunHistoryTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
JobRunHistory.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '运行编号', field: 'num', visible: true, align: 'center', valign: 'middle'},
            {title: '运行参数', field: 'params', visible: true, align: 'center', valign: 'middle'},
            {title: '运行状态', field: 'state', visible: true, align: 'center', valign: 'middle'},
            {title: '运行耗时 单位ms', field: 'cost', visible: true, align: 'center', valign: 'middle'},
            {title: '开始运行的时间', field: 'time', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
JobRunHistory.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        JobRunHistory.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
JobRunHistory.openAddJobRunHistory = function () {
    var index = layer.open({
        type: 2,
        title: '添加',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/jobRunHistory/jobRunHistory_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看详情
 */
JobRunHistory.openJobRunHistoryDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/jobRunHistory/jobRunHistory_update/' + JobRunHistory.seItem.id
        });
        this.layerIndex = index;
    }
};
/**
 * 查看记录详情
 */
JobRunHistory.detail = function () {
	if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + '/jobRunHistory/detail/' + JobRunHistory.seItem.id, function (data) {
            Feng.infoDetail("日志详情", data.log);
        }, function (data) {
            Feng.error("获取详情失败!");
        });
        ajax.start();
    }
};

/**
 * 删除
 */
JobRunHistory.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/jobRunHistory/delete", function (data) {
            Feng.success("删除成功!");
            JobRunHistory.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("jobRunHistoryId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询列表
 */
JobRunHistory.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    JobRunHistory.table.refresh({query: queryData});
};

$(function () {
	var table = null;
    var defaultColunms = JobRunHistory.initColumn();
    console.log(typeof(window.parent.JobInfo));
    if(typeof(window.parent.JobInfo) != "undefined"){
    	table = new BSTable(JobRunHistory.id, "/jobRunHistory/list/" + window.parent.JobInfo.seItem.id, defaultColunms);
    }else{
    	table = new BSTable(JobRunHistory.id, "/jobRunHistory/list", defaultColunms);
    }
    table.setPaginationType("client");
    
    JobRunHistory.table = table.init();
});

/**
 * 任务集管理初始化
 */
var JobSet = {
    id: "JobSetTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
JobSet.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '任务集名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '任务集描述', field: 'desc', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '创建用户', field: 'createPer', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'modTime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改用户', field: 'modPer', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
JobSet.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        JobSet.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加任务集
 */
JobSet.openAddJobSet = function () {
    var index = layer.open({
        type: 2,
        title: '添加任务集',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/jobSet/jobSet_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看任务集详情
 */
JobSet.openJobSetDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '任务集详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/jobSet/jobSet_update/' + JobSet.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除任务集
 */
JobSet.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/jobSet/delete", function (data) {
            Feng.success("删除成功!");
            JobSet.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("jobSetId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询任务集列表
 */
JobSet.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    JobSet.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = JobSet.initColumn();
    var table = new BSTable(JobSet.id, "/jobSet/list", defaultColunms);
    table.setPaginationType("client");
    JobSet.table = table.init();
});

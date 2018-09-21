/**
 * 任务信息管理初始化
 */
var JobInfo = {
    id: "JobInfoTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
JobInfo.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '任务编号', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '任务名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '任务类型', field: 'typeName', visible: true, align: 'center', valign: 'middle'},
            {title: '任务状态', field: 'enableName', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
            if (value == "启用") {
                return ' <label  style="color: green">启用</label>';
            } else {
                return ' <label  style="color: red">禁用</label>';
            }}},
            {title: '创建时间', field: 'createTime', visible: false, align: 'center', valign: 'middle'},
            {title: '创建用户', field: 'createPerName', visible: false, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'modTime', visible: false, align: 'center', valign: 'middle'},
            {title: '修改用户', field: 'modPerName', visible: false, align: 'center', valign: 'middle'},
            {title: '上次运行状态', field: 'lastRunStateName', visible: true, align: 'center', valign: 'middle', formatter: function (value, row, index) {
                if(value!=null&&value!=""){
                    if (value == "成功") {
                        return ' <label  style="color: green">成功</label>';
                    } else {
                        if (value == "失败") {
                            return ' <label  style="color: red">失败</label>';
                        }else{
                            return ' <label  style="color: yellowgreen">运行中</label>';
                        }
                    }}else{
                    return ""
                }
                }},
            {title: '上次运行时间', field: 'lastRunTime', visible: true, align: 'center', valign: 'middle'},
            {title: '上次运行耗时(毫秒)', field: 'lastRunCost', visible: true, align: 'center', valign: 'middle'},
            {title: '所属人', field: 'userInfoName', visible: true, align: 'center', valign: 'middle'},
            {title: '所属任务集', field: 'jobSetName', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
JobInfo.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        JobInfo.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加任务信息
 */
JobInfo.openAddJobInfo = function () {
    var index = layer.open({
        type: 2,
        title: '添加任务信息',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/jobInfo/jobInfo_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看任务信息运行历史
 */
JobInfo.jobRunHistory = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '运行历史',
            area: ['90%', '90%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/jobRunHistory/jobRunHistoryList'
        });
        this.layerIndex = index;
        //最大化
        // layer.full(index)
    }
};

/**
 * 打开查看任务信息配置页面
 */
JobInfo.openJobInfoConfig = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '任务信息详情',
            area: ['90%', '90%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/jobInfo/jobInfo_config/' + JobInfo.seItem.id
        });
        this.layerIndex = index;
        //最大化
    }
};




/**
 * 删除任务信息
 */
JobInfo.delete = function () {
    if (this.check()) {
    	var queren = function(){
    		 var ajax = new $ax(Feng.ctxPath + "/jobInfo/delete", function (data) {
    	            Feng.success("删除成功!");
    	            JobInfo.table.refresh();
    	        }, function (data) {
    	            Feng.error("删除失败!" + data.responseJSON.message + "!");
    	        });
    	        ajax.set("jobInfoId",JobInfo.seItem.id);
    	        ajax.start();
    	        
    	}
        Feng.confirm("是否删除任务" + JobInfo.seItem.name + "?",queren);
    }
};

JobInfo.stop = function () {
    if (this.check()) {
        var queren = function(){
            var ajax = new $ax(Feng.ctxPath + "/jobInfo/stop", function (data) {
                Feng.success("操作成功!");
                JobInfo.table.refresh();
            }, function (data) {
                Feng.error("操作失败!" + data.responseJSON.message + "!");
            });
            ajax.set("jobInfoId",JobInfo.seItem.id);
            ajax.start();

        }
        Feng.confirm("是否删除任务" + JobInfo.seItem.name + "?",queren);
    }
};



/**
 * 启用任务信息
 */
JobInfo.enable = function () {
    if (this.check()) {
    	var queren = function(){
    		 var ajax = new $ax(Feng.ctxPath + "/jobInfo/enableJobInfo", function (data) {
    	            Feng.success("启用成功!");
    	            JobInfo.table.refresh();
    	        }, function (data) {
    	            Feng.error("启用失败!" + data.responseJSON.message + "!");
    	        });
    	        ajax.set("jobInfoId",JobInfo.seItem.id);
    	        ajax.start();
    	        
    	}
        Feng.confirm("是否启用任务" + JobInfo.seItem.name + "?",queren);
    }
};

/**
 * 禁用任务信息
 */
JobInfo.disable = function () {
    if (this.check()) {
    	var queren = function(){
    		 var ajax = new $ax(Feng.ctxPath + "/jobInfo/disableJobInfo", function (data) {
    	            Feng.success("禁用成功!");
    	            JobInfo.table.refresh();
    	        }, function (data) {
    	            Feng.error("禁用失败!" + data.responseJSON.message + "!");
    	        });
    	        ajax.set("jobInfoId",JobInfo.seItem.id);
    	        ajax.start();
    	        
    	}
        Feng.confirm("是否禁用任务" + JobInfo.seItem.name + "?",queren);
    }
};

/**
 * 运行任务信息
 */
JobInfo.runJobInfo = function () {
	if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '运行任务参数信息',
            area: ['600px', '500px'], //宽高
            fix: false, //不固定
            maxmin: false,
            content: Feng.ctxPath + '/jobInfo/runJobInfo/' + JobInfo.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 查询任务信息列表
 */
JobInfo.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['userId'] = $("#userId").val();

    JobInfo.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = JobInfo.initColumn();
    var table = new BSTable(JobInfo.id, "/jobInfo/list", defaultColunms);
    table.setPaginationType("client");
    JobInfo.table = table.init();


    $("#userId").change(function () {
        JobInfo.search()
    })

});

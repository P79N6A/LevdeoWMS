//列名 列ID 是否增加排序 列宽 是否可检索
var columns = [
    { title: "计划单号", data: "planId", name: "i.planId" },
    { title: "盘点日", data: "planDate", name: "i.planDate" },
    { title: "计划数量", data: "plannumTotal", name: "chkCnt.plannumTotal", className: "alignRight" },
    { title: "实盘数量", data: "carResultTotal", name: "chkCnt.carResultTotal", className: "alignRight" },
    { title: "状态", data: "status", name: "i.status" },
    { title: "statusId", data: "statusId", visible: false },
];

$(function () {

    //初始化表格( 详细配置 )
    tableBox = initDataTables(
        "tableBox",
        columns,
        createdRow,
        constant.projectURL + '/inventory-head/getPlanList',
        getSearchConditon,
        1,
        0,
        false,
        true,
        true,
        [1, "desc"]);
    $("#btnSearch").on("click", function () {
        tableBox.ajax.reload();
    });

    //新建计划
    $("#btnNew").on("click", function () {
        window.location.href = '../createCheck/index.html';
    });

});

/**
 * 取得查询条件
 */
function getSearchConditon() {
    return {
        'planDate': $('#iptCheckDate').val(),
        'status': $('#selCheckStatus').val()
    }
}

/**
 * 控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
 * @param {*} row 
 * @param {*} data 
 */
function createdRow(row, data) {
    //双击行，跳转到详情页面
    $(row).dblclick(function () {
        window.location.href = '../checkDetail/index.html?planId=' + data.planId
            + '&planDate=' + data.planDate
            + '&status=' + data.statusId;
    });
}

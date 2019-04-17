/**
 * 充电页面逻辑控制js
 */

//
var STOCKCONTROLLER_NAME = "/stock";

//列名 列ID 是否增加排序 列宽 是否可检索
columns = [
    { title: "VIN码", data: "vin", orderable: true, searchable: true, name: "s.vin" },
    { title: "物料编码", data: "matCode", orderable: true, searchable: true, name: "s.matCode" },
    { title: "物料描述", data: "matName", orderable: true, searchable: true, name: "m.name" },
    { title: "库位号", data: "stockPosition", orderable: true, searchable: true, name: "s.stockPosition" },
    { title: "电池类型", data: "batteryType", orderable: true, searchable: true, name: "s.batteryType" },
    { title: "上次充电日期", data: "lastChargeDate", orderable: true, searchable: true, name: "s.lastChargeDate" },
    { title: "天数", data: "days", orderable: true, searchable: true, name: "s.lastChargeDate", className: "alignRight" }
];

/**
 * 初始化
 */
$(document).ready(function () {

    //初始化表格( 详细配置 )
    tableBox = initDataTables(
        "tableBox",
        columns,
        createdRow,
        constant.projectURL + STOCKCONTROLLER_NAME + '/searchChargeCar',
        getSearchConditon,
        1, 0,
        false,
        true,
        true,
        [7, "asc"]);

    $("#btnSearch").on("click", function () {
        tableBox.ajax.reload();
    });

    $("#btnExport").on("click", function () {
        exportExcel();
    });
})

/**
 * 取得查询条件
 */
function getSearchConditon() {
    return {
        "stockArea": $("#selStockArea").data("vue") ? $("#selStockArea").data("vue").selected : "",
        "batteryType": $("#selBatteryType").data("vue") ? $("#selBatteryType").data("vue").selected : ""
    }
}

/**
 * 控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
 * @param {*} row 
 * @param {*} data 
 * @param {*} dataIndex 
 */
function createdRow(row, data, dataIndex) {
    //第一列右对齐
    // $(row).children('td').eq(0).css('text-align', 'right');
    //编辑按钮绑定事件
    $(row).find('i.edit-btn').click(function () {
        alert('编辑 ' + JSON.stringify(data));
    });
    //删除按钮绑定事件
    $(row).find('i.del-btn').click(function () {
        alert('删除 ' + JSON.stringify(data));
    });
}

/**
 * 导出Excel
 */
function exportExcel() {
    //form添加action
    $("#myHeader form").attr("action", constant.projectURL + STOCKCONTROLLER_NAME + '/exportExcel');
    $("#myHeader form")._submit();
}
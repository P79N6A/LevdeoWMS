//列名 列ID 是否增加排序 列宽 是否可检索
columns = [
    { title: "发货单号", data: "invoiceCode", orderable: true, searchable: true, name: "a.invoiceCode" },
    { title: "VIN码", data: "vin", orderable: true, searchable: true, name: "a.vin" },
    { title: "物料编码", data: "matCode", orderable: true, searchable: true, name: "b.matCode" },
    { title: "物料描述", data: "name", orderable: true, searchable: true, name: "c.name" },
    { title: "检测日", data: "createtime", orderable: true, searchable: true, name: "a.createtime" },
    { title: "检测人", data: "inspector", orderable: true, searchable: true, name: "a.inspector" },
    { title: "不合格理由", data: "reason", orderable: true, searchable: true, name: "a.reason" }
];

//控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
function createdRow(row, data, dataIndex) {
    //第一列右对齐
    $(row).children('td').eq(0).css('text-align', 'right');
}

//取得查询条件
var getSearchConditon = function () {
    return {
        "invoiceCode": $("#inInvoiceCode").val().trim(),
        "vin": $("#inVin").val().trim()
    }
}
$(document).ready(function () {
    //操作日实例化 年-月-日 (中文)
    //初始化表格( 详细配置 )
    tableBox = initDataTables(
        "tableBox",
        columns,
        createdRow,
        constant.projectURL + '/out-pdihistory/PDITable',
        getSearchConditon,
        1, 0,
        false,
        true,
        true,
        [1, "desc"]
    );

    //查询
    $("#btnSearch").on("click", function () {
        tableBox.ajax.reload();
    });
    //导出
    $("#btnExport").on("click", function () {
        downloadExcel();
    });

})
//导出
var downloadExcel = function () {
    //form添加action
    $("#myHeader form").attr("action", constant.projectURL + "/out-pdihistory/downloadExcel");
    $("#myHeader form")._submit();
}

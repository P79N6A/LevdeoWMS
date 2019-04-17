var params = {
    columns: [
        {title: "VIN码", data: "vin", orderable: true, searchable: true, name: "stockLog.vin" },
        {title: "物料编码", data: "matCode", orderable: true, searchable: true, name: "stockLog.matCode" },
        {title: "物料描述", data: "name", orderable: true, searchable: true, name: "mainMat.name" },
        {title: "操作", data: "operateName", orderable: true, searchable: true, name: "sysParm.subName" },
        {title: "SAP单号", data: "sapOrderNum", orderable: true, searchable: true, name: "stockLog.sapOrderNum" },
        {title: "原库位号", data: "oldStorageNo", orderable: true, searchable: true, name: "stockLog.oldStorageNo" },
        {title: "新库位号", data: "newStorageNo", orderable: true, searchable: true, name: "stockLog.newStorageNo" },
        {title: "操作人", data: "operator", orderable: true, searchable: true, name: "sysUser.userName" },
        {title: "操作日", data: "updateTime", orderable: true, searchable: true, name: "stockLog.updateTime" },
        {title: "理由", data: "reason", orderable: true, searchable: true, name: "stockLog.reason" }
    ],
    createdRow: function (row, data, dataIndex) {
        //第一列右对齐
        $(row).children('td').eq(0).css('text-align', 'right');
    }
}

$(document).ready(function () {

    //初始化表格(详细配置)
    tableBox = initDataTables(
        "mainTable",
        params.columns,
        params.createdRow,
        constant.projectURL + "/stocklog/searchLogData",
        getSearchConditon,
        0,
        0,
        false,
        true,
        true,
        [9, "desc"]
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

//取得查询条件
var getSearchConditon = function() {
    return {
        "vin": $("#selVin").val().trim(),
        "matName": $("#selMatName").val().trim(),
        "operator": $("#selOperator").val().trim(),
        "operateDate": $("#selOperateDate").val().trim(),
        "operateCode":$("#selOperaType").val()
    }
}

//导出
var downloadExcel = function () {
    //form添加action
    $("#myHeader form").attr("action", constant.projectURL + "/stocklog/downloadExcel");
    $("#myHeader form")._submit();
}
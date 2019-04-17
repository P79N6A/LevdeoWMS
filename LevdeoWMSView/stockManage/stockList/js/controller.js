/**
 * 
 */

//列名 列ID 是否增加排序 列宽 是否可检索
columns = [
    { title: "VIN码", data: "vin", orderable: true, searchable: true, name: "a.vin" },
    { title: "物料编码", data: "matCode", orderable: true, searchable: true, name: "a.matCode" },
    { title: "物料描述", data: "name", orderable: true, searchable: true, name: "a.matCode" },
    { title: "库位号", data: "stockName", orderable: true, searchable: true, name: "stockName" },
    { title: "入库日", data: "warehouseDate", orderable: true, searchable: true, name: "warehouseDate" },
    { title: "状态", data: "subName", orderable: true, searchable: true, name: "c.subName" },
    // { title: "锁定", data: "lockName", orderable: true, searchable: true, name: "lockName" },
    { title: "成品库", data: "productName", orderable: true, searchable: true, name: "productName" },
    {
        title: "标签",
        data: null,
        sWidth: 60,
        orderable: false,
        searchable: false,
        render: function (data, type, row, meta) {
            var vin = row.vin;
            var stockName = row.stockName;
            if (vin == null || vin == undefined) {
                vin = "";
            } else {
                vin = vin.trim();
            }
            if (stockName == null || stockName == undefined) {
                stockName = "";
            } else {
                stockName = stockName.trim();
            }
            return '<div style="width:60px;cursor:pointer">' +
                '<a onclick="argoxPrint.sendMessage(\'' + stockName + '\', \'' + vin + '\')">打印</a>' +
                '</div>';
        }
    }
];
function createdRow(row, data, dataIndex) {
    //第一列右对齐
    $(row).children('td').eq(0).css('text-align', 'right');
}

$(document).ready(function () {
    $("#form-id").validationEngine("attach", {
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        isOverflown: true,
    });

    //初始化表格( 详细配置 )
    tableBox = initDataTables(
        "tableBox",
        columns,
        createdRow,
        constant.projectURL + '/stock/serchAll',
        getSearchConditon,
        1, 0,
        false,
        true,
        true,
        [1, "asc"]);
    //初始化状态
    initCarStatus();
    //更新弹出按钮
    $("#btnLock").click(function () {
        openStockPop()
        carStatus();

    });
    //查询
    $("#btnSearch").on("click", function () {
        tableBox.ajax.reload();
    });

    //批量更新
    $("#submitBatch").on("click", function () {
        var formFlg = $("#form-id").validationEngine("validate");
        if (!formFlg) {
            return;
        }
        batchUpdate();
    });
    //关闭数据清空
    $(".closedel").on("click", function () {
        $("#area").val("");
        $("#reason").val("");

    });

    //导出
    $("#btnExport").on("click", function () {
        downloadExcel();
    });

    //连接后台打印服务
    argoxPrint.connection();

    //页面离开时，与打印机断开连接
    window.onbeforeunload = function (e) {
        if (argoxPrint.ws.readyState == 1 && argoxPrint.connPrinter) {
            argoxPrint.ws.send("B_ClosePrn");
        }
    }
})
function getSearchConditon() {
    return {
        "vin": $("#inVin").val().trim(),
        "stockArea": $("#selStockArea").data("vue") ? $("#selStockArea").data("vue").selected : "",
        "warehouseDate": $("#date").val(),
        "carStatus": $("#selcarStatus").val(),
        "productStockCode":$("#selProductStock").val()
    }
}
//导出
var downloadExcel = function () {
    //form添加action
    $("#myHeader form").attr("action", constant.projectURL + "/stock/downloadExcel");
    $("#myHeader form")._submit();
}
//消息弹出框
function openStockPop() {
    //绘制消息提示框
    $("#modelLock").modal('show');
}
//消息框隐藏
function popHide() {
    $("#modelLock").modal('hide');
    tableBox.ajax.reload();
    $("#area").val("");
    $("#reason").val("");
}
//初始化状态
var initCarStatus = function () {
    var vm = new Vue({
        el: "#selcarStatus",
        data: {
            initoptions: []
        }
    })

    var optSettings = {
        type: 'POST',
        url: constant.projectURL + '/stock/searchInCarStatus',
        data: {},
        dataType: 'json',
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.error(ERROR_SERVER_EXCEPTION);
                return;
            }
            vm.initoptions = resp.data;
        },
        error: function (resp, textStatus) {
            toastr.error(ERROR_SERVER_EXCEPTION);
        }
    }
    $._ajax(optSettings);

}

//批量更新状态
var carStatus = function () {
    var vm = new Vue({
        el: "#selLock",
        data: {
            options: []
        }
    })

    var optSettings = {
        type: 'POST',
        url: constant.projectURL + '/stock/searchCarStatus',
        data: {},
        dataType: 'json',
        success: function (resp, textStatus) {
            vm.options = resp.data;
        }
    }
    $._ajax(optSettings);

}
//获取textarea值批量更新
var batchUpdate = function () {
    var arr = $("#area").val().split("\n")
    var vins = [];
    var data = [];

    for (var i = 0; i < arr.length; i++) {
        if (arr[i] != "" & vins.indexOf(arr[i]) == -1) {
            vins.push(arr[i]);
        }
    }
    for (var j = 0; j < vins.length; j++) {
        if (vins[j].trim().length > 30) {
            toastr.warning(MESSAGE.WARN_VIN_INVALID);
            return;
        }
        data.push({
            "vin": vins[j].trim(),
            "reason": $("#reason").val(),
            "subCode": $("#selLock").val()
        });
    }
        var optSettings = {
            type: 'POST',
            url: constant.projectURL + '/stock/batchUpdate',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: "application/json",
            success: function (resp, textStatus) {
                if (resp.data == "1") {
                    toastr.warning(resp.msg);

                    return;
                } else if (resp.data == "2") {

                    popHide();
                    toastr.warning(resp.msg);

                    return;
                }
                toastr.success(MESSAGE.INFO_UPDATE_SUCCESS);
                popHide();
            },
            error: function (resp, textStatus) {
                toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION);
            }
        }
        $._ajax(optSettings);
}
//查询数据
var columns = [
    { title: "VIN码", data: "vin", name: "s.vin" },
    { title: "物料编码", data: "matCode", name: "s.matCode" },
    { title: "物料描述", data: "matName", name: "m.name" },
    { title: "原库位", data: "oldPosition", name: "sp.name" },
    { title: "状态", data: "status", name: "s.status" }
];
//错误信息
var columns2 = [
    { title: "错误行号", data: "rowNum", sWidth: 70, orderable: false },
    { title: "错误信息", data: "name", orderable: false },
];

$(function () {

    //初始化表格( 详细配置 )
    tableBox = initDataTables(
        "tableBox",
        columns,
        createdRow,
        constant.projectURL + '/stock/searchMove',
        getSearchConditon,
        1,
        0,
        false,
        true,
        true,
        [1, "asc"]
    );
    tableBox2 = null;

    //搜索
    $("#btnSearch").on("click", function () {
        $("#tableDiv").show();
        $("#tableDiv2").hide();
        tableBox.ajax.reload();
    });

    //导出
    $("#btnExport").click(function () {
        exportYK();
    });

    //导入
    $("#btnImport").click(function () {
        //打开选择文件对话框
        $("#fileToUpload").click();
    });
    $("#fileToUpload").change(function () {
        //选择文件后询问是否上传？
        if (this.value) {
            openConfirmDialog(MESSAGE.CONFIRM_PAD_IMPORT, ImportYK, function () { $("#uploadForm")[0].reset(); });
        }
    });

    //连接后台打印服务
    argoxPrint.connection();

    //页面离开时，与打印机断开连接
    window.onbeforeunload = function (e) {
        if (argoxPrint.ws.readyState == 1 && argoxPrint.connPrinter) {
            argoxPrint.ws.send("B_ClosePrn");
        }
    }
});

/**
 * 取得查询条件
 */
function getSearchConditon() {
    return {
        'stockArea': $('#selStockArea').val(),
        'vin': $('#taVin').val().trim().replace(/\n/g, "','")
    }
}

/**
 * 控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
 * @param {*} row 
 */
function createdRow(row) {
}

/**
 * 导出Excel
 */
function exportYK() {
    $("input[name='stockArea']").val($('#selStockArea').val());
    $("input[name='vin']").val($('#taVin').val().trim().replace(/\n/g, "','"));

    $("#search_form").attr("action", constant.projectURL + "/stock/exportYK");
    $("#search_form")._submit();
}

/**
 * 导入移库excel
 */
function ImportYK() {
    //校验文件格式、大小
    if (!validateFile("#fileToUpload", "xls,xlsx")) {
        $("#uploadForm")[0].reset();
        return;
    }
    $.ajaxFileUpload({
        url: constant.projectURL + '/stock/importYK',
        secureuri: false,
        fileElementId: 'fileToUpload',
        dataType: 'text',
        data: {},
        success: function (res) {
            $("#uploadForm")[0].reset();
            if (res == "0") {
                toastr.warning(MESSAGE.ERROR_NO_DATA);
            } else {
                showTableBox2(res);
            }
        },
        error: function () {
            toastr.error(MESSAGE.ERROR_IMPORT);
        }
    });
}

/**
 * 显示错位信息dataTables
 */
function showTableBox2(res) {
    if (!tableBox2) {
        tableBox2 = initDataTables2(
            "tableBox2",
            columns2,
            createdRow,
            constant.projectURL + '/stock/bachUpdatePosition',
            res
        );
    } else {
        //没有数据时是导入成功的，反之是错误的
        tableBox2.ajax.reload(
            function () {
                if (tableBox2.data().length == 0) {
                    toastr.success(MESSAGE.INFO_IMPORT_SUCCESS);
                    $("#tableDiv").show();
                    $("#tableDiv2").hide();

                    if (argoxPrint.ws.readyState != 1 || !argoxPrint.connPrinter) {
                        if (argoxPrint.ws.readyState != 1) {
                            toastr.error(MESSAGE.ERROR_CONNECT_PRINT_SERVER_FAIL);
                        }
                        if (!argoxPrint.connPrinter) {
                            toastr.error(MESSAGE.ERROR_CONNECT_PRINT_FAIL);
                        }
                        //播放失败音乐
                        //$("#infomp3").attr("src", constant.projectURL+"/music/fail.mp3");
                    } else {
                        //打印
                        res.split(";").forEach(function (item, index) {
                            argoxPrint.sendMessage(item.split(",")[0], item.split(",")[1]);
                        });
                    }
                } else {
                    toastr.warning(MESSAGE.ERROR_UPLOAD_FILE);
                    $("#tableDiv").hide();
                    $("#tableDiv2").show();
                }
            });
    }
}

/**
 * 重写公用DataTables方法
 * @param {*} tableid 
 * @param {*} columns 
 * @param {*} createdRow 
 * @param {*} url 
 */
function initDataTables2(
    tableid
    , columns
    , createdRow
    , url
    , res) {

    //每页显示条数选项
    //导出excel配置 ( 如不需要操作按钮 可配置 buttons:[] )
    var buttons = [];
    var tablebodyHeight = 0;
    tablebodyHeight = document.body.clientHeight - $(".page-header").height() - $("#myHeader").height() - 130;
    //初始化表格( 详细配置 )
    var dts = $("#" + tableid).DataTable({
        //数据源
        ajax: function (data, callback) {
            //ajax请求数据
            $._ajax({
                type: "post",
                url: url,
                data: {},
                //禁用缓存
                cache: false,
                dataType: "json",
                success: function (result) {
                    //封装返回数据
                    var returnData = {};
                    returnData.data = result.data;
                    //没有数据时是导入成功的，反之是错误的
                    if (returnData.data && returnData.data.length === 0) {
                        toastr.success(MESSAGE.INFO_IMPORT_SUCCESS);
                        $("#tableDiv").show();
                        $("#tableDiv2").hide();
                        tableBox.ajax.reload();

                        if (argoxPrint.ws.readyState != 1 || !argoxPrint.connPrinter) {
                            if (argoxPrint.ws.readyState != 1) {
                                toastr.error(MESSAGE.ERROR_CONNECT_PRINT_SERVER_FAIL);
                            }
                            if (!argoxPrint.connPrinter) {
                                toastr.error(MESSAGE.ERROR_CONNECT_PRINT_FAIL);
                            }
                            //播放失败音乐
                            //$("#infomp3").attr("src", constant.projectURL+"/music/fail.mp3");
                        } else {
                            //打印
                            res.split(";").forEach(function (item, index) {
                                argoxPrint.sendMessage(item.split(",")[0], item.split(",")[1]);
                            });
                        }
                    } else {
                        toastr.warning(MESSAGE.ERROR_UPLOAD_FILE);
                        $("#tableDiv").hide();
                        $("#tableDiv2").show();
                    }
                    callback(returnData);
                },
                error: function (a, b, c) {
                    toastr.error(MESSAGE.ERROR_IMPORT)
                }
            });
        },
        //允许横向滚动
        scrollX: true,
        //表格滚动高度(不包含表头)
        scrollY: tablebodyHeight,
        //按钮必要dom
        dom: "Bfrtip",
        //每页显示条数选项
        //导出excel配置 ( 如不需要操作按钮 可配置 buttons:[] )
        buttons: buttons,
        //每页显示条数配置  
        aLengthMenu: [],
        //启用服务器端分页
        serverSide: true,
        //表格搜索总开关
        searching: false,
        //表头排序总开关
        ordering: false,
        //默认排序列 (可设置多个)
        order: [],
        //分页启用    
        paging: false,
        //左下角总条数信息启用( 不启用分页不建议开启此项 )    
        info: false,
        //分页样式
        pagingType: "first_last_numbers",
        //每页显示条数
        iDisplayLength: 10,
        //语言设置
        language: {
            //日文语言包文件为:Japanese.json
            url: "../../assets/plugins/jquery.datatables/la/Chinese.json",
            buttons: {
                pageLength: {
                    _: "每页 %d 条",
                    "-1": "显示所有"
                }
            }
        },
        errMode: "error1",
        //加载loading
        processing: true,
        //显示的列(title:列名,data:数据key)
        columns: columns,
        //控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
        createdRow: function (row, data, dataIndex) {
            createdRow(row, data, dataIndex);
        },
    });
    return dts;
}
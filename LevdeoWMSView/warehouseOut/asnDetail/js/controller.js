//出库单号
var thisOutBoundCode = getUrlParam("sapInvoiceCode");
//订单类型
var thisOderTypeCode = getUrlParam("orderTypeCode");

//列名 列ID 是否增加排序 列宽 是否可检索
columns = [
    { title: "发货单行项目", data: "itemNo", orderable: true, name: "oh.itemNo" },
    { title: "VIN码", data: "vin", orderable: true, name: "vin" },
    { title: "出库仓库", data: "productStock", orderable: true, name: "productStock" },
    { title: "物料编码", data: "matCode", orderable: true, name: "matCode" },
    { title: "物料描述", data: "matName", orderable: true, name: "matName" },
    { title: "数量", data: "carNumber", orderable: true, className: "alignRight", defaultContent: 1, name: "" },
    { title: "库位号", data: "stockPosition", orderable: true, name: "stockPosition" },
    { title: "PDI检测", data: function (data) { return (data.pdi == "" || data.pdi == null) ? "" : data.pdi == "0" ? "合格" : "不合格" }, orderable: true, name: "pdi" },
    { title: "检测人", data: function (data) { return undnulltostring(data.inspectorName) }, orderable: true, name: "sysUser.userName" },
    { title: "不合格理由", data: function (data) { return undnulltostring(data.reason) }, orderable: true, name: "reason" },
    { title: "司机", data: "driverName", orderable: true, name: "od.driverid" },
    {
        title: "操作",
        //html
        data: function (data) {
            var btn = "";
            // 发货单的状态为出库中才能进行换车
            if (data.status == SysParamConstant.P_ASN_STATUS_WAITSEND) {
                btn = '<div class="table-td-btn" style="width:50px;text-align:center">' +
                    '  <i class="edit-btn fa fa-refresh text-primary" title="换车"></i>' +
                    '  <i class="del-btn fa fa-trash-o text-danger" title="删除"></i>' +
                    '</div>';
            }

            return btn;
        },
        orderable: false,
        sWidth: 50
    }
];

//列名 列ID 是否增加排序 列宽 是否可检索
columnsReturn = [
    { title: "发货单行项目", data: "itemNo", orderable: true, name: "oh.itemNo" },
    { title: "VIN码", data: "vin", orderable: true, name: "vin" },
    { title: "出库仓库", data: "productStock", orderable: true, name: "productStock" },
    { title: "物料编码", data: "matCode", orderable: true, name: "matCode" },
    { title: "物料描述", data: "matName", orderable: true, name: "matName" },
    { title: "数量", data: "carNumber", orderable: true, className: "alignRight", defaultContent: 1, name: "" },

];


var changeCar = function (matCode) {

    $("#iptNewVIN").select2({
        // placeholder: "--请选择--",
        allowClear: true,
        ajax: {
            url: constant.projectURL + CONTROLLER_NAME + '/getVinList',
            dataType: "json",
            delay: 250,
            data: function (searchstr, pageno) {
                return {
                    matCode: matCode,
                    searchstr: searchstr,// 搜索框内输入的内容，传递到后端为searchstr
                    pageno: pageno || 1
                };
            },
            cache: false,
            results: function (res, pageno, s2obj) {
                var options = [];
                for (var i = 0, len = res.data.length; i < len; i++) {
                    var option = { "id": res.data[i]["vin"], "text": res.data[i]["vin"] };
                    options.push(option);
                }
                return {
                    results: options,
                    more: res.length > 0
                };
            },
            escapeMarkup: function (markup) { return markup; },
            minimumInputLength: 1,
        },


    });
}


//控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
function createdRow(row, data, dataIndex) {

    //换车列-编辑按钮绑定事件
    $(row).find('i.edit-btn').click(function () {
        //显示Modal
        $('#myModal').modal('show');
        //清空validate验证信息
        $("#modalForm").validationEngine("hideAll");
        //Modal-label赋值
        $("#modalMatCode").html(data.matCode);
        $("#modalMatName").html(data.matName);
        $("#modalOldVIN").html(data.vin);
        $("#invoiceCode").html(data.invoiceCode);
        //清空input并获取焦点
        $('#iptNewVIN').val("");
        // $("#select2-chosen-1").text("");
        changeCar(data.matCode);
        //移除validate验证
        $("#s2id_iptNewVIN").removeClass("validate[required]");
    });

    //删车
    $(row).find('i.del-btn').click(function () {
        openConfirmDialog(MESSAGE.CONFIRM_DELETE, function () {
            //后台更新发货单
            var textAjax = {
                url: constant.projectURL + '/outInvoiceDetails/deleteFromOidByKey',
                data: { invoiceCode: data.invoiceCode, vin: data.vin }
            }
            $._ajaxd(textAjax).done(function () {
                toastr.success(MESSAGE.INFO_DELETE_SUCCESS);
                tableBox.ajax.reload();
            });
        });
    });
}

/**
 * 查询条件-当前出库单单号
 */
function getSearchConditon() {
    return {
        "thisOutBoundCode": thisOutBoundCode
    }
}

//
var myHeaderText = new Vue({
    el: "#myHeader",
    data: {
        customerName: "",
        outBoundCode: "",
        contactInformation: "",
        sapInvoiceCode: "",
        customerAddress: "",
        logisticsParking: "",
        logisticsVehicleNo: "",
        giftName: "",
        shipTypeName: "",
        logisCompanyName: "",
        addMode: false,
        waitMode: false,
        confirmMode: false,
        sendedMode: false,
        washMode: false,
    },
    methods: {
        //出库
        warehouseOut: function () {

            if (!$("#valiForm").validationEngine("validate")) {
                return;
            }

            var textAjax = {
                type: 'POST',
                url: constant.projectURL + '/outInvoiceDetails/getNoPdiCheckCnt',
                data: getSearchConditon(),
                datatype: 'json',
                success: function (resp) {
                    openConfirmDialog("确定车辆出库吗?", function () {
                        //获取更新数据-出库单号,input,下拉
                        function getSearchConditon1() {
                            return {
                                "sapInvoiceCode": $("#sapInvoiceCode").html(),
                                "logisticsParking": $("#logisticsParking").val().trim(),
                                "logisticsVehicleNo": $("#logisticsVehicleNo").val().trim(),
                                "giftName": $("#giftName").val().trim(),
                                "shipType": $("#shipType").val(),
                                "logisCompany": $("#logisCompany").val(),
                                "orderTypeCode": thisOderTypeCode
                            }
                        }
                        //后台更新发货单
                        var textAjax = {
                            type: 'POST',
                            url: constant.projectURL + '/outInvoiceDetails/outBoundOperation',
                            data: getSearchConditon1(),
                            datatype: 'json',
                            success: function (resp) {
                                toastr.success(MESSAGE.INFO_WAREHOUSEOUT_SUCCESS);
                                myHeaderInit();
                                tableBox.ajax.reload();
                            }
                        }
                        $._ajax(textAjax);
                    });
                }
            }
            $._ajax(textAjax);
        },
        //打印备货单
        printPickingList: function () {
            var cntPerPage = 5;//每页表格的条数
            var optSettings = {
                type: "POST",
                url: constant.projectURL + '/outInvoiceDetails/invoicecodePrint',
                data: getSearchConditon(),
                dataType: "json",
                success: function (resp, textStatus) {
                    if (textStatus != "success" || resp.msg != "SERVER_SUCCESS") {
                        toastr.error(MESSAGE.ERROR_PRINT_SEARCH);//打印数据检索失败。
                        return;
                    }
                    if (resp.data == null || resp.data == undefined || resp.data.dataList.length == 0) {
                        toastr.error(MESSAGE.WARN_NODATA_PRINT);//没有要打印的数据。
                        return;
                    }

                    //表格上方数据
                    var headData = resp.data.headData;
                    //表格数据
                    var dataList = resp.data.dataList;
                    //数据总条数
                    var totalcnt = dataList.length;
                    //总页数
                    var pagecnt = Math.ceil(totalcnt / cntPerPage);

                    var printHtml;
                    if (headData == null) {
                        printHtml =
                            $("#scrPrintPick").html().replace("${companyName}", "")
                                .replace("${salecustomName}", "")
                                .replace("${salesOrder}", "")
                                .replace("${logisticsParking}", "")
                                .replace("${sendcustomAddress}", "")
                                .replace("${sapInvoiceCode}", "")
                                .replace("${logisticsVehicleNo}", "")
                                .replace("${createDate}", "")
                                .replace("${shipType}", "")
                                .replace("${giftName}", "")
                                .replace("${sendcustomName}", "")
                                .replace("${makeOrderUser}", "")
                                .replace("${printUser}", top.constant.loginUserName);
                    } else {
                        printHtml =
                            $("#scrPrintPick").html().replace("${companyName}", undnulltostring(headData.companyName))
                                .replace("${salecustomName}", undnulltostring(headData.salecustomName))
                                .replace("${salesOrder}", undnulltostring(headData.salesOrder))
                                .replace("${logisticsParking}", undnulltostring(headData.logisticsParking))
                                .replace("${sendcustomAddress}", undnulltostring(headData.sendcustomAddress))
                                .replace("${sapInvoiceCode}", undnulltostring(headData.sapInvoiceCode))
                                .replace("${logisticsVehicleNo}", undnulltostring(headData.logisticsVehicleNo))
                                .replace("${createDate}", undnulltostring(headData.createDate))
                                .replace("${shipType}", undnulltostring(headData.transportName))
                                .replace("${giftName}", undnulltostring(headData.giftName))
                                .replace("${sendcustomName}", undnulltostring(headData.sendcustomName))
                                .replace("${makeOrderUser}", undnulltostring(headData.makeOrderUserName))
                                .replace("${printUser}", top.constant.loginUserName);
                    }
                    $("#divPrintPick").html("");
                    $("#divPrintOut").html("");

                    for (var i = 0; i < pagecnt; i++) {
                        $("#divPrintPick").append(printHtml.replace("${pagecnt}", i * cntPerPage).replace("${page}", "第 " + (i + 1) + " 页 / 共 " + pagecnt + " 页"));

                        var pagedatas = [];
                        for (var j = i * cntPerPage; j < (i + 1) * cntPerPage; j++) {
                            if (j < totalcnt) {
                                pagedatas.push(tableBox.data()[j]);
                            }
                        }

                        //表格初始化
                        new Vue({
                            el: $("table[name='stockTablePrint']:eq(" + i + ")")[0],
                            data: {
                                items: pagedatas
                            }
                        });
                    }

                    setTimeout(function () {
                        var agent = navigator.userAgent.toLowerCase();
                        //判断是否IE11
                        var isIE11 = agent.indexOf('trident') > -1 && agent.indexOf("rv:11.0") > -1;
                        //判断是否IE<11
                        var isIE = agent.indexOf("compatible") > -1 && agent.indexOf("msie" > -1);
                        //IE浏览器
                        if (isIE11) {
                            // $("#divPrintPick").jqprint();
                            if (document.body.insertAdjacentHTML == null) return;
                            var sWebBrowserCode = '<object width="0" height="0" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></object>';
                            document.body.insertAdjacentHTML('beforeEnd', sWebBrowserCode);
                            var objWebBrowser = document.body.lastChild;
                            if (objWebBrowser == null) return;
                            top.$("div#sidebar").hide();
                            top.$("div#content").removeClass("content");
                            objWebBrowser.ExecWB(7, 1);
                            top.$("div#sidebar").show();
                            top.$("div#content").addClass("content");
                            document.body.removeChild(objWebBrowser);
                        } else if (isIE) {
                            if (window.ActiveXObject == null || document.body.insertAdjacentHTML == null) return;
                            var sWebBrowserCode = '<object width="0" height="0" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></object>';
                            document.body.insertAdjacentHTML('beforeEnd', sWebBrowserCode);
                            var objWebBrowser = document.body.lastChild;
                            if (objWebBrowser == null) return;
                            top.$("div#sidebar").hide();
                            top.$("div#content").removeClass("content");
                            objWebBrowser.ExecWB(7, 1);
                            top.$("div#sidebar").show();
                            top.$("div#content").addClass("content");
                            document.body.removeChild(objWebBrowser);
                        } else {
                            window.print();
                        }
                    }, 200);
                },
                error: function (resp, textStatus) {
                    toastr.error(MESSAGE.ERROR_PRINT_SEARCH);//打印数据检索失败。
                }
            }
            $._ajax(optSettings);
        },
        //打印出库单
        printOutList: function () {
            var cntPerPage = 5;//每页表格的条数
            var optSettings = {
                type: "POST",
                url: constant.projectURL + '/outInvoiceDetails/outBoundPrint',
                data: getSearchConditon(),
                dataType: "json",
                success: function (resp, textStatus) {
                    if (textStatus != "success" || resp.msg != "SERVER_SUCCESS") {
                        if (resp.data == null) {
                            toastr.error(MESSAGE.ERROR_INTERFACE_STOCKOUT_FAIL + resp.msg);//调用出库单接口失败：
                        } else {
                            toastr.error(MESSAGE.ERROR_PRINT_SEARCH);//打印数据检索失败。
                        }
                        return;
                    }
                    if (resp.data == null || resp.data == undefined || resp.data.dataList.length == 0) {
                        toastr.error(MESSAGE.WARN_NODATA_PRINT);//没有要打印的数据。
                        return;
                    }

                    //表格上方数据
                    var headData = resp.data.headData;
                    //表格数据
                    var dataList = resp.data.dataList;
                    //数据总条数
                    var totalcnt = dataList.length;
                    //总页数
                    var pagecnt = Math.ceil(totalcnt / cntPerPage);
                    //当前时间
                    var myDate = new Date();
                    myDatetime = myDate.toLocaleString();

                    var printHtml =
                        $("#scrPrintOut").html().replace("${companyName}", undnulltostring(headData.companyName))
                            .replace("${orderType}", undnulltostring(headData.orderTypeName))
                            .replace("${customerName}", undnulltostring(headData.customerName))
                            .replace("${outBoundCode}", undnulltostring(headData.outBoundCode))
                            .replace("${shipType}", undnulltostring(headData.shipType))
                            .replace("${contactInformation}", undnulltostring(headData.contactInformation))
                            .replace(/\${sapInvoiceCode}/g, undnulltostring(headData.sapInvoiceCode))
                            .replace("${giftName}", undnulltostring(headData.giftName))
                            .replace("${customerAddress}", undnulltostring(headData.customerAddress))
                            .replace("${makeOrderUser}", undnulltostring(headData.makeOrderUserName))
                            .replace("${outStockUser}", undnulltostring(headData.outStockUserName))
                            .replace("${makeOrderDate}", undnulltostring(headData.makeOrderDate))
                            .replace("${outStockDate}", undnulltostring(headData.outStockDate))
                            .replace("${dateTime}", myDatetime);
                    $("#divPrintPick").html("");
                    $("#divPrintOut").html("");

                    for (var i = 0; i < pagecnt; i++) {

                        var pagedatas = [];
                        for (var j = i * cntPerPage; j < (i + 1) * cntPerPage; j++) {
                            if (j < totalcnt) {
                                pagedatas.push(tableBox.data()[j]);
                            }
                        }

                        $("#divPrintOut").append(printHtml.replace("${pagecnt}", i * cntPerPage)
                            .replace("${page}", (i + 1) + "/" + pagecnt)
                            .replace("${tdSum}", pagedatas.length))
                            .append('<img src="../../assets/img/seal/' + headData.companyCode + '.png" style="position: absolute;right:10px;top:200px;width:200px" onerror="this.style.display=\'none\'">');

                        //表格初始化
                        new Vue({
                            el: $("table[name='outTablePrint']:eq(" + i + ")")[0],
                            data: {
                                items: pagedatas
                            }
                        });

                    }

                    setTimeout(function () {
                        var agent = navigator.userAgent.toLowerCase();
                        //判断是否IE11
                        var isIE11 = agent.indexOf('trident') > -1 && agent.indexOf("rv:11.0") > -1;
                        //判断是否IE<11
                        var isIE = agent.indexOf("compatible") > -1 && agent.indexOf("msie" > -1);
                        //IE浏览器
                        if (isIE11) {
                            // $("#divPrint").jqprint();
                            if (document.body.insertAdjacentHTML == null) return;
                            var sWebBrowserCode = '<object width="0" height="0" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></object>';
                            document.body.insertAdjacentHTML('beforeEnd', sWebBrowserCode);
                            var objWebBrowser = document.body.lastChild;
                            if (objWebBrowser == null) return;
                            top.$("div#sidebar").hide();
                            top.$("div#content").removeClass("content");
                            objWebBrowser.ExecWB(7, 1);
                            top.$("div#sidebar").show();
                            top.$("div#content").addClass("content");
                            document.body.removeChild(objWebBrowser);
                        } else if (isIE) {
                            if (window.ActiveXObject == null || document.body.insertAdjacentHTML == null) return;
                            var sWebBrowserCode = '<object width="0" height="0" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></object>';
                            document.body.insertAdjacentHTML('beforeEnd', sWebBrowserCode);
                            var objWebBrowser = document.body.lastChild;
                            if (objWebBrowser == null) return;
                            top.$("div#sidebar").hide();
                            top.$("div#content").removeClass("content");
                            objWebBrowser.ExecWB(7, 1);
                            top.$("div#sidebar").show();
                            top.$("div#content").addClass("content");
                            document.body.removeChild(objWebBrowser);
                        } else {
                            window.print();
                            myHeaderInit();
                        }
                    }, 200);
                },
                error: function (resp, textStatus) {
                    toastr.error(MESSAGE.ERROR_PRINT_SEARCH);//打印数据检索失败。
                }
            }
            $._ajax(optSettings);
        },
        //冲销
        doWash: function () {
            var textAjax = {
                url: constant.projectURL + '/outInvoiceDetails/doWash',
                data: $.extend({}, getSearchConditon(), { orderTypeCode: thisOderTypeCode })
            }

            openConfirmDialog(MESSAGE.CONFIRM_WASH,
                function () {
                    $._ajaxd(textAjax).done(function (resp) {
                        if (resp.msg != "SERVER_SUCCESS") {
                            toastr.error(resp.msg);
                        } else {
                            toastr.success(MESSAGE.INFO_WRITEOFF_SUCCESS);
                            myHeaderInit();
                            tableBox.ajax.reload();
                        }
                    })
                }
            );
        },

        //添加车辆
        addVins: function () {
            tableAddVins = initDataTables(
                "tableAddVins",
                addVinsColumns,
                function () { },
                constant.projectURL + '/outInvoiceDetails/getAddVins',
                function () {
                    return { "outBoundCode": thisOutBoundCode };
                },
                0, 0, true, false, false,
                [2, "asc"]
                , null
                , { processing: false }
                , true
            );
        }
    }
});
//赋值-myHeader
var headInitDFD = $.Deferred();
var myHeaderInit = function () {
    var textAjax = {
        url: constant.projectURL + '/outInvoiceDetails/getThisOutBoundOrder',
        data: getSearchConditon(),
        success: function (resp) {
            //Vue-myHeader给值
            myHeaderText.customerName = resp.data[0].customerName;
            myHeaderText.outBoundCode = resp.data[0].asnStatus == SysParamConstant.P_ASN_STATUS_SENDED ? "CK" + resp.data[0].sapInvoiceCode : "";
            myHeaderText.contactInformation = resp.data[0].contactInformation;
            myHeaderText.sapInvoiceCode = resp.data[0].sapInvoiceCode;
            myHeaderText.customerAddress = resp.data[0].customerAddress;
            myHeaderText.logisticsParking = resp.data[0].logisticsParking;
            myHeaderText.logisticsVehicleNo = resp.data[0].logisticsVehicleNo;
            myHeaderText.giftName = resp.data[0].giftName;
            myHeaderText.shipTypeName = resp.data[0].shipTypeName;
            myHeaderText.logisCompanyName = resp.data[0].logisCompanyName;
            myHeaderText.addMode = (thisOderTypeCode == "601" && resp.data[0].asnStatus == SysParamConstant.P_ASN_STATUS_WAITSEND) ? true : false;
            myHeaderText.waitMode = resp.data[0].asnStatus == SysParamConstant.P_ASN_STATUS_WAITSEND ? true : false;
            myHeaderText.sendedMode = ((resp.data[0].asnStatus == SysParamConstant.P_ASN_STATUS_CONFIRMED || resp.data[0].asnStatus == SysParamConstant.P_ASN_STATUS_SENDED) && resp.data[0].isPrint != "1") ? true : false;
            myHeaderText.washMode = resp.data[0].asnStatus == SysParamConstant.P_ASN_STATUS_SENDED ? true : false;

            //货运方式下拉赋值
            $("#shipType").val(resp.data[0].shipType);

            //物流公司下拉赋值
            $("#logisCompany").val(resp.data[0].logisCompanyCode);

            myHeaderText.$nextTick(function () {
                headInitDFD.resolve();
            })
        }
    }
    $._ajaxd(textAjax);
}

//Modal保存按钮事件
var btnModalSaveFunc = function () {
    //后台更换发货单vin
    var newVin = $("#iptNewVIN").val();
    $('#myModal').modal('hide');
    var textAjax = {
        type: 'POST',
        url: constant.projectURL + '/outInvoiceDetails/updateOutBoundVIN',
        data: { "oldVIN": $("#modalOldVIN").text(), "newVIN": newVin, "invoiceCode": $("#invoiceCode").text() },
        datatype: 'json',
        success: function (resp) {
            toastr.success(MESSAGE.INFO_CHANGE_CHAR_SUCCESS);
            //隐藏Modal
            $('#myModal').modal('hide');
            //刷新Table
            tableBox.ajax.reload();
        }
    }
    $._ajax(textAjax);
}

$(document).ready(function () {

    if (thisOutBoundCode) {
        $.when.apply($, g_deferreds)
            .done(function () {
                g_deferreds = [];
                setTimeout(myHeaderInit, 200);
            });
    }

    $.when(headInitDFD)
        .done(function () {
            //初始化表格( 详细配置 )
            tableBox = initDataTables(
                "tableBox",
                thisOderTypeCode == "601" ? columns : columnsReturn,
                createdRow,
                constant.projectURL + '/outInvoiceDetails/getThisOutBoundVIN',
                getSearchConditon,
                1, 1,
                false,
                false,
                false
            );
        })

    //界面-初始化valiForm表单验证
    $("#valiForm").validationEngine("attach", {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        //关闭-屏幕自动滚动到第一个验证不通过的位置
        scroll: false,
    });

    //换车modal-初始化validate验证
    $("#modalForm").validationEngine('attach', {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        isOverflown: true,
        //验证隐藏
        validateNonVisibleFields: true,
    });

    //监听select选中-赋值给input,用于validate验证
    $("#select2-chosen-1").bind("DOMNodeInserted", function (e) {
        $("#iptNewVIN").text($("#select2-chosen-1").text());
    })

    //换车Modal保存按钮点击事件 
    $(document).on('click', '#btnModalSave', function () {
        //validate验证
        if (!$("#modalForm").validationEngine("validate")) {
            return;
        }
        // openConfirmDialog("确定换车吗?", btnModalSaveFunc);
        btnModalSaveFunc();
    });

    //添加车辆Modal保存按钮点击事件 
    $(document).on('click', '#addVinsSave', function () {
        var vins = [];
        var dataVins = tableAddVins.data();
        for (var a = 0; a < dataVins.length; a++) {
            if (dataVins[a].checked) {
                vins.push(dataVins[a].vin);
            }
        }
        if (vins.length == 0) {
            toastr.info("请选择车辆。");
            return;
        }
        var textAjax = {
            url: constant.projectURL + '/outInvoiceDetails/addVins',
            data: { "vins": vins.join(","), "sapInvoiceCode": $("#sapInvoiceCode").text() }
        }
        $._ajaxd(textAjax)
            .done(function (resp) {
                //隐藏Modal
                $('#addVinsModal').modal('hide');
                //刷新Table
                tableBox.ajax.reload();
            });
    });

    //返回按钮
    addReturnButton();

    //添加车辆弹窗显示后刷新明细
    $('#addVinsModal')
        .on('shown.bs.modal', function () {
            tableAddVins.ajax.reload();
        })
        .on('hidden.bs.modal', function () {
            tableAddVins.destroy();
        })

    //添加车辆明细初始化完成后打开弹窗
    $('#tableAddVins')
        .on('init.dt', function () {
            $('#addVinsModal').modal('show');
        })

})

//添加车辆的列
var addVinsColumns = [
    { title: "VIN码", data: "vin", orderable: true, name: "stock.vin" },
    { title: "出库仓库", data: "productStock", orderable: true, name: "mps.name" },
    { title: "物料编码", data: "matCode", orderable: true, name: "stock.matCode" },
    { title: "物料描述", data: "matName", orderable: true, name: "mainMat.name" },
    { title: "库位号", data: "stockPosition", orderable: true, name: "sys.name" }
];

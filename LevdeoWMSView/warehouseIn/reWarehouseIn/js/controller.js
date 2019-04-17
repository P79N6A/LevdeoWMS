/**
 * 非下线入库
 */

var dataList = [];
columns = [ //显示的列(title:列名,data:数据key)
    { title: "物料编码", data: "matCode", orderable: true, sWidth: 100, searchable: true },
    { title: "物料描述", data: "matDescription", orderable: true, searchable: true },
    { title: "VIN码", data: "vin", orderable: true, sWidth: 100, searchable: true },
    { title: "库位", data: "stockPosition", orderable: true, sWidth: 50, searchable: true },
    { title: "成品库", data: "productStockName", orderable: true, searchable: true }]

var params = {
    trno: 1,
    validUser: false,//是否是有效的作业员
}
var getInitVal = function () {
    $("#iptUcode").select();
    params.initVal = $("#iptUcode").val();
}
var chkdiffVal = function () {
    if ($("#iptUcode").val() != params.initVal) {
        getUserName();
    }
}

//onkeydown：作业员，如果是回车(键盘或扫描枪均可)
var funkeydown = function () {
    if (event.keyCode == 13 || event.which == 13) {
        getUserName();
    }
}

//表格高度
calcHeight("#tableBox tbody", [], 150);

/**
 * 作业员
 * onchange事件按回车也会触发，为了防止按回车同时触发onchange、onkeydown，调用两次getUserName()
 * 用getInitVal()、chkdiffVal()替换onchange，可以实现回车只触发onkeydown，只调用一次getUserName()
 */
//获取作业员名
var getUserName = function () {
    var userCode = $("#iptUcode").val();
    params.initVal = userCode;
    if (userCode == null || userCode == undefined || userCode.trim() == "") {
        $("#iptUname").val("");
        $("#myHeader form").validationEngine("hide");
        return;
    }

    var optSettings = {
        url: constant.projectURL + "/Warehousing/getUserName",
        type: "GET",
        data: { 'userCode': userCode },
        dataType: "json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.info(MESSAGE.ERROR_USERNAME_SEARCH);//作业员名称获取失败。
                params.validUser = false;
                return;
            }
            if (resp.data != null && resp.data != undefined && resp.data.trim() != "") {
                $("#iptUname").val(resp.data);
                params.validUser = true;
            } else {
                $("#iptUname").val("");
                $("#iptUcode").focus();
                //不是有效的作业员
                params.validUser = false;
            }
            $("#myHeader form").validationEngine("validate");
            //光标移到表格第一行
            $("input[name='iptVin']:eq(0)").focus();
        },
        error: function (resp, textStatus) {
            toastr.info(MESSAGE.ERROR_USERNAME_SEARCH);//作业员名称获取失败。
            params.validUser = false;
        }
    }
    $._ajax(optSettings);
}

//onkeydown：VIN码，如果是回车(键盘或扫描枪均可)
var funwskeydown = function (obj) {
    if (event.keyCode == 13 || event.which == 13) {
        //防止chkwsdiffVal中，getWarehousingInfo再次执行
        params.wsinitVal = $(obj).val();
        if (params.wsinitVal.trim() != "") {
            getWarehousingInfo(obj);
        }
    }
}

//获取VIN码初始值
var getwsInitVal = function (obj) {
    $(obj).select();
    params.wsinitVal = $(obj).val();
}
var chkwsdiffVal = function (obj) {
    if ($(obj).val() != params.wsinitVal) {
        getWarehousingInfo(obj);
    }
}

//onkeydown：VIN码，如果是回车(键盘或扫描枪均可)
var getWarehousingInfo = function (obj) {
    var thisval = $(obj).val();
    var thistr = $(obj).parents("tr");
    var trIndex = $(obj).parents("tr").index();
    var trnums = $("#tableBox tbody tr").length;

    //如果值为空
    if (thisval == null || thisval == undefined || thisval.trim() == "") {
        tabFillData.items[trIndex].matCode = "";
        tabFillData.items[trIndex].matDescription = "";
        tabFillData.items[trIndex].stockPosition = "";

        //行去除成功标志class
        thistr.removeClass("successtr");
        //光标停留在当前行
        $(obj).focus();
        //绑定onblur事件，只能在当前行再次运行
        // $(obj).bind("blur", function () {
        //     $(obj).focus();
        // });
        return;
    }

    var repeat = false;
    $("input[name='iptVin']").each(function (index, item) {
        if (trIndex != index && $(item).val() == thisval) {
            repeat = true;
            return false;
        }
    });
    if (repeat) {
        toastr.warning(MESSAGE.WARN_VIN_REPEAT);//入库交接单重复。
        return;
    }

    var optSettings = {
        url: constant.projectURL + "/Warehousing/getStockPosition",
        type: "GET",
        data: { 'vin': thisval.trim(), spFlg: 4 },
        dataType: "json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.info(MESSAGE.ERROR_TRANSITION_SEARCH);//入库交接单信息检索失败。
                tabFillData.items[trIndex].matCode = "";
                tabFillData.items[trIndex].matDescription = "";
                tabFillData.items[trIndex].stockPosition = "";
                //行去除成功标志class
                thistr.removeClass("successtr");
                //光标停留在当前行
                $(obj).focus();
                //绑定onblur事件，只能在当前行再次运行
                $(obj).bind("blur", function () {
                    $(obj).focus();
                });
                return;
            }
            if (resp.data != null && resp.data != undefined && resp.data != "0301" && resp.data != "0300") {

                tabFillData.items[trIndex] = resp.data;
                //如果是最后一行，添加一行
                if (trIndex == trnums - 1) {
                    tabFillData.items.push({});
                }
                tabFillData.$forceUpdate();
                //入库交接单只读
                tabFillData.items[trIndex].readonly = true;
                //行添加成功标志class
                thistr.addClass("successtr");
                //解绑onblur事件
                $(obj).unbind("blur");

                setTimeout(
                    function () {
                        if ($("#tableBox tbody")[0].scrollHeight <= $("#tableBox tbody")[0].clientHeight) {
                            //没有滚动条
                            $("#tableBox thead").html($("#tbhead1").html());
                        } else {
                            //如果有滚动条
                            $("#tableBox thead").html($("#tbhead2").html());
                        }
                    }, 100

                );
                tabFillData.$nextTick(function () {
                    //光标移到下一行
                    $("input[name='iptVin']:eq(" + (trIndex + 1) + ")").focus();
                });
            } else {
                if (resp.data == "0300") {
                    toastr.warning("vin:" + $(obj).val() + "未查询到该车辆");
                } else {
                    toastr.warning("vin:" + $(obj).val() + "车辆没有在借出或者返厂状态无法非下线入库");
                }
            }
        },
        error: function (resp, textStatus) {
            toastr.info(MESSAGE.ERROR_TRANSITION_SEARCH);//入库交接单信息检索失败。
            tabFillData.items[trIndex].matCode = "";
            tabFillData.items[trIndex].matDescription = "";
            tabFillData.items[trIndex].stockPosition = "";
            //行去除成功标志class
            thistr.removeClass("successtr");
            //光标停留在当前行
            $(obj).focus();
            //绑定onblur事件，只能在当前行再次运行
            $(obj).bind("blur", function () {
                $(obj).focus();
            });
        }
    }
    $._ajax(optSettings);
}

//检验作业员是否有效
var chkuser = function () {
    var userCode = $("#iptUcode").val();
    if (userCode == null || userCode == undefined || userCode.trim() == "") {
        return;
    }
    //不是有效的作业员
    if (!params.validUser) {
        return "请输入有效的作业员工号。";
    }
}

//保存前验证
var datasSaveChk = function () {
    //表单验证
    var formFlg = $("#myHeader form").validationEngine("validate");
    if (!formFlg) {
        return;
    }
    //需要保存的数据条数
    var cnt = $("#tableBox tbody tr.successtr").length;
    if (cnt <= 0) {
        toastr.warning(MESSAGE.WARN_NODATA_SAVE);//没有需要保存的数据。
        return;
    }
    //确定保存吗？
    openConfirmDialog(MESSAGE.CONFIRM_SAVE, dataSave);
}
createdRow = function () {
    return {};
}
/**
 * 保存成功后,界面样式变化
 * @param {*} data1 
 */
var searchData = function (data1) {
    $("#tableBox").attr("hidden", "true");
    $("#iptUcode").attr("disabled", "true");
    $("#divBtnSave").attr("hidden", "true");
    $("#tableBox_queryInfo").removeAttr("hidden");
    tableBox = initDataTables(
        "tableBox_queryInfo",
        columns,
        createdRow,
        constant.projectURL + '/Warehousing/searchOnlineIntoWarehouseUpdatingInfo',
        function () { return { wp: data1 } },
        1,
        1, false, false, false);
}

/**
 * 保存
 */
var dataSave = function () {
    collectingTableInfo();
    var optSettings = {
        url: constant.projectURL + "/Warehousing/updateOnlineIntoWarehousingState",
        type: "POST",
        data: { "wp": JSON.stringify(dataList) },
        //contentType:"application/x-www-form-urlencoded",
        dataType: "json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.info(MESSAGE.ERROR_SAVE);//保存失败。
                return;
            }
            if (resp.data != "0401") {
                toastr.success(MESSAGE.INFO_SAVE_SUCCESS);//保存成功。
                searchData(JSON.stringify(dataList));
                saveLog(resp.data);
            } else {
                toastr.warning(WARN_STOCKPOSITION_WAS_FULL);
            }
        },
        error: function (resp, textStatus) {
            toastr.info(MESSAGE.ERROR_SAVE);//保存失败。
        }
    }
    $._ajax(optSettings);
}

/**
 * 保存Log
 */
var saveLog = function (respData) {

    var winfo = [];
    for (var i = 0; i < respData.length; i++) {
        var data = {};
        data.vin = respData[i].vin;
        data.matCode = respData[i].matCode;
        data.newStorageNo = respData[i].stockPositionName;
        data.operateCode = SysParamConstant.P_STOCKLOG_REWAREHOUSEIN;
        data.updateId = $("#iptUcode").val();
        data.createId = $("#iptUcode").val();
        winfo.push(data);
    }

    var optSettings = {
        type: "POST",
        url: constant.projectURL + "/Warehousing/saveLog",
        data: {
            slog: JSON.stringify(winfo),
        },
        dataType: "json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.info(MESSAGE.ERROR_SAVE);//保存失败。
                return;
            }
            toastr.success(MESSAGE.INFO_SAVE_SUCCESS);//保存成功。
            //   location.reload();
        },
        error: function (resp, textStatus) {
            toastr.info(MESSAGE.ERROR_SAVE);//保存失败。
        }
    }
    $._ajax(optSettings);

};
/**
 * 获取 VIN
 */
var collectingTableInfo = function () {
    dataList = [];
    $("#tableBox tbody tr.successtr").each(function (index, thistr) {

        var data = {};
        data.vin = $(thistr).find("input[name='iptVin']").val();
        data.updateId = $("#iptUcode").val();
        dataList.push(data);
    });
}


$(function () {

    $('input:text:first').focus();

    $("#myHeader form").validationEngine("attach", {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        isOverflown: true,
    });

    //表格初始化
    tabFillData = new Vue({
        el: "#tableBox",
        data: {
            items: [{}],
        },
        methods: {
            deleteRow: function (index) {
                if (this.items.length != index + 1) {
                    this.items.splice(index, 1);
                }
                this.$nextTick(function () {
                    $("input[name='iptVin']:eq(" + (this.items.length - 1) + ")").select();
                    $("input[name='iptVin']:eq(" + (this.items.length - 1) + ")").focus();
                });
            }
        }
    });

    //保存
    $("#btnSave").on("click", function () {
        datasSaveChk();
    });

})

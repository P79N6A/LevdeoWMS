/**
 * 打印库位
 */

var params = {
    validUser: false,//是否是有效的作业员
    wsinput: null
}

$(document).ready(function () {
    //表单验证初始化
    $("#myHeader form").validationEngine("attach", {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        isOverflown: true,
    });

    //表格初始化
    tabFillData = new Vue({
        el: "#mainTable",
        data: {
            items: [{}]
        }
    });

    //表格高度
    calcHeight("#mainTable tbody", [], 150);

    //保存
    $("#btnSave").on("click", function () {
        datasSaveChk();
    });

    //将光标放在作业员输入框
    $("#userCode").focus();

    //连接后台打印服务
    argoxPrint.connection();

    //页面离开时，与打印机断开连接
    window.onbeforeunload = function (e) {
        if (argoxPrint.ws.readyState == 1 && argoxPrint.connPrinter) {
            argoxPrint.ws.send("B_ClosePrn");
        }
    }
})

//onkeydown：作业员，如果是回车(键盘或扫描枪均可)
var funkeydown = function () {
    if (event.keyCode == 13 || event.which == 13) {
        getUserName();
    }
}

/**
 * 作业员
 * onchange事件按回车也会触发，为了防止按回车同时触发onchange、onkeydown，调用两次getUserName()
 * 用getInitVal()、chkdiffVal()替换onchange，可以实现回车只触发onkeydown，只调用一次getUserName()
 */
var getInitVal = function () {
    $("#userCode").select();
    params.initVal = $("#userCode").val();
}
var chkdiffVal = function () {
    if ($("#userCode").val() != params.initVal) {
        getUserName();
    }
}

//获取作业员名
var getUserName = function () {
    var userCode = $("#userCode").val();
    params.initVal = userCode;
    if (userCode == null || userCode == undefined || userCode.trim() == "") {
        $("#userName").val("");
        //表单验证隐藏提示
        $("#myHeader form").validationEngine("hide");
        return;
    }

    var optSettings = {
        type: "POST",
        url: constant.projectURL + "/warehousingprint/getUserName",
        data: {
            "userCode": userCode
        },
        dataType: "json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.info(MESSAGE.ERROR_USERNAME_SEARCH);//作业员名称获取失败。
                //不是有效的作业员
                params.validUser = false;
                return;
            }
            if (resp.data != null && resp.data != undefined && resp.data.trim() != "") {
                $("#userName").val(resp.data);
                //光标移到表格第一行
                $("input[name='wsTransition']:eq(0)").focus();
                //是有效的作业员
                params.validUser = true;
            } else {
                $("#userName").val("");
                $("#userCode").focus();
                //不是有效的作业员
                params.validUser = false;
            }
            //表单验证
            $("#myHeader form").validationEngine("validate");
        },
        error: function (resp, textStatus) {
            toastr.info(MESSAGE.ERROR_USERNAME_SEARCH);//作业员名称获取失败。
            //不是有效的作业员
            params.validUser = false;
        }
    }
    $._ajax(optSettings);
}

//onkeydown：入库交接单，如果是回车(键盘或扫描枪均可)
var funwskeydown = function (obj) {
    if (event.keyCode == 13 || event.which == 13) {
        //防止chkwsdiffVal中，getWarehousingInfo再次执行
        params.wsinitVal = $(obj).val();

        getWarehousingInfo(obj);
    }
}

//获取入库交接单初始值
var getwsInitVal = function (obj) {
    $(obj).select();
    params.wsinitVal = $(obj).val();
}
var chkwsdiffVal = function (obj) {
    if ($(obj).val() != params.wsinitVal) {
        getWarehousingInfo(obj);
    }
}

//onkeydown：入库交接单，如果是回车(键盘或扫描枪均可)
var getWarehousingInfo = function (obj) {
    params.wsinput = obj;
    var thisval = $(obj).val();
    var thistr = $(obj).parents("tr");
    var trIndex = $(obj).parents("tr").index();

    //如果值为空
    if (thisval == null || thisval == undefined || thisval.trim() == "") {
        //行去除成功标志class
        thistr.removeClass("successtr");
        thistr.removeClass("failed");
        //去除当前行内容
        // $(obj).parents("tr").find("td:not(:eq(0)):not(:eq(0))").html("");
        tabFillData.items[trIndex] = {};
        tabFillData.$forceUpdate();
        //光标停留在当前行
        $(obj).focus();
        return;
    }

    var repeat = false;
    $("input[name='wsTransition']").each(function (index, item) {
        if (trIndex != index && $(item).val() == thisval) {
            repeat = true;
            return false;
        }
    });
    if (repeat) {
        toastr.warning(MESSAGE.WARN_WSTRANSITION_REPEAT);//入库交接单重复。
        return;
    }

    var optSettings = {
        type: "POST",
        url: constant.projectURL + "/warehousingprint/getWarehousingInfo",
        data: {
            "wsTransition": thisval.trim()
        },
        dataType: "json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                //入库交接单信息检索失败。
                toastr.error(MESSAGE.ERROR_TRANSITION_SEARCH);

                //播放失败音乐
                $("#infomp3").attr("src", constant.projectURL + "/music/fail.mp3");
                //行去除成功标志class
                thistr.removeClass("successtr");
                thistr.addClass("failed");
                //去除当前行内容
                tabFillData.items[trIndex] = {};
                tabFillData.$forceUpdate();
                //光标停留在当前行
                $(obj).focus();
                return;
            }

            var respdata = resp.data;
            if (respdata != null && respdata != undefined) {
                // 本行填充数据
                tabFillData.items[trIndex] = respdata;
                tabFillData.$forceUpdate();

                // 库位号
                var stockPositionName = respdata.stockPositionName;
                // VIN码
                var vin = respdata.vin;
                //打印
                argoxPrint.sendMessage(stockPositionName, vin);
            } else {
                //入库交接单不存在。
                toastr.warning(MESSAGE.ERROR_NOTRANSITION);

                //播放失败音乐
                $("#infomp3").attr("src", constant.projectURL + "/music/fail.mp3");
                //行去除成功标志class
                thistr.removeClass("successtr");
                thistr.addClass("failed");
                //去除当前行内容
                tabFillData.items[trIndex] = {};
                tabFillData.$forceUpdate();
                //光标停留在当前行
                $(obj).focus();
            }
        },
        error: function (resp, textStatus) {
            //入库交接单信息检索失败。
            toastr.error(MESSAGE.ERROR_TRANSITION_SEARCH);
            //播放失败音乐
            $("#infomp3").attr("src", constant.projectURL + "/music/fail.mp3");
            //行去除成功标志class
            thistr.removeClass("successtr");
            thistr.addClass("failed");
            //去除当前行内容
            tabFillData.items[trIndex] = {};
            tabFillData.$forceUpdate();
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

//保存前验证
var datasSaveChk = function () {
    //表单验证
    var formFlg = $("#myHeader form").validationEngine("validate");
    if (!formFlg) {
        return;
    }

    //需要保存的数据条数
    var cnt = $("#mainTable tbody tr.successtr").length;
    if (cnt <= 0) {
        toastr.warning(MESSAGE.WARN_NODATA_SAVE);//没有需要保存的数据。
        return;
    }

    //确定保存吗？
    openConfirmDialog(MESSAGE.CONFIRM_SAVE, datasSave);
}

//保存
var datasSave = function () {
    var dataList = [];
    $("#mainTable tbody tr.successtr").each(function (index, thistr) {
        var data = {}
        data.vin = tabFillData.items[index].vin;
        data.matCode = tabFillData.items[index].matCode;
        data.oldStorageNo = tabFillData.items[index].stockPositionName;
        data.operateCode = SysParamConstant.P_STOCKLOG_PRINT;
        data.createId = $("#userCode").val();
        data.updateId = $("#userCode").val();
        dataList.push(data);
    });

    var optSettings = {
        type: "POST",
        url: constant.projectURL + "/Warehousing/saveLog",
        data: {
            "slog": JSON.stringify(dataList)
        },
        dataType: "json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.info(MESSAGE.ERROR_SAVE);//保存失败。
                return;
            }
            toastr.success(MESSAGE.INFO_SAVE_SUCCESS);//保存成功。
            location.reload(true);
        },
        error: function (resp, textStatus) {
            toastr.info(MESSAGE.ERROR_SAVE);//保存失败。
        }
    }
    $._ajax(optSettings);
}

//检验作业员是否有效
var chkuser = function () {
    var userCode = $("#userCode").val();
    if (userCode == null || userCode == undefined || userCode.trim() == "") {
        return;
    }

    //不是有效的作业员
    if (!params.validUser) {
        return "请输入有效的作业员工号。";
    }
}
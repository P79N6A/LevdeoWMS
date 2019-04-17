/**
 * 钥匙入库
 */
var params = {
    trno: 1
}
var getInitVal = function () {
    $("#userCode").select();
    params.initVal = $("#userCode").val();
}
var chkdiffVal = function () {
    if ($("#userCode").val() != params.initVal) {
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
        return;
    }

    var optSettings = {
        url: constant.projectURL + "/Warehousing/getUserName",
        type: "GET",
        data: { 'userCode': userCode },
        dataType: "json",
        success: function (resp, textStatus) {
            if (resp.data != null && resp.data != undefined && resp.data.trim() != "") {
                $("#iptUname").val(resp.data);
            }
            //光标移到表格第一行
            $("input[name='iptVin']:eq(0)").focus();
        },
    }
    $._ajax(optSettings);
}

//onkeydown：VIN码，如果是回车(键盘或扫描枪均可)
var funwskeydown = function (obj) {
    if (event.keyCode == 13 || event.which == 13) {
        //防止chkwsdiffVal中，getWarehousingInfo再次执行
        params.wsinitVal = $(obj).val();
        getWarehousingInfo(obj);
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
        tabFillData.items[trIndex].sapTransfer = "";
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
        data: { 'vin': thisval.trim(), spFlg: 3 },
        dataType: "json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.error(MESSAGE.ERROR_TRANSITION_SEARCH);//入库交接单信息检索失败。
                tabFillData.items[trIndex].sapTransfer = "";
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
            if (resp.data != null && resp.data != undefined && resp.data.sapTransfer != undefined && resp.data.sapTransfer.trim() != '未过账' && resp.data != "0201" && resp.data != "0202") {
                //解绑onblur事件
                $(obj).unbind("blur");
                tabFillData.items[trIndex] = resp.data;
                //vin单只读
                tabFillData.items[trIndex].readonly = true;
                //如果是最后一行，添加一行
                if (trIndex == trnums - 1) {
                    tabFillData.items.push({});
                }
                tabFillData.$forceUpdate();
                //行添加成功标志class
                thistr.addClass("successtr");
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
                //行去除成功标志class
                thistr.removeClass("successtr");
                //光标停留在当前行
                $(obj).focus();

                if (resp.data == "0201") {
                    toastr.warning(MESSAGE.WARN_NOT_CAR_IN);

                } else if (resp.data == "0202") {
                    toastr.warning(MESSAGE.WARN_SAP_NO_PAY);

                } else {
                    toastr.warning(MESSAGE.INFO_NODATA);
                }
            }
        },
        error: function (resp, textStatus) {
            toastr.error(MESSAGE.ERROR_TRANSITION_SEARCH);//入库交接单信息检索失败。
            tabFillData.items[trIndex].sapTransfer = "";
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
    var userCode = $("#userCode").val();
    if (userCode == null || userCode == undefined || userCode.trim() == "") {
        return;
    }

    var userName = $("#userName").val();
    if (userName == null || userName == undefined || userName.trim() == "") {
        return "请输入有效的作业员工号。";
    }
}

/**
 * 
 */
var dataSave = function () {
    var dataList = [];
    $("#tableBox tbody tr.successtr").each(function (index, thistr) {
        var data = {};
        data.vin = $(thistr).find("input[name='iptVin']").val();
        data.updateId = $("#iptUcode").val();
        dataList.push(data);
    });
    var optSettings = {
        url: constant.projectURL + "/Warehousing/updateKeyIntoWarehousingState",
        type: "GET",
        data: { "wp": JSON.stringify(dataList) },
        dataType: "json",
        success: function (resp, textStatus) {
            saveLog();
        },
    }
    $._ajax(optSettings);
}

/**
 * 
 */
var saveLog = function () {
    var winfo = [];
    $("#tableBox tbody tr.successtr").each(function (index, thistr) {
        var data = {};
        data.vin = $(thistr).find("input[name='iptVin']").val();
        data.matCode = tabFillData.items[index].matCode;
        data.oldStorageNo = tabFillData.items[index].stockPosition;
        data.operateCode = SysParamConstant.P_STOCKLOG_KEYWAREHOUSEIN;
        winfo.push(data);
    });
    var optSettings = {
        type: "POST",
        url: constant.projectURL + "/Warehousing/saveLog",
        data: {
            "slog": JSON.stringify(winfo)
        },
        dataType: "json",
        success: function (resp, textStatus) {
            location.reload();
            toastr.success(MESSAGE.INFO_SAVE_SUCCESS);//保存成功。
        }
    }
    $._ajax(optSettings);
};

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



$(function () {

    $('input:text:first').focus();

    $("#myHeader form").validationEngine("attach", {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100
    });

    //表格初始化
    tabFillData = new Vue({
        el: "#tableBox",
        data: {
            items: [{ readonly: false }]
        },
        methods: {
            deleteRow: function (index) {
                if (this.items.length == index + 1) {
                } else {
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

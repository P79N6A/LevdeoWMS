//获取参数
var stockPosition = getUrlParam("stockPosition");
var stockArea = getUrlParam("stockArea");
var stockAreaCode = getUrlParam("stockAreaCode");
var warehouse = getUrlParam("warehouse");
var isUse = getUrlParam("isUse");
var isLock = getUrlParam("isLock");
var lockReason = getUrlParam("lockReason");

var heights=document.body.clientHeight-$(".page-header").height()-$("#myHeader").height()-80;
    
$("#parDiv").css({"overflow-y":"auto","height":heights});

$(function () {

    //style="overflow-y:auto; height:600px;"
    
    //初始化-myHeader
    initMyHeader();

    //初始化3个优先级select框
    initPrioritySelects();

    //监听-锁定按钮
    $(document).on('click', '#btnLock', function () {
        //库位使用中-禁止锁定
        if (isUse == "使用中") {
            toastr.warning(MESSAGE.WARN_STOCKPOSITION_BE_USED_CANT_LOCK);
            return;
        }
        doLock();
    });

    //监听-解锁按钮
    $(document).on('click', '#btnUnlock', function () {
        openConfirmDialog("确定解锁当前库位吗?", function () { doUnlock(stockPosition) });
    });

    //监听-保存按钮
    $(document).on('click', '#btnSave', function () {
        btnSaveFunc();
    });

    //监听-拷贝按钮
    $(document).on('click', '#btnCopy', function () {
        //显示Modal
        $('#myModalCopy').modal('show');
        $("input[value=radioNo]").prop("checked",true);
        
        //隐藏之前的验证信息
        $("#myModalCopy").validationEngine("hideAll");

        //初始化下拉
        initSelectSp();

        //Modal-label赋值
        $("#copyTemplate").html($("#labPosition").html());

        //Modal确定按钮点击事件 
        $(document).on('click', '#btnCopySure', function () {
            //取值
            var startSp = $("#copyStartSp").find("option:selected").val();
            var endSp = $("#copyEndSp").find("option:selected").val();
            var radioChoose = $('input:radio:checked').val();
            //validate验证
            if (!$("#modalCopyForm").validationEngine("validate")) {
                return;
            }

            //Ajax
            copySpPriority(stockPosition, startSp, endSp, radioChoose);
        });
    });

    //初始化-表单验证
    //主页面
    $("#valiForm").validationEngine('attach', {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        isOverflown: true,
    });
    //锁定modal
    $("#modalForm").validationEngine('attach', {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        isOverflown: true,
    });
    //复制modal
    $("#modalCopyForm").validationEngine('attach', {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        isOverflown: true,
    });
});
/**
 * validate-验证开始库位是否大于终止库位
 */
function checkStartEnd() {
    var valiStartSp = $("#copyStartSp").find("option:selected").val();
    var valiEndSp = $("#copyEndSp").find("option:selected").val();
    if (valiStartSp <= valiEndSp) {
        return;
    }
    return "'终止库位'必须大于'开始库位'";
}
/**
 * myHeader-赋值
 */
function initMyHeader() {
    $("#labstockArea").text(stockArea);
    $("#labPosition").text(getUrlParam("position"));
    $("#labPositionId").text(stockPosition);
    $("#isUse").text(isUse);
    $("#isLock").text(isLock == "是" ? "锁定" : "未锁定");
    $("#lockReason").text(undnulltostring(lockReason));
    //初始化-按钮
    if (isLock == "是") {
        //锁定状态-显示-解锁按钮btnSave
        $("#btnUnlock").show();
    } else {
        //未锁定-显示-锁定按钮
        $("#btnCopy").show();
        $("#btnSave").show();
        $("#btnLock").show();
    }
}
/**
 * 初始化3个优先级select框
 */
function initPrioritySelects() {
    $._ajax({
        type: 'GET',
        url: constant.projectURL + '/sys-stock-position/getPositionPrioritys',
        data: {
            'warehouse': warehouse,
            'stockPosition': stockPosition
        },
        dataType: 'json',
        success: function (resp, textStatus) {
            if (resp && resp.data) {
                var prioritys = resp.data.prioritys;
                var colorPriority = resp.data.colorPriority;
                var brandPriority = resp.data.brandPriority;
                var seriesPriority = resp.data.seriesPriority;

                $("#priorityLV1").val(prioritys.priorityLV1);
                $("#priorityLV2").val(prioritys.priorityLV2);
                $("#priorityLV3").val(prioritys.priorityLV3);
                $("#priorityName1").text(prioritys.priorityName1);
                $("#priorityName2").text(prioritys.priorityName2);
                $("#priorityName3").text(prioritys.priorityName3);

                $(".priorityLV").each(function () {
                    var select = $(this).parent().parent().parent().find("select").eq(0);
                    switch ($(this).val()) {
                        case "01"://系列
                            for (i in seriesPriority) {
                                select.append('<option value="' + seriesPriority[i].code + '">' + seriesPriority[i].name + '</option>');
                            }
                            break;
                        case "02"://品牌
                            for (i in brandPriority) {
                                select.append('<option value="' + brandPriority[i].code + '">' + brandPriority[i].name + '</option>');
                            }
                            break;
                        case "03"://颜色
                            for (i in colorPriority) {
                                select.append('<option value="' + colorPriority[i].code + '">' + colorPriority[i].name + '</option>');
                            }
                            break;
                    }
                });
                var priorityData1 = resp.data.priorityData1;
                var priorityData2 = resp.data.priorityData2;
                var priorityData3 = resp.data.priorityData3;

                if (priorityData1 != null && priorityData2 != null && priorityData3 != null) {
                    $(".priorityLV").each(function () {
                        var select = $(this).parent().parent().parent().find("select").eq(1);
                        switch ($(this).attr('id')) {
                            case "priorityLV1":
                                for (i in priorityData1) {
                                    select.append('<option value="' + priorityData1[i].code + '">' + priorityData1[i].name + '</option>');
                                }
                                break;
                            case "priorityLV2":
                                for (i in priorityData2) {
                                    select.append('<option value="' + priorityData2[i].code + '">' + priorityData2[i].name + '</option>');
                                }
                                break;
                            case "priorityLV3":
                                for (i in priorityData3) {
                                    select.append('<option value="' + priorityData3[i].code + '">' + priorityData3[i].name + '</option>');
                                }
                                break;
                        }
                    });
                }
            } else {
                toastr.error(MESSAGE.ERROR_SEARCHING_FAIL);
                return;
            }
            $('#multiselect_1').multiselect({
                //重写-moveToRight
                moveToRight: function (Multiselect, $options, event, silent, skipStack) {
                    //right-select
                    var $varRight = Multiselect.$right;
                    //循环选中的options
                    $options.each(function (index, option) {
                        var $option = $(option);
                        //调用 move方法
                        $varRight.rightMoveLD($option);
                    })
                    //取消选中
                    $varRight.find("option").prop('selected', false);

                },
            });
            $('#multiselect_2').multiselect({
                //同上
                moveToRight: function (Multiselect, $options, event, silent, skipStack) {
                    var $varRight = Multiselect.$right;
                    $options.each(function (index, option) {
                        var $option = $(option);
                        $varRight.rightMoveLD($option);
                    })
                    $varRight.find('option').prop('selected', false);
                },
            });
            $('#multiselect_3').multiselect({
                //同上
                moveToRight: function (Multiselect, $options, event, silent, skipStack) {
                    var $varRight = Multiselect.$right;
                    $options.each(function (index, option) {
                        var $option = $(option);
                        $varRight.rightMoveLD($option);
                    })
                    $varRight.find("option").prop('selected', false);
                },
            });
        },
        error: function (resp, textStatus) {
            toastr.error(MESSAGE.ERROR_SEARCHING_FAIL);
        }
    });
}

/**
 * move方法-moveToRight使用
 */
$.fn.rightMoveLD = function ($option) {

    //right选中时执行before-并取消选择状态
    var rightCheck = $(this).find("option:selected");
    if (rightCheck.length == 1) {
        rightCheck.before($option);
        //获取选中option-2个
        var twoRightCheck = $(this).find("option:selected");
        //取消第一个选中
        twoRightCheck.first().prop('selected', false);
        return this;
    }
    //right未选中时执行append-并取消选择状态
    this.append($option).find('option').prop('selected', false);

    return this;
};

/**
 * 锁定库位
 */
function doLock() {
    //显示Modal
    $('#myModal').modal('show');
    //隐藏之前的验证信息
    $("#myModalCopy").validationEngine("hideAll");
    //Modal-label赋值
    $("#mdlPositionCode").html($("#labPositionId").html());
    $("#mdlPositionName").html($("#labPosition").html());
    //清空input并获取焦点
    $('#mdlLockReason').val("").focus();

    //Modal确定按钮点击事件 
    $(document).on('click', '#btnModalSure', function () {
        stockPosition = $("#mdlPositionCode").html();;
        lockReason = $('#mdlLockReason').val().trim();
        //validate验证
        if (!$("#modalForm").validationEngine("validate")) {
            return;
        }
        //锁定库位
        lockPosition(stockPosition, lockReason);
    });
}

//锁定库位-调用后台
function lockPosition(stockPosition, lockReason) {
    var textAjax = {
        type: 'POST',
        url: constant.projectURL + '/sys-stock-position/lockPosition',
        data: { "stockPosition": stockPosition, "lockReason": lockReason },
        datatype: 'json',
        success: function (resp) {
            if (resp.code == "0") {
                //隐藏Modal
                $('#myModal').modal('hide');
                //隐藏-锁定按钮
                $("#btnLock").hide();
                $("#btnCopy").hide();
                $("#btnSave").hide();
                //显示-按钮
                $("#btnUnlock").show();

                toastr.success(MESSAGE.INFO_LOCK_SUCCESS);
                //修改myHead
                $("#isLock").text("锁定");
                $("#lockReason").text(lockReason);
            } else {
                toastr.error(resp.msg);
            }

        },
        error: function () {
            toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION);
        }
    }
    $._ajax(textAjax);
}

/**
 * 解锁库位
 */
function doUnlock(stockPosition) {
    var textAjax = {
        type: 'POST',
        url: constant.projectURL + '/sys-stock-position/unLockPosition',
        data: { "stockPosition": stockPosition },
        datatype: 'json',
        success: function (resp) {
            if (resp.code == "0") {
                //隐藏-解锁按钮
                $("#btnUnlock").hide();
                //显示-按钮
                $("#btnLock").show();
                $("#btnCopy").show();
                $("#btnSave").show();

                toastr.success(MESSAGE.INFO_UNLOCK_SUCCESS);
                //修改myHead
                $("#isLock").text("未锁定");
                $("#lockReason").text("");
            } else {
                toastr.error(resp.msg);
            }

        },
        error: function () {
            toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION);
        }
    }
    $._ajax(textAjax);
}

/**
 * 保存-库位优先级
 */
function btnSaveFunc() {

    /**
     * 三个select中的option全部选中-才可取到值
     */
    $("#multiselect_to_1").find("option").attr("selected", true);
    $("#multiselect_to_2").find("option").attr("selected", true);
    $("#multiselect_to_3").find("option").attr("selected", true);
    //validate验证
    if (!$("#valiForm").validationEngine("validate")) {
        return;
    }
    var priorityLV1 = $("#priorityLV1").val();
    var multiselect_to_1 = $("#multiselect_to_1").val();
    var priorityLV2 = $("#priorityLV2").val();
    var multiselect_to_2 = $("#multiselect_to_2").val();
    var priorityLV3 = $("#priorityLV3").val();
    var multiselect_to_3 = $("#multiselect_to_3").val();

    //弹出提示框
    openConfirmDialog(MESSAGE.CONFIRM_SAVE, function () { btnSaveAjax() });

    var btnSaveAjax = function () {
        var textAjax = {
            type: 'POST',
            url: constant.projectURL + '/sys-sp-priority/insertPriority',
            data: {
                "stockPosition": stockPosition,
                "priorityLV1": priorityLV1,
                "data1": multiselect_to_1.toString(),
                "priorityLV2": priorityLV2,
                "data2": multiselect_to_2.toString(),
                "priorityLV3": priorityLV3,
                "data3": multiselect_to_3.toString()
            },
            dataType: 'json',
            success: function (resp, textStatus) {
                if (resp.code == "0") {
                    toastr.success(MESSAGE.INFO_SAVE_SUCCESS);
                } else {
                    toastr.warning(resp.msg);
                }
            }
        }
        $._ajax(textAjax);
    }
}

/**
 * 库位下拉
 */
function initSelectSp() {
    $("select").each(function (i, select) {
        var dataType = $(select).data("typecopy");
        if (!dataType) {
            return;
        }
        //$(select).attr("v-model", "selected");

        $(select).append('<option></option>');

        $(select).append('<option v-for="option in options" v-bind:value="option.stockPosition">{{ option.name }}</option>');
        var optSettings = {
            selid: $(select).attr("id"),
            type: 'POST',
            url: constant.projectURL + '/sys-stock-position/getCopySp',
            data: { "stockAreaCode": stockAreaCode },
            dataType: 'json',
            success: function (resp, textStatus) {
                if (textStatus != "success") {
                    toastr.error(MESSAGE.ERROR_SEARCHING_FAIL);
                    return;
                }

                var vm = new Vue({
                    el: "#" + this.selid,
                    data: {
                        options: resp.data,
                    }
                })

                $("#" + this.selid).data("vue", vm);
            },
            error: function (resp, textStatus) {
                toastr.error(MESSAGE.ERROR_SEARCHING_FAIL);
            }
        }
        $._ajax(optSettings);
    });
}
/**
 * 批量插入-库位优先策略
 * @param {*} stockPosition 
 * @param {*} startSp 
 * @param {*} endSp 
 * @param {*} radioChoose 
 */
function copySpPriority(stockPosition, startSp, endSp, radioChoose) {
    if (stockPosition == null || startSp == null || endSp == null || radioChoose == null) {
        toastr.warning(MESSAGE.WARN_PARAMETER_NOT_EMPTY);
    }
    var textAjax = {
        type: 'POST',
        url: constant.projectURL + '/sys-stock-position/updateSpPrioBatch',
        data: {
            "stockPosition": stockPosition,
            "startSp": startSp,
            "endSp": endSp,
            "radioChoose": radioChoose,
        },
        dataType: 'json',
        success: function (resp, textStatus) {
            if (resp.code == "0") {
                toastr.success(MESSAGE.INFO_COPY_SUCCESS);
                //隐藏Modal
                $('#myModalCopy').modal('hide');
            } else {
                toastr.warning(resp.msg);
            }
        }
    }
    $._ajax(textAjax);

}



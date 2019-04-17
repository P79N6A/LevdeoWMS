//列名 列ID 是否增加排序 列宽 是否可检索
var columns = [
    { title: "VIN码", data: "vin", name: "s.vin" },
    { title: "物料编码", data: "matCode", name: "s.matCode" },
    { title: "物料描述", data: "materielName", name: "m.name" },
    { title: "库位号", data: "stockPosition", name: "sp.name" },
    { title: "状态", data: "status", name: "p.subName" }
];
$(function () {

    
    //实例化 年-月-日 (中文)
    $("#iptCheckDate").datetimepicker({
        //设置语言 中文(需引入语言包)
        language: 'zh-CN',
        //选择日期后，不会再跳转去选择时分秒 
        minView: 'month',
        //选择日期后，文本框显示的日期格式 
        format: 'yyyy-mm-dd',
        //选择后自动关闭
        autoclose: true,
        //显示‘今日’按钮
        todayBtn: 1,
        //清除按钮
        clearBtn: true,//清除按钮
        startDate:new Date()//不能选之前的日期
    }).attr("readonly","readonly");

    //表单验证初始化
    $("#newCheckForm").validationEngine("attach", {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        //自动隐藏-时间3000毫秒
        //autoHidePrompt: true,
        //autoHideDelay: 3000,
        isOverflown: true,
    });

    //初始化表格( 详细配置 )
    tableBox = initDataTables(
        "tableBox",
        columns,
        createdRow,
        constant.projectURL + '/inventory-head/getAllCarList',
        null,
        1,
        0,
        false,
        false,
        false);
    $("#btnSearch").on("click", function () {
        tableBox.ajax.reload();
    });

    //保存按钮消息绑定
    $("#btnSave").click(function () {
        if (!$("#newCheckForm").validationEngine("validate")) {
            return;
        }
        //绑定确认消息提示框
        //格式:openConfirmDialog(消息，确定按钮的回调方法，取消按钮的回调方法)，没有可以为空或null
        openConfirmDialog(MESSAGE.CONFIRM_SAVE, saveNewCheck);
    });
})

/**
 * 保存新建盘点计划
 */
function saveNewCheck() {
    var planDate = $("#iptCheckDate").val();
    $._ajax({
        type: 'post',
        url: constant.projectURL + '/inventory-head/saveNewPlan',
        data: {
            'planDate': planDate
        },
        dataType: 'json',
        success: function (resp) {
            if (resp.data) {
                toastr.success(MESSAGE.INFO_SAVE_SUCCESS);
                window.location.href = '../checkDetail/index.html?planId=' + resp.data
                    + '&planDate=' + planDate
                    + '&status=01';
            } else {
                toastr.warning(MESSAGE.ERROR_SAVE);
            }
        },
        error: function () {
            toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION);
        }
    });
}

/**
 * 控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
 * @param {*} row
 */
function createdRow(row) {
    //第一列右对齐
    $(row).children('td').eq(0).css('text-align', 'right');
}
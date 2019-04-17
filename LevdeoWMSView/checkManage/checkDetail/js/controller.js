//计划单号
var planId;
//盘点日
var planDate;
//盘点状态
var planStaus;
//盘点人下拉框
var planIdSelect;
//是否完成盘点
var isCompleteCheck;
//dataTables列     
var columns;
//一行tr
var newTr;
//当前登录人
var operator = top.$("#spanUser").text().split(/[\(\)]/)[1];
//是否有错误信息
var hasError;

//初始化dataTables
function initColums() {
    columns = [
        {
            title: "VIN码",
            data: function (data) {
                return '<div class="vin" style="width:100%; padding-left:8px; text-align:left;" oldData="' + data.vin + '">' + data.vin + '</div>';
            },
            sWidth: 170,
            name: "i.vin",
            orderable: false
        },
        { title: "物料编码", data: "matCode", name: "s.matCode", orderable: false },
        { title: "物料描述", data: "materielName", name: "m.name", orderable: false },
        { title: "库位号", data: "stockPosition", name: "sp.name", orderable: false },
        {
            title: "计划数量",
            data: function (data) {
                return data.outPlan == '1' ? "计划外" : data.planNum;
            },
            orderable: false,
            name: "i.planNum"
        },
        {
            title: "车盘点结果",
            data: function (data) {
                var carResult = data.carResult ? data.carResult : '';
                return isCompleteCheck ? carResult :
                    '<input type="text" class="carResult justNumber" value="' + carResult + '" '
                    + ' oldData="' + carResult + '" style="width:100%;text-align:right;" maxlength=2>';
            },
            orderable: false,
            name: "i.carResult"
        },
        // {
        //     title: "钥匙盘点结果", data: function (data) {
        //         var carResult = data.carResult ? data.keyResult : '';
        //         return isCompleteCheck ? keyResult :
        //             '<input type="text" class="keyResult" value="' + keyResult + '"'
        //             + ' onkeyup="justNumber(this)" onchange="justNumber(this)" '
        //             + 'style="width:60px;" maxlength=2>';
        //     },
        //     orderable: isCompleteCheck, name: "i.keyResult"
        // },
        {
            title: "盘点人",
            data: function (data) {
                var checkPerson = data.checkPerson ? data.checkPerson : '';
                return isCompleteCheck ? data.checkPersonName :
                    planIdSelect
                        .replace("value='" + checkPerson + "'", "value='" + checkPerson + "' selected")
                        .replace("{oldData}", checkPerson)
                    ;
            },
            orderable: false,
            name: "i.checkPerson"
        },
        {
            title: "删除",
            data: function (data) {
                if (isCompleteCheck || data.outPlan != '1') {
                    return '';
                } else {
                    return '<div class="table-td-btn" style="text-align:center">'
                        + '  <i class="del-btn fa fa-trash-o text-danger" title="删除计划外车辆"'
                        + '   onclick="delDataBase(this)"></i>'
                        + '</div>';
                }
            },
            orderable: false,
            name: "i.outPlan",
            visible: !isCompleteCheck
        }
    ];
}

$(function () {
    //初始化页面传参
    initParams();
    //获取用户列表
    getAllUserList();
    //初始化新增tr
    initNewTr();
    //初始化dataTable列
    initColums();

    //初始化表格( 详细配置 )
    tableBox = initDataTables(
        "tableBox",
        columns,
        createdRow,
        constant.projectURL + '/inventory-head/getPlanDetailsList',
        getSearchConditon,
        0,
        0,
        false,
        false,
        false,
        null,
        function () {
            if (!isCompleteCheck) {
                if (tableBox.data().length == 0) {
                    $("#tableBox tbody tr").remove();
                }
                addTr(false);
            }
        }
    );

    //PDA导入
    $("#btnImport").click(function () {
        //打开选择文件对话框
        $("#fileToUpload").click();
    });
    $("#fileToUpload").change(function () {
        if (this.value) {//是否选择文件
            checkPda();//校验PDA文件
        }
    });

    //打印
    $("#btnPrint").click(function () {
        doPrint();
    });

    //保存
    $("#btnSave").click(function () {
        var list = formSerializeAndCheck(false);// 校验数据
        if (list) {
            openConfirmDialog(// 询问是否保存
                MESSAGE.CONFIRM_SAVE,
                function () { saveInventoryDetails(SysParamConstant.P_CHECK_CHECKING, list); }
            );
        }
    });

    //完成
    $("#btnComplete").click(function () {
        var list = formSerializeAndCheck(true);//校验数据
        if (list) {
            openConfirmDialog(// 询问是否完成
                MESSAGE.CONFIRM_COMPLITE_CHECK,
                function () { saveInventoryDetails(SysParamConstant.P_CHECK_OVER, list); }
            );
        }
    });
});

//打印
var cntPerPage = 23;//每页表格的条数
var doPrint = function () {
    var optSettings = {
        type: "POST",
        url: constant.projectURL + '/inventory-head/doPrint',
        data: {
            "planId": planId
        },
        dataType: "json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.error(MESSAGE.ERROR_PRINT_SEARCH);//打印数据检索失败。
                return;
            }
            if (resp.data == null || resp.data == undefined || resp.data.length == 0) {
                toastr.warning(MESSAGE.WARN_NODATA_PRINT);//没有要打印的数据。
                return;
            }

            //数据总条数
            var totalcnt = resp.data.length;
            //总页数
            var pagecnt = Math.ceil(totalcnt / cntPerPage);

            var printHtml = $("#scrPrint").html().replace("${planDate}", planDate).replace(/\$\{planId\}/g, planId)
                .replace("${barcodePath}", constant.barcodePath);
            $("#divPrint").html("");

            for (var i = 0; i < pagecnt; i++) {
                $("#divPrint").append(printHtml.replace("${pagecnt}", i * cntPerPage).replace("${page}", (i + 1) + "/" + pagecnt));

                var pagedatas = [];
                for (var j = i * cntPerPage; j < (i + 1) * cntPerPage; j++) {
                    if (j < totalcnt) {
                        pagedatas.push(tableBox.data()[j]);
                    } else {
                        pagedatas.push({});
                    }
                }

                //表格初始化
                new Vue({
                    el: $("table[name='tablePrint']:eq(" + i + ")")[0],
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
                }
            }, 1000);
        },
        error: function (resp, textStatus) {
            toastr.error(MESSAGE.ERROR_PRINT_SEARCH);//打印数据检索失败。
        }
    }
    $._ajax(optSettings);
}

/**
 * 初始化页面传参
 */
function initParams() {
    planId = getUrlParam("planId");
    planDate = getUrlParam("planDate");
    planStaus = getUrlParam("status");
    isCompleteCheck = planStaus == SysParamConstant.P_CHECK_OVER;
    $("#labPlanId").html(planId);
    $("#labPlanDate").html(planDate);
    if (isCompleteCheck) {
        $("#btnImport").remove();
        $("#btnSave").remove();
        $("#btnComplete").remove();
        $("#btnAdd").remove();
    }
}


//获取用户列表
function getAllUserList() {
    $._ajax({
        type: 'GET',
        url: constant.projectURL + '/inventory-head/getAllUserList',
        data: {},
        dataType: 'json',
        async: false,
        success: function (resp) {
            planIdSelect = "<select class='checkPerson' oldData='{oldData}'><option value=''></option>";
            if (resp.data) {
                for (i in resp.data) {
                    planIdSelect += "<option value='" + resp.data[i].userCode + "'>" + resp.data[i].userName + "</option>"
                }
            }
            planIdSelect += "</select>";
        },
        error: function () {
            toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION);
        }
    });
};

/**
 * 初始化新增tr
 */
function initNewTr() {
    newTr =
        '<tr role="row" class="odd">'
        + '<td style="text-align: right;">{id}</td>'
        + '<td><input type="text" class="vin justEnglish" maxlength=30 onkeypress="addTrKeyPress(event)" ></td>'
        + '<td></td>'
        + '<td></td>'
        + '<td></td>'
        + '<td>计划外</td>'
        + '<td><input type="text" class="carResult justNumber" style="text-align:right;" maxlength=2></td>'
        + '<td>' + planIdSelect + '</td>'
        + '<td><div class="table-td-btn" style="text-align:center"><i class="del-btn fa fa-trash-o text-danger" title="删除计划外车辆" onclick="delTr(this)"></i></div></td>'
        + '</tr>';
}

/**
 * 取得查询条件
 */
function getSearchConditon() {
    return { 'planId': planId }
}

/**
 * 控制表格式样及事件
 * @param {*} row 行html
 * @param {*} data 行数据
 * @param {*} dataIndex 行号
 */
function createdRow(row, data, dataIndex) {
    if (data.outPlan == "0") {
        $(row).children('td').eq(5).css('text-align', 'right');
    }
    if (isCompleteCheck) {
        $(row).children('td').eq(6).css('text-align', 'right').css('padding-right', '8px');
    }
}

/**
 * 校验PDA文件
 */
function checkPda() {
    //校验文件格式、大小
    if (!validateFile("#fileToUpload", "csv")) {
        $("#uploadForm")[0].reset();
        return;
    }
    //校验表单数据
    var list = formSerializeAndCheck(false);
    if (list) {
        openConfirmDialog(//询问是否导入
            MESSAGE.CONFIRM_PAD_IMPORT,
            function () { pdaUpload(list); },
            function () { $("#uploadForm")[0].reset(); }
        );
    } else {
        $("#uploadForm")[0].reset();
    }
}

/**
 * 上传PDA文件
 * @param {*} list 表单列表
 */
function pdaUpload(list) {
    $.ajaxFileUpload({
        url: constant.projectURL + '/inventory-head/pdaUpload',
        secureuri: false,
        fileElementId: 'fileToUpload',
        dataType: 'text',
        data: {
            "planId": planId,
            "list": JSON.stringify(list)
        },
        success: function (res) {
            if (res == "0") {
                toastr.success(MESSAGE.INFO_IMPORT_SUCCESS);
                tableBox.ajax.reload();
            } else if (res == "-1") {
                toastr.warning(MESSAGE.ERROR_READ_FILE);
            } else {
                toastr.warning(res);
            }
            $("#uploadForm")[0].reset();
        },
        error: function () {
            toastr.error(MESSAGE.ERROR_IMPORT);
        }
    });
}

/**
 * 保存或完成盘点详情
 * @param isComplete 是否完成
 * @param list 表单数据
 */
function saveInventoryDetails(status, list) {
    // 如果数据沒有任何变化，就无需走后台
    if (list.length == 0 && status == planStaus) {
        toastr.success(MESSAGE.INFO_SAVE_SUCCESS);//保存成功
        tableBox.ajax.reload();//刷新表单
        return;
    }

    $._ajax({
        type: 'post',
        url: constant.projectURL + '/inventory-head/savePlanDetails',
        data: {
            'status': status,
            'planId': planId,
            'list': JSON.stringify(list)
        },
        dataType: 'json',
        async: false,
        success: function (resp) {
            if (resp.data && resp.data != 0) {
                toastr.success(MESSAGE.INFO_SAVE_SUCCESS);//保存成功
                if (status == SysParamConstant.P_CHECK_OVER) {//完成盘点
                    location.href = location.href.split("?")[0] + "?planId=" + planId + "&planDate=" + planDate + "&status=03";
                } else {
                    planStaus = SysParamConstant.P_CHECK_CHECKING;
                    tableBox.ajax.reload();//刷新表单
                }
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
 * 表单校验，如果有错误信息，提示错误信息，没有则返回inventoryList
 * @param isComplete 是否是点击的完成
 */
function formSerializeAndCheck(isComplete) {

    //盘点详情列表
    var inventoryList = new Array();
    //表单tr集合
    var trs = $("#tableBox tbody tr");

    //清除错误信息
    hasError = false;
    $("#tableBox .error").removeClass("error");
    vinList = new Array();

    //变量trs
    for (var i = 0; i < trs.size(); i++) {
        var inventory = new Object();//一条盘点详情记录
        var checkList;//盘点信息列表，包括VIN、车辆盘点、[钥匙盘点]、盘点人

        //校验盘点信息
        checkList = validateCheckInfo(trs, i, isComplete);
        //有错误信息或者盘点信息未被修改，就没必要封装inventoryList了
        if (hasError || !checkList) {
            continue;
        }

        inventory.vin = checkList[0];//VIN
        inventory.carResult = checkList[1];//车辆盘点
        inventory.keyResult = checkList[2];//[钥匙盘点]
        inventory.checkPerson = checkList[3];//盘点人
        inventory.operator = operator;//操作人
        inventory.checkTime = null;//盘点时间为服务器当前时间
        inventoryList.push(inventory);//封装list
    }

    //是否有必填项没填，这里跟VIN是否重复分2次弹框了
    if (hasError) {
        toastr.warning(MESSAGE.ERROR_DATA_IS_NULL);
    } else if ($(".vinRepeat").size() > 0) {//是否有重复的VIN
        hasError = true;
        $(".vinRepeat").addClass("error");
        toastr.warning(MESSAGE.ERROR_VIN_REPEAT);
    }
    $(".vinRepeat").removeClass("vinRepeat");

    //错误信息提示
    return hasError ? null : inventoryList;
}

/**
 * 校验盘点信息
 * @param {*} trs 表单tr集合
 * @param {*} index 行号
 * @param {*} isComplete 是否是“完成”
 */
function validateCheckInfo(trs, index, isComplete) {

    var tr = trs.eq(index);
    var isOutPlan = tr.find(".vin").is("input");//是否是计划外车辆
    var newVin = isOutPlan ? tr.find(".vin").val() : tr.find(".vin").text();//新的VIN
    var newCarResult = tr.find(".carResult").val();//新的车辆盘点
    var newCheckPerson = tr.find(".checkPerson").val();//新的盘点人

    //没有填写则过滤掉数据
    if (!newVin && !newCarResult && !newCheckPerson) {
        return null;
    }

    //检测必填项是否都填写了
    var trHasError;
    if (isOutPlan) {//计划外车辆验证
        trHasError = !(newVin && newCarResult && newCheckPerson);
    } else {//计划内车辆验证
        trHasError = (!newCarResult ^ !newCheckPerson) || (isComplete && !newCarResult);
    }

    //没填的必填项标红
    if (trHasError) {
        hasError = true;
        if (!newVin) {
            tr.find(".vin").addClass("error");
        }
        if (!newCarResult) {
            tr.find(".carResult").addClass("error");
        }
        if (!newCheckPerson) {
            tr.find(".checkPerson").addClass("error");
        }
    }

    var oldVin = tr.find(".vin").attr("oldData");//旧的VIN
    var oldCarResult = tr.find(".carResult").attr("oldData");//旧的车辆盘点
    var oldCheckPerson = tr.find(".checkPerson").attr("oldData");//旧的盘点人

    //如果没有修改数据，则无需验证，也无需发送给后台
    if (
        newVin == oldVin
        && newCarResult == oldCarResult
        && newCheckPerson == oldCheckPerson
    ) {
        return null;
    }

    //检测VIN码是否重复，感觉还是一轮for循环比较快，这里新定义了一个错误class：vinRepeat
    for (var i = index - 1; i >= 0; i--) {
        var vinClass = trs.eq(i).find(".vin");
        var oldVin = vinClass.is("input") ? vinClass.val() : vinClass.text();
        if (newVin == oldVin) {
            tr.find(".vin").addClass("vinRepeat");
            vinClass.addClass("vinRepeat");
            break;
        }
    }

    //如果有错误返回null，否则返回盘点列表：VIN、车辆盘点、[钥匙盘点]、盘点人
    return hasError ? null : [newVin, newCarResult, null, newCheckPerson];
}


/**
 * 新增一行tr
 * @param {*} ischeck 是否检查空行
 */
function addTr(ischeck) {
    var index = $("#tableBox tbody tr").size();
    //初始化页面或刷新页面时无需检查空行
    if (!ischeck) {
        $("#tableBox tbody").append(newTr.replace("{id}", index + 1));
        return;
    }
    //检查是否有空行
    var inputVins = $("input.vin");
    for (var i = 0; i < inputVins.size(); i++) {
        if (!inputVins.eq(i).val()) {
            inputVins.eq(i).focus();
            toastr.warning(MESSAGE.ERROR_VIN_IS_NULL);
            return;
        }
    }
    $("#tableBox tbody").append(newTr.replace("{id}", index + 1));
    $(".vin:last").focus();
}

/**
 * 回车新增一行tr
 */
function addTrKeyPress(event) {
    if (event.keyCode == "13") {
        addTr(true);
    }
}

/**
 * 删除计划外车辆，要删数据库信息
 * @param {*} obj 要删除的行
 */
function delDataBase(obj) {
    openConfirmDialog(MESSAGE.CONFIRM_DELETE, function () {
        $._ajax({
            type: 'post',
            url: constant.projectURL + '/inventory-head/delPlanDetails',
            data: {
                'planId': planId,
                'vin': $(obj).parent().parent().parent().find(".vin").text()
            },
            dataType: 'json',
            success: function () {
                //删除成功
                toastr.success(MESSAGE.INFO_DELETE_SUCCESS);
                delTr(obj);
            },
            error: function () {
                toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION);
            }
        });
    });
}

/**
 * 删除计划外车辆，不走数据库
 * @param {*} obj 要删除的行
 */
function delTr(obj) {
    var tr = $(obj).parent().parent().parent();
    //如果只剩1行输入行了，则只清空数据
    if (tr.find("input.vin").size() == 1 && $("input.vin").size() == 1) {
        tr.find("input,select").val("");
    } else {
        var index = tr.find("td").eq(0).text() - 1;
        //页面删除
        tr.remove();
        //重新编号
        var trs = $("#tableBox tbody tr");
        for (var i = index; i < trs.size(); i++) {
            trs.eq(i).find("td").eq(0).text(i + 1);
        }
    }
}

/**
 * 错误信息3秒自动关闭
 */
// function closeTimeOut(){
//     clearTimeout(errorTimeOut);
//     errorTimeOut = setTimeout('$("#tableBox .error").removeClass("error")',6000);
// }

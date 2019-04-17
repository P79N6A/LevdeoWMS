//列名 列ID 是否增加排序 列宽 是否可检索
columnsLeft = [
    { title: "名称", data: "subName", orderable: false, searchable: false }

];
//默认
columnDefault = [
    { title: "编码", data: "code", orderable: false, searchable: false, sWidth: '40%' },
    { title: "名称", data: "name", orderable: false, searchable: false, sWidth: '60%' }
];
//客户名称
columnsCustomers = [
    { title: "客户编码", data: "code", orderable: false, searchable: false },
    { title: "客户名称", data: "name", orderable: false, searchable: false },
    { title: "客户电话", data: "tel", orderable: false, searchable: false },
    { title: "客户地址", data: "addr", orderable: false, searchable: false }
];
//销售部门
columnSaleCompany = [
    { title: "销售部门编码", data: "code", orderable: false, searchable: false },
    { title: "销售部门名称", data: "name", orderable: false, searchable: false },
    { title: "销售公司编码", data: "pCode", orderable: false, searchable: false },
    { title: "销售公司名称", data: "pName", orderable: false, searchable: false }
];

/**
 * 取得查询条件
 */
function getSearchConditon() {
    return {
        "subCode": $("#paramLabel").data("subCode")
    }
}
var myform = new Vue({

    el: "#myform",
    data: {
        lbCode: "",//第一个label标签
        mainCode: "",//第一个文本框内容
        lbName: "",//第二个label标签
        mainName: "",//第二个文本框内容
        showMode1: true,//控制文本框与标签切换
        lbSubCode: "",//第三个label标签
        mainSubCode: "",//第三个文本框内容
        lbSubName: "",//第四个label标签
        mainSubName: "",//第四个文本框内容
        showMode2: false,//控制第二，三div显示或隐藏  （只有客户与销售部门需要显示）
        showMode3: false//控制第三个文本框与标签切换 （只有销售部门需要显示）
    }
});

//判断是否为更新模式
/**
 * 1为新建模式
 * 0为更新模式
 */
var dialogState = 0;

var table;
/**
 * 主数据类型-控制表格式样及事件
 * @param {*} row 
 * @param {*} data 
 * @param {*} dataIndex 
 */
function createdRow(row, data, dataIndex) {

    //行点击事件
    $(row).on("click", function () {

        //行改变颜色
        $("tr").removeClass("myActive");
        $(this).addClass("myActive");
        //赋值
        $("#paramLabel").text(data.subName);
        $("#paramLabel").data("subCode", data.subCode);
        $("#mainInfoTitle").text(data.subName + "详情");
        if (data.subCode == SysParamConstant.P_MAIN_CUSTOMERS) {

            table.destroy();
            $("#tableBoxFunc").empty()

            table = createTable(columnsCustomers);
        } else if (data.subCode == SysParamConstant.P_MAIN_SALE_COMPANY) {
            table.destroy();
            $("#tableBoxFunc").empty()
            table = createTable(columnSaleCompany);
        } else {
            table.destroy();
            $("#tableBoxFunc").empty()
            table = createTable(columnDefault);
        }

    });
}

//打开弹出层
$('#openDialog').click(function () {
    btnCreate(1, null);
});

//清空弹出层内容
var clearText=function(){
    myform.mainCode="";
    myform.mainName="";
    myform.mainSubCode="";
    myform.mainSubName="";
}
/**
 * 新建按钮-弹出modal
 */
function btnCreate(state,datas) {
   
    
   
    if ($("#paramLabel").data("subCode") != undefined && $("#paramLabel").data("subCode") != "") {
        $('#mainDateType').text($("#paramLabel").text());
        $('#modalCreate').modal("show");
        $('#modalCreate').on('shown.bs.modal', function () {
            $("input:eq(0)", this).focus();
        })
        //新增执行以下代码
        if (state == 1) {
           // datas=null;
           clearText();
           $("#lbSt").addClass("myRequired");//增加code验证
           $("#lbRd").addClass("myRequired");//增加subcode验证
           $("#tName").val("添加");//设置弹出层的值
            //显示modal-输入框获取焦点
            if ($("#paramLabel").data("subCode") == SysParamConstant.P_MAIN_CUSTOMERS) {//当code为客户信息时

                myform.lbCode = "客户编码";
                myform.lbName = "客户名称";
                myform.lbSubCode = "客户电话";
                myform.lbSubName = "客户地址";
                myform.showMode1 = true;
                myform.showMode2 = true;
                myform.showMode3 = true;


            } else if ($("#paramLabel").data("subCode") == SysParamConstant.P_MAIN_SALE_COMPANY) {//当code为销售部时

                myform.lbCode = "销售部门编码";
                myform.lbName = "销售部门名称";
                myform.lbSubCode = "销售公司编码";
                myform.lbSubName = "销售公司名称";
                myform.showMode2 = true;
                myform.showMode3 = true;
                myform.showMode1 = true;
            } else {
                myform.lbCode = "编码";
                myform.lbName = "名称";
                myform.showMode1 = true;
                myform.showMode2 = false;
            }

            //修改执行以下代码
        } else {
            $("#tName").html("修改");
            $("#lbSt").removeClass("myRequired");//code不能修改 取消code表单验证
            //显示modal-输入框获取焦点
            if ($("#paramLabel").data("subCode") == SysParamConstant.P_MAIN_CUSTOMERS) {//当code为客户信息时

                $("#lbRd").addClass("myRequired");//增加subcode验证
                myform.lbCode = "客户编码";
                myform.lbName = "客户名称";
                myform.lbSubCode = "客户电话";
                myform.lbSubName = "客户地址";
                myform.mainCode = datas.code;
                myform.mainName = datas.name;
                myform.mainSubCode = datas.tel;
                myform.mainSubName = datas.addr;
                myform.showMode1 = false;
                myform.mainCode = datas.code;
                myform.mainName = datas.name;
                myform.showMode2 = true;
                myform.showMode3 = true;

            } else if ($("#paramLabel").data("subCode") == SysParamConstant.P_MAIN_SALE_COMPANY) {//当code为销售部时

                $("#lbRd").removeClass("myRequired");//销售部code不能修改 取消subcode表单验证
                myform.lbCode = "销售部门编码";
                myform.lbName = "销售部门名称";
                myform.lbSubCode = "销售公司编码";
                myform.lbSubName = "销售公司名称";
                myform.showMode1 = false;//将第一个输入框变成标签，表示不允许修改
                myform.showMode2 = true;//显示第二个文本框 及三，四，div
                myform.showMode3 = false;//不允许修改公司编码 否则会找不到公司印章
                myform.mainCode = datas.code;
                myform.mainName = datas.name;
                myform.mainSubCode = datas.pCode;
                myform.mainSubName = datas.pName;
            } else {
                myform.lbCode = "编码";
                myform.lbName = "名称";
                myform.mainCode = datas.code;
                myform.mainName = datas.name;
                myform.showMode1 = false;
                myform.showMode2 = false;
                myform.showMode3 = false;
                

            }

        }
        dialogState=state;
    } else {
        toastr.warning(MESSAGE.WARN_CHOOSE_MAINDATA)
    }


}

/**
 * 新建modal-确定按钮
 */
function mdlCreateSure(state) {
    var formFlg = $(".form-id").validationEngine("validate");
    if (!formFlg) {
        return;
    }
    if ($("#paramLabel").data("subCode") == SysParamConstant.P_MAIN_CUSTOMERS || $("#paramLabel").data("subCode") == SysParamConstant.P_MAIN_SALE_COMPANY) {
        data = {
            "tbName": $("#paramLabel").data("subCode"),
            "code": myform.mainCode,
            "name": myform.mainName,
            "subCode": myform.mainSubCode,
            "subName": myform.mainSubName
        }
    } else {
        data = {
            "tbName": $("#paramLabel").data("subCode"),
            "code": myform.mainCode,
            "name": myform.mainName
        }
    }

    var optSettings = {
        "url": state == 1 ? constant.projectURL + '/mainData/addNewMainData' : constant.projectURL + '/mainData/updateMainData',
        "data": JSON.stringify(data),
        "type": "POST",
        "dataType": "json",
        "contentType": "application/json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION);
                return;
            }
            if (resp.data != '01001' && resp.data != '01002') {
                $('#modalCreate').modal('hide');
                $("#iptNewRoleName").val("");
                $("#paramLabel").text("").data("roleId", "");
                toastr.success(state==1? MESSAGE.INFO_ADD_MAINDATA_SUCCESS:MESSAGE.INFO_CHANGE_MAINDATA_SUCCESS);
                if (resp.data === SysParamConstant.P_MAIN_CUSTOMERS) {

                    table.destroy();
                    $("#tableBoxFunc").empty()

                    table = createTable(columnsCustomers);
                } else if (resp.data == SysParamConstant.P_MAIN_SALE_COMPANY) {
                    table.destroy();
                    $("#tableBoxFunc").empty()
                    table = createTable(columnSaleCompany);
                } else {
                    table.destroy();
                    $("#tableBoxFunc").empty()
                    table = createTable(columnDefault);
                }
            } else {
                if (resp.data == "01001") {
                    toastr.warning(MESSAGE.WARN_MAINDATA_CODE_REPEAT);
                } else if (resp.data == "01002") {
                    toastr.warning(MESSAGE.WARN_MAINDATA_NAME_REPEAT);
                }
            }


        },
        error: function (resp, textStatus) {
            toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION);
        }
    }
    $._ajax(optSettings);
}

$(function () {

    table = createTable(columnDefault);
    $(".form-id").validationEngine("attach", {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        isOverflown: true,
    });

    //初始化-主数据
    tableBox = initDataTables(
        "tableBox",
        columnsLeft,
        createdRow,
        constant.projectURL + '/mainData/getMainDataList',
        function () { },
        0, 0, false, false, false
    );

   
});

$("#btnSave").click(function(){
    mdlCreateSure(dialogState);
});

//公共方法，多次初始化调用
function createTable(columns) {


    return $("#tableBoxFunc").DataTable({
        ajax: {
            url: constant.projectURL + '/mainData/getMainDataInfo',
            data: {
                "subCode": $("#paramLabel").data("subCode")
            }
        },
        //因为需要多次初始化，所以需要设置允许销毁实例
        "destroy": true,
        //列的配置信息通过变量传进来
        "columns": columns,
        //启用服务器端分页
        serverSide: false,
        //表格搜索总开关
        searching: false,
        paging: false,
        info: false,
        ordering: false,
        //允许横向滚动
        scrollX: true,
        scrollY: document.body.clientHeight - $(".page-header").height() - $("#myHeader").height() - 182,
        createdRow: function (row, data, dataIndex) {
            $(row).dblclick(function () {
                    btnCreate(0,data);
            });
        }
    });
}

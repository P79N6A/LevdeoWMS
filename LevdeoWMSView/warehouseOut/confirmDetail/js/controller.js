//当前出库单号
var thisOutBoundCode = getUrlParam("sapInvoiceCode");
//确认模式： 3：宣传品；2：配件
var mode = getUrlParam("mode");

//列名 列ID 是否增加排序 列宽 是否可检索
columns = [
    { title: "VIN码", data: "vin", orderable: true, sWidth: 100, searchable: true, name: "vin" },
    { title: "出库仓库", data: "productStock", orderable: true, sWidth: 80, searchable: true, name: "productStock" },
    { title: "物料编码", data: "matCode", orderable: true, sWidth: 100, searchable: true, name: "matCode" },
    { title: "物料描述", data: "matName", orderable: true, searchable: true, name: "matName" },
    { title: "数量", data: "carNumber", orderable: true, sWidth: 50, className: "alignRight", searchable: true, defaultContent: 1, name: "" },
    { title: "库位号", data: "stockPosition", orderable: true, sWidth: 50, searchable: true, name: "stockPosition" },
];

//控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
function createdRow(row, data, dataIndex) {
}
/**
 * 当前出库单单号
 */
function getSearchConditon() {
    return {
        "thisOutBoundCode": thisOutBoundCode
    }
}
var myBreadcrumb = new Vue({
    el: "#myBreadcrumb",
    data: {
        pageName: mode == "2" ? "随车配件确认" : "随车宣传品确认",
    }
});

//
var myHeaderText = new Vue({
    el: "#myHeader",
    data: {
        customerName: '',
        outBoundCode: '',
        shipTypeName: '',
        contactInformation: '',
        sapInvoiceCode: '',
        logisticsParking: '',
        customerAddress: '',
        giftName: '',
        logisticsVehicleNo: '',
        giftChecker: '',
        proChecker: '',
        logisCompanyName: '',
        showPc: false,
        showFc: false,
        pageName: mode == "2" ? "随车配件确认" : "随车宣传品确认",
    },
    methods: {
        //确认按钮事件-status=01-gift 01
        doConfirmPc: function () {
            openConfirmDialog(MESSAGE.CONFIRM_SAVE, function () {
                var textAjax = {
                    type: 'POST',
                    url: constant.projectURL + '/outInvoiceDetails/updateProStatus',
                    data: getSearchConditon(),
                    datatype: 'json',
                    success: function (resp) {
                        toastr.success(MESSAGE.INFO_SAVE_SUCCESS);
                        myHeaderInit();
                        tableBox.ajax.reload();
                    }
                }
                $._ajax(textAjax);
            });
        },
        //确认按钮事件-status=01-gift 01
        doConfirmFc: function () {
            openConfirmDialog(MESSAGE.CONFIRM_SAVE, function () {
                var textAjax = {
                    type: 'POST',
                    url: constant.projectURL + '/outInvoiceDetails/updateGiftStatus',
                    data: getSearchConditon(),
                    datatype: 'json',
                    success: function (resp) {
                        toastr.success(MESSAGE.INFO_SAVE_SUCCESS);
                        myHeaderInit();
                        tableBox.ajax.reload();
                    }
                }
                $._ajax(textAjax);
            });
        }
    }
});

//赋值-myHeader
var myHeaderInit = function () {
    var textAjax = {
        type: 'POST',
        url: constant.projectURL + '/outInvoiceDetails/getThisOutBoundOrder',
        data: getSearchConditon(),
        datatype: 'json',
        async: false,
        success: function (resp) {
            //Vue-myHeader给值
            myHeaderText.customerName = resp.data[0].customerName;
            myHeaderText.outBoundCode = resp.data[0].outBoundCode;
            myHeaderText.shipTypeName = resp.data[0].shipTypeName;
            myHeaderText.contactInformation = resp.data[0].contactInformation;
            myHeaderText.sapInvoiceCode = resp.data[0].sapInvoiceCode;
            myHeaderText.logisticsParking = resp.data[0].logisticsParking;
            myHeaderText.customerAddress = resp.data[0].customerAddress;
            myHeaderText.giftName = resp.data[0].giftName;
            myHeaderText.logisticsVehicleNo = resp.data[0].logisticsVehicleNo;
            myHeaderText.proChecker = resp.data[0].proChecker;
            myHeaderText.giftChecker = resp.data[0].giftChecker;
            myHeaderText.logisCompanyName = resp.data[0].logisCompanyName;
            myHeaderText.showPc = mode == "2" || resp.data[0].proStatus == "1" ? false : true;//宣传品确认按钮
            myHeaderText.showFc = mode == "3" || resp.data[0].giftStatus == "1" ? false : true;//配件确认按钮
        }
    }
    return $._ajaxd(textAjax);
}

$(document).ready(function () {
    //初始化myHeader
    myHeaderInit();

    myHeaderText.$nextTick(function () {
        //初始化表格( 详细配置 )
        tableBox = initDataTables(
            "tableBox",
            columns,
            createdRow,
            constant.projectURL + '/outInvoiceDetails/getThisOutBoundVIN',
            getSearchConditon,
            1, 0,
            false,
            false,
            false
        );
    });
    
    addReturnButton();
})

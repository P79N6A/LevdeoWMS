columns = [ //显示的列(title:列名,data:数据key)
    { title: "入库交接单", data: "wsTransition", orderable: true, sWidth: 100, searchable: true, name: "w.wsTransition" },
    { title: "物料编码", data: "matCode", orderable: true, sWidth: 100, searchable: true, name: "w.matCode" },
    { title: "物料描述", data: "matDescription", orderable: true, searchable: true, name: "matDescription" },
    { title: "VIN码", data: "vin", orderable: true, sWidth: 100, searchable: true, name: "w.vin" },
    { title: "完工日期", data: "finishedTime", orderable: true, sWidth: 80, searchable: true, name: "w.finishedTime" },
    { title: "状态", data: "state", orderable: true, sWidth: 50, searchable: true, name: "state" },
    { title: "库位", data: "stockPosition", orderable: true, sWidth: 50, searchable: true, name: "w.stockPosition" },
    { title: "SAP过账", data: "sapTransfer", orderable: true, sWidth: 50, searchable: true, name: "sapTransfer" },
    { title: "成品库", data: "productStockName", orderable: true, searchable: true, name: "productStockName" },
    {
        //额外表格操作列
        title: "操作",
        sWidth: 50,
        data: function (row) {
            var btn = ""
            if (row.stateCode == SysParamConstant.P_CAR_STATUS_PREWAREHOUSEIN) {
                btn = '<div class="table-td-btn" style="text-align:center">' +
                    '  <i class="edit-btn fa fa-pencil text-primary"  title="修改库位号"></i>' +
                    '  <i class="del-btn fa fa-trash-o text-danger" title="删除入库交接单"></i>' +
                    '</div>';
            }
            return btn;
        },
        orderable: false
    }];

/**
 * 
 * @param {*} row 
 * @param {*} data 
 * @param {*} dataIndex 
 */
function createdRow(row, data, dataIndex) {

    //换库
    $(row).find('i.edit-btn').click(function () {
        $._ajax({
            url: constant.projectURL + '/Warehousing/getStockPosition',
            type: "GET",
            data: { 'vin': data.vin, spFlg: 1 },
            dataType: "json",
            success: function (resp) {
                if (resp.data != null) {
                    modifySpVue.formData = resp.data.formData;
                    modifySpVue.spList = resp.data.spList;
                    $('#updateStockPosition').modal('show');
                } else {
                    toastr.warning(MESSAGE.ERROR_SERVER_EXCEPTION);
                }
            }
        });
    });
    //删除按钮绑定事件
    $(row).find('i.del-btn').click(function () {
        openConfirmDialog(MESSAGE.CONFIRM_DELETE, function () {
            delWarehousing(data.vin);
        });
    });
}

/**
 * 查询条件
 */
function getSearchConditon() {
    return {
        "finishedTime": $("#ipt_finished").val(),
        "vin": $("#ipt_vin").val().toUpperCase(),
        "state": $("#sel_state").val(),
        "sapTransfer": $("#sel_sap").val()
    }
}

/**
 * 
 */
var downloadExcel = function () {
    //form添加action
    //getSearchConditon
    $("#myHeader form").attr("action", constant.projectURL + "/Warehousing/exportWsExcel");
    $("#myHeader form")._submit();
}

/**
 * 更换库位
 * @param {*} vin 
 * @param {*} position
 */
function updatePosition(vin, position) {
    $._ajax({
        url: constant.projectURL + '/Warehousing/updateStockPosition',
        type: "POST",
        data: {
            "vin": vin,
            "stockPosition": position
        },
        success: function (resp) {
            //todo-验证
            if (resp.code == "0") {
                $('#updateStockPosition').modal('hide');
                toastr.success(MESSAGE.INFO_UPDATE_SUCCESS);
                tableBox.ajax.reload();
            } else {
                toastr.warning(resp.msg);
            }
        }
    });
};


/**
 * 删除
 * @param {*} vin 
 */
function delWarehousing(vin) {
    $._ajax({
        url: "/LevdeoWMSWebService/Warehousing/delWarehousing",
        type: "GET",
        data: { 'vin': vin },
        dataType: "json",
        success: function (resp) {
            if (resp.data != null && resp.data == "2000") {
                toastr.success(MESSAGE.INFO_DELETE_SUCCESS);
                tableBox.ajax.reload();
            } else {
                toastr.warning(MESSAGE.ERROR_WAREHOUSE_DEL_FAILED);
            }
        }
    });
}

$(function () {

    /**
     * modal
     */
    $('#updateStockPosition').on('shown.bs.modal', function () {
        //表单验证初始化
        $("#updateStockPosition form").validationEngine("attach", {
            //只在表单提交的时候验证
            validationEventTrigger: null,
            isOverflown: true,
        });
    }).on('hidden.bs.modal', function () {
        $("#updateStockPosition form").validationEngine("hideAll");
    })

    //检索入库一览
    $("#btnup").on('click', function () {
        tableBox.ajax.reload();
    });

    //导出到EXCEL文件
    $("#btnExport").on('click', function () {
        downloadExcel()
    });

    //初始化入库一览表
    tableBox = initDataTables(
        "tableBox",
        columns,
        createdRow,
        constant.projectURL + '/Warehousing/wsInfo',
        getSearchConditon,
        1,
        1, false, true, true,
        [1, "desc"]);

    //
    modifySpVue = new Vue({
        el: "#updateStockPosition .modal-dialog",
        data: {
            formData: {},
            spList: []
        },
        methods: {
            saveNewSp: function () {
                //表单验证
                var formFlg = $("#updateStockPosition form").validationEngine("validate");
                if (!formFlg) {
                    return;
                }
                var vin = this.formData.vin;
                //新库位
                var position = this.formData.selectedSp;
                updatePosition(vin, position);
            }
        }
    })
})  
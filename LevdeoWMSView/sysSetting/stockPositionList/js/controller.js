//列名 列ID 是否增加排序 列宽 是否可检索
var columns = [
    { title: "库位编号", data: "positionId",name: "positionId" },
    { title: "库位名称", data: "position", name: "position" },
    { title: "仓库名称", data: "warehouse", name: "warehouse" },
    { title: "库区名称", data: "stockArea", name: "stockArea" },
    { title: "库区类型", data: "flagName", name: "stockArea" },
    { title: "状态", data: "ssubName", name: "sys.subName"  },
    { title: "warehouseId", data: "warehouseId",visible: false },
    { title: "stockAreaCode", data: "stockAreaCode",visible: false },
    { title: "锁定",data:"isLock",name:"isLock" },
    { title: "锁定理由", data: "lockReason",name: "lockReason" }
];

$(function () {

    //初始化下拉菜单
    initSelect();

    //初始化表格( 详细配置 )
    tableBox = initDataTables(
        "tableBox",
        columns,
        createdRow,
        constant.projectURL + '/sys-stock-position/getPositionList',
        getSearchConditon,
        0,
        0,
        false,
        true,
        true);
    $("#btnSearch").on("click", function () {
        tableBox.ajax.reload();
    }); 
});

/**
 * 取得查询条件
 */
function getSearchConditon() {
    return {
        'warehouse': $('#selWarehouse').val(),
        'stockArea': $('#selStockArea').val(),
        'spUseStatus': $('#selUseStatus').val(),
        'spLockStatus': $('#selLockStatus').val(),
    }
}
/**
 * 控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
 * @param {*} row 
 * @param {*} data 
 * @param {*} dataIndex 
 */
function createdRow(row, data, dataIndex) {
    //第一列右对齐
    $(row).children('td').eq(0).css('text-align', 'right');

    //双击行，跳转到详情页面
    $(row).dblclick(function(){
        if(data.flagName == "临时库区"){
            return;
        }
        window.location.href = '../stockPositionDetail/index.html?stockPosition=' + data.positionId
            + "&warehouse=" + data.warehouseId
            + "&stockAreaCode=" + data.stockAreaCode
            + "&stockArea=" + data.stockArea
            + "&position=" + data.position
            + "&isUse=" + data.ssubName
            + "&isLock=" + data.isLock
            + "&lockReason=" + data.lockReason;
    });
}

/**
 * 初始化下拉菜单
 */
function initSelect(){
    $._ajax({
        type: 'GET',
        url: constant.projectURL + '/sys-stock-position/getWarehouse',
        data: {},
        dataType: 'json',
        success: function (resp, textStatus) {
           $("#selWarehouse").html('<option></option>');
           if(resp.data){
            for(i in resp.data){
                $("#selWarehouse").append('<option value="'+resp.data[i].warehouseCode+'">'+resp.data[i].name+'</option>');
            }
           }
           $("#selWarehouse").change(function(){
               var warehouseCode = this.value;
               $._ajax({
                type: 'GET',
                url: constant.projectURL + '/sys-stock-position/getStockArea',
                data: {
                    "warehouseCode" : warehouseCode
                },
                dataType: 'json',
                success: function (resp, textStatus) {
                   $("#selStockArea").html('<option></option>');
                   if(resp.data){
                    for(i in resp.data){
                        $("#selStockArea").append('<option value="'+resp.data[i].stockAreaCode+'">'+resp.data[i].name+'</option>');
                    }
                   }
                },
                error: function (resp, textStatus) {
                    toastr.error(MESSAGE.ERROR_SEARCHING_FAIL);
                }
            });
           })
        },
        error: function (resp, textStatus) {
            toastr.error(MESSAGE.ERROR_SEARCHING_FAIL);
        }
    });
}

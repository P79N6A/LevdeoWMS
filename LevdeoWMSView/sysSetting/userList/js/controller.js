
//列名 列ID 是否增加排序 列宽 是否可检索
columns = [
    { title: "作业员编号", data: "userCode", orderable: true, searchable: true, name: "s.userCode" },
    { title: "作业员姓名", data: "userName", orderable: true, searchable: true, name: "s.userName" },
    { title: "电话", data: "phone", orderable: true, searchable: true, name: "s.phone" },
    { title: "角色", data: "roleName", orderable: true, searchable: true, name: "r.roleName" },
    { title: "状态", data: "ustatus", orderable: true, searchable: true, name: "p.subName" },
    { title: "仓库", data: "warehouseName", orderable: true, searchable: true, name: "w.name" },
    { title: "SAP账号", data: "sapUserCode", orderable: true, searchable: true, name: "s.sapUserCode" },
];
/**
 * 初始化
 */
$(document).ready(function () {
    //初始化表格( 详细配置 )
    tableBox = initDataTables(
        "tableBox",
        columns,
        createdRow,
        constant.projectURL + '/sys-user/getOperateInfo',
        getSearchConditon,
        0, 0,
        false,
        true,
        true);
    $("#btsearch").on("click", function () {
        tableBox.ajax.reload();
    });
})
/**
 * 取得查询条件
 */
function getSearchConditon() {
    return {
        "userCode": $("#operateDept").val().toUpperCase(),
        "userName": $("#operateName").val().toUpperCase(),
        "roleName": $("#operateRole").val()
    }
}
/**
 * 控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
 * @param {*} row 
 * @param {*} data 
 * @param {*} dataIndex 
 */
function createdRow(row, data, dataIndex) {
    //双击跳转页面
    $(row).dblclick(function () {
        window.location.href = encodeURI("../userDetail/index.html?userCode=" + data.userCode);
    });
}

/**
 * 新建用户
 */
function createUser() {
    location.href = '../userDetail/index.html';
}

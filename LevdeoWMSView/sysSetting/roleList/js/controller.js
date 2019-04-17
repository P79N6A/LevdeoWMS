//列名 列ID 是否增加排序 列宽 是否可检索
columnsLeft = [
    { title: "角色名", data: "roleName", orderable: false, searchable: false, name: "roleName" },
    {
        title: "操作",
        sWidth: 50,
        data: function () {
            var btn = '<div class="table-td-btn" style="text-align:center">' +
                '  <i class="edit-btn fa fa-pencil text-primary" title="修改角色"></i>' +
                '  <i class="del-btn fa fa-trash-o text-danger" title="删除角色"></i>' +
                '</div>';
            return btn;
        },
        orderable: false
    }
];
//列名 列ID 是否增加排序 列宽 是否可检索
columnsRight = [
    { title: "功能名", data: "groFunctionName", orderable: false, searchable: false, name: "groFunctionName" }
];

/**
 * 取得查询条件
 */
function getSearchConditon() {
    return {
        "roleId": $("#roleIdLabel").data("roleId")
    }
}

/**
 * 角色一览-控制表格式样及事件
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
        $("#roleIdLabel").text(data.roleName);
        $("#roleIdLabel").data("roleId", data.roleId);
        tableBoxFunc.ajax.reload();
    });

    //修改角色
    $(row).find('i.edit-btn').click(function () {
        //updateRoleName(data.roleId, data.roleName);
        //得到需要更改的角色id
        $("#roleIdLabel").data("roleId", data.roleId);
        $('#updiv').html(data.roleName);
        //弹出modal
        $('#modalUpRole').modal("show");
        //输入框获取焦点
        $('#modalUpRole').on('shown.bs.modal', function () {
            $("input:eq(0)", this).focus();
        })
    });

    //删除角色
    $(row).find('i.del-btn').click(function () {
        //deleteRole(data.roleId);
        //弹出确认框-确定按钮,调用后台执行删除
        openConfirmDialog(MESSAGE.CONFIRM_DELETE, function () {
            $._ajax({
                "url": constant.projectURL + '/sys-role/delList',
                "data": "roleId=" + data.roleId,
                "type": "POST",
                "dataType": "json",
                success: function (resp) {
                    if (resp.code == "0") {
                        toastr.success(MESSAGE.INFO_DELETE_SUCCESS);
                        $("#roleIdLabel").text("").data("roleId", "");
                        tableBox.ajax.reload();
                        tableBoxFunc.ajax.reload();
                    } else {
                        toastr.error(resp.msg);
                    }
                }
            })
        })
    });
}

/**
 * 修改角色名modal-确认按钮
 */
function updateRoleName() {
    var formFlg = $(".form-id").validationEngine("validate");
    if (!formFlg) {
        return;
    }
    data = {
        "roleName": $("#updateInput").val().trim(),
        "roleId": $("#roleIdLabel").data("roleId")
    }
    $._ajax({
        "url": constant.projectURL + '/sys-role/updateList',
        "data": JSON.stringify(data),
        "type": "POST",
        "dataType": "json",
        "contentType": "application/json",
        success: function (resp) {
            if (resp.code == "0") {
                $("#updateInput").val("");
                $('#modalUpRole').modal('hide');
                toastr.success(MESSAGE.INFO_UPDATE_SUCCESS);
                $("#roleIdLabel").text("").data("roleId", "");
                tableBox.ajax.reload();
            } else {
                toastr.error(resp.msg);
            }
        }
    });
}

/**
 * 新建按钮-弹出modal
 */
function btnCreate() {
    //显示modal-输入框获取焦点
    $('#modalCreate').modal("show");
    $('#modalCreate').on('shown.bs.modal', function () {
        $("input:eq(0)", this).focus();
    })
}

/**
 * 新建modal-确定按钮
 */
function mdlCreateSure() {
    var formFlg = $(".form-id").validationEngine("validate");
    if (!formFlg) {
        return;
    }
    data = {
            "roleName": $("#iptNewRoleName").val().trim()
        }

    var optSettings = {
        "url": constant.projectURL + '/sys-role/regList',
        "data": JSON.stringify(data),
        "type": "POST",
        "dataType": "json",
        "contentType": "application/json",
        success: function (resp, textStatus) {
            if (textStatus != "success") {
                toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION);
                return;
            }
            $('#modalCreate').modal('hide');
            $("#iptNewRoleName").val("");
            $("#roleIdLabel").text("").data("roleId", "");
            toastr.success(MESSAGE.INFO_CREATE_SUCCESS);
            tableBox.ajax.reload();
            tableBoxFunc.ajax.reload();

        },
        error: function (resp, textStatus) {
            toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION);
        }
    }
    $._ajax(optSettings);
}

/**
 * 保存按钮
 * 更改角色对应的功能
 */
function btnSave() {
    if (!$("#roleIdLabel").text()) {
        toastr.warning("请选择角色。");
        return;
    }

    var num = [];
    var dataFunc = tableBoxFunc.data();
    for (var a = 0; a < dataFunc.length; a++) {
        if (dataFunc[a].checked) {
            num.push(dataFunc[a].functionId);
        }
    }
    if (num.length == 0) {
        toastr.info("请选择功能。");
    } else {
        openConfirmDialog(MESSAGE.CONFIRM_SAVE, function () {
            var optSettings = {
                type: 'GET',
                url: constant.projectURL + '/sys-role-function/updateConnectionTable',
                data: {
                    "newRoleId": $("#roleIdLabel").data("roleId"),
                    "num": num.join(",")
                },
                dataType: 'json',
                success: function (resp, textStatus) {
                    if (resp.code == "0") {
                        toastr.success('保存成功');
                    } else {
                        toastr.error(resp.msg);
                    }
                }
            }
            $._ajax(optSettings);
        });
    }
}

$(document).ready(function () {

    //初始化绑定表单验证(验证关联主要为控件的class validate)  
    $(".form-id").validationEngine("attach", {
        //只在表单提交的时候验证
        validationEventTrigger: null,
        //错误提示距离顶部偏移量
        scrollOffset: 100,
        isOverflown: true,
    });

    //初始化-角色一览表
    tableBox = initDataTables(
        "tableBox",
        columnsLeft,
        createdRow,
        constant.projectURL + '/sys-role/getPlanList',
        function () { },
        0, 0, false, false, false
    );

    //初始化-功能一览表
    tableBoxFunc = initDataTables(
        "tableBoxFunc",
        columnsRight,
        function () { },
        constant.projectURL + '/sys-role-function/getFunctionList',
        getSearchConditon,
        0, 0, true, false, false
    );

    //关闭modal-清空输入框内容,清空validate验证信息
    $('*[data-dismiss="modal"]').on("click", function () {
        $('*[type="text"]').val("");
        //隐藏validate验证信息
        $("#modalCreate form").validationEngine("hideAll");
        $("#modalUpRole form").validationEngine("hideAll");
    });
});
//页面vue对象
var contentVm = new Vue({
  el: "#myContent",
  data: {
    formdata: {},
    userCodeDisabled: false,
  }
});
var footerVm = new Vue({
  el: "#myFooter",
  data: {
    showReset: false
  },
  methods: {
    //保存
    saveUser: function () {
      //表单验证
      if (!$("#myContent form").validationEngine("validate")) {
        return;
      }

      //更新
      var url = ""
      if (contentVm.userCodeDisabled) {
        url = constant.projectURL + '/sys-user/updateUser';

        //新建
      } else {
        url = constant.projectURL + '/sys-user/insertUser';
      }

      openConfirmDialog(MESSAGE.CONFIRM_SAVE, function () {
        var optSettings = {
          url: url,
          data: {
            "userCode": contentVm.formdata.userCode,
            "userName": contentVm.formdata.userName,
            "phone": contentVm.formdata.phone,
            "roleId": $("#operateRole").data("vue").selected,
            "warehouse": $("#warehouse").data("vue").selected,
            "isDel": $("#accountStatus").data("vue").selected,
            "sapUserCode": contentVm.formdata.sapUserCode,
          },
        }
        $._ajaxd(optSettings)
          .done(function (resp, textStatus) {
            if (resp.code === "0") {
              toastr.success(MESSAGE.INFO_SAVE_SUCCESS);
              getUserInfo(contentVm.formdata.userCode);
            }
          });
      });
    },

    //重置密码
    resetPwd: function () {
      openConfirmDialog(MESSAGE.CONFIRM_RESETPWD, function () {
        var optSettings = {
          url: constant.projectURL + '/sys-user/resetPassword',
          data: {
            "userCode": contentVm.formdata.userCode
          },
        }
        $._ajaxd(optSettings)
          .done(function (resp, textStatus) {
            if (resp.code === "0") {
              toastr.success(MESSAGE.INFO_RESETPWD_SUCCESS);
            }
          });
      });
    }
  }
});

$(document).ready(function () {

  //表单验证初始化
  $("#myContent form").validationEngine("attach", {
    //只在表单提交的时候验证
    validationEventTrigger: null,
    isOverflown: true,
  });

  var userCode = getUrlParam("userCode");
  //从一览画面迁移
  if (userCode) {
    $.when.apply($, g_deferreds)
      .done(function () {
        g_deferreds = [];
        getUserInfo(userCode);
      });
  }
})

/**
 * 取得user信息
 * @param {*} userCode 
 */
function getUserInfo(userCode) {
  var optSettings = {
    url: constant.projectURL + '/sys-user/getUserInfo',
    data: {
      "userCode": userCode
    }
  }
  $._ajaxd(optSettings)
    .done(function (resp, textStatus) {
      if (resp.code === "0") {
        contentVm.formdata = resp.data;
        contentVm.userCodeDisabled = true;
        var roleVm = $("#operateRole").data("vue");
        roleVm.selected = resp.data.roleId;
        var warehouseVm = $("#warehouse").data("vue");
        warehouseVm.selected = resp.data.warehouse;
        var accountStatusVm = $("#accountStatus").data("vue");
        accountStatusVm.selected = resp.data.isDel;

        footerVm.showReset = true;
      }
    });
}
/**
 * 共通下拉初始化
 */
$(function () {

  $("select").each(function (i, select) {
    var dataType = $(select).data("type");
    if (!dataType) {
      return;
    }
    $(select).attr("v-model", "selected");

    if (!$(select).data("noempty")) {
      $(select).append('<option></option>');
    }

    $(select).append(SELECT_PROPS[dataType].TEMPLET);
    var optSettings = {
      selid: $(select).attr("id"),
      type: 'POST',
      url: constant.projectURL + SELECT_PROPS[dataType].URL,
      data: {},
      dataType: 'json'
    }
    $._ajaxd(optSettings)
      .done(function (resp, textStatus) {
        var vm = new Vue({
          el: "#" + this.selid,
          data: {
            selected: SELECT_PROPS[dataType].DEFAULT,
            options: resp.data,
          }
        })
        $("#" + this.selid).data("vue", vm);
      });
  });

  $("input[data-type=VIN_LIST]").each(function (i, input) {
    $(input).select2({
      // placeholder: "--请选择--",
      allowClear: true,
      ajax: {
        url: constant.projectURL + CONTROLLER_NAME + '/getVinList',
        dataType: "json",
        delay: 250,
        data: function (searchstr, pageno) {
          return {
            searchstr: searchstr,// 搜索框内输入的内容，传递到后端为searchstr
            pageno: pageno || 1
          };
        },
        cache: false,
        results: function (res, pageno, s2obj) {
          var options = [];
          for (var i = 0, len = res.data.length; i < len; i++) {
            var option = { "id": res.data[i]["vin"], "text": res.data[i]["vin"] };
            options.push(option);
          }
          return {
            results: options,
            more: res.length > 0
          };
        },
        escapeMarkup: function (markup) { return markup; },
        minimumInputLength: 1,
      },

      // initSelection: function (element, callback) {
      //   var id;
      //   id = $(element).val();
      //   srm_ajax("/Base/GetMaterielInfoByKey", "post", "json", { srmMaterielId: $(element).val() }, function (res) {
      //     if (res && res[0] && res[0].MID && res[0].MNAME) {
      //       callback({ "id": res[0].MID, "text": res[0].MNAME });
      //     }
      //   }, null);
      // }
    });
  });

  /**
   * 客户下拉，带查询
   */
  $("input[data-type=CUSTOMER_LIST]").each(function (i, input) {
    $(input).select2({
      placeholder: " ",
      allowClear: true,
      ajax: {
        url: constant.projectURL + CONTROLLER_NAME + '/getCustomerList',
        dataType: "json",
        delay: 250,
        data: function (searchstr, pageno) {
          return {
            searchstr: searchstr
          };
        },
        cache: false,
        results: function (res, pageno, s2obj) {
          var len = res.data.length;
          var options = [];
          for (var i = 0; i < len; i++) {
            var option = { "id": res.data[i]["code"], "text": res.data[i]["name"] };
            options.push(option);
          }
          return {
            results: options,
            more: res.length > 0
          };
        },
        //字符转义处理
        escapeMarkup: function (markup) { return markup; },
        //最小需要输入多少个字符才进行查询(maximumSelectionLength表示最大输入限制)
        minimumInputLength: 1,
      }
    });
  });

  $("input[data-type=DATAPICKER]").each(function (i, input) {
    //实例化 年-月-日 (中文)
    $(input).datetimepicker({
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
    }).attr("readonly", "readonly");
  });
})
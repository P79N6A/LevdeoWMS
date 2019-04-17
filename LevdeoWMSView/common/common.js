//全局延迟对象
var g_deferreds = [];
//ajax 请求共通
$._ajax = function (opt) {
  var s = opt.success;
  var settings = {
    beforeSend: function (xhr) {
      var token = localStorage.getItem("token");
      xhr.setRequestHeader("Authorization", "Bearer " + token);
    },
    success: function (resp, textStatus) {
      if (resp.msg === "SERVER_ERROR" || resp.msg === "VALIDATOR_ERROR" || resp.msg === "TOKEN_ERROR") {
        toastr.error(resp.msg);
      } else if (resp.code !== "0") {
        toastr.warning(resp.msg);
      } else {
        s = $.proxy(s, this);
        s(resp, textStatus);
      }
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      console.log("Error：" + XMLHttpRequest.msg);
    },
    complete: function (XHR, TS) {
      if (TS === "error") {
        localStorage.removeItem("token");
        parent.location.href = constant.viewURL + "/login.html";
      }
    }
  }
  $.extend(opt, settings);
  $.ajax(opt);
}

//ajax 请求共通
$._ajaxd = function (opt) {
  var s = opt.success;
  var settings = {
    beforeSend: function (xhr) {
      var token = localStorage.getItem("token");
      xhr.setRequestHeader("Authorization", "Bearer " + token);
    },
    type: 'POST',
    dataType: 'json',
  }
  $.extend(opt, settings);
  var dfd = $.ajax(opt)
    .done(function (resp, textStatus) {
      if (resp.msg === "SERVER_ERROR" || resp.msg === "VALIDATOR_ERROR" || resp.msg === "TOKEN_ERROR") {
        toastr.error(resp.msg);
      } else if (resp.code !== "0") {
        toastr.warning(resp.msg);
      }
    })
    .fail(function (XMLHttpRequest, textStatus, errorThrown) {
      console.log("Error：" + XMLHttpRequest.msg);
    })
    .always(function (XHR, TS) {
      if (TS === "error") {
        localStorage.removeItem("token");
        parent.location.href = constant.viewURL + "/login.html";
      }
    });
  g_deferreds.push(dfd);
  return dfd;
}

/**
 * 业务共通处理
 */
$(function () {

  //菜单权限判断
  var htmlurl = document.location.pathname.substring(constant.viewURL.length + 1);
  if (htmlurl != "login.html" && htmlurl != "index.html") {
    var optSettings = {
      type: "POST",
      url: constant.projectURL + '/sys-user/menuJurisdiction',
      data: {
        "htmlurl": htmlurl
      },
      dataType: "json",
      success: function (resp, textStatus) {
        if (textStatus != "success") {
          location.href(constant.projectURL + "/login.html");
        }
      },
      error: function (resp, textStatus) {
        location.href(constant.projectURL + "/login.html");
      }
    }
    $._ajax(optSettings);
  }

  //处理中画面初始化
  $("body").append('<div id="page-loader" class="fade"><span class="spinner"></span></div>');
  $(document).ajaxStart(function () {
    //显示loading
    ui.loading();
  }).ajaxStop(function () {
    //隐藏loading
    ui.loading('hide');
  });

  //在父页面显示消息提示框
  toastr = parent.toastr;
  //消息提示框属性设定
  toastr.options = {
    preventDuplicates: true,  //重复内容只显示一次
  }
  toastr.remove();
})

/**
 * 通用确认消息提示框
 * @param {*} message 提示消息
 * @param {*} confirmCallBack 点击确定按钮的回调函数
 * @param {*} canelCallBack  点击取消按钮的回调函数
 * @author 宋桂帆
 */
var random_Prefix = "confirm_" + (+new Date());
function openConfirmDialog(message, confirmCallBack, canelCallBack) {
  //绘制消息提示框
  if ($("#" + random_Prefix).size() == 0) {
    $('body').append(
      '<div class="modal fade" data-backdrop="static" data-keyboard="false" id="' + random_Prefix + '" style="z-index:1053">'
      + '<div class="modal-dialog modal-sm">'
      + '<div class="modal-content">'
      + '<div class="modal-header">'
      + '<button type="button" class="close ' + random_Prefix + '_close" aria-hidden="true">×</button>'
      + '<h5 class="modal-title">确认</h5>'
      + '</div>'
      + '<div class="modal-body"><h4 id="' + random_Prefix + '_message">'
      + (message ? message : '')
      + '</h4></div>'
      + '<div class="modal-footer">'
      + '<button class="btn btn-sm btn-white ' + random_Prefix + '_close">取消</button>'
      + '<button class="btn btn-sm btn-success" id="' + random_Prefix + '_submit">确定</button>'
      + '</div>'
      + '</div>'
      + '</div>'
      + '</div>'
    );
  } else {
    $("#" + random_Prefix + "_message").text(message);
  }

  //显示消息提示框
  //二重弹出遮罩层层叠水平增加
  $("#" + random_Prefix).on('shown.bs.modal', function () {
    $("div.modal-backdrop.fade.in:eq(1)").css("z-index", 1052);
    $("." + random_Prefix + "_close").focus();
  }).modal('show');

  //确定按钮绑定
  $("#" + random_Prefix + "_submit").unbind("click").click(function () {
    if (confirmCallBack) {
      if (typeof confirmCallBack == 'function') {
        confirmCallBack();
      } else if (typeof confirmCallBack == 'string') {
        eval(confirmCallBack);
      }
    }
    $("#" + random_Prefix).modal('hide');
  });

  //取消按钮绑定
  $("." + random_Prefix + "_close").unbind("click").click(function () {
    if (canelCallBack) {
      if (typeof canelCallBack == 'function') {
        canelCallBack();
      } else if (typeof canelCallBack == 'string') {
        eval(canelCallBack);
      }
    }
    $("#" + random_Prefix).modal('hide');
  });
}


/**
 * 初始化datatables
 * @param {*} tableid 
 * @param {*} columns 
 * @param {*} createdRow 
 * @param {*} url 
 * @param {*} getSearchConditon 
 * @param {*} fixedLeftColumns 
 * @param {*} fixedRightColumns 
 * @param {*} checkboxFlg 
 * @param {*} paging 
 * @param {*} pageinfo 
 */
function initDataTables(tableid
  , columns
  , createdRow
  , url
  , getSearchConditon
  , fixedLeftColumns
  , fixedRightColumns
  , checkboxFlg
  , paging
  , pageinfo
  , order
  , callBackFun
  , pdtSetting
  , ajGlobal) {

  //
  columns = $.extend(true, [], columns);

  //添加序号列
  columns.splice(0, 0, {
    title: "序号",
    orderable: false,
    sWidth: 20,
    className: "alignRight",
    render: function (data, type, row, meta) {
      return meta.row + 1;
    },
  });

  //添加checkbox列
  if (checkboxFlg) {
    columns.splice(1, 0,
      {
        title: '<div style="text-align: center"><input type="checkbox" class="js_checkbox_header"></div>',
        data: "checked",
        orderable: false,
        sWidth: 20,
        sClass: "hiddenCol",
        render: function (data, type, full, meta) {
          return '<div style="text-align: center"><input type="checkbox"  class="js_checkbox" ' + data + '/></div>';
        },
      });
  }

  //全选checkbox点击
  $("body").delegate("div[id^=" + tableid + "] .dataTables_scrollHead input.js_checkbox_header", "click", function (e) {
    $("input.js_checkbox").prop("checked", $(this).prop("checked")).change();
  })

  //每页显示条数选项
  //导出excel配置 ( 如不需要操作按钮 可配置 buttons:[] )
  var buttons = [];
  var tablebodyHeight = 0;
  if (paging) {
    var buttons = [{
      extend: "pageLength",
      //按钮样式
      className: "btn-white",
    }]

    //滚动区高度
    tablebodyHeight = document.body.clientHeight - $(".page-header").height() - $("#myHeader").height() - 210;
  } else {
    //滚动区高度
    tablebodyHeight = document.body.clientHeight - $(".page-header").height() - $("#myHeader").height() - 130;
    if ($(".panel-heading").length) {
      tablebodyHeight = tablebodyHeight - $(".panel-heading").height() - 30;
    }
  }

  var dtSetting = {
    //数据源
    ajax: function (data, callback, settings) {
      //封装请求参数
      var param = {};
      //页面显示记录条数，在页面显示每页显示多少项的时候
      param.limit = data.length;
      //开始的记录序号
      param.start = data.start;
      //当前页码
      param.page = data.start / data.length + 1;
      //计数器(请求数自增)
      param.draw = data.draw;
      //排序方式( 如需要传入后台 )
      param.order = data.order;
      //检索( 如需要传入后台 )
      param.search = data.search;
      //扩展请求参数，加入业务画面查询条件
      var searchConditon = getSearchConditon ? getSearchConditon() : {};
      $.extend(searchConditon, {
        "currentPage": param.page,
        "start": param.start,
        "limit": param.limit,
        "orderColumn": data.order.length > 0 ? data.columns[data.order[0].column].name : "",
        "orderDir": data.order.length > 0 ? data.order[0].dir : ""
      });
      //ajax请求数据
      $._ajax({
        extraData: {
          paging: paging,
        },
        type: "post",
        url: url,
        data: searchConditon,
        //禁用缓存
        cache: false,
        dataType: "json",
        global: !ajGlobal,
        success: function (result) {
          //封装返回数据
          var returnData = {};
          //分页
          if (this.extraData.paging) {
            //请求计数
            returnData.draw = param.draw++;
            //返回数据全部记录 ( 3573为总条数, 应从后台取出 )
            returnData.recordsTotal = result.data.total;
            //后台不实现过滤功能，每次查询均视作全部结果
            returnData.recordsFiltered = result.data.total;
            //返回的数据列表 (此列表应为jsonarray)
            returnData.data = result.data.list;

            //不分页
          } else {
            //返回的数据列表 (此列表应为jsonarray)
            returnData.data = result.data;
          }
          //没有数据的时候提醒
          if (!returnData.data || returnData.data.length === 0) {
            returnData.data = [];
            toastr.info(MESSAGE.INFO_NODATA)
          }
          //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
          //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
          callback(returnData);
          if (callBackFun && typeof callBackFun == 'function') {
            callBackFun();
          }
          //清除全选
          $(".js_checkbox_header").prop("checked", "");
        },
        error: function (a, b, c) {
          toastr.error(MESSAGE.ERROR_SERVER_EXCEPTION)
        }
      });
    },
    //冻结前两列 及尾列
    fixedColumns: {
      leftColumns: fixedLeftColumns,
      rightColumns: fixedRightColumns
    },
    //允许横向滚动
    scrollX: true,
    //表格滚动高度(不包含表头)
    scrollY: tablebodyHeight,
    //按钮必要dom
    dom: "Bfrtip",
    //每页显示条数选项
    //导出excel配置 ( 如不需要操作按钮 可配置 buttons:[] )
    buttons: buttons,
    //每页显示条数配置  
    aLengthMenu: [[10, 25, 50], ["10条", "25条", "50条"]],
    //启用服务器端分页
    serverSide: true,
    //表格搜索总开关
    searching: false,
    //表头排序总开关
    ordering: true,
    //默认排序列 (可设置多个)
    order: order ? order : [],
    //分页启用    
    paging: paging,
    //左下角总条数信息启用( 不启用分页不建议开启此项 )    
    info: pageinfo,
    //分页样式
    pagingType: "first_last_numbers",
    //每页显示条数
    iDisplayLength: 10,
    //语言设置
    language: {
      //日文语言包文件为:Japanese.json
      url: "../../assets/plugins/jquery.datatables/la/Chinese.json",
      buttons: {
        pageLength: {
          _: "每页 %d 条",
          "-1": "显示所有"
        }
      }
    },
    errMode: "error1",
    //加载loading
    processing: true,
    //显示的列(title:列名,data:数据key)
    columns: columns,
    //控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
    createdRow: function (row, data, dataIndex) {
      if ($(row).find('input[type=checkbox][class=js_checkbox]').length === 1) {
        $(row).find('input[type=checkbox][class=js_checkbox]').change(function (e) {
          data.checked = $(this).prop("checked");
        })
      }
      createdRow(row, data, dataIndex);
    },
  }

  //初始化表格( 详细配置 )
  var dts = $("#" + tableid).DataTable($.extend({}, dtSetting, pdtSetting));
  return dts;
}

/**
 * 获取页面参数
 * @author 宋桂帆
 * @param {*} name 参数名
 * @returns 参数值
 */
function getUrlParam(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
  var r = decodeURI(window.location.search).substr(1).match(reg);
  return r ? unescape(r[2]) : null;
}

/**
 * 
 * @param {*} targetId 
 * @param {*} ctrls 
 * @param {*} offset 
 */
function calcHeight(targetId, ctrls, offset) {
  var heightResult = document.body.clientHeight - $(".page-header").height() - $("#myHeader").height() - offset;
  for (c in ctrls) {
    heightResult = heightResult - $(c).height();
  }
  $(targetId).height(heightResult);
}

/**
 * 封装token参数
 * @author 宋桂帆
 */
$.fn.extend({
  _submit: function () {
    var obj = $('<input type="hidden" name="token" value="' + localStorage.getItem("token") + '">');
    $(this).append(obj);
    $(this).attr("method", "post").submit();
    $(obj).remove();
  }
})

/**
 * 将undefined、null转换成""
 * @param {*} obj 
 */
var undnulltostring = function (obj) {
  if (obj == null || obj == undefined || obj == "null") {
    return "";
  } else {
    return obj;
  }
};

/**
 * 校验文件大小、扩展名(仅单文件)
 * @author 宋桂帆
 * @param {*} elementId input file元素的id，比如"#fileToUpload"
 * @param {*} type 支持的文件扩展名
 * @returns 文件是否符合要求
 */
function validateFile(elementId, type) {
  var obj = $(elementId)[0];
  if (!obj || !obj.files) {
    return false;
  }
  //限制单个文件最大大小5M
  var maxSize = 5 * 1024 * 1024;
  var file = obj.files[0];
  if (file.size == 0) {
    toastr.warning(MESSAGE.ERROR_NO_DATA);
    return false;
  }
  if (file.size > maxSize) {
    toastr.warning(MESSAGE.ERROR_FILE_TOO_BIG);
    return false;
  }
  //校验文件格式
  if (!type) {
    return true;
  }
  type = type + ",";
  var extName = file.name.substr(file.name.lastIndexOf(".") + 1) + ",";
  if (type.indexOf(extName) == -1) {
    toastr.warning(MESSAGE.ERROR_FILE_TYPE);
    return false;
  } else {
    return true;
  }
}

/**
 * 
 */
function addReturnButton() {
  $("#myHeader div.form-group div.pull-right")
    .append('<button type="button" class="btn btn-white" onClick="localStorage.setItem(\'MOVE_BACK_FLAG\',1);window.history.back(-1);">返回</button>')
}

function isMoveBack() {
  var moveBackFlag = localStorage.getItem("MOVE_BACK_FLAG");
  localStorage.removeItem("MOVE_BACK_FLAG");
  return moveBackFlag == "1" ? true : false;
}

function setMoveParam(data) {
  localStorage.setItem("PAGE_MOVE_PARAM", JSON.stringify(data));
}

function getMoveParam() {
  var data = localStorage.getItem("PAGE_MOVE_PARAM");
  return JSON.parse(data);
}

function removeMoveParam() {
  localStorage.removeItem("PAGE_MOVE_PARAM");
}

function removeDtState() {
  localStorage.removeItem("DataTables_tableBox_/LevdeoWMSView/warehouseOut/asnList/index.html");
  localStorage.removeItem("DataTables_tableBox_/LevdeoWMSView/warehouseOut/asnList/index2.html");
  localStorage.removeItem("DataTables_tableBox_/LevdeoWMSView/warehouseOut/asnList/index3.html");
}
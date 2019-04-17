/**
 * 共通消息定义
 */
MESSAGE = {

  /**
   * 提示消息
   */
  INFO_NODATA: "没有相关数据。",
  INFO_SAVE_SUCCESS: "保存成功。",
  INFO_DELETE_SUCCESS: "删除成功。",
  INFO_UPDATE_SUCCESS: "更新成功。",
  INFO_SEARCH_SUCCESS: "检索成功。",
  INFO_IMPORT_SUCCESS: "导入成功。",
  INFO_PRINT_SUCCESS: "打印成功。",
  INFO_RESETPWD_SUCCESS: "重置密码成功。",
  INFO_ADD_MAINDATA_SUCCESS:"添加主数据成功。",
  INFO_CREATE_SUCCESS:"新建成功。",
  INFO_LOCK_SUCCESS:"锁定成功。",
  INFO_UNLOCK_SUCCESS:"解锁成功。",
  INFO_COPY_SUCCESS:"复制成功。",
  INFO_WAREHOUSEOUT_SUCCESS:"出库成功。",
  INFO_WRITEOFF_SUCCESS:"冲销成功。",
  INFO_CHANGE_CHAR_SUCCESS:"换车成功",
  INFO_CHANGE_MAINDATA_SUCCESS:"修改主数据成功",

  /**
   * 警告消息
   */
  WARN_NODATA_SAVE: "没有需要保存的数据。",
  WARN_NOCHECK_SUBMIT: "请勾选VIN后再进行提交。",
  WARN_NODATA_PRINT: "没有要打印的数据。",
  WARN_CHECK_PRIORITYLV: "停车策略不能重复。",
  WARN_WSTRANSITION_REPEAT: "入库交接单重复。",
  WARN_VIN_REPEAT: "VIN码重复。",
  WARN_STOCKPOSITION_INUSE:"该车位被占用，请换一个车位。",
  WARN_IMPORT_PDA_INVALID:"当前VIN码状态不是待入库。",
  WARN_SAVE_REASON:"请选择入库理由。",
  WARN_STOCKPOSITION_WAS_FULL:"无法入库，库位已满。",
  WARN_NOT_CAR_IN:"该车不是预定入库车辆。",
  WARN_SAP_NO_PAY:"SAP未过账车辆无法执行钥匙入库。",
  WARN_CHOOSE_MAINDATA:"请先选择主数据。",
  WARN_MAINDATA_CODE_REPEAT:"主数据编码重复，请重新填写。",
  WARN_MAINDATA_NAME_REPEAT:"主数据名称重复，请重新填写。",
  WARN_VIN_INVALID:"请输入有效的vin码",
  WARN_STOCKPOSITION_BE_USED_CANT_LOCK:"当前库位使用中,无法锁定。",
  WARN_PARAMETER_NOT_EMPTY:"参数不可为空",

  /**
   * 询问消息
   */
  CONFIRM_SAVE: "确定保存吗？",
  CONFIRM_DELETE: "确定删除吗？",
  CONFIRM_CREATE: "确定新建吗？",
  CONFIRM_UPDATE: "确定修改吗？",
  CONFIRM_PAD_IMPORT: "确定导入吗？",
  CONFIRM_COMPLITE_CHECK: "确定完成吗？",
  CONFIRM_STOP_SERVICE: "确定停用吗？",
  CONFIRM_RESETPWD: "确定重置密码吗？",
  CONFIRM_START_SERVICE: "确定启用吗？",
  CONFIRM_WASH: "确定冲销吗？",

  /**
   * 错误消息
   */
  ERROR_SERVER_EXCEPTION: "服务器错误。",
  ERROR_SAVE: "保存失败。",
  ERROR_USERNAME_SEARCH: "作业员名称获取失败。",
  ERROR_TRANSITION_SEARCH: "入库交接单信息检索失败。",
  ERROR_NOTRANSITION: "入库交接单不存在。",
  ERROR_IMPORT: "导入失败。",
  ERROR_PRINT_SEARCH: "打印数据检索失败。",
  ERROR_PRINT: "打印失败。",
  ERROR_UPLOAD_FILE: "导入信息有误。",
  ERROR_READ_FILE: "文件解析失败。",
  ERROR_NO_DATA: "没有符合的数据。",
  ERROR_FILE_TOO_BIG: "文件大小不能超过5M。",
  ERROR_FILE_TYPE: "上传的文件类型不符。",
  ERROR_WAREHOUSE_DEL_FAILED:"只能删除状态为待入库的车辆。",
  ERROR_MOVESP_TOO_LONG:"vin不能超过30个字符，新旧库位号不能超过50个字符。",
  ERROR_DATA_IS_NULL:"红框内的信息不能为空。",
  ERROR_VIN_REPEAT:"VIN码不能重复。",
  ERROR_VIN_IS_NULL:"请输入VIN。",
  ERROR_CONNECT_PRINT_SERVER_FAIL:"连接后台打印服务失败。",
  ERROR_CONNECT_PRINT_FAIL:"连接打印机失败。",
  ERROR_SEARCHING_FAIL:"检索失败。",
  ERROR_INTERFACE_STOCKOUT_FAIL:"调用出库单接口失败：",
}
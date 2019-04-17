
CONTROLLER_NAME = "/selectInit";
SELECT_PROPS = {


  /**
   * 车辆状态
   */
  CAR_STATUS: {
    URL: this.CONTROLLER_NAME + '/getCarStatus',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',

  },

  /**
   * 电池类型
   */
  BATTERY_TYPE: {
    URL: this.CONTROLLER_NAME + '/getBatteryType',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },

  /**
   * SAP过账状态
   */
  SAP_TYPE: {
    URL: this.CONTROLLER_NAME + '/getSapType',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },

  /**
   * 盘点状态
   */
  CHECK_TYPE: {
    URL: this.CONTROLLER_NAME + '/getCheckType',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },

  /**
   * 库区
   */
  STOCK_AREA_LIST: {
    URL: this.CONTROLLER_NAME + '/getStockAreaList',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.stockAreaCode">{{ option.name }}</option>',
  },
/**
   * 所在仓库的库区
   */
  STOCK_AREA_LIST_WITH_WAREHOUSE: {
    URL: this.CONTROLLER_NAME + '/getStockAreaListWithWarehouse',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.stockAreaCode">{{ option.name }}</option>',
  },

  /**
   * 发货单类型
   */
  INVOICE_TYPE: {
    URL: this.CONTROLLER_NAME + '/getInvoiceType',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },
  /**
   * 发货单状态
   */
  INVOICE_STATUS: {
    URL: this.CONTROLLER_NAME + '/getInvoiceStatus',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },
  /**
   * 客户
   */
  CUSTOMER_NAME: {
    URL: this.CONTROLLER_NAME + '/getCustomer',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.code">{{ option.name }}</option>',
  },
  /**
   * 非下线入库理由
   */
  ONLINE_WAREHOUSING_REASON: {
    URL: this.CONTROLLER_NAME + '/getOnlineReason',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
    DEFAULT: "07"
  },
  /**
   * 货运方式
   */
  TRANSPORT_TYPE: {
    URL: this.CONTROLLER_NAME + '/getTransportType',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.code">{{ option.name }}</option>',
  },

  /**
   * 角色
   */
  ROLE_TYPE: {
    URL: this.CONTROLLER_NAME + '/getRoleType',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.roleId">{{ option.roleName}}</option>',
  },
  /**
   * 仓库名
   */
  WAREHOUSING_NAME: {
    URL: this.CONTROLLER_NAME + '/getRoleType',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.name">{{ option.name }}</option>',
  },
  /**
   * 仓库管理_仓库list
   */
  WAREHOUSE_LIST: {
    URL: this.CONTROLLER_NAME + '/getWareHouseList',
    TEMPLET: '<option v-for="option in options" :value="option.warehouseCode">{{ option.name }}</option>',
  },
  /**
   * 停车优先策略list
   */
  PRIORITY_LIST: {
    URL: this.CONTROLLER_NAME + '/getPriorityList',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },
  /**
   * 入库管理状态 待入库 车辆入库 在库
   */
  WAREHOUSE_STATUS: {
    URL: this.CONTROLLER_NAME + '/getWarehouseState',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },
  /**
   * 账号状态  在用  停用
   */
  ACCOUNT_STATUS_LIST: {
    URL: this.CONTROLLER_NAME + '/getAccountStatus',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },
  /**
   * 库位使用情况
   */
  SP_USE_STATUS: {
    URL: this.CONTROLLER_NAME + '/getSpUseStatus',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },
  /**
   * 库位使用情况
   */
  SP_LOCK_STATUS: {
    URL: this.CONTROLLER_NAME + '/getSpLockStatus',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },
  /**
   * 获取物流公司下拉
   */
  LOGIS_COMPANY_LIST: {
    URL: this.CONTROLLER_NAME + '/getlogisCompanys',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.code">{{ option.name }}</option>',
  },
  /**
   * 操作类型下拉
   */
  OPERA_TYPE:{
    URL: this.CONTROLLER_NAME + '/getOperaType',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.subCode">{{ option.subName }}</option>',
  },
  /**
   * 成品库下拉
   */
  PRODUCT_STOCK_LIST:{
    URL: this.CONTROLLER_NAME + '/getProductStockCode',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.code">{{ option.name }}</option>',
  },
  /**
   * 销售公司下拉
   */
  SALE_COMPANY_LIST:{
    URL: this.CONTROLLER_NAME + '/getSaleCompany',
    TEMPLET: '<option v-for="option in options" v-bind:value="option.pCode">{{ option.pName }}</option>',
  },
}
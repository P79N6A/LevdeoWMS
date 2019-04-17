/**
 * 系统参数常量
 */
SysParamConstant = {

    /**
     * 车辆状态
     */
    PARAM_TYPE_CAR_STATUS: "0001",
    //待入库
    P_CAR_STATUS_PREWAREHOUSEIN: "01",
    //车辆入库
    P_CAR_STATUS_CARWAREHOUSEIN: "02",
    //在库
    P_CAR_STATUS_INSTORAGE: "03",
    //充电
    P_CAR_STATUS_CHARGE: "04",
    //维修
    P_CAR_STATUS_FIX: "05",
    //借用
    P_CAR_STATUS_LEND: "06",
    //返厂
    P_CAR_STATUS_RETURN: "07",
    //出库中
    P_CAR_STATUS_SENDING: "08",
    //出库
    P_CAR_STATUS_SENDED: "09",
    //调拨
    P_CAR_STATUS_ADJUST: "10",
    //待出库
    P_CAR_STATUS_WAITSEND: "11",

    /**
     * sap过账
     */
    PARAM_TYPE_SAP_PAY: "0002",
    //未复核
    P_SAP_WAIT_CHECK: "C",
    //已作废
    P_SAP_CANCELLED: "D",
    //核准过账
    P_SAP_PAYED: "G",
    //复核
    P_SAP_CHECKED: "S",

    /**
     * 盘点计划状态
     */
    PARAM_TYPE_CHECK_STATUS: "0003",
    //待执行
    P_CHECK_WAIT: "01",
    //执行中
    P_CHECK_CHECKING: "02",
    //完成
    P_CHECK_OVER: "03",

    /**
     * 电池类型
     */
    PARAM_TYPE_BATTERY_TYPE: "0004",
    //锂电池
    P_BATTERY_LI: "01",
    //铅酸电池
    P_BATTERY_PL: "02",

    /**
     * 发货单类型
     */
    PARAM_TYPE_ASN_TYPE: "0005",
    //普通
    P_ASN_COMMON: "01",
    //冲量
    P_ASN_AMOUNT: "02",

    /**
     * 操作
     */
    PARAM_TYPE_STOCKLOG_TYPE: "0006",
    //打印库位
    P_STOCKLOG_PRINT: "01",
    //车辆入库
    P_STOCKLOG_CARWAREHOUSEIN: "02",
    //在库
    P_STOCKLOG_INSTORAGE: "03",
    //钥匙入库
    P_STOCKLOG_KEYWAREHOUSEIN: "04",
    //充电
    P_STOCKLOG_CHARGE: "05",
    //维修
    P_STOCKLOG_FIX: "06",
    //借用
    P_STOCKLOG_LEND: "07",
    //返厂
    P_STOCKLOG_RETURN: "08",
    //移库
    P_STOCKLOG_MOVE: "09",
    //出库
    P_STOCKLOG_SENDED: "10",
    //非下线入库
    P_STOCKLOG_REWAREHOUSEIN: "11",
    //借用取消
    P_STOCKLOG_CANCEL_ADJUST: "12",
    //归还取消
    P_STOCKLOG_CANCEL_BACK: "13",
    //调拨
    P_STOCKLOG_ADJUST: "14",
    //归还
    P_STOCKLOG_BACK: "15",
    //冲销
    P_STOCKLOG_WASH: "16",

    /**
     * 超期库存
     */
    PARAM_TYPE_OUTOFDATE_STOCK: "0007",
    //超期库存
    P_OUTOFDATE_STOCK: "01",

    /**
     * 锁定
     */
    PARAM_TYPE_LOCK_STATUS: "0008",
    //是
    P_LOCK_ON: "01",
    //否
    P_LOCK_OFF: "02",

    /**
     * 停车优先策略
     */
    PARAM_TYPE_PARKING_POLICY: "0009",
    //系列
    P_PARKING_POLICY_SERIES: "01",
    //品牌
    P_PARKING_POLICY_BRAND: "02",
    //颜色
    P_PARKING_POLICY_COLOR: "03",

    /**
     * 非下线理由
     */
    PARAM_TYPE_REWAREHOUSEIN_REASON: "0010",
    //借出
    P_REWAREHOUSEIN_REASON_LEND: "07",
    //返厂
    P_REWAREHOUSEIN_REASON_RETURN: "08",

    /**
     * 使用状态
     */
    PARAM_TYPE_IN_USE: "0011",
    //未使用
    P_NOT_USE: "01",
    //使用中
    P_IN_USE: "02",

    /**
     * 发货单状态
     */
    PARAM_TYPE_ASN_STATUS: "0012",
    //待出库
    P_ASN_STATUS_WAITSEND: "01",
    //待确认
    P_ASN_STATUS_WAITCONFIRM: "02",
    //出库中
    P_ASN_STATUS_CONFIRMED: "03",
    //已出库
    P_ASN_STATUS_SENDED: "04",

    /**
     * 账号状态
     */
    PARAM_TYPE_ACCOUNT_STATUS: "0013",
    //在用
    P_ACCOUNT_STATUS_USING: "0",
    //停用
    P_ACCOUNT_STATUS_STOP: "1",

    /**
     * 接口返回值
     */
    PARAM_TYPE_API_RESULT: "0014",
    //N:失败
    P_API_RESULT_FAIL: "N",
    //Y:成功
    P_API_RESULT_SUCCESS: "Y",

    /**
     * 主数据维护
     */
    PARAM_TYPE_MAIN_MAINTEN: "0015",
    //品牌
    P_MAIN_BRAND: "01",
    //颜色
    P_MAIN_COLOR: "02",
    //系列
    P_MAIN_SERIES: "03",
    //物流公司
    P_MAIN_LOGIS_COMP: "04",
    //物料描述
    P_MAIN_MAT: "05",
    //订单种类
    P_MAIN_ORDER_TYPE: "06",
    //成品库
    P_MAIN_PRODUCT_STOCK: "07",
    //货运方式
    P_MAIN_TRANSPORT: "08",
    //客户信息
    P_MAIN_CUSTOMERS: "09",
    //销售部门
    P_MAIN_SALE_COMPANY: "10",
}

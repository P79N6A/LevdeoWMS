package com.tre.jdevtemplateboot.common.constant;

/**
 * 系统参数常量
 */
public final class SysParamConstant {
    //禁止实例化
    private SysParamConstant() {}

    /**
     * 车辆状态
     */
    public static final String PARAM_TYPE_CAR_STATUS = "0001";
    //待入库
    public static final String P_CAR_STATUS_PREWAREHOUSEIN = "01";
    //车辆入库
    public static final String P_CAR_STATUS_CARWAREHOUSEIN = "02";
    //在库
    public static final String P_CAR_STATUS_INSTORAGE = "03";
    //充电
    public static final String P_CAR_STATUS_CHARGE = "04";
    //维修
    public static final String P_CAR_STATUS_FIX = "05";
    //借用
    public static final String P_CAR_STATUS_LEND = "06";
    //返厂
    public static final String P_CAR_STATUS_RETURN = "07";
    //出库中
    public static final String P_CAR_STATUS_SENDING = "08";
    //出库
    public static final String P_CAR_STATUS_SENDED = "09";
    //调拨
    public static final String P_CAR_STATUS_ADJUST = "10";
    //待出库
    public static final String P_CAR_STATUS_WAITSEND = "11";

    /**
     * sap过账
     */
    public static final String PARAM_TYPE_SAP_PAY = "0002";
    //未复核
    public static final String P_SAP_WAIT_CHECK = "C";
    //已作废
    public static final String P_SAP_CANCELLED = "D";
    //核准过账
    public static final String P_SAP_PAYED = "G";
    //复核
    public static final String P_SAP_CHECKED = "S";

    /**
     * 盘点计划状态
     */
    public static final String PARAM_TYPE_CHECK_STATUS = "0003";
    //待执行
    public static final String P_CHECK_WAIT = "01";
    //执行中
    public static final String P_CHECK_CHECKING = "02";
    //完成
    public static final String P_CHECK_OVER = "03";

    /**
     * 电池类型
     */
    public static final String PARAM_TYPE_BATTERY_TYPE = "0004";
    //锂电池
    public static final String P_BATTERY_LI = "01";
    //铅酸电池
    public static final String P_BATTERY_PL = "02";

    /**
     * 发货单类型
     */
    public static final String PARAM_TYPE_ASN_TYPE = "0005";
    //普通
    public static final String P_ASN_COMMON = "01";
    //冲量
    public static final String P_ASN_AMOUNT = "02";

    /**
     * 操作
     */
    public static final String PARAM_TYPE_STOCKLOG_TYPE = "0006";
    //打印库位
    public static final String P_STOCKLOG_PRINT = "01";
    //车辆入库
    public static final String P_STOCKLOG_CARWAREHOUSEIN = "02";
    //在库
    public static final String P_STOCKLOG_INSTORAGE = "03";
    //钥匙入库
    public static final String P_STOCKLOG_KEYWAREHOUSEIN = "04";
    //充电
    public static final String P_STOCKLOG_CHARGE = "05";
    //维修
    public static final String P_STOCKLOG_FIX = "06";
    //借用
    public static final String P_STOCKLOG_LEND = "07";
    //返厂
    public static final String P_STOCKLOG_RETURN = "08";
    //移库
    public static final String P_STOCKLOG_MOVE = "09";
    //出库
    public static final String P_STOCKLOG_SENDED = "10";
    //非下线入库
    public static final String P_STOCKLOG_REWAREHOUSEIN = "11";
    //借用取消
    public static final String P_STOCKLOG_CANCEL_ADJUST = "12";
    //归还取消
    public static final String P_STOCKLOG_CANCEL_BACK = "13";
    //调拨
    public static final String P_STOCKLOG_ADJUST = "14";
    //归还
    public static final String P_STOCKLOG_BACK = "15";
    //冲销
    public static final String P_STOCKLOG_WASH = "16";

    /**
     * 超期库存
     */
    public static final String PARAM_TYPE_OUTOFDATE_STOCK = "0007";
    //超期库存
    public static final String P_OUTOFDATE_STOCK = "01";

    /**
     * 锁定
     */
    public static final String PARAM_TYPE_LOCK_STATUS = "0008";
    //是
    public static final String P_LOCK_ON = "01";
    //否
    public static final String P_LOCK_OFF = "02";

    /**
     * 停车优先策略
     */
    public static final String PARAM_TYPE_PARKING_POLICY = "0009";
    //系列
    public static final String P_PARKING_POLICY_SERIES = "01";
    //品牌
    public static final String P_PARKING_POLICY_BRAND = "02";
    //颜色
    public static final String P_PARKING_POLICY_COLOR = "03";

    /**
     * 非下线理由
     */
    public static final String PARAM_TYPE_REWAREHOUSEIN_REASON = "0010";
    //借出
    public static final String P_REWAREHOUSEIN_REASON_LEND = "07";
    //返厂
    public static final String P_REWAREHOUSEIN_REASON_RETURN = "08";

    /**
     * 使用状态
     */
    public static final String PARAM_TYPE_IN_USE = "0011";
    //未使用
    public static final String P_NOT_USE = "01";
    //使用中
    public static final String P_IN_USE = "02";

    /**
     * 发货单状态
     */
    public static final String PARAM_TYPE_ASN_STATUS = "0012";
    //待出库
    public static final String P_ASN_STATUS_WAITSEND = "01";
    //待确认
    public static final String P_ASN_STATUS_WAITCONFIRM = "02";
    //出库中
    public static final String P_ASN_STATUS_CONFIRMED = "03";
    //已出库
    public static final String P_ASN_STATUS_SENDED = "04";

    /**
     * 账号状态
     */
    public static final String PARAM_TYPE_ACCOUNT_STATUS = "0013";
    //在用
    public static final String P_ACCOUNT_STATUS_USING = "0";
    //停用
    public static final String P_ACCOUNT_STATUS_STOP = "1";

    /**
     * 接口返回值
     */
    public static final String PARAM_TYPE_API_RESULT = "0014";
    //N:失败
    public static final String P_API_RESULT_FAIL = "N";
    //Y:成功
    public static final String P_API_RESULT_SUCCESS = "Y";

    /**
     * 主数据维护
     */
    public static final String PARAM_TYPE_MAIN_MAINTEN = "0015";
    //品牌
    public static final String P_MAIN_BRAND = "01";
    //颜色
    public static final String P_MAIN_COLOR = "02";
    //系列
    public static final String P_MAIN_SERIES = "03";
    //物流公司
    public static final String P_MAIN_LOGIS_COMP = "04";
    //物料描述
    public static final String P_MAIN_MAT = "05";
    //订单种类
    public static final String P_MAIN_ORDER_TYPE = "06";
    //成品库
    public static final String P_MAIN_PRODUCT_STOCK = "07";
    //货运方式
    public static final String P_MAIN_TRANSPORT = "08";
    //客户信息
    public static final String P_MAIN_CUSTOMERS = "09";
    //销售部门
    public static final String P_MAIN_SALE_COMPANY = "10";
}

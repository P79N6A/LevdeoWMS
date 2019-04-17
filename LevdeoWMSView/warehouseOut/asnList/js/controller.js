//列名 列ID 是否增加排序 列宽 是否可检索
columnsMode1 = [
	{ title: "发货单号", data: "sapInvoiceCode", orderable: true, searchable: true, name: "oh.sapInvoiceCode" },
	{ title: "行项目号", data: "itemNo", orderable: true, searchable: true, name: "oh.itemNo" },
	{ title: "物料编码", data: "matCode", orderable: true, searchable: true, name: "oh.matCode" },
	{ title: "物料描述", data: "matName", orderable: true, searchable: true, name: "mainMat.name" },
	{ title: "数量", data: "amount", orderable: true, searchable: true, className: "alignRight", name: "oh.amount" },
	{ title: "开单日期", data: "makeOrderDate", orderable: true, searchable: true, name: "oh.makeOrderDate" },
	{ title: "客户名称", data: "customerName", orderable: true, searchable: true, name: "m.name" },
	{ title: "销售公司", data: "companyName", orderable: true, searchable: true, name: "mc.name" },
	{ title: "销售单类型", data: "orderType", orderable: true, searchable: true, name: "mot.name" },
	{ title: "状态", data: "outStatus", orderable: true, searchable: true, name: "s.subName" },
	{ title: "出库日期", data: "outBoundDate", orderable: true, searchable: true, name: "outBoundDate" },
];
columnsMode23 = [
	{ title: "发货单号", data: "sapInvoiceCode", orderable: true, searchable: true, name: "oh.sapInvoiceCode" },
	{ title: "出库日期", data: "sendedDate", orderable: true, searchable: true, name: "oh.sendedDate" },
	{ title: "客户名称", data: "customerName", orderable: true, searchable: true, name: "cstm.name" },
	{ title: "状态", data: "outStatus", orderable: true, searchable: true, name: "oh.status" },
	{ title: "配件确认人", data: "giftChecker", orderable: true, searchable: true, name: "oh.giftChecker" },
	{ title: "宣传品确认人", data: "proChecker", orderable: true, searchable: true, name: "oh.proChecker" },
];

// 控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
function createdRow(row, data, dataIndex) {
	// 双击跳转'出库Page'-传参outBoundCode
	$(row).dblclick(function () {

		//
		setMoveParam($.extend({}, getSearchConditon(), { customerName: $("#select2-chosen-1").text() }));

		// mode1
		if (mode == "1") {
			window.location.href = '../../warehouseOut/asnDetail/index.html?sapInvoiceCode=' + data.sapInvoiceCode + "&orderTypeCode=" + data.orderTypeCode;
		}
		// mode2-配件确认
		// mode3-宣传品确认
		else {
			window.location.href = '../../warehouseOut/confirmDetail/index.html?sapInvoiceCode=' + data.sapInvoiceCode + "&mode=" + mode;
		}
	});
}
/**
 * 根据mode动态显示页面布局
 */
function loadingPage() {
	// 01-全部操作
	if (mode == "1") {
		// 初始化表格( 详细配置 )
		// 获取-全部出库单
		tableBox = initDataTables(
			"tableBox",
			columnsMode1,
			createdRow,
			constant.projectURL + '/outInvoiceDetails/getOutBoundOrder',
			getSearchConditon,
			1, 0,
			false,
			true,
			true,
			[1, "desc"],
			null,
			{ stateSave: true }
		);
	}
	// 02-03-确认随车物料
	else if (mode == "2" || mode == "3") {
		// 初始化表格( 详细配置 )
		// 获取-状态为 待确认、完成 的出库单
		tableBox = initDataTables(
			"tableBox",
			columnsMode23,
			createdRow,
			constant.projectURL + '/outInvoiceDetails/getConfirmAndCompleteOrders',
			getSearchConditon,
			1, 0,
			false,
			true,
			true,
			[1, "desc"],
			null,
			{ stateSave: true }
		);

	}
}

// 取得查询条件
var getSearchConditon = function () {
	return {
		"outBoundCode": $("#outBoundCode").val().trim(),
		"outBoundDate": $("#outBoundDate").val().trim(),
		"customerCode": $("#customer").val(),
		"outStatus": $("#outStatus").val() ? $("#outStatus").val().join(",") : "",
	}
}

$(document).ready(function () {

	$.when.apply($, g_deferreds).done(function () {
		g_deferreds = [];

		$('#outStatus').selectpicker({
			style: 'btn-white', //样式 可使用 btn-primary 等
			noneSelectedText: '',
		});

		if (isMoveBack()) {
			var mparam = getMoveParam();
			$("#customer").val(mparam.customerCode);
			$("#outStatus").selectpicker("val", mparam.outStatus.split(","));
			$("#select2-chosen-1").text(mparam.customerName);
			removeMoveParam();

			//
		} else {
			removeDtState();
			$("#outStatus").selectpicker("val", [SysParamConstant.P_ASN_STATUS_WAITSEND, SysParamConstant.P_ASN_STATUS_WAITCONFIRM, SysParamConstant.P_ASN_STATUS_CONFIRMED]);
		}

		// 根据mode动态显示页面布局
		loadingPage();
	});

	// 表单验证初始化
	$("#valiForm").validationEngine('attach', {
		// 只在表单提交的时候验证
		validationEventTrigger: null,
		// 错误提示距离顶部偏移量
		scrollOffset: 100,
		isOverflown: true,
		// 自动隐藏-时间3000毫秒
		autoHidePrompt: true,
		autoHideDelay: 3000
	});


	// 查询
	$("#btnSearch").on("click", function () {

		if (!$("#valiForm").validationEngine("validate")) {
			return;
		}
		tableBox.ajax.reload();
	});
})

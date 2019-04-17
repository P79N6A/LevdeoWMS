//列名 列ID 是否增加排序 列宽 是否可检索
columns = [
	{ title: "客户名称", data: "customerName", orderable: true, searchable: true, name: "m.name" },
	{ title: "销售公司", data: "companyName", orderable: true, searchable: true, name: "mc.pname" },
	{ title: "发货单号", data: "sapInvoiceCode", orderable: true, searchable: true, name: "oh.sapInvoiceCode" },
	{ title: "行项目号", data: "itemNo", orderable: true, searchable: true, name: "oh.itemNo" },
	{ title: "物料编码", data: "matCode", orderable: true, searchable: true, name: "oh.matCode" },
	{ title: "物料描述", data: "matName", orderable: true, searchable: true, name: "mainMat.name" },
	{ title: "数量", data: "amount", orderable: true, searchable: true, className: "alignRight", name: "oh.amount" },
	{ title: "VIN码", data: "vin", orderable: true, searchable: true, name: "st.vin" },
	{ title: "出库仓库", data: "productStockName", orderable: true, searchable: true, name: "st.productStockCode" },
	{ title: "库位", data: "stockPositionName", orderable: true, searchable: true, name: "stockPositionName" },
	{ title: "出库日期", data: "outBoundDate", orderable: true, searchable: true, name: "outBoundDate" },
	{ title: "销售单类型", data: "orderType", orderable: true, searchable: true, name: "mot.name" },
	{ title: "状态", data: "outStatus", orderable: true, searchable: true, name: "s.subName" },
	{ title: "已打印次数", data: "printCnt", orderable: true, searchable: true, name: "oh.isPrint" },
];

// 控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
function createdRow(row, data, dataIndex) {

}

// 取得查询条件
var getSearchConditon = function () {
	return {
		"outBoundCode": $("#outBoundCode").val().trim(),
		"outBoundDateFrom": $("#outBoundDateFrom").val(),
		"outBoundDateTo": $("#outBoundDateTo").val(),
		"customerCode": $("#customer").val(),
		"outStatus": $("#outStatus").val(),
		"saleCompany": $("#saleCompany").val() ? $("#saleCompany").val().join(",") : ""
	}
}
$(document).ready(function () {

	$.when.apply($, g_deferreds).done(function () {
		g_deferreds = [];

		$('#saleCompany').selectpicker({
			style: 'btn-white', //样式 可使用 btn-primary 等
			noneSelectedText: '',
		});

		// 获取-全部出库单
		tableBox = initDataTables(
			"tableBox",
			columns,
			createdRow,
			constant.projectURL + '/outInvoiceDetails/getAsnReport',
			getSearchConditon,
			1, 0,
			false,
			true,
			true,
			[3, "desc"],
			null,
			{}
		);
	});

	// 查询
	$("#btnSearch").on("click", function () {
		tableBox.ajax.reload();
	});
	// 查询
	$("#btnExport").on("click", function () {
		exportExcel();
	});
})

/**
 * 导出Excel
 */
function exportExcel() {
	//form添加action
	$("#myHeader form").attr("action", constant.projectURL + '/outInvoiceDetails/exportAsnReport');
	$("#myHeader form")._submit();
}
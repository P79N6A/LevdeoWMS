/**
 * 库区管理页面逻辑控制js
 */
var CONTROLLER_NAME = "/sys-stock-area";

//列名 列ID 是否增加排序 列宽 是否可检索
columns = [
	{ title: "库区编号", data: "stockAreaCode", orderable: true, sWidth: 60, searchable: true, name: "s.stockAreaCode" },
	{ title: "库区名", data: "name", orderable: true, sWidth: 100, searchable: true, name: "s.name" },
	{ title: "库区类型", data: "flagName", orderable: true, sWidth: 60, searchable: true, name: "s.flag" },
	{ title: "停放数量", data: "amount", orderable: true, sWidth: 60, searchable: true, name: "s.amount", className: "alignRight" },
	{ title: "所属仓库", data: "warehouseName", orderable: true, sWidth: 80, searchable: true, name: "warehouseName" },
	{ title: "库区位置", data: "location", orderable: true, searchable: true, name: "s.location" },
];

/**
 * 初始化
 */
$(document).ready(function () {

	//表单验证初始化
	$("#myDetail form").validationEngine("attach", {
		validationEventTrigger: null,
		//错误提示距离顶部偏移量
		scrollOffset: 100,
		isOverflown: true,
	});
	$("#modalForm").validationEngine("attach", {
		validationEventTrigger: null,
		//错误提示距离顶部偏移量
		scrollOffset: 100,
		isOverflown: true,
	});

	//初始化表格( 详细配置 )
	tableBox = initDataTables(
		"tableBox",
		columns,
		createdRow,
		constant.projectURL + CONTROLLER_NAME + '/searchAll',
		getSearchConditon,
		1,
		0,
		false,
		true,
		true);
	//查询按钮-点击事件
	$("#btnSearch").on("click", function () {
		tableBox.ajax.reload();
	});

	//modal-确定按钮-点击事件
	$("#btnModalSure").on("click", function () {
		var formFlg = $("#modalForm").validationEngine("validate");
			if (!formFlg) {
				return;
			}
			addSpFunction();
	});
	
	//之前的库位数量
	var beforeAmount = "";
	$('input[name=areType]').change(function () {

		//临时库区
		if (this.value == '0') {
			$("#amount").attr('disabled', "disabled");
			beforeAmount = vm.area.amount;
			//radio
			vm.area.flag = "0";
			vm.area.amount = constant.tmpSpAmount;
			
		} else if (this.value == '1') {//固定库区
			$("#amount").removeAttr('disabled');
			//radio
			vm.area.flag = "1";
			vm.area.amount = beforeAmount;
		}
	});

})

/**
 * validate验证新增数量是否在1-100之间
 */
function checkAddAmount(){
	//新增数量
	var valiAddAmount = $("#addAmount").val();
	if(valiAddAmount < 1 || valiAddAmount > 999){
		return "* 请输入1-999之间的整数";
	}
}
/**
 * 取得查询条件
 */
function getSearchConditon() {
	return {
		"warehouse": $("#selectWarehouse").data("vue") ? $("#selectWarehouse").data("vue").selected : "",
		"stockArea": $('#areaName').val().trim()
	}
}

/**
 * 控制表格式样及事件 参数含义( 行html, 行数据, 行号 )
 * @param {*} row 
 * @param {*} data 
 * @param {*} dataIndex 
 */
function createdRow(row, data, dataIndex) {
	//双击跳转'详细页面'-传参仓库编号dispatchListNo
	$(row).dblclick(function () {
		var areaCode = $(this).children().eq(1).text().trim();
		vm.update(areaCode);
		$("#amount").attr('disabled', "disabled");
	});
}

/**
 * 新增库位
 */
function addSpFunction(){
	//新增数量
	var addAmount = $("#addAmount").val();
	//总库位数量
	vm.area.amount = Number(addAmount) + Number(vm.area.amount);
	saveOrUpdateFunc();
}
/**
 * 保存或更新库区信息
 */
function saveOrUpdateFunc() {
	var url = constant.projectURL + CONTROLLER_NAME;
	var tail = vm.area.stockAreaCode == null ? "/save" : "/update";
	url = url + tail;
	vm.area.warehouseCode = $('#wareHouse').val().trim();
	vm.area.startNumber = vm.startNO;
	vm.area.endNumber = vm.endNO;
	$._ajax({
		type: "POST",
		contentType: "application/json;charset=UTF-8",
		url: url,
		data: JSON.stringify(vm.area),
		dataType: 'json',
		success: function (r) {
			if (r.data && r.code * 1 === 0) {
				toastr.success(MESSAGE.INFO_SAVE_SUCCESS);
				$("#myModal").modal('hide');
				$("#btnAddSp").show();
				vm.showDelete = true;
				vm.getArea(r.data);
			} else {
				toastr.error(r.msg);
			}
		}
	});
}

var vm = new Vue({
	el: '#rrapp',
	data: {
		showList: true,
		showDelete: false,
		//showModal: false,
		area: {
			stockAreaCode: '',
			name: '',
			location: '',
			acreage: '',
			amount: '',
			remark: '',
			flag: '',
			startNumber: '',
			endNumber: '',
			warehouseCode: '',
			createId: '',
			createTime: '',
			updateId: '',
			updateTime: ''
		}
	},
	computed: {
		startNO: function () {
			if (!this.showList && this.area.name && this.area.amount) {
				return (this.area.name + '-00001');
			}
		},
		endNO: function () {
			if (!this.showList && this.area.name && this.area.amount) {
				var txt = vm.area.name + '-';
				for (var i = 0; i < 5 - (this.area.amount + "").length; i++) {
					txt = txt + '0';
				}
				txt = txt + this.area.amount;
				return txt;
			}
		}
	},
	methods: {
		add: function () {
			$('input[name=areType]').removeAttr('disabled');
			$("#wareHouse").removeAttr('disabled');
			$("#iptStockArea").removeAttr('disabled');
			$("#amount").removeAttr('disabled');
			$("#wareHouse").find("option").attr("selected", false);
			$("#btnAddSp").hide();
			vm.showList = false;
			vm.showDelete = false;
			vm.area = {
				flag: "1"
			};
		},
		update: function (code) {
			if (code == null) {
				return;
			}
			vm.showList = false;
			vm.showDelete = true;
			vm.getArea(code);
		},
		del: function () {
			var code = vm.area.stockAreaCode;
			if (code == null) {
				return;
			}
			var url = constant.projectURL + CONTROLLER_NAME + '/delete';
			openConfirmDialog(MESSAGE.CONFIRM_DELETE, function () {
				$._ajax({
					type: "POST",
					contentType: "application/json;charset=UTF-8",
					url: url,
					data: code,

					// dataType: 'json',
					success: function (r) {
						if (r.code * 1 === 0) {
							toastr.success(MESSAGE.INFO_DELETE_SUCCESS);
							vm.reload();
						} else {
							toastr.error(r.msg);
						}
					}
				});
			});
		},
		saveOrUpdate: function (event) {
			var formFlg = $("#myDetail form").validationEngine("validate");
			if (!formFlg) {
				return;
			}
			//表单验证
			openConfirmDialog(MESSAGE.CONFIRM_SAVE, saveOrUpdateFunc);
				
		},
		getArea: function (code) {
			if (code == null) {
				return;
			}
			var url = constant.projectURL + CONTROLLER_NAME + "/info"
			$._ajax({
				type: "GET",
				url: url,
				data: { "code": code },
				dataType: 'json',
				success: function (r) {
					if (r.data && r.code * 1 === 0) {
						vm.area = r.data;
						$('#wareHouse').val(r.data.warehouseCode);
						$("#wareHouse").attr('disabled', "disabled");
						$("#amount").attr('disabled', "disabled");
						$("#iptStockArea").attr('disabled', "disabled");
						$('input[name=areType]').attr("disabled", "disabled");
					} else {
						toastr.error(r.msg);
					}
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			tableBox.ajax.reload();
		},
		addSp: function (event){
			$("#addAmount").val("");
			//隐藏之前的验证信息
			$("#myModal").validationEngine("hideAll");
			$("#myModal").modal('show');
		}
	}
});
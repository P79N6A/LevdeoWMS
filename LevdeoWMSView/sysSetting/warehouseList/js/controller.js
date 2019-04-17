/**
 * 仓库管理页面逻辑控制js
 */

var CONTROLLER_NAME = "/sys-warehouse";

//列名 列ID 是否增加排序 列宽 是否可检索
columns = [
	{ title: "仓库编号", data: "warehouseCode", orderable: true, sWidth: 60, searchable: true, name: "s.warehouseCode" },
	{ title: "仓库名", data: "name", orderable: true, sWidth: 150, searchable: true, name: "s.name" },
	{ title: "负责人", data: "contact", orderable: true, sWidth: 80, searchable: true, name: "s.contact" },
	{ title: "电话", data: "phoneNo", orderable: true, sWidth: 80, searchable: true, name: "s.phoneNo" },
	{ title: "地址", data: "address", orderable: true, searchable: true, name: "s.address" },
	{ title: "第一优先级", data: "priorityLv1Name", orderable: true, sWidth: 80, searchable: true, name: "s.priorityLV1" },
	{ title: "第二优先级", data: "priorityLv2Name", orderable: true, sWidth: 80, searchable: true, name: "s.priorityLV2" },
	{ title: "第三优先级", data: "priorityLv3Name", orderable: true, sWidth: 80, searchable: true, name: "s.priorityLV3" },
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

	//初始化表格( 详细配置 )
	tableBox = initDataTables(
		"tableBox",
		columns,
		createdRow,
		constant.projectURL + CONTROLLER_NAME + '/searchAll',
		getSearchConditon,
		0,
		0,
		false,
		true,
		true);
	$("#btnSearch").on("click", function () {
		tableBox.ajax.reload();
	});
})

/**
 * 取得查询条件
 */
function getSearchConditon() {
	return {
		"wareHouse": $("#selWareHouse").data("vue") ? $("#selWareHouse").data("vue").selected : "",
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
		var warehouseCode = $(this).children().eq(1).text().trim();
		vm.update(warehouseCode);
	});
}

var vm = new Vue({
	el: '#rrapp',
	data: {
		showList: true,
		showDelete: false,
		title: null,
		house: {
			warehouseCode: '',
			name: '',
			address: '',
			contact: '',
			phoneNo: '',
			remark: '',
			priorityLV1: '',
			priorityLV2: '',
			priorityLV3: '',
			createId: '',
			createTime: '',
			updateId: '',
			updateTime: ''
		}
	},
	methods: {
		add: function () {
			vm.showList = false;
			vm.showDelete = false;
			//取消下拉选中
			$("#priorityLV1").find("option").prop('selected', false);
			$("#priorityLV2").find("option").prop('selected', false);
			$("#priorityLV3").find("option").prop('selected', false);
			//清空validate验证信息
			$("#myDetail form").validationEngine("hide");
			vm.title = "新增";
			vm.house = {};
		},
		update: function (code) {
			if (code == null) {
				return;
			}
			vm.showList = false;
			vm.showDelete = true;
			vm.title = "修改";
			vm.getHouse(code);
		},
		del: function () {
			var code = vm.house.warehouseCode;
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
			//表单验证
			var formFlg = $("#myDetail form").validationEngine("validate");
			if (!formFlg) {
				return;
			}

			if ($("#priorityLV1").val() == $("#priorityLV2").val() ||
				$("#priorityLV1").val() == $("#priorityLV3").val() ||
				$("#priorityLV2").val() == $("#priorityLV3").val()) {
				toastr.warning(MESSAGE.WARN_CHECK_PRIORITYLV);
				return;
			}

			openConfirmDialog(MESSAGE.CONFIRM_SAVE, function () {
				var url = constant.projectURL + CONTROLLER_NAME;
				var tail = vm.house.warehouseCode == null ? "/save" : "/update";

				url = url + tail;
				vm.house.priorityLV1 = $("#priorityLV1").val();
				vm.house.priorityLV2 = $("#priorityLV2").val();
				vm.house.priorityLV3 = $("#priorityLV3").val();
				vm.house.createId = "";
				vm.house.updateId = "";
				$._ajax({
					type: "POST",
					contentType: "application/json;charset=UTF-8",
					url: url,
					data: JSON.stringify(vm.house),
					dataType: 'json',
					success: function (r) {
						if (r.code * 1 === 0) {
							toastr.success(MESSAGE.INFO_SAVE_SUCCESS);
							//获取保存后的仓库信息
							vm.getHouse(r.data.warehouseCode);
							//显示删除按钮
							vm.showDelete = true;
						} else {
							toastr.error(r.msg);
						}
					}
				});
			});
		},
		getHouse: function (code) {
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
						vm.house = r.data;
						$('#priorityLV1').val(r.data.priorityLV1);
						$('#priorityLV2').val(r.data.priorityLV2);
						$('#priorityLV3').val(r.data.priorityLV3);
					} else {
						toastr.error(r.msg);
					}
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			tableBox.ajax.reload();
		}
	}
});
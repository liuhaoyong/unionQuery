/**********************************
***								***
***			前端数据模拟		***
***								***
**********************************/
/* 模拟 新增/编辑 任务数据 */
// Mock.mock("/dt/Task_edit.htm", {
// 	platforms: [{
// 		code: "1",
// 		id: "30650e93-835e-4bf6-94a3-8e55d6667259",
// 		subtype: "挖财IOS"
// 	}, {
// 		code: "2",
// 		id: "30650e93-835e-4bf6-94a3-",
// 		subtype: "挖财Android"
// 	}, {
// 		code: "3",
// 		id: "30650e93-835e-4bf6-94a3-8e5d6667259",
// 		subtype: "钱管家IOS"
// 	}, {
// 		code: "4",
// 		id: "30650e93-835e-4bf6-94a3-8d6667259",
// 		subtype: "钱管家Android"
// 	}, {
// 		code: "5",
// 		id: "30650e93-835e-4bf-94a3-8e6667259",
// 		subtype: "挖财宝IOS"
// 	}, {
// 		code: "6",
// 		id: "30650e93-835e-4bf6-94a3-8e55d6669",
// 		subtype: "挖财宝IOS"
// 	}]
// });
// Mock.mock(/\/dt\/Task_edit\.htm\?task\.id=/, function() {
// 	return {
// 		id: Random.natural(100000, 10000000),
// 		name: Random.string("upper", 12),
// 		subName: Random.string("upper", 12),
// 		userRange: Random.natural(0, 1),
// 		status: Random.natural(0, 1),
// 		description: Random.string("upper", 50),
// 		url: "",
// 		finish: 0,
// 		award: 0,
// 		pic: "http://wacai-file.b0.upaiyun.com/img/web/core/logo_5.png",
// 		showBtn: Random.natural(0, 1),
// 		activeBtnName: "签到",
// 		finishBtnName: "已签到",
// 		orderNo: "1",
// 		recommend: Random.natural(0, 1),
// 		platCodes: Random.range(1, 7, 2),
// 		platforms: [{
// 			code: "1",
// 			id: "30650e93-835e-4bf6-94a3-8e55d6667259",
// 			subtype: "挖财IOS"
// 		}, {
// 			code: "2",
// 			id: "30650e93-835e-4bf6-94a3-",
// 			subtype: "挖财Android"
// 		}, {
// 			code: "3",
// 			id: "30650e93-835e-4bf6-94a3-8e5d6667259",
// 			subtype: "钱管家IOS"
// 		}, {
// 			code: "4",
// 			id: "30650e93-835e-4bf6-94a3-8d6667259",
// 			subtype: "钱管家Android"
// 		}, {
// 			code: "5",
// 			id: "30650e93-835e-4bf-94a3-8e6667259",
// 			subtype: "挖财宝IOS"
// 		}, {
// 			code: "6",
// 			id: "30650e93-835e-4bf6-94a3-8e55d6669",
// 			subtype: "挖财宝IOS"
// 		}]
// 	};
// });

/* 模拟任务列表数据 */
// Mock.mock(/\/dt\/Task_list\.htm\?pageInfo\.pageSize=\d*&pageInfo\.pageIndex=\d*/, function() {
// 	return {
// 		pageInfo: {
// 			firstRow: 1,
// 			lastRow: 50,
// 			offset: 0,
// 			pageCount: 3,
// 			pageIndex: 1,
// 			pageSize: 50,
// 			rows: 50,
// 			size: 15,
// 			totalCount: 123
// 		},
// 		tasks: [{
// 			id: Random.natural(100000, 10000000),
// 			name: Random.string("upper", 12),
// 			subName: Random.string("upper", 12),
// 			userRange: Random.natural(0, 1),
// 			status: Random.natural(0, 1),
// 			description: Random.string("upper", 50),
// 			url: "http://www.wacai.com",
// 			finish: 0,
// 			award: 0,
// 			pic: "http://wacai-file.b0.upaiyun.com/img/web/core/logo_5.png",
// 			showBtn: Random.natural(0, 1),
// 			activeBtnName: "签到",
// 			finishBtnName: "已签到",
// 			orderNo: "1",
// 			finishTime: Random.natural(0, 500),
// 			recommend: Random.natural(0, 1),
// 			platCodes: Random.range(1, 7, 2)
// 		}, {
// 			id: Random.natural(100000, 10000000),
// 			name: Random.string("upper", 12),
// 			subName: Random.string("upper", 12),
// 			userRange: Random.natural(0, 1),
// 			status: Random.natural(0, 1),
// 			description: Random.string("upper", 50),
// 			url: "http://www.wacai.com",
// 			finish: 0,
// 			award: 0,
// 			pic: "http://wacai-file.b0.upaiyun.com/img/web/core/logo_5.png",
// 			showBtn: Random.natural(0, 1),
// 			activeBtnName: "签到",
// 			finishBtnName: "已签到",
// 			orderNo: "2",
// 			finishTime: Random.natural(0, 500),
// 			recommend: Random.natural(0, 1),
// 			platCodes: Random.range(1, 7, 2)
// 		}, {
// 			id: Random.natural(100000, 10000000),
// 			name: Random.string("upper", 12),
// 			subName: Random.string("upper", 12),
// 			userRange: Random.natural(0, 1),
// 			status: Random.natural(0, 1),
// 			description: Random.string("upper", 50),
// 			url: "http://www.wacai.com",
// 			finish: 0,
// 			award: 0,
// 			pic: "http://wacai-file.b0.upaiyun.com/img/web/core/logo_5.png",
// 			showBtn: Random.natural(0, 1),
// 			activeBtnName: "签到",
// 			finishBtnName: "已签到",
// 			orderNo: "2",
// 			finishTime: Random.natural(0, 500),
// 			recommend: Random.natural(0, 1),
// 			platCodes: Random.range(1, 7, 2)
// 		}, {
// 			id: Random.natural(100000, 10000000),
// 			name: Random.string("upper", 12),
// 			subName: Random.string("upper", 12),
// 			userRange: Random.natural(0, 1),
// 			status: Random.natural(0, 1),
// 			description: Random.string("upper", 50),
// 			url: "http://www.wacai.com",
// 			finish: 0,
// 			award: 0,
// 			pic: "http://wacai-file.b0.upaiyun.com/img/web/core/logo_5.png",
// 			showBtn: Random.natural(0, 1),
// 			activeBtnName: "签到",
// 			finishBtnName: "已签到",
// 			orderNo: "5",
// 			finishTime: Random.natural(0, 500),
// 			recommend: Random.natural(0, 1),
// 			platCodes: Random.range(1, 7, 2)
// 		}]
// 	};
// });
// $.ajax({
//     url: '/dt/Task_edit.htm',
//     dataType: 'json',
//     success: function(data) {
// 		console.log(data);
//     $('<pre>').text(JSON.stringify(data, null, 4))
//         .appendTo('body');
//     }
// });

// $.ajax({
//     url: '/dt/Task_edit.htm&id=1',
//     dataType: 'json',
//     success: function(data) {
// 		console.log(data);
//     $('<pre>').text(JSON.stringify(data, null, 4))
//         .appendTo('body');
//     }
// });

var taskModel,
	listRuleModel;

var oFReader = new FileReader();
var rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

$(function() {

	var taskModelData = {
		task: {
			id: "",
			title: "",
			subtitle: "",
			userRange: 0,
			status: 0,
			description: "",
			url: "",
			finish: 0,
			award: 0,
			pic: "",
			showBtn: true,
			activeBtnName: "",
			finishBtnName: "",
			orderNo: "",
			recommend: false,
			awardTimes: 0,
			maxTimes: 0,
			urlType: 0,
			code: "",
			extInfo: {
				picUrl: ""
			}
		},
		platCodes: [],
		platforms: []
	};
	/**
	 * 
	 * avalon 初始化任务弹出框
	 */
	taskModel = avalon.define({
		$id: "editTask",
		$defaultData: taskModelData,
		$url: "/dt/Task_edit.htm",
		$userIdFile: "<input type='file' name='userIdFile' id='userIdFile'>",
		$imgFile: "<input type='file' name='pic' ms-change='$updatePic' id='taskPic'>",
		data: avalon.mix(true, {}, taskModelData),
		isEdit: false,

		/* 更新model数据,重新渲染View */
		$updateModel: function(ajaxData, isEdit) {
			var model = taskModel,
				needData;

			ajaxData.platforms = base.mapToArray(ajaxData.platforms, "code", "name");
			needData = bops.av.getModelData(model.$defaultData, ajaxData);
			model.isEdit = !!isEdit;
			avalon.vmMix(true, model.data, needData);			
			avalon.scan($("#editTask")[0], model);
		},
		/* 更新商品图片 */
		$updatePic: function() {
			var oFile = this.files[0];

			if (oFile.length === 0) return;
			if (!rFilter.test(oFile.type)) {
				alert("必须选择一个有效的图片!");
				return;
			}
			oFReader.readAsDataURL(oFile);
			oFReader.onload = function(oFREvent) {
				taskModel.data.task.extInfo.picUrl = oFREvent.target.result;
			};
		}
	});

	/**
	 * avalon 初始化任务列表
	 */
	listTaskModel = avalon.define({
		$id: "taskList",
		$url: "/dt/Task_list.htm?pageInfo.pageSize=50&pageInfo.pageIndex=1",
		data: {
			pageInfo: {
				pageList: []
			},
			tasks: []
		},
		checkeds: [],

		$updateModel: function(ajaxData) {
			var model = listTaskModel;

			model.checkeds.clear();
			avalon.vmMix(true, model.data, ajaxData);			
			avalon.scan($("#listContent")[0], model);
			setTimeout(function() {
				$("#listContent").removeClass("loading").find("table").stop().animate({"opacity": "1"}, 300);
			}, 200);
		},
		$showEdit: function() {			
			var id = $(this).attr("data-id");

			bops.av.updataEditView(taskModel, "task.id=" + id);
			$("#editTask").modal("show");
		},
		$deleteTask: function() {
			var id = $(this).attr("data-id"),
				ensure = confirm("确定要删除任务么");

			if (ensure) {
				$.ajax({
					type: "POST",
					url: "/dt/Task_updateStatus.htm",
					data: base.paramData({"ids": [id], "status": 2})
				}).done(function(ajaxData) {
					alert("删除成功!");			
					bops.av.updataListView(listTaskModel, listTaskModel.data.pageInfo.pageIndex);
				}).fail(function(err) {
					alert(err.responseText);
				});
			}
		},
		$jumpPage: function() {
			var model = listTaskModel,
				targetPage = $(this).attr("data-page"),
				maxPage = model.data.pageInfo.pageCount;

			if (targetPage < 1 || targetPage > maxPage) return;
			$("#listContent").addClass("loading").find("table").stop().css({"opacity": 0});
			bops.av.updataListView(model, targetPage);
		}
	});

	/* 设置状态 */
	function updateStatus(status) {
		var submitData = listTaskModel.checkeds.$model;

		if (submitData.length === 0) return;
		$.ajax({
			type: "POST",
			url: "/dt/Task_updateStatus.htm",
			data: base.paramData({"ids": submitData, "status": status})
		}).done(function(ajaxData) {
			alert("提交成功!");			
			bops.av.updataListView(listTaskModel, listTaskModel.data.pageInfo.pageIndex);
		}).fail(function(err) {
			alert(err.responseText);
		});
	}

	/* 保存顺序 */
	function updateOrderNo() {
		var submitData = [];

		$.each(listTaskModel.data.tasks.$model, function(i, val){
			submitData.push({"id": val.id, "orderNo": val.orderNo});
		});
		if (submitData.length === 0) return;
		$.ajax({
			type: "POST",
			url: "/dt/Task_updateOrderNo.htm",
			data: base.paramData(submitData, "tasks")
		}).done(function(ajaxData) {
			alert("提交成功!");			
			bops.av.updataListView(listTaskModel, listTaskModel.data.pageInfo.pageIndex);
		}).fail(function(err) {
			alert(err.responseText);
			bops.av.updataListView(listTaskModel, listTaskModel.data.pageInfo.pageIndex);
		});
	}

	/**
	 * 页面DOM事件绑定
	 */
	// 新增任务
	$("#addTask").on("click", function() {
		bops.av.updataEditView(taskModel);
		$("#editTask").modal("show");
	});

	var canSubmit = true;

	$("#taskSubmit").on("click", function() {
		var modelData = taskModel.$model;

		if (!canSubmit) return false;
		if (!modelData.isEdit) {
			if (modelData.data.userRange == 1 && $("#userIdFile").val() === "") {
				alert("请选择指定用户文件");
				return false;
			}
			if ($("#taskPic").val() === "") {
				alert("请选择任务图片");
				return false;	
			}
		}

		canSubmit = false;
		$("#taskForm").ajaxSubmit({
			type: "POST",
			url: "/dt/Task_save.htm",
			success: function(ajaxData) {
				alert("提交成功!");			
				bops.av.updataListView(listTaskModel, listTaskModel.data.pageInfo.pageIndex);
				$("#editTask").modal("hide");
				canSubmit = true;
			},
			error: function(err) {
				alert(err.responseText);
				canSubmit = true;
			}
		});
	});

	// 设置状态
	$("#disabledStatus").on("click", function() {
		updateStatus(0);
	});
	$("#activeStatus").on("click", function() {
		updateStatus(1);
	});
	// 修改顺序
	$("#updateOrderNo").on("click", function() {
		updateOrderNo();
	});

	// 默认进第一页
	$("#listContent").addClass("loading").find("table").stop().css({"opacity": 0});
	bops.av.updataListView(listTaskModel);
});
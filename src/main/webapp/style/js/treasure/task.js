/**********************************
***								***
***			前端数据模拟		***
***								***
**********************************/

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
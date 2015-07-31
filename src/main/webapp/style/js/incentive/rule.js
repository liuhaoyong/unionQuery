var editRuleModel,
listRuleModel;

$(function() {

	/**
	 * avalon 初始化金币规则弹出框
	 */
	editRuleModel = avalon.define("editRule", function(vm) {
		// 默认数据
		vm.defaultData = {
			rule: {
				id: "",
				name: "",
				scoreRule: "",
				score: "", 		//	如果规则表达式不为空，则默认选中 “按规则表达式计算”
				unique: 0,
				startTime: (new Date()).Format(),
				endTime: (new Date(new Date().getTime() + 31536000000)).Format(),
				status: 1,
				task: "true",
				tradeType: 0,
				description: ""
			},
			codes: [],	// 选中的平台
			ids: []			// app内部链接
		};
		vm.submitData = avalon.mix(true, {}, vm.defaultData);
		vm.url = "/inc/Rule_edit.htm";	// 更新数据的url
		// 辅助数据
		vm.isEdit = false;
		vm.scoreType = +!!vm.submitData.rule.scoreRule;
		vm.typePlatformsArr = [];
		vm.showScale = function() {
			var task = vm.submitData.rule.task == 'true',
			scoreType = vm.scoreType == 1;

			return task && scoreType;
		};


		/* 重新渲染 */
		vm.updateView = function(ajaxData, isEdit) {
			var typePlatformsArr = [],
				item, i;

			if (isEdit) ajaxData.rule.task += "";	//	task转成字符串

			ajaxData = avalon.mix(true, {}, vm.defaultData.$model, ajaxData);

			for(i in ajaxData.platAnchorIds) ajaxData.ids.push(ajaxData.platAnchorIds[i]);
			for (i in ajaxData.typePlatforms) {

				item = {
					"type": i,
					"platforms": ajaxData.typePlatforms[i], 	// 平台列表
					"platAnchors": ajaxData.platAnchors[i], 	// app内部链接,
					"platformsTypeName": bops.incentive.getPlatformsTypeName(i),	//	总平台
					"codes": [],	// 已选中的平台
					"id": null
				};

				// 筛选出已经选中的平台
				if (isEdit && ajaxData.codes && ajaxData.codes.length) {
					$.each(item.platforms, function(i, val) {
						var code = val.code;

						if ($.inArray(code, ajaxData.codes) !== -1) item.codes.push(code);
					});
				}
				// 筛选出已经选中 内部链接
				if (isEdit && ajaxData.rule.task == "true" && ajaxData.ids && ajaxData.ids.length) {
					$.each(item.platAnchors, function(i, val) {
						var id = val.id;

						if ($.inArray(id, ajaxData.ids) !== -1) {
							item.id = id;
							return false;
						}
					});
				}
				
				ajaxData.platAnchors[i].unshift({// 默认添加一个空的内部链接
					id: 0,
					name: "空",
					platform: i,
					url: ""
				});
				typePlatformsArr.push(item);
			}

			delete ajaxData.id;
			delete ajaxData.typePlatforms;
			delete ajaxData.platAnchorIds;
			delete ajaxData.platAnchors;
			vm.isEdit = isEdit;
			vm.submitData = avalon.vmMix(true, vm.submitData, ajaxData);
			vm.typePlatformsArr = typePlatformsArr;
			vm.scoreType = +!!vm.submitData.rule.scoreRule;
			avalon.scan($("#editRule")[0], editRuleModel);
		};
		/* 可见平台全选按钮事件 */
		vm.checkAllPlatform = function() {
			var codes = vm.typePlatformsArr[this.value].codes,
				$sublingsLabel = $(this).parent().parent().siblings("label"),
				isChecked = this.checked,
				allCodes = [],
				submitCodes = vm.submitData.codes;

			submitCodes.removeAll(codes);		// 更新submitData.codes;
			codes.clear();		// 全反选
			if (isChecked) {	// 全选
				$sublingsLabel.each(function() {
					var val = $(this).find("input").val();
					allCodes.push($(this).find("input").val());
					submitCodes.ensure(val);	// 更新submitData.codes;
				});

				codes.pushArray(allCodes);
			}
		};
		/* 更新submitData.codes .typePlatformsArr每个元素对象的codes合并) */
		vm.updateCodes = function() {
			var codes = vm.submitData.codes,
				val = this.value;

			if (this.checked) {
				codes.ensure(val);
			} else {
				codes.remove(val);
			}

			vm.updateIds();
		};
		/* 更新submitData.ids .typePlatformsArr每个元素对象的ids集合) */
		vm.updateIds = function() {
			var ids = vm.submitData.ids;

			ids.clear();
			if (vm.submitData.rule.task == "true") {	// 为任务
				avalon.each(vm.typePlatformsArr, function(i, val) {
					var id = val.id;

					if (id && val.codes.length) ids.ensure(id);
				});
			}
		};
	});

	/* 监听 是否为任务 切换,更新 ids */
	editRuleModel.submitData.rule.$watch("task", function(newValue, oldValue) {
		editRuleModel.updateIds();
	});



	/**
	 * 金币规则列表VM
	 */
	listRuleModel = avalon.define("ruleList", function(vm) {
		vm.defaultData = {
			pageInfo: {
				pageList: []
			},
			rules: []
		};
		vm.data = avalon.mix({}, vm.defaultData);
		vm.url = "/inc/Rule_list.htm";	// 更新数据的url
		vm.checkedRules = [];

		/* 重新渲染 */
		vm.updateView = function(ajaxData) {
			vm.checkedRules.clear();	// 清除vm已选中
			vm.data = avalon.mix(vm.data, ajaxData);	// 更新数据
			avalon.scan($("#ruleContent")[0], listRuleModel);
			setTimeout(function() {
				$("#ruleContent").removeClass("loading").find("table").stop().animate({"opacity": "1"}, 300);
			}, 200);
		};
		/* 初始化显示弹出框事件 */
		vm.showEditRule = function() {
			var id = $(this).attr("data-id");

			bops.incentive.updataEditView(editRuleModel, "id=" + id);
			$("#editRule").modal("show");
		};
		/* 页面跳转功能 */
		vm.jumpPage = function() {
			var targetPage = $(this).attr("data-page"),
				maxPage = vm.data.pageInfo.pageCount;

			if (targetPage < 1 || targetPage > maxPage) return;
			$("#ruleContent").addClass("loading").find("table").stop().css({"opacity": 0});
			bops.incentive.updataListView(listRuleModel, targetPage);
		};
	});

	/* 设置状态 */
	function updateStatus(status) {
		var submitData = listRuleModel.checkedRules.$model;

		if (submitData.length === 0) return;
		$.ajax({
			type: "POST",
			url: "/inc/Rule_updateStatus.htm",
			data: base.paramData({"ids": submitData, "status": status})
		}).done(function(ajaxData) {
			alert("提交成功!");			
			bops.incentive.updataListView(listRuleModel, listRuleModel.data.pageInfo.pageIndex);
		}).fail(function(err) {
			alert(err.responseText);
		});
	}

	/* 保存顺序 */
	function updateOrderNo() {
		var submitData = [];

		$.each(listRuleModel.data.rules.$model, function(i, val){
			submitData.push({"id": val.id, "orderNo": val.orderNo});
		});
		if (submitData.length === 0) return;
		$.ajax({
			type: "POST",
			url: "/inc/Rule_updateOrderNo.htm",
			data: base.paramData(submitData, "rules")
		}).done(function(ajaxData) {
			alert("提交成功!");			
			// bops.incentive.updataListView(listRuleModel, listRuleModel.data.pageInfo.pageIndex);
		}).fail(function(err) {
			alert(err.responseText);
			bops.incentive.updataListView(listRuleModel, listRuleModel.data.pageInfo.pageIndex);
		});
	}


	/**
	 * 页面DOM事件绑定
	 */
	// 新增金币规则
	$("#addRule").on("click", function() {
		bops.incentive.updataEditView(editRuleModel);
		$("#editRule").modal("show");
	});
	var canSubmit = true;
	$("#addRuleSubmit").on("click", function() {
		var submitData = editRuleModel.submitData.$model;


		if (!canSubmit) return;
		if (new Date(submitData.rule.startTime).getTime() > new Date(submitData.rule.endTime).getTime()) {
			alert("开始时间不能晚于结束时间");
			return;
		}

		if (submitData.rule.description === "") {			
			alert("描述不能为空");
			return;
		}

		/*  查看金币数选项，切换到定额，规则置为空 */
		if (editRuleModel.scoreType == "0") {			
			editRuleModel.submitData.rule.scoreRule = "";
		} else if (editRuleModel.submitData.rule.task == "false") {
			editRuleModel.submitData.rule.score = "";
		}

		canSubmit = false;
		$.ajax({
			type: "POST",
			url: "/inc/Rule_save.htm",
			data: base.paramData(submitData)
		}).done(function(ajaxData) {
			alert("提交成功!");			
			bops.incentive.updataListView(listRuleModel, listRuleModel.data.pageInfo.pageIndex);
			$("#editRule").modal("hide");
			canSubmit = true;
		}).fail(function(err) {
			alert(err.responseText);
			canSubmit = true;
		});
	});
	// 设置状态
	$("#disabledStatus").on("click", function() {
		updateStatus(4);
	});
	$("#activeStatus").on("click", function() {
		updateStatus(1);
	});
	// 修改顺序
	$("#updateOrderNo").on("click", function() {
		updateOrderNo();
	});


	$("#ruleContent").addClass("loading").find("table").stop().css({"opacity": 0});
	// 默认进第一页
	bops.incentive.updataListView(listRuleModel, 1);
});
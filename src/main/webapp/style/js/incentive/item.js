var listItemModel;
var editItemModel;

var oFReader = new FileReader();
var rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

$(function() {

	/**
	 * 商品弹出框VM
	 */
	editItemModel = avalon.define("editItem", function(vm) {
		vm.defaultData = {
			platCodes: [],
			item: {
				id: "",
				type: 1,
				img: "",
				title: "",
				coin: "",
				price: "",
				status: 0,
				ruleId: 0,
				count: 5000,
				cycle: 0,
				times: 0,
				notice: "",
				codeList: "",
				comment: "",
				imgUrl: ""
	        }
		};
		vm.submitData = avalon.mix(true, {}, vm.defaultData);
		vm.isEdit = false;
		vm.url = "/inc/Item_edit.htm";
		vm.typePlatformsArr = [];
		vm.rules = [];
		vm.ruleId = "";
		vm.imgFile = '<input type="file" name="img" ms-change="updateImg" value="">';
		vm.inputFile = '<input type="file" name="store">';

		vm.updateView = function(ajaxData, isEdit) {
			var typePlatformsArr = [],
				item, i;
			
			ajaxData = avalon.mix(true, {}, vm.defaultData.$model, ajaxData);

			for (i in ajaxData.typePlatforms) {
				item = {
					"type": i,
					"platforms": ajaxData.typePlatforms[i], 	// 平台列表
					"platformsTypeName": bops.incentive.getPlatformsTypeName(i),	//	总平台
					"platCodes": []	// 已选中的平台
				};

				// 筛选出已经选中的平台
				if (isEdit && ajaxData.platCodes && ajaxData.platCodes.length) {
					$.each(item.platforms, function(i, val) {
						var code = val.code;

						if ($.inArray(code, ajaxData.platCodes) !== -1) item.platCodes.push(code);
					});
				}

				typePlatformsArr.push(item);
			}

			delete ajaxData.id;
			delete ajaxData.typePlatforms;
			vm.rules = ajaxData.rules;
			delete ajaxData.rules;
			vm.isEdit = isEdit;
			vm.submitData = avalon.vmMix(true, vm.submitData, ajaxData);
			vm.typePlatformsArr = typePlatformsArr;
			avalon.scan($("#editItem")[0], editItemModel);

			// 更新一些值,触发change
			vm.ruleId = "";
			vm.ruleId = ajaxData.item.ruleId;
			vm.imgFile = "";
			vm.imgFile = '<input type="file" name="img" ms-change="updateImg" value="">';
			vm.inputFile = "";
			vm.inputFile = '<input type="file" name="store">';
		};
		/* 更新上架状态 */
		vm.updateStatus = function() {
			vm.submitData.item.status = +this.checked;
		};
		/* 更新商品图片 */
		vm.updateImg = function() {
			var oFile = this.files[0];

			if (oFile.length === 0) return;
			if (!rFilter.test(oFile.type)) {
				alert("必须选择一个有效的图片!");
				return;
			}
			oFReader.readAsDataURL(oFile);
			oFReader.onload = function(oFREvent) {
				vm.submitData.item.imgUrl = oFREvent.target.result;
			};
		};
		/* 可见平台全选按钮事件 */
		vm.checkAllPlatform = function() {
			var platCodes = vm.typePlatformsArr[this.value].platCodes,
				$sublingsLabel = $(this).parent().parent().siblings("label"),
				isChecked = this.checked,
				allCodes = [],
				submitCodes = vm.submitData.platCodes;

			submitCodes.removeAll(platCodes);		// 更新submitData.platCodes;
			platCodes.clear();		// 全反选
			if (isChecked) {	// 全选
				$sublingsLabel.each(function() {
					var val = $(this).find("input").val();
					allCodes.push($(this).find("input").val());
					submitCodes.ensure(val);	// 更新submitData.platCodes;
				});

				platCodes.pushArray(allCodes);
			}
		};
		/* 更新submitData.platCodes .typePlatformsArr每个元素对象的platCodes合并) */
		vm.updateCodes = function() {
			var platCodes = vm.submitData.platCodes,
				val = this.value;

			if (this.checked) {
				platCodes.ensure(val);
			} else {
				platCodes.remove(val);
			}
		};
		vm.updateRuleId = function() {
			vm.submitData.item.ruleId = vm.ruleId;
		};
	});


	/* 
	 *	商城规则列表VM
	 */
	listItemModel = avalon.define("itemList", function(vm) {
		vm.data = {
			pageInfo: {
				pageList: []
			},
			items: []
		};
		vm.url = "/inc/Item_list.htm";	// 更新数据的url
		vm.checkedItems = [];

		/* 重新渲染 */
		vm.updateView = function(ajaxData) {
			vm.checkedItems.clear();	// 清除vm已选中
			if (ajaxData.items) {
				$.each(ajaxData.items, function(i, v) {
					v.time = new Date(v.createTime*1000).Format();
				});
			}
			vm.data = ajaxData;	// 更新数据
			avalon.scan($("#itemContent")[0], listItemModel);
			setTimeout(function() {
				$("#itemContent").removeClass("loading").find("table").stop().animate({"opacity": "1"}, 300);
			}, 200);
		};
		/* 初始化显示弹出框事件 */
		vm.showEditItem = function() {
			var id = $(this).attr("data-id");

			bops.incentive.updataEditView(editItemModel, "item.id=" + id);
			$("#editItem").modal("show");
		};
		/* 补货 */
		vm.showeUpdateCount = function() {
			var id = $(this).attr("data-id"),
				title = $(this).attr("data-title");

			updateCount.title = title;
			updateCount.updateView({
				id: id,
				changeCount: 0
			});
			$("#updateCount").modal("show");
		};
		/* 页面跳转功能 */
		vm.jumpPage = function() {
			var targetPage = $(this).attr("data-page"),
				maxPage = vm.data.pageInfo.pageCount;

			if (targetPage < 1 || targetPage > maxPage) return;
			$("#itemContent").addClass("loading").find("table").stop().css({"opacity": 0});
			bops.incentive.updataListView(listItemModel, targetPage);
		};
	});


	/* 
	 *	补货VM
	 */
	var updateCount = avalon.define("updateCount", function(vm) {
		vm.defaultData = {
			id: "",
			changeCount: 0
		};
		vm.submitData = avalon.mix({}, vm.defaultData);

		vm.canSubmit = true;
		vm.title = "";

		/* 重新渲染 */
		vm.updateView = function(data) {

			vm.submitData = avalon.mix({}, data);	// 更新数据

			avalon.scan($("#updateCount")[0], updateCount);
		};
		vm.submitUpdateCount = function() {
			var submitData = updateCount.submitData.$model;

			if (!vm.canSubmit) return;

			vm.canSubmit = false;
			$.ajax({
				type: "POST",
				url: "/inc/Item_updateCount.htm",
				data: "item.id=" + submitData.id + "&item.changeCount=" + submitData.changeCount
			}).done(function(ajaxData) {
				alert("提交成功!");			
				$("#updateCount").modal("hide");
				bops.incentive.updataListView(listItemModel, listItemModel.data.pageInfo.pageIndex);
				vm.canSubmit = true;
			}).fail(function(err) {
				alert(err.responseText);
				vm.canSubmit = true;
			});
		};
	});

	/* 设置状态 */
	function updateStatus(status) {
		var submitData = listItemModel.checkedItems.$model;

		if (submitData.length === 0) return;
		$.ajax({
			type: "POST",
			url: "/inc/Item_updateStatus.htm",
			data: base.paramData({"ids": submitData, "status": status})
		}).done(function(ajaxData) {
			alert("提交成功!");			
			bops.incentive.updataListView(listItemModel, listItemModel.data.pageInfo.pageIndex);
		}).fail(function(err) {
			alert(err.responseText);
		});
	}

	/* 保存顺序 */
	function updateOrderNo() {
		var submitData = [];

		$.each(listItemModel.data.items.$model, function(i, val){
			submitData.push({"id": val.id, "orderNo": val.orderNo});
		});
		if (submitData.length === 0) return;
		$.ajax({
			type: "POST",
			url: "/inc/Item_updateOrderNo.htm",
			data: base.paramData(submitData, "items")
		}).done(function(ajaxData) {
			alert("提交成功!");			
			// bops.incentive.updataListView(listItemModel, listItemModel.data.pageInfo.pageIndex);
		}).fail(function(err) {
			alert(err.responseText);
			bops.incentive.updataListView(listItemModel, listItemModel.data.pageInfo.pageIndex);
		});
	}


	/**
	 * 页面DOM事件绑定
	 */
	$("#addItem").on("click", function() {
		bops.incentive.updataEditView(editItemModel);		
		$("#editItem").modal("show");
	});

	var canSubmit = true;
	$("#addItemSubmit").on("click", function() {
		if (!canSubmit) return;

		canSubmit = false;
		$("#editItemForm").ajaxSubmit({
			type: "POST",
			url: "/inc/Item_save.htm",
			success: function(ajaxData) {
				alert("提交成功!");			
				bops.incentive.updataListView(listItemModel, listItemModel.data.pageInfo.pageIndex);
				$("#editItem").modal("hide");
				canSubmit = true;
			},
			error: function(err) {
				alert(err.responseText);
				canSubmit = true;
			}
		});
	});
	$("#upStatus").on("click", function() {
		updateStatus(1);
	});
	$("#downStatus").on("click", function() {
		updateStatus(0);
	});
	$("#deleteItem").on("click", function() {
		var bool = confirm("确定执行删除操作么?");

		if (bool) updateStatus(2);
	});
	$("#updateOrderNo").on("click", function() {
		updateOrderNo();
	});

	
	
	$("#itemContent").addClass("loading").find("table").stop().css({"opacity": 0});
	// 默认进第一页
	bops.incentive.updataListView(listItemModel, 1);
});
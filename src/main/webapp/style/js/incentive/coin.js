var listCoinModel;



$(function() {
	/* 
	 *	金币发放记录列表VM
	 */
	listCoinModel = avalon.define("coinList", function(vm) {
		vm.data = {
			pageInfo: {
				pageList: []
			},
			issues: []
		};
		vm.url = "/inc/Coin_issues.htm";	// 更新数据的url

		/* 重新渲染 */
		vm.updateView = function(ajaxData) {
			if (ajaxData.issues) {
				$.each(ajaxData.issues, function(i, v) {
					v.time = new Date(v.createTime*1000).Format();
				});
			}

			vm.data = avalon.mix(vm.data, ajaxData);	// 更新数据
			avalon.scan($("#coinContent")[0], listCoinModel);
			setTimeout(function() {
				$("#coinContent").removeClass("loading").find("table").stop().animate({"opacity": "1"}, 300);
			}, 200);
		};
		/* 页面跳转功能 */
		vm.jumpPage = function() {
			var targetPage = $(this).attr("data-page"),
				maxPage = vm.data.pageInfo.pageCount;

			if (targetPage < 1 || targetPage > maxPage) return;
			$("#coinContent").addClass("loading").find("table").stop().css({"opacity": 0});

			bops.incentive.updataListView(listCoinModel, targetPage);
		};
	});

	function updataListView(VM, data, pageIndex, pageSize) {
		pageIndex = pageIndex || 1;
		pageSize = pageSize || 50;

		// http://localhost:8280/inc/Item_orders.htm?item.id=1
		$.ajax({
			url: VM.url,
			data: "pageInfo.pageIndex=" + pageIndex
		}).done(function(ajaxData) {
			ajaxData.pageInfo = base.createPage(ajaxData.pageInfo);	// 生成avalon翻页数据

			// 渲染视图
			VM.updateView(ajaxData);
		});
	}

	// 默认进第一页
	bops.incentive.updataListView(listCoinModel, 1);


	/* 
	 *	新增金币批量发放VM
	 */
	var addCoin = avalon.define("addCoin", function(vm) {
		vm.isValidateStore= 1; // 1:未校验 2:校验失败 3:校验成功
		vm.inputFile = '<input type="file" name="issueFile">';

		/* 重新渲染 */
		vm.updateView = function() {
			vm.isValidateStore = 1;	// 更新数据

			vm.inputFile = "";
			vm.inputFile = '<input type="file" name="issueFile">';
			avalon.scan($("#addCoin")[0], addCoin);
		};
		/* 校验优惠券 */
		vm.validateStore = function() {
			$("#addCoinForm").ajaxSubmit({
				type: "POST",
				url: "/inc/Coin_checkIssue.htm",
				success: function() {
					vm.isValidateStore = 3;
					$("#error").html("");
				},
				error: function(err) {
					$("#error").html(err.responseText);				
				}
			});
		};
	});
	
	var canSubmit = true;
	$("#addCoinSubmit").on("click", function() {
		if (!canSubmit) return;
		if (addCoin.isValidateStore != 3) {
			alert("请先校验文件");
			return;
		}

		canSubmit = false;
		$("#addCoinForm").ajaxSubmit({
			type: "POST",
			url: "/inc/Coin_issue.htm",
			success: function(ajaxData) {
				alert("提交成功!");			
				bops.incentive.updataListView(listCoinModel, listCoinModel.data.pageInfo.pageIndex);
				$("#addCoinContrainer").modal("hide");
				canSubmit = true;
			},
			error: function(err) {
				alert(err.responseText);				
				canSubmit = true;
			}
		});
	});


	$("#addCoin").on("click", function() {
		$("#error").html("");
		addCoin.updateView();
		$("#addCoinContrainer").modal("show");
	});
});
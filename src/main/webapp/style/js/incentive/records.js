var listRecordsModel;

$(function() {
	/* 
	 *	兑换记录列表VM
	 */
	listRecordsModel = avalon.define("records", function(vm) {
		vm.data = {
			pageInfo: {
				pageList: []
			},
			orders: []
		};
		vm.item = {
			id: "",
			mobile: "",
			title: "",
			account: "",
			code: "",
			itemId: base.getQueryStringRegExp("item.id")
		};
		vm.url = "/inc/Item_orders.htm";	// 更新数据的url

		/* 重新渲染 */
		vm.updateView = function(ajaxData) {
			if (ajaxData.orders && ajaxData.orders.length) {
				$.each(ajaxData.orders, function(i, v) {
					v.time = new Date(v.createTime*1000).Format();
				});
			}
			vm.data = ajaxData;	// 更新数据
			avalon.scan($("#recordsContent")[0], listRecordsModel);
			setTimeout(function() {
				$("#recordsContent").removeClass("loading").find("table").stop().animate({"opacity": "1"}, 300);
			}, 200);
		};
		/* 页面跳转功能 */
		vm.filterList = function() {
			$("#recordsContent").addClass("loading").find("table").stop().css({"opacity": 0});
			updataListView(listRecordsModel, 1);
		};
		/* 页面跳转功能 */
		vm.jumpPage = function() {
			var targetPage = $(this).attr("data-page"),
				maxPage = vm.data.pageInfo.pageCount;

			if (targetPage < 1 || targetPage > maxPage) return;
			$("#recordsContent").addClass("loading").find("table").stop().css({"opacity": 0});
			updataListView(listRecordsModel, targetPage);
		};
	});

	function updataListView(VM, pageIndex, pageSize) {
		var data = {
				pageInfo: {},
				condition: {}
			},
			item = VM.item.$model;

		for (var i in item) {
			if (item[i]) data.condition[i] = item[i];
		}
		// if (item.id) data.condition.uid = item.id;
		// if (item.itemId) data.condition.itemId = item.itemId;
		// if (item.mobile) data.condition.mobile = item.mobile;
		// if (item.account) data.condition.account = item.account;
		// if (item.code) data.condition.code = item.code;

		data.pageInfo.pageIndex = pageIndex || 1;
		data.pageInfo.pageSize = pageSize || 50;

		$.ajax({
			url: VM.url,
			type: "POST",
			data: base.paramData(data)
		}).done(function(ajaxData) {
			ajaxData.pageInfo = base.createPage(ajaxData.pageInfo);	// 生成avalon翻页数据

			// 渲染视图
			VM.updateView(ajaxData);
		});
	}


	// 默认进第一页
	updataListView(listRecordsModel, 1);
});
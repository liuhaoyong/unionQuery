/**
 * Created by Jinbo on 2014/12/13.
 */
/* 模拟发放记录列表数据 */
// Mock.mock(/\/dt\/Receiving_list\.htm/, function() {
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
// 		receivings: [{
// 			time: Random.datetime(),
// 			receiveTime: Random.datetime(),
// 			account: Random.string("upper", 12),
// 			name: Random.string("upper", 12),
// 			uid: Random.natural(10000, 100000),
// 			type: Random.natural(0, 2),
// 			mobile: 13588888888,
// 			status: Random.natural(0, 1)
// 		}, {
// 			time: Random.datetime(),
// 			receiveTime: Random.datetime(),
// 			account: Random.string("upper", 12),
// 			name: Random.string("upper", 12),
// 			uid: Random.natural(10000, 100000),
// 			type: Random.natural(0, 2),
// 			mobile: 13588888888,
// 			status: Random.natural(0, 1)
// 		}, {
// 			time: Random.datetime(),
// 			receiveTime: Random.datetime(),
// 			account: Random.string("upper", 12),
// 			name: Random.string("upper", 12),
// 			uid: Random.natural(10000, 100000),
// 			type: Random.natural(0, 2),
// 			mobile: 13588888888,
// 			status: Random.natural(0, 1)
// 		}, {
// 			time: Random.datetime(),
// 			receiveTime: Random.datetime(),
// 			account: Random.string("upper", 12),
// 			name: Random.string("upper", 12),
// 			uid: Random.natural(10000, 100000),
// 			type: Random.natural(0, 2),
// 			mobile: 13588888888,
// 			status: Random.natural(0, 1)
// 		}, {
// 			time: Random.datetime(),
// 			receiveTime: Random.datetime(),
// 			account: Random.string("upper", 12),
// 			name: Random.string("upper", 12),
// 			uid: Random.natural(10000, 100000),
// 			type: Random.natural(0, 2),
// 			mobile: 13588888888,
// 			status: Random.natural(0, 1)
// 		}]
// 	};
// });
// /* 模拟获取奖品名称列表数据 */
// Mock.mock(/\/dt\/Award_names\.htm/, function() {
// 	return [{
// 		name: "奖品1",
// 		awardId: "1"
// 	}, {
// 		name: "奖品2",
// 		awardId: "2"
// 	}, {
// 		name: "奖品3",
// 		awardId: "3"
// 	}];
// });


var listReceivingModel;

$(function() {
	/* 
	 *	兑换记录列表VM
	 */
	listReceivingModel = avalon.define({
		$id: "receivingList",
		$url: "/dt/Award_receivings.htm",
		data: {
			pageInfo: {
				pageList: []
			},
			receivings: []
		},
		filter: {
			awardType: "",
			awardId: "",
			status: "",
			account: "",
			userId: ""
		},
		awardNames: [{name: "全部", awardId: ""}],

		$updateModel: function(ajaxData) {
			var model = listReceivingModel;


			if (ajaxData.receivings) {
				$.each(ajaxData.receivings, function(i, v) {
					v.createTime = new Date(v.createTime*1000).Format();
					v.receiveTime = new Date(v.receiveTime*1000).Format();
				});
			}
			avalon.vmMix(true, model.data, ajaxData);			
			avalon.scan($("#receiving")[0], model);
			setTimeout(function() {
				$("#listContent").removeClass("loading").find("table").stop().animate({"opacity": "1"}, 300);
			}, 200);
		},
		$filterList: function() {
			$("#listContent").addClass("loading").find("table").stop().css({"opacity": 0});
			updataListView(listReceivingModel, 1);
		},
		$jumpPage: function() {
			var model = listReceivingModel,
				targetPage = $(this).attr("data-page"),
				maxPage = model.data.pageInfo.pageCount;

			if (targetPage < 1 || targetPage > maxPage) return;
			$("#listContent").addClass("loading").find("table").stop().css({"opacity": 0});
			updataListView(model, targetPage);
		}
	});
	listReceivingModel.filter.$watch("awardType", function(n) {
		var model = listReceivingModel,
			names = model.awardNames;

		names.clear();
		model.filter.awardId = "";	// 清空选中奖品id
		if (n === "") {
			names.push({name: "全部", awardId: ""});
		} else {
			$.ajax({
				url: "/dt/Award_names.htm",
				dataType: "json",
				data: "condition.awardType=" + n
			}).done(function(ajaxData) {
				var nameArray = [];
				// 渲染视图
				names.push({name: "全部", awardId: ""});
				$.each(ajaxData.awards, function(i, val) {
					nameArray.push({
						name: val.name,
						awardId: val.id
					});
				});
				names.pushArray(nameArray);
			});
		}
	});

	function updataListView(VM, pageIndex, pageSize) {
		var data = {
				pageInfo: {},
				condition: {}
			},
			filter = VM.$model.filter;

		for (var i in filter) {
			if (filter[i]) data.condition[i] = filter[i];
		}

		data.pageInfo.pageIndex = pageIndex || 1;
		data.pageInfo.pageSize = pageSize || 50;

		$.ajax({
			url: VM.$url,
			type: "POST",
			dataType: "json",
			data: base.paramData(data)
		}).done(function(ajaxData) {
			ajaxData.pageInfo = base.createPage(ajaxData.pageInfo);	// 生成avalon翻页数据

			// 渲染视图
			VM.$updateModel(ajaxData);
		});
	}

	// 默认进第一页
	updataListView(listReceivingModel, 1);
});
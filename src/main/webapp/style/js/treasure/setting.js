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


var settingModel;

$(function() {
	/* 
	 *	兑换记录列表VM
	 */
	settingModel = avalon.define({
		$id: "setting",
		$url: "dt/Award_setting.htm",
		data: {
			setting: {
				id: "",
				mdBudget: 0,
				mdLimit: 0,
				off: false,
				updateTime: 0
			},
			awards: []
		},

		$updateModel: function(ajaxData) {
			var model = settingModel;

			$.each(ajaxData.awards, function(i, val) {
				if (val.type != 0) {
					val.unitCost = (val.unitCost + "00000000").replace(/(^\d+\.\d{8}).*/, "$1");
				}
				val.unitCostIsError = !/^\d+\.\d{8}$/.test(val.unitCost);
				val.intervalValIsError = !/^\d*$/.test(val.intervalVal);
				val.avgValIsError = !/^\d*$/.test(val.avgVal);
				val.yieldDaysIsError = !/^\d*$/.test(val.yieldDays);
			});
			avalon.vmMix(true, model.data, ajaxData);			
			avalon.scan($("#receiving")[0], model);
			setTimeout(function() {
				$("#listContent").removeClass("loading").find("table").stop().animate({"opacity": "1"}, 300);
			}, 200);
		},
		$input: function(item, name) {
			var reg = new RegExp($(this).attr("data-pattern"));

			if (item.type == 0) {
				item[name + "IsError"] = false;
			} else {
				item[name + "IsError"] = !reg.test(item[name]);
			}
		},
		$change: function(item, name) {
			var reg = new RegExp($(this).attr("data-pattern"));

			console.log(!reg.test(item[name]))
			if (item.type == 0) {
				item[name + "IsError"] = false;
			} else {
				item[name + "IsError"] = !reg.test(item[name]);
			}
		}
	});

	function init() {
		$.ajax({
			url: settingModel.$url, // data不可以使用 "data": data 的写法,不然Mock.mock无效
		}).done(function(ajaxData) {
			// 渲染视图
			settingModel.$updateModel(ajaxData);
		});
	}

	init();

	var canSubmit = true;
	$("#settingBtn").on("click", function() {
		var submitData = settingModel.$model.data,
			IsError = false;

		if (!canSubmit) return false;

		// 判断单位成本是否设置错误
		$.each(submitData.awards, function(i, val) {
			if (val.status == 0) return true;
			if (val.type != 1 && val.unitCostIsError) {
				IsError = true;
				alert("单位成本设置错误！");
			}
			if (val.intervalValIsError) {
				IsError = true;
				alert("均值只能为整数");
			}
			if (val.avgValIsError) {
				IsError = true;
				alert("中间值间隔只能为整数");
			}
			if (val.yieldDaysIsError) {
				IsError = true;
				alert("收益天数只能为整数");
			}
			if (IsError) return false;
		});
		if (IsError) return false;
		canSubmit = false;
		$.ajax({
			url: "/dt/Award_saveSetting.htm",
			data: base.paramData(submitData),
			type: "POST",
			success: function(data) {
				alert("算法调整成功！配置的每人日成本为" + data.resultInfo.configMdCost);
				$("#error").html("");
				init();
				canSubmit = true;
			},
			error: function(err) {
				alert("提交失败！");
				$("#error").html(err.responseText);
				canSubmit = true;
			}
		});
	});
});
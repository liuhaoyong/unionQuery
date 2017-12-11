/**********************************
***								***
***			前端数据模拟		***
***								***
**********************************/


 
var listAwardModel;
var awardModel;
var oFReader = new FileReader();
var rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

$(function() {
	var awardModelData = {
		award: {
			type: 0,
			id: "",
			name: "",
			duration: 0,
			msg: "",
			pic: "",
			maxTimes: 0,
			store: "",
			extInfo: {
				picUrl: ""
			}
		}
	};

	awardModel = avalon.define({
		$id: "editAward",
		$defaultData: awardModelData.award,
	    $url: '/dt/Award_edit.htm',
		storeFile: "<input type='file' name='store' id='storeFile'>",
		imgFile: "<input type='file' name='pic' ms-change='$updatePic' id='awardPic'>",
		data: avalon.mix(true, {}, awardModelData.award),
		isEdit: false,

		/* 更新model数据,重新渲染View */
		$updateModel: function(ajaxData, isEdit) {
			var model = awardModel,
				needData = bops.av.getModelData(model.$defaultData, ajaxData.award);

			model.isEdit = !!isEdit;
			model.imgFile = "";
			model.storeFile = "";
			model.imgFile = "<input type='file' name='pic' ms-change='$updatePic' id='awardPic'>";
			model.storeFile = "<input type='file' name='store' id='storeFile'>";
			avalon.vmMix(true, model.data, needData);			
			avalon.scan($("#editAward")[0], model);
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
				awardModel.data.extInfo.picUrl = oFREvent.target.result;
			};
		}
	});



	/**
	 * avalon 初始化奖品列表
	 */
	listAwardModel = avalon.define({
		$id: "awardList",
		$url: "/dt/Award_list.htm",
		data: {
			pageInfo: {
				pageList: []
			},
			awards: []
		},

		$updateModel: function(ajaxData) {
			var model = listAwardModel;

			
			if (ajaxData.awards) {
				$.each(ajaxData.awards, function(i, v) {
					v.time = new Date(v.createTime*1000).Format();
				});
			}
			avalon.vmMix(true, model.data, ajaxData);			
			avalon.scan($("#listContent")[0], model);
			setTimeout(function() {
				$("#listContent").removeClass("loading").find("table").stop().animate({"opacity": "1"}, 300);
			}, 200);
		},
		$showEdit: function() {			
			var id = $(this).attr("data-id");

			bops.av.updataEditView(awardModel, "award.id=" + id);
			$("#editAward").modal("show");
		},
		$deleteAward: function() {
			var id = $(this).attr("data-id"),
				name = $(this).attr("data-name"),
				bool = confirm("确定执行删除"+ name +"的操作么?");

			if (bool) {

				$.ajax({
					type: "POST",
					url: "/dt/Award_updateStatus.htm",
					data: "ids=" + id + "&status=2"
				}).done(function(ajaxData) {
					alert("提交成功!");
					bops.av.updataListView(listAwardModel, listAwardModel.data.pageInfo.pageIndex);			
				}).fail(function(err) {
					alert(err.responseText);
				});
			}
		},
		$jumpPage: function() {
			var model = listAwardModel,
				targetPage = $(this).attr("data-page"),
				maxPage = model.data.pageInfo.pageCount;

			if (targetPage < 1 || targetPage > maxPage) return;
			$("#listContent").addClass("loading").find("table").stop().css({"opacity": 0});
			bops.av.updataListView(model, targetPage);
		}
	});
	
	/**
	 * 页面DOM事件绑定
	 */
	// 新增任务
	$("#addAward").on("click", function() {
		// bops.av.updataEditView(awardModel, "award.id=");
		awardModel.$updateModel(awardModelData);
		$("#editAward").modal("show");
	});
	
	var canSubmit = true;

	$("#awardSubmit").on("click", function() {
		var modelData = awardModel.$model;

		if (!canSubmit) return false;
		if (modelData.data.type == "0" && !modelData.isEdit) {
			if ($("#storeFile").val() === "") {
				alert("请选择优惠券文件");
				return false;
			}
			if($("#awardPic").val() === "") {
				alert("请选择奖励图片");
				return false;	
			}
		}

		canSubmit = false;
		$("#awardForm").ajaxSubmit({
			type: "POST",
			url: "/dt/Award_save.htm",
			success: function(ajaxData) {
				alert("提交成功!");			
				bops.av.updataListView(listAwardModel, listAwardModel.data.pageInfo.pageIndex);	
				$("#editAward").modal("hide");
				canSubmit = true;
			},
			error: function(err) {
				alert(err.responseText);
				canSubmit = true;
			}
		});
	});

	// 默认进第一页
	$("#listContent").addClass("loading").find("table").stop().css({"opacity": 0});
	bops.av.updataListView(listAwardModel);
});
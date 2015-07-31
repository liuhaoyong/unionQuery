(function(jQuery) {
	jQuery.uploadpic = {
		uploadServiceUrl : (window.location.href.substring(10,14) == 'test') ? 'http://cmstest.tudou.com/center/util/ajaxUploadPic.do':'http://cms.tudou.com/center/util/ajaxUploadPic.do',
		startUploadPic : function(formId,objId,inputFile) {
			var fileForm = document.getElementById(formId);
			var saction = fileForm.action;
			var sencoding = fileForm.encoding;
			var senctype = fileForm.enctype;
			var starget = fileForm.target;
			var sonsubmit = fileForm.onsubmit;
			var smethod = fileForm.method;
			$("#cj_upload_pic_size_"+formId).val($(inputFile).attr('data-uploadPicSize'));
			$("#cj_upload_pic_scale_"+formId).val($(inputFile).attr('data-uploadPicScale'));
			var jc = $("#cj_upload_pic_item_call_back_"+formId).html();
			if(jc && jc != null && jc != '' && jc != 'null') {
				eval('itemUrl = '+jc+'();');
				$("#cj_upload_pic_item_url_"+formId).val(itemUrl);
			}
			fileForm.action = jQuery.uploadpic.uploadServiceUrl;
			fileForm.encoding = "multipart/form-data";
			fileForm.method="POST";
			fileForm.enctype = 'multipart/form-data';
			fileForm.target = 'upload_target';
			fileForm.onsubmit = null;
			$("#"+formId).find("input[name=objId]").val(objId);
			jQuery.uploadpic.objId = objId;
			fileForm.submit();
			fileForm.action = saction;
			fileForm.encoding = sencoding;
			fileForm.enctype = senctype;
			fileForm.target = starget;
			fileForm.onsubmit = sonsubmit;
			fileForm.method = smethod;
			var tempForm = document.createElement('form');
			$(inputFile).before(tempForm);
			$(tempForm).append($(inputFile));
			tempForm.reset();
			$(tempForm).after($(inputFile));
			$(tempForm).remove();
		},
		finishUploadPic : function(data) {
		  	if(data.system == 1) {
			  	jQuery.uploadpic.onSuccesses[jQuery.uploadpic.objId](data.uploadPicInfo.picUrl);
		  	} else if(data.system == -1) {
		  		jQuery.uploadpic.onErrors[jQuery.uploadpic.objId](data.message);
		  	}
		  	return true;
		},
		onSuccesses:[],
		onErrors:[],
		objId:null
	};
	jQuery.fn.createUploadpic = function(options) {
		var options = jQuery.extend({
			onSuccess:function(data){},
			onError:function(data){},
			itemUrl:null,
			itemUrlCallback:null,
			appId:0,
			uploadPicSize:0,
			uploadPicScale:'',
			formId:null,
			uploadButtonText:"上传图片",
			encoding:"UTF-8"
		},options);
		var $this = $(this);
		var objId = $this.attr("id");
		var formId = options.formId;
		var form = $("#"+formId);
		if(!form.find('input[name="encoding"]').length) {
			form.append('<input type="hidden" name="encoding" value="'+options.encoding+'"/>');
		}
		if(!$("#cj_upload_callback_"+formId).length) {
			form.append('<input type="hidden" name="callback" id="cj_upload_callback_'+formId+'" value="jQuery.uploadpic.finishUploadPic"/>');
		}
		if(!$("#cj_upload_pic_item_url_"+formId).length) {
			form.append('<input type="hidden" name="uploadPicItemUrl" id="cj_upload_pic_item_url_'+formId+'" value=""/>');
		}
		if(!$("#cj_upload_pic_item_app_id_"+formId).length) {
			form.append('<input type="hidden" name="uploadPicItemAppId" id="cj_upload_pic_item_app_id_'+formId+'" value="0"/>');
		}
		if(!$("#cj_upload_pic_size_"+formId).length) {
			form.append('<input type="hidden" name="uploadPicSize" id="cj_upload_pic_size_'+formId+'" value="0"/>');
		} 
		if(!$("#cj_upload_pic_scale_"+formId).length) {
			form.append('<input type="hidden" name="uploadPicScale" id="cj_upload_pic_scale_'+formId+'" value="0"/>');
		} 
		if(!$("#cj_upload_pic_item_obj_id_"+formId).length) {
			form.append('<input type="hidden" name="objId" id="cj_upload_pic_item_obj_id_'+formId+'"  value ="" />');
		}
		if(!$("#cj_upload_pic_item_call_back_"+formId).length) {
			form.append('<span style="display:none;" id="cj_upload_pic_item_call_back_'+formId+'"  >'+options.itemUrlCallback+'</span>');
		}
		jQuery.uploadpic.onSuccesses[objId] = options.onSuccess;
		jQuery.uploadpic.onErrors[objId] = options.onError;
		jQuery.uploadpic.objId = objId;
		if(!$("#div_"+objId).length) {
			var html = '<div style="position:relative;height:32px;width:52px;display:inline" id="div_'+objId+'">\
						<select name="uploadType'+objId+'"><option value="ori">原图</option><option value="zip">压缩</option ></select>\
			        	<input name="uploadPic'+objId+'" data-uploadPicSize="' + options.uploadPicSize + '" data-uploadPicScale="' + options.uploadPicScale + '" onchange="jQuery.uploadpic.startUploadPic(\''+formId+'\',\''+objId+'\',this)" type="file" \
			        	style="height:30px;position:absolute;z-index:1;width:74px;top:-10px;left:70px;opacity:0;filter:alpha (opacity=0);cursor:pointer;" /> \
			        	<a class="btn" href="javascript:void(0)" >'+options.uploadButtonText+'</a>\
						</div>';
	    	$this.after(html);
    	}
		if(options.itemUrl) {
			$("#cj_upload_pic_item_url_"+formId).val(options.itemUrl);
		}
		$("#cj_upload_pic_item_app_id_"+formId).val(options.appId);
		$("#cj_upload_pic_size_"+formId).val(options.uploadPicSize);
		$("#cj_upload_pic_scale_"+formId).val(options.uploadPicScale);
		if(!$("#upload_target").length) {
			$("body").append('<iframe style="width: 0px; height: 0px; border: 0px solid rgb(255, 255, 255);" src="javascript:;" name="upload_target" id="upload_target"></iframe>');
		}
	};
	jQuery.fn.createTextInputUploadpic = function(options) {
		$textInput = $(this);
 		$textInput.createUploadpic(jQuery.extend({
			onSuccess:function(uploadPic){
				$("#"+jQuery.uploadpic.objId).val(uploadPic);
			},
			onError:function(errorTip) {
				alert(errorTip);
			}
		},options));
	};
})(jQuery);
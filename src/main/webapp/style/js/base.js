// 输入框提示信息
$(function(){
	    if (!('placeholder' in document.createElement('input'))) {
        $('.placeholder input[placeholder], .placeholder textarea[placeholder]').each(function(k, v) {
            var $obj = $(v),
                val = $obj.val(),
                placeholder = $obj.attr('placeholder');
            
            if (val == '') {
                $obj.val(placeholder);                   
            }
            
            $obj.focus(function() {
                if ($obj.val() === placeholder) {
                    $obj.val('');
                }
            }).blur(function() {
                val = $obj.val();
                if (val == '' || val == placeholder) {
                    $obj.val(placeholder);
                }
            });
        });
    }
	
	})
//msg
function msg(a)
{   
	var a=a;
	if(a==true){
		$("#msg").show().removeClass("msg_error");
		//$("#msg").find("p").html('发布成功')
	} else {
		$("#msg").show().addClass("msg_error");
		//$("#msg").find("p").html('发布失败')
	}
}

$.browser = {};
$.browser.msie = false;
//弹出层
var maskDefaultSetting={bgColor:"#000",opacity:.6,zIndex:999,onClick:null};function MaskLayer(g){var c="100%",e="absolute",b="0px",a=this,d=a;a.settings=$.extend({},maskDefaultSetting,g);a.maskLayer=$("<div/>",{css:{background:a.settings.bgColor,opacity:a.settings.opacity,"z-index":a.settings.zIndex,display:"none",left:b,top:b}});$("body").append(a.maskLayer);if($.browser.msie&&$.browser.version=="6.0"){a.maskLayer.css({position:e});a.replacement=function(){var b=Math.max(document.documentElement.scrollWidth,document.documentElement.clientWidth),a=Math.max(document.documentElement.scrollHeight,document.documentElement.clientHeight);d.maskLayer.css({width:b+"px",height:a+"px"})};var f=$("<iframe/>",{css:{position:e,top:b,left:b,width:c,height:c,filter:"alpha(opacity=0)",display:"block"}});a.maskLayer.append(f);a.settings.onClick&&f.contents().click(function(){d.settings.onClick()})}else{a.maskLayer.css({position:"fixed",width:c,height:c});a.settings.onClick&&a.maskLayer.click(function(){d.settings.onClick()})}}MaskLayer.prototype.show=function(){var a=this,b=a;if(a.maskLayer.is(":hidden")){if($.browser.msie&&$.browser.version=="6.0"){$(window).bind("resize.maskLayer",function(){b.replacement()});a.replacement()}a.maskLayer.fadeIn("fast")}};MaskLayer.prototype.hide=function(){if(this.maskLayer.is(":visible")){this.maskLayer.fadeOut("fast");$(window).unbind("resize.maskLayer")}};var msgboxDefaultSetting={title:null,closeExist:true,skin:"default",isFixed:true,position:{x:"center",y:"center"},masklayer:{bgColor:"#000",opacity:.3},zIndex:999,shadow:false,existTime:null,content:null,width:"auto",height:"auto",btns:null,maskOnClick:null,onClose:null};function setPostion(){var c="center",a=this,b=a.settings.position,d=0,e=0;if($.browser.msie&&$.browser.version=="6.0"||a.settings.isFixed==false){d=b.x==c?$(document).scrollLeft()+($(window).width()-a.msgbox.width())/2:$(document).scrollLeft()+parseInt(b.x);e=b.y==c?$(document).scrollTop()+($(window).height()-a.msgbox.height())/2:$(document).scrollTop()+parseInt(b.y)}else{d=b.x==c?($(window).width()-a.msgbox.width())/2:parseInt(b.x);e=b.y==c?($(window).height()-a.msgbox.height())/2:parseInt(b.y)}a.msgbox.is(":animated")&&a.msgbox.stop(true);a.msgbox.animate({left:d+"px",top:e+"px"},300)}function MsgBox(k){var c="<div>",g="javascript:void(0)",a=this,m=a;a.settings=$.extend(true,{},msgboxDefaultSetting,k);var l=!($.browser.msie&&$.browser.version=="6.0")&&a.settings.isFixed?"fixed":"absolute";a.msgbox=$("<div/>",{css:{position:l,"z-index":a.settings.zIndex+1,display:"none"}});$("body").append(a.masklayer).append(a.msgbox);a.settings.shadow&&a.msgbox.addClass("msg_shadow");if(a.settings.masklayer)a.maskLayer=new MaskLayer({bgColor:a.settings.masklayer.bgColor,opacity:a.settings.masklayer.opacity,zIndex:a.settings.zIndex,onClick:a.settings.maskOnClick});var f=$("<table>",{cellpadding:"0",cellspacing:"0",border:"0","class":"msg_table"}),e=$("<tr/>");e.append('<td class="msg_background_horn msg_top_left">&nbsp;</td>');e.append('<td class="msg_top_center">&nbsp;</td>');var j=$("<td>",{"class":"msg_background_horn msg_top_right"});e.append(j);if(a.settings.closeExist){var i=$("<a>",{href:g,"class":"msg_background_horn msg_close_btn"});j.append(i);i.click(function(){m.hide()})}f.append(e);var d=$("<tr/>");d.append('<td class="msg_content_left">&nbsp;</td>');var b=$("<td>",{"class":"msg_content"});a.settings.title&&b.append('<div class="msg_title">'+a.settings.title+"</div>");contentInner=$(c,{css:{width:typeof a.settings.width=="string"?a.settings.width:a.settings.width+"px",height:typeof a.settings.height=="string"?a.settings.height:a.settings.height+"px"}});b.append(contentInner);contentInner.append(a.settings.content);proxy=null;if(a.settings.btns){var h=$(c,{"class":"msg_btns_container"});$.each(a.settings.btns,function(){var a=this,e=a,b=$(c,{"class":"msg_tc"});a.style&&typeof a.style=="object"?b.css(a.style):b.addClass(a.style);var d=$("<a>",{href:g,"class":"msg_bt_sure"});e.onClick&&d.click(function(){e.onClick();if($.browser.msie&&$.browser.version=="6.0")return false});d.append($("<span>",{text:a.text}));b.append(d);h.append(b)});b.append(h)}d.append(b);d.append('<td class="msg_content_right">&nbsp;</td>');f.append(d);f.append('<tr><td class="msg_background_horn msg_bottom_left">&nbsp;</td><td class="msg_bottom_center">&nbsp;</td><td class="msg_background_horn msg_bottom_right">&nbsp;</td></tr>');a.msgbox.append(f)}MsgBox.prototype.show=function(){var a=this,b=a;if(a.msgbox.is(":hidden")){$(window).bind("resize.msgBox",function(){setPostion.call(b)});if($.browser.msie&&$.browser.version=="6.0"&&a.settings.isFixed){var d=a.settings.shadow?a.msgbox.width()-5:a.msgbox.width(),c=a.settings.shadow?a.msgbox.height()-5:a.msgbox.height();a.msgbox.css({width:d+"px",height:c+"px"});$(window).bind("scroll.msgBox",function(){setTimeout(function(){setPostion.call(b)},160)})}setPostion.call(a);a.settings.masklayer&&a.maskLayer.show();a.msgbox.fadeIn("fast");a.settings.existTime&&setTimeout(function(){b.hide()},a.settings.existTime)}};MsgBox.prototype.hide=function(){var a=this;if(a.msgbox.is(":visible")){a.settings.onClose&&a.settings.onClose();a.settings.masklayer&&a.maskLayer.hide();a.msgbox.fadeOut("fast");$(window).unbind("resize.msgBox");$.browser.msie&&$.browser.version=="6.0"&&a.settings.isFixed&&$(window).unbind("scroll.msgBox")}}

//去除空格
String.prototype.trim = function()
{
  return this.replace(/(^\s*)|(\s*$)/g, "");
}
//是否数字检测
function isNumber(name) //数值检测
{ 
  if(name.length == 0)
     return false;
  for(i = 0; i < name.length; i++) { 
    if(name.charAt(i) < "0" || name.charAt(i) > "9")
	  return false;
    }
  return true;
}
//检测是否包含特殊字符
function containSpecial( s )      
{      
    var containSpecial = RegExp(/[(\~)(\!)(\@)(\#)(\$)(\%)(\[)(\])(\{)(\})(\|)(\\)(\/)(\<)(\>)(\?)(\)]+/);      
    return (containSpecial.test(s) );      
}

// 对Date的扩展，将 Date 转化为指定格式的String   
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
// 例子：   
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.Format = function(fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份   
        "d+": this.getDate(), //日   
        "h+": this.getHours(), //小时   
        "m+": this.getMinutes(), //分   
        "s+": this.getSeconds(), //秒   
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度   
        "S": this.getMilliseconds() //毫秒   
    }, k;
    
    fmt = fmt || "yyyy-MM-dd hh:mm:ss";
    
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }
    return fmt;
}

var bops = {},  
    base = {};  // 基础方法

/**
 * 把json格式对象转成可提交字符串格式,会过滤掉函数 {a: {b: 3}, b: [1], c: "d"} -> a.b=3&b[0]=1&c=d 
 * @param  {Object} data   要转化的json对象
 * @param  {String} prefix 要带的前缀
 * @return {String}        字符串
 */
base.paramData = function(data, prefix) {
    var _getType = ({}).toString,
        _paramArray = function(arr, prefix) {
            var result = [],
                j = 0,
                len = arr.length;

            for (; j < len; j++) {
                var data = arr[j],
                    type = _getType.call(data),
                    subPrefix = prefix + "[" + j + "]";

                result = result.concat(_paramAll(arr[j], prefix + "[" + j + "]"));
            }

            return result;
        },
        _paramAll = function(data, prefix) {
            var result = [],
                type = _getType.call(data);

            switch (type) {
                case "[object Object]":
                    var subPrefix = prefix ? prefix + "." : "",
                        i;

                    for (i in data) result = result.concat(_paramAll(data[i], subPrefix + i));
                    break;
                case "[object Array]":
                    result = result.concat(_paramArray(data, prefix));
                    break;
                case "[object String]":
                case "[object Number]":
                    result.push(prefix + "=" + encodeURIComponent(data));
            }

            return result;
        };

    prefix = prefix || "";

    return (function() {
        return _paramAll(data, prefix);
    }()).join("&"); 
};

/**
 * 生成avalon翻页数据
 * @param  {Object} pageInfo ajax返回的pageinfo数据
 * @return {Object}          pageInfo
 */
base.createPage = function(pageInfo) {
    pageInfo = pageInfo || {};
    var pageList = [],
        pageCount = pageInfo.pageCount || 1,
        pageIndex = pageInfo.pageIndex || 1,
        l = false,
        r = false;

    if (pageCount <= 10) {
        var i = 0;
        for (; i < pageCount; i++) pageList.push(i+1);
    } else if (pageIndex <= 6) {
        r = true;
        pageList = [1, 2, 3, 4, 5, 6, 7, 8];
    } else if (pageIndex >= (pageCount-6)) {
        l = true;
        pageList = [pageCount-6, pageCount-5, pageCount-4, pageCount-3, pageCount-2, pageCount-1, pageCount];
    } else {
        l = r = true;
        pageList = [pageIndex-3, pageIndex-2, pageIndex-1, pageIndex, pageIndex+1, pageIndex+2, pageIndex+3];
    }

    pageInfo.showLeft = l;
    pageInfo.showRight = r;
    pageInfo.pageList = pageList;

    return pageInfo;
}

/**
 * 用页面链接上获取参数
 * @param  {String} name    要获取的参数名
 * @return {String}         参数值
 */
base.getQueryStringRegExp = function(name) { 
    var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");   

    return reg.test(location.href) ? unescape(RegExp.$2.replace(/\+/g, " ")) : ""; 
};

/**
 * map形式的数据转成包含一组对象的Array
 * {2: "挖财Android"} -> [{key: 2, val: "挖财Android"}]
 * @param  {Object} obj         map类型的数据,如{2: "挖财Android", 3: "挖财IOS", 30: "钱管家IOS", 31: "钱管家Android", 40: "挖财宝IOS", 41: "挖财宝Android"}
 * @param  {String} keyName     对象key
 * @param  {String} valName     对象Name
 * @return {Array}          
 */
base.mapToArray = function(obj, keyName, valName) { 
    var result = [],
        i;

    for (i in obj) {
        var temp = {};
        
        temp[keyName] = i;
        temp[valName] = obj[i];

        result.push(temp);
    }

    return result;
};


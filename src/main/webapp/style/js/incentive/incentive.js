bops.incentive = bops.incentive ? bops.incentive : {};

/**
 * 获取平台名称
 * @param  {number} type 平台类型 1|2|3|4
 * @return {String}      平台名称
 */
bops.incentive.getPlatformsTypeName = function(type) {
	var name = "";

	switch(type+"") {
		case "1":
			name = "挖财记账理财";
			break;
		case "2":
			name = "信用卡管家";
			break;
		case "3":
			name = "钱管家";
			break;
		case "4":
			name = "理财精选";
			break;
	}

	return name;
};

/**
 * 获取商品列表方法
 * @param  {Number} pageIndex 页码，默认为1
 * @param  {String} pageSize  每页显示的数量，默认为50
 */
bops.incentive.updataListView = function(VM, pageIndex, pageSize) {
	pageIndex = pageIndex || 1;
	pageSize = pageSize || 50;

	$.ajax({
		url: VM.url,
		data: "pageInfo.pageSize=" + pageSize + "&pageInfo.pageIndex=" + pageIndex
	}).done(function(ajaxData) {
		ajaxData.pageInfo = base.createPage(ajaxData.pageInfo);	// 生成avalon翻页数据

		// 渲染视图
		VM.updateView(ajaxData);
	});
};

/**
 * 获取规则数据,并渲染弹出页面
 * @param  {String} id 规则id号
 */
bops.incentive.updataEditView = function(VM, data) {
	var isEdit = (data !== undefined);

	$.ajax({
		url: VM.url,
		data: data
	}).done(function(ajaxData) {
		// 渲染视图		
		VM.updateView(ajaxData, isEdit);
	});
};


avalon.vmMix = function () {
    var options, name, src, copy, copyIsArray, clone,
            target = arguments[0] || {},
            i = 1,
            length = arguments.length,
            deep = false

    // 如果第一个参数为布尔,判定是否深拷贝
    if (typeof target === "boolean") {
        deep = target
        target = arguments[1] || {}
        i++
    }

    //确保接受方为一个复杂的数据类型
    if (typeof target !== "object" && avalon.type(target) !== "function") {
        target = {}
    }

    //如果只有一个参数，那么新成员添加于mix所在的对象上
    if (i === length) {
        target = this
        i--
    }

    for (; i < length; i++) {
        //只处理非空参数
        if ((options = arguments[i]) != null) {
            for (name in options) {
                src = target[name]
                copy = options[name]

                // 防止环引用
                if (target === copy) {
                    continue
                }
                if (deep && copy && (avalon.isPlainObject(copy) || (copyIsArray = Array.isArray(copy)))) {

                    if (copyIsArray) {
                        copyIsArray = false
                        clone = src && Array.isArray(src) ? src : []

                        if (src.clear) {
                        	target[name] = src.clear().pushArray(copy)
                        	continue
                        }
                    	

                    } else {
                        clone = src && avalon.isPlainObject(src) ? src : {}

                    }
                    
                    target[name] = avalon.vmMix(deep, clone, copy)

                } else if (copy !== void 0) {
                    target[name] = copy
                }
            }
        }
    }
    return target
} 
avalon.config.debug = false;
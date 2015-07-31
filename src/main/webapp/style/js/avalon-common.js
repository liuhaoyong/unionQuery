bops.av = bops.av ? bops.av : {};

avalon.vmMix = function() {
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

/**
 * 根据model的defaultData,从ajaxData中拿出model需要的数据
 * @param  {Object} defaultData model的默认数据
 * @param  {Object} ajaxData    一般为ajax返回的数据
 * @return {[type]}             model需要的数据
 */
bops.av.getModelData = function(defaultData, ajaxData) {
	var modelData = {},
		src, copy,
		i;

	for (i in defaultData) {
		if (defaultData.hasOwnProperty(i)) {
			src = defaultData[i];
			copy = ajaxData[i];

			if (typeof src === "object" && copy) {
				modelData[i] = Array.isArray(src) ? copy : arguments.callee(src, copy);
			} else {
				modelData[i] = (copy !== undefined) ? copy : defaultData[i];
			}
		}
	}

	return modelData;
};

/**
 * 获取商品列表方法
 * @param  {Number} pageIndex 页码，默认为1
 * @param  {String} pageSize  每页显示的数量，默认为50
 */
bops.av.updataListView = function(VM, pageIndex, pageSize) {
	pageIndex = pageIndex || 1;
	pageSize = pageSize || 50;

	$.ajax({
		url: VM.$url + "?" + "pageInfo.pageSize=" + pageSize + "&pageInfo.pageIndex=" + pageIndex, // data不可以使用 "data": data 的写法,不然Mock.mock无效
		dataType: "json" // 此句不可省略,不然Mock.mock无效
	}).done(function(ajaxData) {
		ajaxData.pageInfo = base.createPage(ajaxData.pageInfo); // 生成avalon翻页数据

		// 渲染视图
		VM.$updateModel(ajaxData);
	});
};

/**
 * 获取规则数据,并渲染弹出页面
 * @param  {String} id 规则id号 ("id=1235")
 */
bops.av.updataEditView = function(VM, data) {
	var isEdit = !!data;

	data = data ? ("?" + data) : "";
	$.ajax({
		url: VM.$url + data, 	// data不可以使用 "data": data 的写法,不然Mock.mock无效
		dataType: "json" 			// 此句不可省略,不然Mock.mock无效
	}).done(function(ajaxData) {
		// 渲染视图		
		VM.$updateModel(ajaxData, isEdit);
	});
};
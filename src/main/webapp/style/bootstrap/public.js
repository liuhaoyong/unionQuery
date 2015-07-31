function setDate(obj,option) {
	var root=$(obj).closest(".form-group");
	var today = new Date();
	root.find(".endTime").val(toFormat(today));
	switch (option) {
	case 1:
		root.find(".beginTime").val(toFormat(today));
		break;
	case 2:
		root.find(".beginTime").val(toFormat(new Date(today - 1000 * 3600 * 24 * 7)));
		break;
	case 3:
		root.find(".beginTime").val(toFormat(getPreMonth(today, 1)));
		break;
	case 4:
		root.find(".beginTime").val(toFormat(getPreMonth(today, 6)));
		break;
	case 5:
		root.find(".beginTime").val(toFormat(getPreMonth(today, 12)));
		break;
	}
}
function setDateEx(obj,unit,num) {
	var root=$(obj).closest(".form-group");
	var today = new Date();
	root.find(".endTime").val(toFormat(today));
	switch (unit) {
		case 1:
			root.find(".beginTime").val(toFormat(getPreMonth(today, num)));
			break;
		case 2:
			root.find(".beginTime").val(toFormat(new Date(today - 1000 * 3600 * 24 * num)));
			break;
	}
}


function getPreMonth(date, month) {
	var strDate;
	var dvalue = date.getMonth() - month;
	if (dvalue < 0) {
		strDate = (date.getFullYear() - 1) + '-' + (13 + dvalue) + '-'
				+ date.getDate();
	} else {
		strDate = date.getFullYear() + '-' + (date.getMonth() + 1 - month)
				+ '-' + date.getDate();
	}
	return new Date(Date.parse(strDate.replace(/-/g, "/")));
}

function toFormat(d) {
	var D = [ '00', '01', '02', '03', '04', '05', '06', '07', '08', '09' ];
	with (d || new Date)
		return [ getFullYear() + '-' + (D[getMonth() + 1] || getMonth() + 1)
				+ '-' + (D[getDate()] || getDate()) ];
};

// 表单checkbox
var minCheck = $('[type=checkbox]').not('[data-type=allCheck]');

$('[data-type=allCheck]').on('click', function() {
	var $this = $(this);
	var checkGroup = $this.closest('.form-group').find('[type=checkbox]');
	if (this.checked) {
		checkGroup.prop('checked', true);
	} else {
		checkGroup.prop('checked', false);
	}
});

minCheck.on('click',
		function() {
			var $this = $(this);
			var allCheckBox = $this.closest('.form-group').find(
					'[data-type=allCheck]');
			if (!this.checked) {
				allCheckBox.prop('checked', false);
			} else {
				checkAllchecker($this);
			}
		})
$(function() {
	var tableThead = $('#tableThead');
	var floatTr;
	var resize = function() {
		var top = $(window).scrollTop();
		if (floatTr) {
			floatTr.remove();
			floatTr = null;
		}
		if (top > tableThead.offset().top) {
			floatTr = tableThead.clone();
			tableThead.parent().append(floatTr);
			floatTr.addClass('titfixed');
			floatTr.find('tr').css("width", $("#maintable").css("width"));
			var ths = floatTr.find("th");
			var source = $("#tableBody").find("tr").eq(0).find("td");
			for (i = 0; i < ths.size(); i++) {
				ths.eq(i).css("width", source.eq(i).outerWidth());
			}
		}
	};
	$(window).on('scroll', resize);
	$(window).on('resize', resize)
})
function checkAllchecker(obj) {
	var check = true;
	var checkboxes = obj.closest('.form-group').find('[type=checkbox]').not(
			'[data-type=allCheck]');
	checkboxes.each(function() {
		if (!this.checked) {
			check = false;
		}
	})
	if (check) {
		obj.closest('.form-group').find('[data-type=allCheck]').prop('checked',
				true);
	}
}

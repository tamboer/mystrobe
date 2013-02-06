
function initSelectableItems(id) {
	
	$("#" + id + " li").bind("click", function() {
		
		if (!$(this).hasClass()) {
			$(this).siblings().removeClass("ui-selected");
			$(this).addClass("ui-selected");
		}
	});
	
}
$(document).ready(function() {
	$(".weui-wepay-pay-select__li").on('click',function(){
		$(this).children().addClass("weui-wepay-pay-select__item_on");
		$(this).siblings().children().removeClass("weui-wepay-pay-select__item_on");
		return false;
	})
});
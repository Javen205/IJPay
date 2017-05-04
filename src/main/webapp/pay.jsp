<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,user-scalable=0">
<title>Javen微信支付开发指南</title>
<link rel="stylesheet"
	href="//cdn.bootcss.com/weui/1.1.1/style/weui.min.css">
<link rel="stylesheet"
	href="//cdn.bootcss.com/jquery-weui/1.0.1/css/jquery-weui.min.css">
<link rel="stylesheet" href="<%=path%>/static/css/demos.css">
</head>
<body>
<body ontouchstart>
	<div class="weui-tab">
		<div class="weui-tab__bd">
			<div id="tab1" class="weui-tab__bd-item weui-tab__bd-item--active">
				<header class='demos-header'>
				<h1 class="demos-title">微信支付</h1>
				</header>
				<div class="bd">
					<div class="page__bd">
						<div class="weui-cell">
							<div class="weui-cell__hd">
								<label class="weui-label">金额(￥)</label>
							</div>
							<div class="weui-cell__bd">
								<input class="weui-input" type="number" id="money"
									placeholder="请输入数字金额,单位分">
							</div>
						</div>
					</div>
					<div class="weui-btn-area">
						<a href="javascript:scanCode();" class="weui-btn weui-btn_primary">确定支付</a>
					</div>
					<div style="text-align: center; margin-top: 30px">
						<img  id="qrcode" alt="" src="">
					</div>
				</div>
			</div>
			<div id="tab2" class="weui-tab__bd-item">
				<header class='demos-header'>
				<h1 class="demos-title">微信支付</h1>
				</header>
				<div class="bd">
					<div class="page__bd">
						<div class="weui-cell">
							<div class="weui-cell__hd">
								<label class="weui-label">金额(￥)</label>
							</div>
							<div class="weui-cell__bd">
								<input class="weui-input" type="number" id="micropay_money"
									placeholder="请输入数字金额,单位分">
							</div>
						</div>
						<div class="weui-cell">
							<div class="weui-cell__hd">
								<label class="weui-label">条形码</label>
							</div>
							<div class="weui-cell__bd">
								<input class="weui-input" type="number" id="auth_code"
									placeholder="请输入条形码">
							</div>
						</div>
					</div>
					<div class="weui-btn-area">
						<a href="javascript:micropay();"   class="weui-btn weui-btn_primary">确定支付</a>
					</div>
				</div>
			</div>
			<div id="tab3" class="weui-tab__bd-item">
				<header class='demos-header'>
				<h1 class="demos-title">微信支付</h1>
				</header>
				<div class="bd">
					<div class="page__bd">
						<div class="weui-cell">
							<div class="weui-cell__hd">
								<label class="weui-label">金额(￥)</label>
							</div>
							<div class="weui-cell__bd">
								<input class="weui-input" type="number" id="web_money"
									placeholder="请输入数字金额,单位分">
							</div>
						</div>
					</div>
					<div class="weui-btn-area">
						<a href="javascript:wxpay();" class="weui-btn weui-btn_primary">确定支付</a>
					</div>
					<div class="weui-btn-area">
						<a  href="<%=path %>/toOauth" class="weui-btn weui-btn_warn">重新获取openId</a>
					</div>
				</div>
			</div>
			<div id="tab4" class="weui-tab__bd-item">
				<header class='demos-header'>
					<h1 class="demos-title">微信支付</h1>
				</header>
				<h1 class="demos-title">Javen</h1>
				<div class="weui-msg__extra-area" style="margin-bottom: 100px">
					<div class="weui-footer">
						<p class="weui-footer__links">
							<a href="http://wpa.qq.com/msgrd?v=3&uin=572839485&site=qq&menu=yes" target="_blank" class="weui-footer__link">Javen提供技术支持</a>
						</p>
						<p class="weui-footer__text">Copyright © 2015-2017</p>
					</div>
				</div>
			</div>
		</div>

		<div class="weui-tabbar">
			<a href="#tab1" class="weui-tabbar__item weui-bar__item--on"> <span
				class="weui-badge"
				style="position: absolute; top: -.4em; right: 1em;">8</span>
				<div class="weui-tabbar__icon">
					<img src="<%=path%>/static/images/icon_nav_button.png" alt="">
				</div>
				<p class="weui-tabbar__label">扫码支付</p>
			</a> <a href="#tab2" class="weui-tabbar__item">
				<div class="weui-tabbar__icon">
					<img src="<%=path%>/static/images/icon_nav_msg.png" alt="">
				</div>
				<p class="weui-tabbar__label">刷卡支付</p>
			</a> <a href="#tab3" class="weui-tabbar__item">
				<div class="weui-tabbar__icon">
					<img src="<%=path%>/static/images/icon_nav_article.png" alt="">
				</div>
				<p class="weui-tabbar__label">公众号支付</p>
			</a> <a href="#tab4" class="weui-tabbar__item">
				<div class="weui-tabbar__icon">
					<img src="<%=path%>/static/images/icon_nav_cell.png" alt="">
				</div>
				<p class="weui-tabbar__label">我</p>
			</a>
		</div>
	</div>
</body>
<script src="//cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="//cdn.bootcss.com/jquery-weui/1.0.1/js/jquery-weui.min.js"></script>
<script src="<%=path%>/static/layer/layer.js"></script>
<script type="text/javascript" src="<%=path%>/js/jquery.qrcode.min.js"></script>
<script type="text/javascript">
/* 微信扫码支付 */
function scanCode(){
	$.showLoading("正在加载...");
	var total_fee=$.trim($("#money").val());
	$.post("<%=path%>/wxsubpay/scanCode", {
			total_fee : total_fee,
		}, function(res) {
			$.hideLoading();
			if (res.code == 0) {
				var name = res.data;
				console.log(name);
				showScanCode(name);
			} else {
				if (res.code == 2) {
					layer.alert(res.message);
				} else {
					layer.msg("error：" + res.message, {
						shift : 6
					});
				}
			}
		});

	}

	function showScanCode(name) {
		$('#qrcode').attr("src","<%=path%>/"+name);
	}
	/* 微信扫码支付 END*/
	/* 微信刷卡支付 */
	function micropay(){
		$.showLoading("正在加载...");
		var total_fee=$.trim($("#micropay_money").val());
		var auth_code=$.trim($("#auth_code").val());
		$.post("<%=path%>/wxsubpay/micropay",
		    {
		      total_fee:total_fee,
		      auth_code:auth_code,
		    },
		    function(res){
		    	$.hideLoading();
		    	if (res.code == 0) {
					layer.msg("支付成功", {shift: 6});
					
					self.location="<%=path%>/success.jsp";
			} else {
				if (res.code == 2) {
					layer.alert(res.message);
				} else {
					layer.msg("error：" + res.message, {
						shift : 6
					});
				}
			}
		});
	}
	/* 微信刷卡支付 END*/
	/* 微信公众号支付支付 */
	function wxpay(){
		$.showLoading("正在加载...");
		var total_fee=$.trim($("#web_money").val());
		$.post("<%=path%>/wxsubpay",
		    {
		      total_fee:total_fee,
		    },
		    function(res){
		    	$.hideLoading();
		    	if (res.code == 0) {
		    		var data=$.parseJSON(res.data);
		    		
		    		if (typeof WeixinJSBridge == "undefined"){
		    			if( document.addEventListener ){
		    				document.addEventListener('WeixinJSBridgeReady', onBridgeReady(data), false);
		    			}else if (document.attachEvent){
		    				document.attachEvent('WeixinJSBridgeReady', onBridgeReady(data)); 
		    				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(data));
		    			}
		    		}else{
		    			onBridgeReady(data);
		    		}
		    	}else{
		    		if (res.code == 2) {
		    			layer.alert(res.message) ;
		    		}else{
		    			layer.msg("error："+res.message, {shift: 6});
		    		}
		    	}
		    }); 
		
	}

	function onBridgeReady(json){
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest', 
			json,
			function(res){
				// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
				if(res.err_msg == "get_brand_wcpay_request:ok" ) {
					layer.msg("支付成功", {shift: 6});
					
					self.location="<%=path%>/success.jsp";

					} else {
						layer.msg("支付失败", {
							shift : 6
						});
					}
				});
	}
	/* 微信公众号支付支付 END */
</script>
</html>
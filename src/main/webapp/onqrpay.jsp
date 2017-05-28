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
<title>一码统天下</title>
<link rel="stylesheet"
	href="//cdn.bootcss.com/weui/1.1.1/style/weui.min.css">
<link rel="stylesheet"
	href="//cdn.bootcss.com/jquery-weui/1.0.1/css/jquery-weui.min.css">
<link rel="stylesheet" href="<%=path%>/static/css/demos.css">
</head>
<body>
<body ontouchstart>
	<div class="weui-tab__bd">
		<div>
			<header class='demos-header'>
			<h1 class="demos-title">一码统天下</h1>
			<c:if test="${payModel == 0}">
				<p class='demos-sub-title'>
					IJPay 让支付触手可及，<strong style="color: red;">请在微信客户端或者支付宝客户端中打开
						-By Javen205</strong>
				</p>
			</c:if> <c:if test="${payModel == 1}">
				<p class='demos-sub-title'>IJPay 让支付触手可及，一码统天下之微信支付</p>

			</c:if> <c:if test="${payModel == 2}">
				<p class='demos-sub-title'>IJPay 让支付触手可及，一码统天下之支付宝支付</p>
			</c:if> </header>
			<c:if test="${payModel != 0}">
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
						<a href="javascript:oneQrPay();" class="weui-btn weui-btn_primary">确定支付</a>
					</div>
				</div>
			</c:if>

		</div>
	</div>
</body>
<script src="//cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="//cdn.bootcss.com/jquery-weui/1.0.1/js/jquery-weui.min.js"></script>
<script src="<%=path%>/static/layer/layer.js"></script>
<script type="text/javascript">
	var payModel = ${payModel};
	function oneQrPay() {
		var total_fee=$.trim($("#money").val());
		if (payModel == 2) {
			//支付宝
			location.href = <%=path%>"/oneqrpay/toPay?payModel=2&total_fee="+total_fee;
		} else {
			$.showLoading("正在加载...");
			$.post("<%=path%>/oneqrpay/toPay", {
					total_fee : total_fee,
					payModel : payModel,
				}, function(res) {
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
						
					} else {
						if (res.code == 2) {
							layer.alert(res.message);
						} else {
							layer.msg("error：" + res.message, {shift : 6});
						}
					}
				});
		}

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
</script>
</html>
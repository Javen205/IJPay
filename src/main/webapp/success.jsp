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
	<header class='demos-header'>
	<h1 class="demos-title">微信支付</h1>
	</header>
	<div class="weui-msg">
		<div class="weui-msg__icon-area">
			<i class="weui-icon-success weui-icon_msg"></i>
		</div>
		<div class="weui-msg__text-area">
			<h2 class="weui-msg__title">支付成功</h2>
			<!-- <p class="weui-msg__desc">
				内容详情，可根据实际需要安排，如果换行则不超过规定长度，居中展现<a href="javascript:void(0);">文字链接</a>
			</p> -->
		</div>
		<div class="weui-msg__opr-area">
			<p class="weui-btn-area">
				<a href="javascript:closeWindow();" class="weui-btn weui-btn_primary">确认</a>
			</p>
		</div>
		<div class="weui-msg__extra-area">
			<div class="weui-footer">
				<p class="weui-footer__links">
					<a href="javascript:void(0);" class="weui-footer__link">Javen提供技术支持</a>
				</p>
				<p class="weui-footer__text">Copyright © 2008-2016</p>
			</div>
		</div>
	</div>
</body>
<script src="//cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="//cdn.bootcss.com/jquery-weui/1.0.1/js/jquery-weui.min.js"></script>
<script type="text/javascript">
function closeWindow(){
	WeixinJSBridge.call('closeWindow');
}
</script>
</html>
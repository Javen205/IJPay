package com.ijpay.jfinal.demo.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class IndexController extends Controller {
	private Log log = Log.getLog(IndexController.class);

	public void index() {
        render("index.html");
	}

	public void json(){
		Map<String,Object> map = new HashMap<>();
		map.put("IJPay","https://gitee.com/javen205/IJPay");
		map.put("IJPay Doc","https://javen205.gitee.io/ijpay");
		map.put("author","https://javen.blog.csdn.net");
		map.put("TNW","微信公众号开发脚手架 https://gitee.com/javen205/TNW");

		log.info(JsonKit.toJson(map));
		
		renderJson(map);
	}

	public void test(){
		String jsonStr="{\"access_token\":\"24_Yu_egoPZUfah7MaLRUKRFq7Zm_k5eMeDoNEfTSqsa_zI5DDJcC_pGykWsXWn3ywIWv_efZtuS194qysxKYGiRQ\",\"expires_in\":7200,\"refresh_token\":\"24_NHNbiHbsjkz6tPtkY7b7UyX_glaYrVHuWQ-KBU1KFt9SMGhHlY6UelStVO6BRm6w23vYPSB0Twr4DLI1x67UiQ\",\"openid\":\"oMOBxuAE9oK6sTSx3ntILJvXHDzU\",\"scope\":\"snsapi_base\"}";
		System.out.println(jsonStr);
		Map<String, Object> temp = (Map) JsonUtils.parse(jsonStr, Map.class);
		System.out.println(temp);
		renderJson(temp);
	}
}
package com.javen.jpay.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.TemplateData;
import com.jfinal.weixin.sdk.api.TemplateMsgApi;

public class WeiXinUtils {
	
	public static String filterWeixinEmoji(String source){
		if (containsEmoji(source)) {
			source = filterEmoji(source);
		}
		return source;
	}

	/**
	 * 检测是否有emoji字符
	 * 
	 * @param source
	 * @return 一旦含有就抛出
	 */
	public static boolean containsEmoji(String source) {
		if (StrKit.isBlank(source)) {
			return false;
		}

		int len = source.length();

		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);

			if (isEmojiCharacter(codePoint)) {
				// do nothing，判断到了这里表明，确认有表情字符
				return true;
			}
		}

		return false;
	}

	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {

		if (!containsEmoji(source)) {
			return source;// 如果不包含，直接返回
		}
		// 到这里铁定包含
		StringBuilder buf = null;

		int len = source.length();

		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);

			if (isEmojiCharacter(codePoint)) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}

				buf.append(codePoint);
			} else {
			}
		}

		if (buf == null) {
			return source;// 如果没有找到 emoji表情，则返回源字符串
		} else {
			if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
				buf = null;
				return source;
			} else {
				return buf.toString();
			}
		}
	}
	
	/** 
     * emoji表情转换(hex -> utf-16) 
     *  
     * @param hexEmoji 
     * @return 
     */  
    public static String emoji(int hexEmoji) {  
        return String.valueOf(Character.toChars(hexEmoji));  
    }
	
	/**
	 * 发送模板消息
	 * @param orderId
	 * @param price
	 * @param couresName
	 * @param teacherName
	 * @param openId
	 * @param url
	 * @return
	 */
	public static ApiResult sendTemplateMessage_2(String orderId,String price,String couresName,String teacherName,String openId,String url){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒");
		String time=sdf.format(new Date());
		ApiResult result = TemplateMsgApi.send(TemplateData.New()
				.setTemplate_id("7y1wUbeiYFsUONKH1IppVi47WwViICAjREZSdR3Zahc")
				.setTopcolor("#743A3A")
				.setTouser(openId)
				.setUrl(url)
				.add("first", "您好,你已成功购买课程", "#000000")
				.add("keyword1", orderId, "#FF0000")
				.add("keyword2", price+"元", "#c4c400")
				.add("keyword3", couresName, "#c4c400")
				.add("keyword4",teacherName, "#c4c400")
				.add("keyword5",time, "#0000FF")
				.add("remark", "\n 请点击详情直接看课程直播，祝生活愉快", "#008000")
				.build());
		
		
		return result;
	}
	
	public static ApiResult sendTemplateMessageByOpen(String orderId,String price,String couresName,String teacherName,String openId,String url){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒");
		String time=sdf.format(new Date());
		ApiResult result = TemplateMsgApi.send(TemplateData.New()
				.setTemplate_id("7y1wUbeiYFsUONKH1IppVi47WwViICAjREZSdR3Zahc")
				.setTopcolor("#743A3A")
				.setTouser(openId)
				.setUrl(url)
				.add("first", "您好,你已成功购买课程", "#000000")
				.add("keyword1", orderId, "#FF0000")
				.add("keyword2", price+"元", "#c4c400")
				.add("keyword3", couresName, "#c4c400")
				.add("keyword4",teacherName, "#c4c400")
				.add("keyword5",time+"\n我们的专业客服人员会在24小时内与您联系，请注意接听我们的电话，再次感谢您的支持！", "#000000")
				.add("remark", "\n 请点击详情直接看课程直播，祝生活愉快", "#008000")
				.build());
		
		return result;
	}
	
	public static ApiResult sendTemplateMessageByPrivate(String orderId,String price,String couresName,String teacherName,String openId,String url){
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒");
		String time=sdf.format(new Date());
		ApiResult result = TemplateMsgApi.send(TemplateData.New()
				.setTemplate_id("7y1wUbeiYFsUONKH1IppVi47WwViICAjREZSdR3Zahc")
				.setTopcolor("#743A3A")
				.setTouser(openId)
				.setUrl(url)
				.add("first", "您好,你已成功购买课程", "#000000")
				.add("keyword1", orderId, "#FF0000")
				.add("keyword2", price+"元", "#c4c400")
				.add("keyword3", couresName, "#c4c400")
				.add("keyword4",teacherName, "#c4c400")
				.add("keyword5",time, "#000000")
				.add("remark", "\n我们的专业客服人员会在24小时内与您联系，请注意接听我们的电话，再次感谢您的支持！", "#008000")
				.build());
		
		return result;
	}
	
}
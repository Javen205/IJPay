/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       日志打印工具类
 * =============================================================================
 */
package com.jpay.unionpay;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @ClassName LogUtil
 * @Description acpsdk日志工具类
 * @date 2016-7-22 下午4:04:35
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
public class LogUtil {

	private final static Logger GATELOG = LoggerFactory.getLogger("ACP_SDK_LOG");
	private final static Logger GATELOG_ERROR = LoggerFactory.getLogger("SDK_ERR_LOG");
	private final static Logger GATELOG_MESSAGE = LoggerFactory.getLogger("SDK_MSG_LOG");

	final static String LOG_STRING_REQ_MSG_BEGIN = "============================== SDK REQ MSG BEGIN ==============================";
	final static String LOG_STRING_REQ_MSG_END = "==============================  SDK REQ MSG END  ==============================";
	final static String LOG_STRING_RSP_MSG_BEGIN = "============================== SDK RSP MSG BEGIN ==============================";
	final static String LOG_STRING_RSP_MSG_END = "==============================  SDK RSP MSG END  ==============================";

	/**
	 * 记录普通日志
	 * 
	 * @param cont
	 */
	public static void writeLog(String cont) {
		GATELOG.info(cont);
	}

	/**
	 * 记录ERORR日志
	 * 
	 * @param cont
	 */
	public static void writeErrorLog(String cont) {
		GATELOG_ERROR.error(cont);
	}

	/**
	 * 记录ERROR日志
	 * 
	 * @param cont
	 * @param ex
	 */
	public static void writeErrorLog(String cont, Throwable ex) {
		GATELOG_ERROR.error(cont, ex);
	}

	/**
	 * 记录通信报文
	 * 
	 * @param msg
	 */
	public static void writeMessage(String msg) {
		GATELOG_MESSAGE.info(msg);
	}

	/**
	 * 打印请求报文
	 * 
	 * @param reqParam
	 */
	public static void printRequestLog(Map<String, String> reqParam) {
		writeMessage(LOG_STRING_REQ_MSG_BEGIN);
		Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			writeMessage("[" + en.getKey() + "] = [" + en.getValue() + "]");
		}
		writeMessage(LOG_STRING_REQ_MSG_END);
	}

	/**
	 * 打印响应报文.
	 * 
	 * @param res
	 */
	public static void printResponseLog(String res) {
		writeMessage(LOG_STRING_RSP_MSG_BEGIN);
		writeMessage(res);
		writeMessage(LOG_STRING_RSP_MSG_END);
	}

	/**
	 * debug方法
	 * 
	 * @param cont
	 */
	public static void debug(String cont) {
		if (GATELOG.isDebugEnabled()) {
			GATELOG.debug(cont);
		}
	}
}


package com.ijpay.unionpay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author UnionPay
 */
public class LogUtil {

	private final static Logger GATE_LOG = LoggerFactory.getLogger("ACP_SDK_LOG");
	private final static Logger GATE_LOG_ERROR = LoggerFactory.getLogger("SDK_ERR_LOG");
	private final static Logger GATE_LOG_MESSAGE = LoggerFactory.getLogger("SDK_MSG_LOG");

	final static String LOG_STRING_REQ_MSG_BEGIN = "============================== SDK REQ MSG BEGIN ==============================";
	final static String LOG_STRING_REQ_MSG_END = "==============================  SDK REQ MSG END  ==============================";
	final static String LOG_STRING_RSP_MSG_BEGIN = "============================== SDK RSP MSG BEGIN ==============================";
	final static String LOG_STRING_RSP_MSG_END = "==============================  SDK RSP MSG END  ==============================";

	public static void writeLog(String cont) {
		GATE_LOG.info(cont);
	}

	public static void writeErrorLog(String cont) {
		GATE_LOG_ERROR.error(cont);
	}

	public static void writeErrorLog(String cont, Throwable ex) {
		GATE_LOG_ERROR.error(cont, ex);
	}

	public static void writeMessage(String msg) {
		GATE_LOG_MESSAGE.info(msg);
	}

	public static void printRequestLog(Map<String, String> reqParam) {
		writeMessage(LOG_STRING_REQ_MSG_BEGIN);
		Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			writeMessage("[" + en.getKey() + "] = [" + en.getValue() + "]");
		}
		writeMessage(LOG_STRING_REQ_MSG_END);
	}

	public static void printResponseLog(String res) {
		writeMessage(LOG_STRING_RSP_MSG_BEGIN);
		writeMessage(res);
		writeMessage(LOG_STRING_RSP_MSG_END);
	}

	public static void debug(String cont) {
		if (GATE_LOG.isDebugEnabled()) {
			GATE_LOG.debug(cont);
		}
	}
}

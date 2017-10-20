/**
 * Copyright (c) 2015-2017, Javen Zhou  (javen205@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jpay.unionpay;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jpay.ext.kit.DateKit;
import com.jpay.ext.kit.StrKit;

/**
 * 
 * @author Javen 2017年10月6日
 */
public class UnionPayApiConfig {
	private UnionPayApiConfig() {
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String version;
		private String encoding;
		private String signMethod;
		private String txnType;
		private String txnSubType;
		private String bizType;
		private String channelType;
		private String accessType;
		private String merId;
		private String frontUrl;
		private String backUrl;
		private String orderId;
		private String currencyCode;
		private String txnAmt;
		private String txnTime;
		private String payTimeout;
		private String accNo;
		private String reqReserved;
		private String orderDesc;
		private String acqInsCode;
		private String merCatCode;
		private String merName;
		private String merAbbr;
		private String origQryId;
		private String settleDate;
		private String fileType;
		private String bussCode;
		private String billQueryInfo;
		private String qrNo;
		private String termId;
		private String accType;
		private String encryptCertId;
		private String customerInfo;

		public Map<String, String> createMap() {
			Map<String, String> map = new HashMap<String, String>();
			if (StrKit.isBlank(version)) {
				version = "5.1.0";
			}
			if (StrKit.isBlank(encoding)) {
				encoding = "UTF-8";
			}
			if (StrKit.isBlank(signMethod)) {
				signMethod = "01";
			}
			if (StrKit.isBlank(txnType)) {
				txnType = "01";
			}
			if (StrKit.isBlank(txnSubType)) {
				txnSubType = "01";
			}
			if (StrKit.isBlank(bizType)) {
				bizType = "000201";
			}
			if (StrKit.isBlank(channelType)) {
				channelType = "07";
			}
			if (StrKit.isBlank(accessType)) {
				accessType = "0";
			}
			if (StrKit.isBlank(merId)) {
				throw new IllegalArgumentException("merId 值不能为 null");
			}
			if (StrKit.isBlank(backUrl)) {
				backUrl = SDKConfig.getConfig().getBackUrl();
			}
			if (StrKit.isBlank(frontUrl)) {
				frontUrl = SDKConfig.getConfig().getFrontUrl();
			}

			if (StrKit.isBlank(orderId)) {
				orderId = String.valueOf(System.currentTimeMillis());
			}
			if (orderId.contains("_") || orderId.contains("-")) {
				throw new IllegalArgumentException("orderId 值不应含“-”或“_”");
			}
			if (StrKit.isBlank(currencyCode)) {
				currencyCode = "156";
			}
			if (StrKit.isBlank(txnAmt)) {
				txnAmt = "1";
			}
			if (StrKit.isBlank(txnTime)) {
				txnTime = DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern);
			}
			if (StrKit.isBlank(payTimeout)) {
				payTimeout = DateKit.toStr(new Date(), 15 * 60 * 1000, DateKit.UnionTimeStampPattern);
			}
			

			map.put("version", version);
			map.put("encoding", encoding);
			map.put("signMethod", signMethod);
			map.put("txnType", txnType);
			map.put("txnSubType", txnSubType);
			map.put("bizType", bizType);
			map.put("channelType", channelType);
			map.put("accessType", accessType);
			map.put("merId", merId);
			map.put("frontUrl", frontUrl);
			map.put("backUrl", backUrl);
			map.put("orderId", orderId);
			map.put("currencyCode", currencyCode);
			map.put("txnAmt", txnAmt);
			map.put("txnTime", txnTime);
			map.put("payTimeout", payTimeout);
			map.put("accNo", accNo);
			map.put("reqReserved", reqReserved);
			map.put("orderDesc", orderDesc);
			map.put("acqInsCode", acqInsCode);
			map.put("merCatCode", merCatCode);
			map.put("merName", merName);
			map.put("merAbbr", merAbbr);
			map.put("origQryId", origQryId);
			map.put("settleDate", settleDate);
			map.put("fileType", fileType);
			map.put("bussCode", bussCode);
			map.put("billQueryInfo", billQueryInfo);
			map.put("qrNo", qrNo);
			map.put("termId", termId);
			map.put("accType", accType);
			map.put("encryptCertId", encryptCertId);
			map.put("customerInfo", customerInfo);
			
			return setSignMap(map);
		}
		
		public Map<String, String> setSignMap(Map<String, String> map) {
			// 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
			return AcpService.sign(map, encoding);
		}
		

		public Builder setVersion(String version) {
			this.version = version;
			return this;
		}

		public Builder setEncoding(String encoding) {
			this.encoding = encoding;
			return this;
		}

		public Builder setSignMethod(String signMethod) {
			this.signMethod = signMethod;
			return this;
		}

		public Builder setTxnType(String txnType) {
			this.txnType = txnType;
			return this;
		}

		public Builder setTxnSubType(String txnSubType) {
			this.txnSubType = txnSubType;
			return this;
		}

		public Builder setBizType(String bizType) {
			this.bizType = bizType;
			return this;
		}

		public Builder setChannelType(String channelType) {
			this.channelType = channelType;
			return this;
		}

		public Builder setAccessType(String accessType) {
			this.accessType = accessType;
			return this;
		}

		public Builder setMerId(String merId) {
			this.merId = merId;
			return this;
		}

		public Builder setFrontUrl(String frontUrl) {
			this.frontUrl = frontUrl;
			return this;
		}

		public Builder setBackUrl(String backUrl) {
			this.backUrl = backUrl;
			return this;
		}

		public Builder setOrderId(String orderId) {
			this.orderId = orderId;
			return this;
		}

		public Builder setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
			return this;
		}

		public Builder setTxnAmt(String txnAmt) {
			this.txnAmt = txnAmt;
			return this;
		}

		public Builder setTxnTime(String txnTime) {
			this.txnTime = txnTime;
			return this;
		}

		public Builder setPayTimeout(String payTimeout) {
			this.payTimeout = payTimeout;
			return this;
		}

		public Builder setAccNo(String accNo) {
			this.accNo = accNo;
			return this;
		}

		public Builder setReqReserved(String reqReserved) {
			this.reqReserved = reqReserved;
			return this;
		}

		public Builder setOrderDesc(String orderDesc) {
			this.orderDesc = orderDesc;
			return this;
		}

		public Builder setAcqInsCode(String acqInsCode) {
			this.acqInsCode = acqInsCode;
			return this;
		}

		public Builder setMerCatCode(String merCatCode) {
			this.merCatCode = merCatCode;
			return this;
		}

		public Builder setMerName(String merName) {
			this.merName = merName;
			return this;
		}

		public Builder setMerAbbr(String merAbbr) {
			this.merAbbr = merAbbr;
			return this;
		}

		public Builder setOrigQryId(String origQryId) {
			this.origQryId = origQryId;
			return this;
		}

		public Builder setSettleDate(String settleDate) {
			this.settleDate = settleDate;
			return this;
		}

		public Builder setFileType(String fileType) {
			this.fileType = fileType;
			return this;
		}

		public Builder setBussCode(String bussCode) {
			this.bussCode = bussCode;
			return this;
		}

		public Builder setBillQueryInfo(String billQueryInfo) {
			this.billQueryInfo = billQueryInfo;
			return this;
		}

		public Builder setQrNo(String qrNo) {
			this.qrNo = qrNo;
			return this;
		}

		public Builder setTermId(String termId) {
			this.termId = termId;
			return this;
		}

		public Builder setAccType(String accType) {
			this.accType = accType;
			return this;
		}

		public Builder setEncryptCertId(String encryptCertId) {
			this.encryptCertId = encryptCertId;
			return this;
		}

		public Builder setCustomerInfo(String customerInfo) {
			this.customerInfo = customerInfo;
			return this;
		}
		
		
	}

}

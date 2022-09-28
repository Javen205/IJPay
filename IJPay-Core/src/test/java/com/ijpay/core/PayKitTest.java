package com.ijpay.core;


import cn.hutool.core.date.DateUtil;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.model.CertificateModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class PayKitTest {

	@Test
	public void checkCertificateIsValid() {
		CertificateModel model = new CertificateModel();
		model.setNotAfter(DateUtil.offsetDay(new Date(),2));
		boolean isValid = PayKit.checkCertificateIsValid(model, -1);
		Assert.assertTrue(isValid);
	}

	@Test
	public void checkCertificateIsValidByPath() {
	    String path = "";
		boolean isValid =  PayKit.checkCertificateIsValid(path,-2);
		Assert.assertFalse(isValid);
	}
}

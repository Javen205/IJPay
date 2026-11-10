package com.ijpay.alipay;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AliPayApiToMapTest {

	@Test
	public void toMapToleratesNullValueArray() {
		Map<String, String[]> requestParams = new HashMap<String, String[]>();
		requestParams.put("nullValue", null);
		requestParams.put("multi", new String[]{"1", "2"});

		Map<String, String> result = AliPayApi.toMap(requestParams);

		Assert.assertEquals("", result.get("nullValue"));
		Assert.assertEquals("1,2", result.get("multi"));
	}

	@Test
	public void toMapReturnsEmptyMapForNullInput() {
		Map<String, String> result = AliPayApi.toMap(null);

		Assert.assertNotNull(result);
		Assert.assertTrue(result.isEmpty());
	}
}

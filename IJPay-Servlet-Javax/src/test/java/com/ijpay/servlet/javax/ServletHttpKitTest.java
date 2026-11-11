package com.ijpay.servlet.javax;

import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

public class ServletHttpKitTest {

	@Test
	public void readDataReadsBodyAndClosesRequestReader() {
		ServletRequestStub stub = new ServletRequestStub().body("line1\nline2");
		HttpServletRequest request = stub.build();

		Assert.assertEquals("line1\nline2", ServletHttpKit.readData(request));
		Assert.assertTrue("读取完成后应关闭请求的 Reader", stub.isReaderClosed());
	}

	@Test
	public void readDataWrapsGetReaderIOException() {
		HttpServletRequest request = new ServletRequestStub().failOnGetReader().build();

		try {
			ServletHttpKit.readData(request);
			Assert.fail("getReader 抛出 IOException 时应包装为 RuntimeException");
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getCause() instanceof IOException);
		}
	}

	@Test
	public void readDataIgnoresCloseFailureSilently() throws Exception {
		ServletRequestStub stub = new ServletRequestStub().body("body").closeReaderWithError();
		PrintStream originalErr = System.err;
		ByteArrayOutputStream captured = new ByteArrayOutputStream();
		try {
			System.setErr(new PrintStream(captured, true, "UTF-8"));
			Assert.assertEquals("body", ServletHttpKit.readData(stub.build()));
		} finally {
			System.setErr(originalErr);
		}
		Assert.assertTrue("关闭失败前仍应尝试关闭 Reader", stub.isReaderClosed());
		Assert.assertEquals("关闭失败的 Reader 不应输出堆栈", 0, captured.size());
	}

	@Test
	public void toMapJoinsMultiValueParameters() {
		HttpServletRequest request = new ServletRequestStub()
			.parameter("a", "1", "2")
			.parameter("b", "3")
			.build();

		Map<String, String> result = ServletHttpKit.toMap(request);

		Assert.assertEquals("1,2", result.get("a"));
		Assert.assertEquals("3", result.get("b"));
	}
}

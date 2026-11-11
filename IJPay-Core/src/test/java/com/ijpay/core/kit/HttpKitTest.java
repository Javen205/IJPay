package com.ijpay.core.kit;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpKitTest {

	@Test
	public void toMapJoinsMultiValueWithComma() {
		Map<String, String[]> params = new LinkedHashMap<String, String[]>();
		params.put("a", new String[]{"1", "2", "3"});
		params.put("b", new String[]{"4"});

		Map<String, String> result = HttpKit.toMap(params);

		Assert.assertEquals("1,2,3", result.get("a"));
		Assert.assertEquals("4", result.get("b"));
	}

	@Test
	public void toMapConvertsEmptyArrayToEmptyString() {
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("empty", new String[0]);

		Map<String, String> result = HttpKit.toMap(params);

		Assert.assertEquals("", result.get("empty"));
	}

	@Test
	public void toMapReturnsEmptyMapForNullInput() {
		Map<String, String> result = HttpKit.toMap(null);

		Assert.assertNotNull(result);
		Assert.assertTrue(result.isEmpty());
	}

	@Test
	public void toMapToleratesNullValueArray() {
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("nullValue", null);

		Map<String, String> result = HttpKit.toMap(params);

		Assert.assertEquals("", result.get("nullValue"));
	}

	@Test
	public void readDataJoinsLinesWithLineFeed() {
		Assert.assertEquals("a\nb\nc", HttpKit.readData(new StringReader("a\nb\nc")));
		Assert.assertEquals("a\nb", HttpKit.readData(new StringReader("a\r\nb\r\n")));
		Assert.assertEquals("", HttpKit.readData(new StringReader("")));
	}

	@Test
	public void readDataDoesNotCloseReader() throws IOException {
		CloseTrackingReader reader = new CloseTrackingReader("body");

		Assert.assertEquals("body", HttpKit.readData(reader));
		Assert.assertFalse("HttpKit.readData 不应关闭调用方的 Reader", reader.closed);
	}

	static final class CloseTrackingReader extends StringReader {
		boolean closed;

		CloseTrackingReader(String content) {
			super(content);
		}

		@Override
		public void close() {
			closed = true;
			super.close();
		}
	}
}

package com.etouch.taf.core.datamanager.txt.test;

import static org.junit.Assert.*;

import java.util.Properties;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import com.etouch.taf.core.datamanager.txt.TextReader;
import com.etouch.taf.core.test.util.TafTestUtil;
import com.etouch.taf.util.LogUtil;

public class TextReaderTest {
	
	private static Log log = LogUtil.getLog(TextReaderTest.class);
	
	private static Properties prop = null;
	
	TextReader trd = null;
	
	@Before
	public void setup()
	{
		prop = TafTestUtil.loadProps(TafTestUtil.propFilePath);
		String current =System.getProperty("user.dir");
		trd = new TextReader(current+prop.getProperty("txtFilePath"));
	}

	@Test
	public void test() {
		if(trd!=null)
		Assert.assertNotNull(trd.readText());
	}

}

package com.etouch.taf.core.datamanager.doc.test;

import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import com.etouch.taf.core.datamanager.doc.DocxReader;
import com.etouch.taf.core.test.util.TafTestUtil;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;

public class DocxReaderTest {

	/** The Log object **/
	private static Log log = LogUtil.getLog(DocxReaderTest.class);

	private static Properties prop = null;
	DocxReader dr = null;

	@Before
	public void setUp() {
		prop = TafTestUtil.loadProps(TafTestUtil.propFilePath);
		String current = System.getProperty("user.dir");
		dr = new DocxReader(current + prop.getProperty("docxFilePath"));
	}

	@Test
	public void testReadParagraps() throws Exception {
		String str = dr.readParagraphs();
		// CommonUtil.sop(str);
		Assert.assertNotNull(str);

	}

	@Test
	public void testReadHeader() {
		String str = dr.readHeader(2);
		CommonUtil.sop("Header is" + str);
		Assert.assertNotNull(str);
	}

	@Test
	public void testReadFooter() {

		String str = dr.readFooter(2);
		CommonUtil.sop("Footer is" + str);
		Assert.assertNotNull(str);

	}

	@Test
	public void testReadImages() {
		boolean isImageExtracted = false;
		List<byte[]> images = dr.readImages();
		if (images != null && images.size() > 0) {
			isImageExtracted = true;
			CommonUtil.sop("Image count in the doc is: "
					+ String.valueOf(images.size()));
		}
		Assert.assertTrue(isImageExtracted);

	}

	/*
	 * @Test public void testReadDocumentSummary() {
	 * 
	 * String str = dr.readDocumentSummary(); CommonUtil.sop(str);
	 * Assert.assertNotNull(str);
	 * 
	 * }
	 */

}

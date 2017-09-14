package com.etouch.taf.core.datamanager.doc.test;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.apache.commons.logging.Log;
import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;

import com.etouch.taf.core.datamanager.doc.DocReader;
import com.etouch.taf.core.test.util.TafTestUtil;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;

public class DocReaderTest {

	/** The Log object **/
	private static Log log = LogUtil.getLog(DocReaderTest.class);

	private static Properties prop = null;

	DocReader dr = null;

	@Before
	public void setUp() {
		prop = TafTestUtil.loadProps(TafTestUtil.propFilePath);
		String current = System.getProperty("user.dir");
		dr = new DocReader(current + prop.getProperty("docFilePath"));
	}

	@Test
	public void testReadParagraps() throws Exception {
		StringBuilder str = dr.readParagraphs();
		// CommonUtil.sop(str);
		Assert.assertNotNull(str);

	}

	@Test
	public void testReadHeader() throws IOException, XmlException {
		String str = dr.readHeader(2);
		CommonUtil.sop(str);
		Assert.assertNotNull(str);

	}

	@Test
	public void testReadFooter() throws IOException, XmlException {

		String str = dr.readFooter(2);
		CommonUtil.sop(str);
		Assert.assertNotNull(str);

	}

	@Test
	public void testReadImages() {
		boolean isImageExtracted = false;
		List<byte[]> images = dr.readImages();
		System.out.println(images.size());
		if (images != null && images.size() > 0) {
			isImageExtracted = true;
			// CommonUtil.sop(String.valueOf(images.size()));
		}
		Assert.assertTrue(isImageExtracted);

	}

	//@Test
	public void testReadDocumentSummary() {

		String str = dr.readDocumentSummary();
		// CommonUtil.sop(str);
		Assert.assertNotNull(str);

	}

}

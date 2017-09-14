package com.etouch.taf.core.datamanager.pdf.test;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.etouch.taf.core.datamanager.pdf.PDFCreator;
import com.etouch.taf.core.test.util.TafTestUtil;
import com.etouch.taf.util.LogUtil;

public class PDFCreatorTest {

	/** The Log object **/
	private static Log log = LogUtil.getLog(PDFCreatorTest.class);

	private static Properties prop = null;

	/** The resulting PDF file. */
	private static String resultPDF = null;

	/** Paths to images. */
	private static final String[] RESOURCES = {
	/*
	 * "bruno_ingeborg.jpg", "map.jp2", "info.png", "close.bmp", "movie.gif",
	 * "butterfly.wmf", "animated_fox_dog.gif", "marbles.tif", "amb.jb2"
	 */
	"Img1.png", "Img3.png", "Img4.png", "Img8.png"

	};

	private static String imgSource = null;

	@Before
	public void setUp() {
		prop = TafTestUtil.loadProps(TafTestUtil.propFilePath);
		String current = System.getProperty("user.dir");
		imgSource = current + prop.getProperty("imageSource");
		resultPDF = imgSource + "image_types.pdf";
	}

	@Test
	public void testCreatePDF() {

		boolean isPDFCreated = false;

		try {
			new PDFCreator().createPdf(RESOURCES, imgSource);
			isPDFCreated = true;
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}

		Assert.assertTrue(isPDFCreated);
	}

}

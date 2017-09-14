package com.etouch.taf.core.datamanager.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;

/**
 * This class is used to create a pdf file with selected images.
 * 
 * @author eTouch
 * 
 */
public class PDFCreator {

	/**
	 * Creates a PDF document.
	 * 
	 * @param filename
	 *            the path to the new PDF document
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void createPdf(String[] imgResource, String imgSourcePath) throws DocumentException, IOException {
		// step 1
		Document document = new Document();
		// step 2 - Commented the writer initialization as its not getting used
		// step 3
		document.open();
		// step 4
		// Adding a series of images
		Image img;

		for (int i = 0; i < imgResource.length; i++) {
			img = Image.getInstance(String.format(imgSourcePath + "\\%s", imgResource[i]));
			if (img.getScaledWidth() > 300 || img.getScaledHeight() > 300) {
				img.scaleToFit(300, 300);
			}
			document.add(new Paragraph(String.format("%s is an image of type %s", imgResource[i], img.getClass().getName())));
			document.add(img);
		}
		// step 5
		document.close();
	}
}
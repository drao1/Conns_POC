package com.etouch.taf.core.datamanager.doc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import com.etouch.taf.util.LogUtil;

public class DocxReader {

	private static Log log = LogUtil.getLog(DocxReader.class);

	
	private XWPFDocument doc = null;

	public DocxReader(String filePath) {
		try {
			InputStream fs = new FileInputStream(filePath);
			this.doc = new XWPFDocument(OPCPackage.open(fs));
		} catch (Exception ex) {
			log.error(ex);
		}

	}

	/*
	 * This method returns the paragraphs from .docx document as a String
	 */
	public String readParagraphs() throws IOException  {
		XWPFWordExtractor extractor = null;
		StringBuilder sb = new StringBuilder();
		try {
			extractor = new XWPFWordExtractor(doc);
			sb.append(extractor.getText());
		} catch (Exception ex) {
			log.error(ex);
		} finally {
			if (extractor != null)
				extractor.close();
		}

		return sb.toString();
	}
	/*
	 * This method returns the Header from the .docx file.
	 */
	public String readHeader(int pageNumber) {

		StringBuilder sb = new StringBuilder();
		try {
			XWPFHeaderFooterPolicy headerStore = new XWPFHeaderFooterPolicy(doc);
			String header = (headerStore.getHeader(pageNumber)).getText();
			sb.append(header);
		} catch (Exception ex) {
			log.error(ex.getMessage(),ex);
		}
		return sb.toString();

	}
	/*
	 * This method returns the footer from .docx file
	 */
	public String readFooter(int pageNumber) {

		StringBuilder sb = new StringBuilder();
		try {
			XWPFHeaderFooterPolicy headerStore = new XWPFHeaderFooterPolicy(doc);
			String footer = (headerStore.getFooter(pageNumber)).getText();
			sb.append(footer);
		} catch (Exception ex) {
			log.error(ex);
		}
		return sb.toString();

	}

	/*
	 * This method returns List of images read from the .docx file
	 */
	public List<byte[]> readImages() {
		
		List<byte[]> result = new ArrayList<>();

		try {
			for (XWPFPictureData picture : doc.getAllPictures()) {
				result.add(picture.getData());
							}

		} catch (Exception ex) {
			log.error(ex);
		}

		return result;
	}
}// end of class
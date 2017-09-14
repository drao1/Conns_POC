package com.etouch.taf.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.etouch.taf.webui.selenium.WebPage;

/**
 * The Class FileDownload.
 */
public class FileDownload {

	/** The web page. */
	private WebPage webPage;

	/** The max wait. */
	final int MAX_WAIT = 55;

	/** The log. */
	static Log log = LogUtil.getLog(FileDownload.class);

	/** The file output stream. */
	FileOutputStream fileOutputStream = null;

	/** The response. */
	HttpResponse response = null;

	/** The file name. */
	String fileName = "tempfile";

	/** The output file path. */
	String outputFilePath = null;

	/**
	 * Download file.
	 * 
	 * @param downloadUrl
	 *            the download url
	 * @param filePath
	 *            the file path
	 * @return the http response
	 * @throws Exception
	 *             the exception
	 */
	public HttpResponse downloadFile(String downloadUrl, String filePath) throws Exception {

		try {

			HttpClient httpClient = HttpClientBuilder.create().build();

			// get the HTTP status of the url to check if url exists
			HttpGet httpGet = new HttpGet(downloadUrl);
			response = httpClient.execute(httpGet);

			Pattern regex = Pattern.compile("(?<=filename=\").*?(?=\")");

			HttpEntity entity = response.getEntity();
			// check if entity response object is not null
			if (entity != null) {
				outputFilePath = filePath + fileName;

				// Create file object
				File outputFile = new File(outputFilePath);

				// get the entity content of the file to be downloaded
				InputStream inputStream = entity.getContent();

				// Write the entity content out to the output stream
				fileOutputStream = new FileOutputStream(outputFile);

				CommonUtil.sop(" File Path" + outputFilePath);

				int read = 0;
				byte[] bytes = new byte[2048];

				// start writing to output stream only if byte size is not equal
				// to -1
				while ((read = inputStream.read(bytes)) != -1) {
					fileOutputStream.write(bytes, 0, read);
				}

				// Print the file size and content type of the downloaded file
				CommonUtil.sop("File size is ------> " + outputFile.length() + " bytes");
			}
		} catch (FileNotFoundException e) {
			log.debug("File downloading failed!!!");
			log.debug("FileNotFoundException", e);
		} finally {
			// close the file stream after reading
			fileOutputStream.close();

		}
		return response;
	}

	public static void main(String args[]) {
		String downloadUrl = "file://localhost/C:/Lavanya/Project/eTouch/eTap_framework/emp1.xls";
		String filePath = "C:\\Lavanya\\Project\\eTouch\\eTap_framework\\FileDownload\\";
		try {
			HttpResponse response = new FileDownload().downloadFile(downloadUrl, filePath);
			log.debug(response.getEntity().getContentType().getValue());
		} catch (Exception ex) {
			log.debug(ex);
		}
	}
}
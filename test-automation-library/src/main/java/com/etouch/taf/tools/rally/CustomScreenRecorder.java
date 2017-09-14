/**
 * 
 */
package com.etouch.taf.tools.rally;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.screenrecorder.ScreenRecorder;

/**
 * @author Sudarshan Bhattacharjee
 *
 */
public class CustomScreenRecorder extends ScreenRecorder {

	private String videoFileName;
	
	/**
	 * @param cfg
	 * @param captureArea
	 * @param fileFormat
	 * @param screenFormat
	 * @param mouseFormat
	 * @param audioFormat
	 * @param movieFolder
	 * @throws IOException
	 * @throws AWTException
	 */
	public CustomScreenRecorder(GraphicsConfiguration cfg,
			Rectangle captureArea, Format fileFormat, Format screenFormat,
			Format mouseFormat, Format audioFormat, File movieFolder, String customVideoFileName)
			throws IOException, AWTException {
		super(cfg, captureArea, fileFormat, screenFormat, mouseFormat,
				audioFormat, movieFolder);
		
		this.videoFileName =  customVideoFileName;
	}

	/* (non-Javadoc)
	 * @see org.monte.screenrecorder.ScreenRecorder#createMovieFile(org.monte.media.Format)
	 */
	@Override
	protected File createMovieFile(Format fileFormat) throws IOException {
		if (!movieFolder.exists()) {
			movieFolder.mkdirs();
		} else if (!movieFolder.isDirectory()) {
			throw new IOException("\"" + movieFolder + "\" is not a directory.");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH.mm.ss");

		return new File(movieFolder, videoFileName + "_" + dateFormat.format(new Date())
				+ "." + Registry.getInstance().getExtension(fileFormat));
	}

}

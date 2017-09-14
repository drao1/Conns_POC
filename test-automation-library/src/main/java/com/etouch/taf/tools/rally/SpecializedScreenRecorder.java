package com.etouch.taf.tools.rally;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.OutputType;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.util.LogUtil;

import io.appium.java_client.AppiumDriver;

public class SpecializedScreenRecorder extends ScreenRecorder {
	static Log log = LogUtil.getLog(SpecializedScreenRecorder.class);
	private String name;
	static Process child;
	static File scrFile,targetFile;
	static DateFormat dateFormat,dateFormat2;
	static String DateForFolderName,DateForFileName;
	static String droidAtScreenarJarLocation = "..\\test-automation-library\\resources\\droidAtScreen.jar";
	private static ScreenRecorder screenRecorder;
	static String platform="web";
	static protected TestBed testBed = null;
	
	public SpecializedScreenRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
			Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
			throws IOException, AWTException {
		super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
		this.name = name;
	}

	protected File createMovieFile(Format fileFormat) throws IOException {
		if (!movieFolder.exists()) {
			movieFolder.mkdirs();
		} else if (!movieFolder.isDirectory()) {
			throw new IOException("\"" + movieFolder + "\" is not a directory.");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

		return new File(movieFolder,
				name + "-" + dateFormat.format(new Date()) + "." + Registry.getInstance().getExtension(fileFormat));
	}

	/**
	 * This method initiates DroidAtScreen to cast connected devices and uses
	 * monte jar to record activities on casted devices
	 * 
	 * @param String
	 *            Location
	 * @throws IOException,
	 *             AWTException
	 */

	public static void startVideoRecordingForMobile(String videoLocation) throws IOException, AWTException {
		int widthX=400,heightX=750;
		
		try{
			if(processIsTerminated())
			{
			int size = TestBedManagerConfiguration.INSTANCE.getMobileConfig().getCurrentTestBeds().length;
			if(size>2)
			{
				widthX=100;
				heightX=1200;
			}
			else if(size==2)
			{
				widthX=400;
				heightX=1100;
			}
			
			log.info("Starting droidAtScree.jar");
			
		child = Runtime.getRuntime().exec("java -jar " + droidAtScreenarJarLocation);
		}
		}
		catch(Throwable e)
		{
			log.error("Failed to find droidAtScreen.jar at "+droidAtScreenarJarLocation );
		}
		log.info("Resolution is :"+widthX+" "+heightX);
		log.info("Video Location is set to :"+videoLocation);
		File file = new File(videoLocation);
		Rectangle captureSize = new Rectangle(widthX, 0, heightX, 800);
		// Set frame capture ratio
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
				new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
						CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
						Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
				null, file, "MyVideo");
		platform = "Mobile";
		screenRecorder.start();

	}
/**
 * Initiates video recording for desktop browser
 * @param videoLocation
 * @throws IOException
 * @throws AWTException
 */
	public static void startVideoRecordingForDesktopBrowser(String videoLocation) throws IOException, AWTException {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		File file = new File(videoLocation);
		Rectangle captureSize = new Rectangle(0, 0, width, height);
		// Set frame capture ratio
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
				new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
						CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
						Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
				new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
				null, file, "MyVideo");
		screenRecorder.start();

	}

	/**
	 * Stops recording and stop DroidAtScreen process
	 * 
	 * @param
	 * @throws IOException,
	 *             AWTException
	 */
	public static void stopVideoRecording() throws IOException, AWTException {
		screenRecorder.stop();
		if(platform.equalsIgnoreCase("Mobile"))
				
		child.destroy();
		}
	
	public void getScreenShot(String FileLocationScreenShot,String Method,AppiumDriver driver) throws IOException
	{
	
	scrFile = driver.getScreenshotAs(OutputType.FILE);
	dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh");
	dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy__hh_mm");
	DateForFolderName= dateFormat.format(new Date());
	DateForFileName= dateFormat2.format(new Date());
	targetFile=new File(FileLocationScreenShot+"src/test/resources/testdata/Screenshot/"+DateForFolderName+"/" + Method +DateForFileName+".jpg");
	FileUtils.copyFile(scrFile,targetFile);
	
	}
	/**
	 * Verify if child process to start droidAtScreen is already running
	 * prevents multiple recording session while executing test in parallel
	 * @return
	 */
	private static boolean processIsTerminated () {
	    try {
	    	
	       int value = child.exitValue();
	     
	    } catch (IllegalThreadStateException itse) {
	 
	        return false;
	    }
	    catch(Exception e)
        {
        	
        	return true;
        }
	    return true;
	}

}

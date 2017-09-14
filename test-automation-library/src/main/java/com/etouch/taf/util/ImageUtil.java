/*
 * 
 */
package com.etouch.taf.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;


/**
 * This class contains utility methods for comparing Images.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class ImageUtil {
	
	/** The log. */
	private static Log log = LogUtil.getLog(ImageUtil.class);

	/**
	 * Compare images using m d5.
	 *
	 * @param currentScreenShotFile the current screen shot file
	 * @param goldImageScreenShotFile the gold image screen shot file
	 * @return true, if successful
	 */
	public static boolean compareImagesUsingMD5(String currentScreenShotFile,
			String goldImageScreenShotFile ) {
		boolean isMD5ValueSame = false;
		try {
			File currentScreenShot = new File(currentScreenShotFile);
			File goldImageScreenShot = new File(goldImageScreenShotFile);
			// Calculate MD5 value for the image found environment being checked
			// (e.g. QA)
			String md5ValueForQA = getMG5ValueForImage(currentScreenShot);
			// Calculate MD5 value for the golden image
			String md5ValueForProd = getMG5ValueForImage(goldImageScreenShot);

			isMD5ValueSame = md5ValueForQA.equals(md5ValueForProd);
		} catch (IOException iex) {
			log.debug("IOException", iex);
		} catch (NoSuchAlgorithmException noAlgo) {
			log.debug("NoSuchAlgorithmException", noAlgo);
		} catch (Exception ex) {
			log.debug(ex);
		}

		return isMD5ValueSame;

	}

	/**
	 * Calculates the MD5 Value for a given image.
	 *
	 * @param imgFile            File object for a screenshot/image
	 * @return the m g5 value for image
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws Exception the exception
	 */
	private static String getMG5ValueForImage(File imgFile) throws IOException,
			NoSuchAlgorithmException, Exception {
		BufferedImage buffImg = ImageIO.read(imgFile);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(buffImg, "png", outputStream);
		byte[] data = null;
		if(OSUtil.is64BitOS() == true) {
			data = Base64.decodeBase64(outputStream.toByteArray());	
		} else if(OSUtil.is32BitOS() == true) {
			data = outputStream.toByteArray();
		}
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data);
		byte[] hash = md.digest();
		return getHexValueForBytes(hash);
	}
	
	/**
	 * Converts the MD5 byte value to a Hex String.
	 *
	 * @param inBytes the in bytes
	 * @return the hex value for bytes
	 */
	private static String getHexValueForBytes(byte[] inBytes) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < inBytes.length; i++) { // for loop ID:1
			hexString.append(Integer.toString((inBytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		} // Belongs to for loop ID:1
		return hexString.toString();
	} // Belongs to returnHex class
	
	
	public static void scale(File screenshotFile) throws IOException {
        try {
           
           double fileSize = new Long(screenshotFile.length()).doubleValue();
           
           if(fileSize >= 5000000){
        	   log.debug("Screenshot file size exceeds 5 MB. Attempting to reduce...");
        	   BufferedImage originalScreenshotImage = ImageIO.read(screenshotFile);
        	   BufferedImage newScreenshotImage = resize(originalScreenshotImage, fileSize );
               ImageIO.write(newScreenshotImage, "PNG", screenshotFile);
           }
           
        } catch (IOException e) {
        	log.error("Unable to Reduce the image size...");
        	throw new IOException("Unable to Reduce the image size...", e.getCause());
        }
    }

	private static BufferedImage resize(BufferedImage existingImg, double fileSize) {  
		
		if( fileSize < 5000000){
			
	        int origWidth = existingImg.getWidth();
	        int origHeight = existingImg.getHeight();
	        int scaledWidth = (int) (origWidth / 2);
	        int scaledHeight = (int) (origHeight / 2);
	        Image scaledImage = existingImg.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
	        existingImg = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g = existingImg.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        g.drawImage(scaledImage, 0, 0, null);
	        g.dispose();
	        
		}
		else {
			existingImg = resize(existingImg, (fileSize / 2 ));
		}
		
		return existingImg;
    }
	
}

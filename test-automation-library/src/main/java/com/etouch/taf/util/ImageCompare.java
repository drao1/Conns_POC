package com.etouch.taf.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.swing.GrayFilter;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.ImageWriter;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageCompare {

	protected BufferedImage img1 = null;
	protected BufferedImage img2 = null;
	protected BufferedImage imgc = null;
	protected String expectedImageURL = null;
	protected int comparex = 0;
	protected int comparey = 0;
	protected int factorA = 0;
	protected int factorD = 10;
	protected boolean match = false;
	protected int debugMode = 0; // 1: textual indication of change, 2:
									// difference of factors

	static Log log = LogUtil.getLog(ImageCompare.class);

	// constructor 1. use filenames
	public ImageCompare(String file1, String file2) throws ImageFormatException, IOException {
		this(loadJPG(file1), loadJPG(file2));
	}

	// constructor 2. use awt images.
	public ImageCompare(Image img1, Image img2) {
		this(imageToBufferedImage(img1), imageToBufferedImage(img2));
	}

	// constructor 3. use buffered images. all roads lead to the same place.
	// this place.
	public ImageCompare(BufferedImage img1, BufferedImage img2) {
		this.img1 = img1;
		this.img2 = img2;
		autoSetParameters();
	}

	// constructor 4. use urls
	public ImageCompare(URL actualImageUrl, URL expectedImageUrl) throws IOException {
		this(urlToBufferedImage(actualImageUrl), urlToBufferedImage(expectedImageUrl));

	}

	// like this to perhaps be upgraded to something more heuristic in the
	// future.
	protected void autoSetParameters() {
		comparex = 10;
		comparey = 10;
		factorA = 10;
		factorD = 10;
	}

	// set the parameters for use during change detection.
	public void setParameters(int x, int y, int factorA, int factorD) {
		this.comparex = x;
		this.comparey = y;
		this.factorA = factorA;
		this.factorD = factorD;
	}

	// want to see some stuff in the console as the comparison is happening?
	public void setDebugMode(int m) {
		this.debugMode = m;
	}

	// compare the two images in this object.
	public void compare() {
		// setup change display image
		imgc = imageToBufferedImage(img2);
		Graphics2D gc = imgc.createGraphics();
		gc.setColor(Color.RED);
		// convert to gray images.
		img1 = imageToBufferedImage(GrayFilter.createDisabledImage(img1));
		img2 = imageToBufferedImage(GrayFilter.createDisabledImage(img2));
		// how big are each section
		int blocksx = (int) (img1.getWidth() / comparex);
		int blocksy = (int) (img1.getHeight() / comparey);
		// set to a match by default, if a change is found then flag non-match
		this.match = true;
		// loop through whole image and compare individual blocks of images
		for (int y = 0; y < comparey; y++) {
			if (debugMode > 0)
				log.debug("|");
			for (int x = 0; x < comparex; x++) {
				int b1 = getAverageBrightness(img1.getSubimage(x * blocksx, y * blocksy, blocksx - 1, blocksy - 1));
				int b2 = getAverageBrightness(img2.getSubimage(x * blocksx, y * blocksy, blocksx - 1, blocksy - 1));
				int diff = Math.abs(b1 - b2);
				if (diff > factorA) { // the difference in a certain region has
										// passed the threshold value of factorA
					// draw an indicator on the change image to show where
					// change was detected.
					gc.drawRect(x * blocksx, y * blocksy, blocksx - 1, blocksy - 1);
					this.match = false;
				}
				if (debugMode == 1)
					log.debug((diff > factorA ? "X" : " "));
				if (debugMode == 2)
					log.debug(diff + (x < comparex - 1 ? "," : ""));
			}
			if (debugMode > 0)
				log.debug("|");
		}

	}

	// return the image that indicates the regions where changes where detected.
	public BufferedImage getChangeIndicator() {
		return imgc;
	}

	// returns a value specifying some kind of average brightness in the image.
	protected int getAverageBrightness(BufferedImage img) {
		Raster r = img.getData();
		int total = 0;
		for (int y = 0; y < r.getHeight(); y++) {
			for (int x = 0; x < r.getWidth(); x++) {
				total += r.getSample(r.getMinX() + x, r.getMinY() + y, 0);
			}
		}
		return (int) (total / ((r.getWidth() / factorD) * (r.getHeight() / factorD)));
	}

	// returns true if image pair is considered a match
	public boolean match() {
		return this.match;
	}

	// buffered images are just better.
	protected static BufferedImage imageToBufferedImage(Image img) {
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(img, null, null);
		return bi;
	}

	protected static BufferedImage urlToBufferedImage(URL imageUrl) {
		ImageWriter imgWriter = new ImageWriter();
		BufferedImage bi = null;
		try {
			bi = imgWriter.readImage(imageUrl);
		} catch (IOException e) {

			log.debug("IOException", e);
		}
		return bi;
	}

	// write a buffered image to a jpeg file.
	protected static void saveJPG(Image img, String filename) throws FileNotFoundException {
		BufferedImage bi = imageToBufferedImage(img);
		FileOutputStream out = new FileOutputStream(filename);

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
		param.setQuality(0.8f, false);
		encoder.setJPEGEncodeParam(param);
		try {
			encoder.encode(bi);
			out.close();
		} catch (java.io.IOException io) {
			log.debug("IOException", io);
		}
	}

	// read a jpeg file into a buffered image
	protected static Image loadJPG(String filename) throws ImageFormatException, IOException {
		FileInputStream in = new FileInputStream(filename);
		JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
		return decoder.decodeAsBufferedImage();
	}

}
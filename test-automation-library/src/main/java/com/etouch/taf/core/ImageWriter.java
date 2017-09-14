package com.etouch.taf.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.etouch.taf.webui.ITafElement;

public class ImageWriter {

	/**
	 * 
	 * @param URL
	 * @return
	 * @throws IOException
	 */
	public BufferedImage readImage(URL url) throws IOException {

		BufferedImage image = ImageIO.read(url);
		BufferedImage filtered = null;

		int w = image.getWidth(null);
		int h = image.getHeight(null);
		if (image.getType() != BufferedImage.TYPE_INT_RGB) {
			BufferedImage bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics big = bi2.getGraphics();
			big.drawImage(image, 0, 0, null);
			image = bi2;
			filtered = image;
		}
		return filtered;
	}

	/**
	 * 
	 * @param image
	 * @param formatName
	 * @param file
	 * @throws IOException
	 */
	public void writeImage(BufferedImage image, String formatName, File file) throws IOException {
		ImageIO.write(image, formatName, file);
	}

	/**
	 * 
	 * @param webDriver
	 * @param element
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage shootWebElement(RemoteWebDriver webDriver, ITafElement element) throws IOException {
		File screen = webDriver.getScreenshotAs(OutputType.FILE);

		org.openqa.selenium.Point p = element.getLocation();

		int width = element.getSize().getWidth();
		int height = element.getSize().getHeight();

		BufferedImage img = ImageIO.read(screen);

		return img.getSubimage(p.getX(), p.getY(), width, height);

	}

}
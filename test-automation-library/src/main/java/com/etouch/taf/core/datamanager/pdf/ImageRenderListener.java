package com.etouch.taf.core.datamanager.pdf;

/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */


import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.driver.web.SafariDriver;
import com.etouch.taf.util.LogUtil;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class ImageRenderListener implements RenderListener {

    /** The new document to which we've added a border rectangle. */
    protected String path = "";
    private static Log log = LogUtil.getLog(ImageRenderListener.class);

    /**
     * Creates a RenderListener that will look for images.
     */
    public ImageRenderListener(String path) {
        this.path = path;
    }
    
    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
     */
    @Override
    public void beginTextBlock() throws UnsupportedOperationException{
    	//No changes are required here
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
     */
    @Override
    public void endTextBlock() throws UnsupportedOperationException{
    	//No changes are required here
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(
     *     com.itextpdf.text.pdf.parser.ImageRenderInfo)
     */
    @Override
    public void renderImage(ImageRenderInfo renderInfo) {
        try {
            String filename;
            FileOutputStream os;
            PdfImageObject image = renderInfo.getImage();
            if (image == null) 
            	return;
            filename = String.format(path, renderInfo.getRef().getNumber(), image.getFileType());
            os = new FileOutputStream(filename);
            os.write(image.getImageAsBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
        	log.debug("IOException", e);
        }
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(
     *     com.itextpdf.text.pdf.parser.TextRenderInfo)
     */
    @Override
    public void renderText(TextRenderInfo renderInfo) throws UnsupportedOperationException{
    	//No changes are required here
    }
}
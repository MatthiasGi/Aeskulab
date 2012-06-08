package de.gianfelice.aeskulab.system.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * This utility is used to scale images but keeping their ratio. Also integrates
 * methods useful for managing files.
 * 
 * @author  Dr. Franz Graf (http://frickelblog.wordpress.com/fast-image-scaling)
 * @author  Matthias Gianfelice
 * @version 1.0.3
 */
public class ImageScaler {

	// ------------------------------- Method(s) -------------------------------
	/**
	 * Returns the format of a file-object. Useful for working with buffered
	 * images.
	 *
	 * @param file the file
	 * @return      The type of the given file
	 */
	public static String getFormatName(File file) {
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) return null;
			
			ImageReader reader = iter.next();
			iis.close();
			return reader.getFormatName();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Scales an image.
	 *
	 * @param img the img
	 * @param d the d
	 * @return     A scaled image
	 */
    public static BufferedImage scaleImage(BufferedImage img, Dimension d) {
        img = scaleByHalf(img, d);
        img = scaleExact(img, d);
        return img;
    }

    /**
     * As long as the image is twice as big as the given dimension: Scale image
     * with factor 0.5 and Nearest Neighbour Interpolation.
     *
     * @param img the img
     * @param d the d
     * @return     A scaled image
     */
    private static BufferedImage scaleByHalf(BufferedImage img, Dimension d) {        
        int w = img.getWidth();
        int h = img.getHeight();
        float factor = getBinFactor(w, h, d);

        // make new size
        w *= factor;
        h *= factor;
        BufferedImage scaled = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);  
        
        g.drawImage(img, 0, 0, w, h, null);
        g.dispose();        
        return scaled;
    }

    /**
     * Last step to scale this image with Bilinear Interpolation.
     *
     * @param img the img
     * @param d the d
     * @return     A scaled image
     */
    private static BufferedImage scaleExact(BufferedImage img, Dimension d) {
        float factor = getFactor(img.getWidth(), img.getHeight(), d);

        // create the image
        int w = (int) (img.getWidth() * factor);
        int h = (int) (img.getHeight() * factor);
        BufferedImage scaled = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, w, h, null);
        g.dispose();
        return scaled;
    }

    /**
     * Get the factor that should be applied to do the.
     *
     * @param width the width
     * @param height the height
     * @param dim the dim
     * @return        A factor to scale the image with
     * {@link #scaleByHalf(BufferedImage, Dimension)} operation.
     */
    private static float getBinFactor(int width, int height, Dimension dim) {
    	float factor = 1;    	
        float target = getFactor(width, height, dim);
        
        if (target <= 1) {
        	while (factor / 2 > target) {
        		factor /= 2;
        	}
        } else {
        	while (factor * 2 < target) {
        		factor *= 2;
        	}
        }

        return factor;
    }

    /**
     * Get exact factor that should be applied to the image to fit in the given
     * dimension.
     *
     * @param width the width
     * @param height the height
     * @param dim the dim
     * @return        A factor to scale the image with
     */
    private static float getFactor(int width, int height, Dimension dim) {
        float sx = dim.width / (float) width;
        float sy = dim.height / (float) height;
        return Math.min(sx, sy);
    }

}
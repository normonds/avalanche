package avalanche;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class AvalancheUtil {

public static void appendToFile (String file, String str) {
	try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
	    out.print(str);
	} catch (IOException e) {
		e.printStackTrace();
	    //exception handling left as an exercise for the reader
	}
}
public static String createSymbols (String str) {
	return str.replace("d","♦").replace("h","♥").replace("c","♣").replace("s","♠");
}
public static BufferedImage invertImage (BufferedImage input, boolean makeNew) {
	int MONO_THRESHOLD = 368;
	BufferedImage output;
	if (makeNew) {
		output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_RGB);
	} else output = input;
    for (int x = 0; x < input.getWidth(); x++) {
        for (int y = 0; y < input.getHeight(); y++) {
            int rgba = input.getRGB(x, y);
            
            java.awt.Color col = new java.awt.Color(rgba, true);
//            if (col.getRed() + col.getGreen() + col.getBlue() > MONO_THRESHOLD)
//                col = new java.awt.Color(255, 255, 255);
//            else
//                col = new java.awt.Color(0, 0, 0);
            
            col = new java.awt.Color(255 - col.getRed(),
                            255 - col.getGreen(),
                            255 - col.getBlue());
            
            output.setRGB(x, y, col.getRGB());
        }
    }
    return output;
}
public static boolean compareBitmaps (BufferedImage cached, BufferedImage big/*, int[] rect*/) {
	if (cached==null || big==null) return false;
	if (cached.getWidth()!=big.getWidth() || cached.getHeight()!=cached.getHeight()) {
		System.out.println(cached.getWidth()+"-"+cached.getHeight()+","+big.getWidth()+"-"+big.getHeight());
		return false;
	}
	for (int x=0;x<cached.getWidth();x++) {
		for (int y=0;y<cached.getHeight();y++) {
			if (cached.getRGB(x, y)!=big.getRGB(x/*+rect[0]*/, y/*+rect[1]*/)) {
				return false;
			}
		}
	}
	return true;
}
public static List<String> readFile (String dir, String file) {
	File curDir = new File(dir);
	for (final File fileEntry : curDir.listFiles()) {
		if (fileEntry.isDirectory()) {
		} else {
			if (fileEntry.getName().equals(file)) {
				 try {
					return Files.readAllLines(fileEntry.toPath(), Charset.defaultCharset());
				} catch (IOException e) {
					System.out.println("Error reading file:"+fileEntry.getPath());
				}
			}
		}
	}
	System.out.println("File "+file+" not found");
	return new ArrayList<String>();
}

public static List<String> loadRegionFile (final File folder, String file) {
	List<String> ret = new ArrayList<String>();
	for (final File fileEntry : folder.listFiles()) {
		if (fileEntry.isDirectory()) {
			//System.out.println("--"  + fileEntry);
		} else {
			//System.out.println(fileEntry);
			if (fileEntry.getName().indexOf("regions."+file)>-1) {
				 try {
					System.out.println(Files.readAllLines(fileEntry.toPath(), Charset.defaultCharset()));
					return Files.readAllLines(fileEntry.toPath(), Charset.defaultCharset());
				} catch (IOException e) {
					System.out.println("Error reading file:"+fileEntry.getPath());
				}
				//System.out.println(fileEntry.getName());
			}
			//System.out.println(fileEntry.getName());
		}
	}
	return ret;
}

//final File folder = new File("/home/you/Desktop");
//listFilesForFolder(folder);
public static ImageData convertToSWT (BufferedImage bufferedImage) {
	if (bufferedImage.getColorModel() instanceof DirectColorModel) {
		/*
		 * DirectColorModel colorModel =
		 * (DirectColorModel)bufferedImage.getColorModel(); PaletteData palette
		 * = new PaletteData( colorModel.getRedMask(),
		 * colorModel.getGreenMask(), colorModel.getBlueMask()); ImageData data
		 * = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
		 * colorModel.getPixelSize(), palette); WritableRaster raster =
		 * bufferedImage.getRaster(); int[] pixelArray = new int[3]; for (int y
		 * = 0; y < data.height; y++) { for (int x = 0; x < data.width; x++) {
		 * raster.getPixel(x, y, pixelArray); int pixel = palette.getPixel(new
		 * RGB(pixelArray[0], pixelArray[1], pixelArray[2])); data.setPixel(x,
		 * y, pixel); } }
		 */
		DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
		PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(),
				colorModel.getBlueMask());
		ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(),
				palette);
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				int rgb = bufferedImage.getRGB(x, y);
				int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
				data.setPixel(x, y, pixel);
				if (colorModel.hasAlpha()) {
					data.setAlpha(x, y, (rgb >> 24) & 0xFF);
				}
			}
		}
		return data;
	} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
		IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
		int size = colorModel.getMapSize();
		byte[] reds = new byte[size];
		byte[] greens = new byte[size];
		byte[] blues = new byte[size];
		colorModel.getReds(reds);
		colorModel.getGreens(greens);
		colorModel.getBlues(blues);
		RGB[] rgbs = new RGB[size];
		for (int i = 0; i < rgbs.length; i++) {
			rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
		}
		PaletteData palette = new PaletteData(rgbs);
		ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(),
				palette);
		data.transparentPixel = colorModel.getTransparentPixel();
		WritableRaster raster = bufferedImage.getRaster();
		int[] pixelArray = new int[1];
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				raster.getPixel(x, y, pixelArray);
				data.setPixel(x, y, pixelArray[0]);
			}
		}
		return data;
	} else if (bufferedImage.getColorModel() instanceof ComponentColorModel) {
		ComponentColorModel colorModel = (ComponentColorModel) bufferedImage.getColorModel();
		// ASSUMES: 3 BYTE BGR IMAGE TYPE
		PaletteData palette = new PaletteData(0x0000FF, 0x00FF00, 0xFF0000);
		ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(),
				palette);
		// This is valid because we are using a 3-byte Data model with no
		// transparent pixels
		data.transparentPixel = -1;
		WritableRaster raster = bufferedImage.getRaster();
		int[] pixelArray = new int[3];
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				raster.getPixel(x, y, pixelArray);
				int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
				data.setPixel(x, y, pixel);
			}
		}
		return data;
	}
	return null;
}
}

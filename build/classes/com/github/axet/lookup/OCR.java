package com.github.axet.lookup;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.github.axet.lookup.common.ClassResources;
import com.github.axet.lookup.common.FontFamily;
import com.github.axet.lookup.common.FontSymbol;
import com.github.axet.lookup.common.FontSymbolLookup;
import com.github.axet.lookup.common.ImageBinary;
import com.github.axet.lookup.common.ImageBinaryGrey;

public class OCR extends OCRCore {

    public OCR(float threshold) {
        super(threshold);
    }

    /**
     * set sensitivity
     * 
     * @param threshold
     *            1 - exact match. 0 - not match. -1 - opposite difference
     */
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float getThreshold() {
        return threshold;
    }

    /**
     * Load fonts / symbols from a class directory or jar file
     * 
     * @param c
     *            class name, corresponded to the resources. com.example.MyApp.class
     * @param path
     *            path to the fonts folder. directory should only contain folders with fonts which to load
     * 
     */
    public void loadFontsDirectory(Class<?> c, File path) {
        ClassResources e = new ClassResources(c, path);

        List<String> str = e.names();

        for (String s : str)
            loadFont(c, new File(path, s));
    }

    /**
     * Load specified font family to load
     * 
     * @param c
     *            class name, corresponded to the resources. com.example.MyApp.class
     * @param path
     *            path to the fonts folder. directory should only contain folders with fonts which to load.
     * @param name
     *            name of the font to load
     * 
     */
    public void loadFont(Class<?> c, File path) {
        ClassResources e = new ClassResources(c, path);

        List<String> str = e.names();

        for (String s : str) {
            File f = new File(path, s);

            InputStream is = c.getResourceAsStream(f.getPath());

            String symbol = FilenameUtils.getBaseName(s);

            try {
                symbol = URLDecoder.decode(symbol, "UTF-8");
            } catch (UnsupportedEncodingException ee) {
                throw new RuntimeException(ee);
            }

            String name = path.getName();
            loadFontSymbol(name, symbol, is);
        }
    }

    public void loadFontSymbol(String fontName, String fontSymbol, InputStream is) {
        FontFamily ff = fontFamily.get(fontName);
        if (ff == null) {
            ff = new FontFamily(fontName);
            fontFamily.put(fontName, ff);
        }
        FontSymbol f = loadFontSymbol(ff, fontSymbol, is);
        ff.add(f);
    }

    public FontSymbol loadFontSymbol(FontFamily ff, String fontSymbol, InputStream is) {
        BufferedImage bi = Capture.load(is);
        return new FontSymbol(ff, fontSymbol, bi);
    }

    public String recognize(BufferedImage bi) {
        ImageBinary i = new ImageBinaryGrey(bi);

        return recognize(i);
    }

    public String recognize(ImageBinary i) {
        List<FontSymbol> list = getSymbols();

        return recognize(i, 0, 0, i.getWidth() - 1, i.getHeight() - 1, list);
    }

    /**
     * 
     * @param fontSet
     *            use font in the specified folder only
     * @param bi
     * @return
     */
    public String recognize(BufferedImage bi, String fontSet) {
        ImageBinary i = new ImageBinaryGrey(bi);

        return recognize(i, fontSet);
    }

    public String recognize(ImageBinary i, String fontSet) {
        List<FontSymbol> list = getSymbols(fontSet);

        return recognize(i, 0, 0, i.getWidth() - 1, i.getHeight() - 1, list);
    }

    public String recognize(ImageBinary i, int x1, int y1, int x2, int y2) {
        List<FontSymbol> list = getSymbols();

        return recognize(i, x1, y1, x2, y2, list);
    }

    public String recognize(ImageBinary i, int x1, int y1, int x2, int y2, String fontFamily) {
        List<FontSymbol> list = getSymbols(fontFamily);

        return recognize(i, x1, y1, x2, y2, list);
    }

    public String recognize(ImageBinary i, int x1, int y1, int x2, int y2, List<FontSymbol> list) {
        List<FontSymbolLookup> all = findAll(list, i, x1, y1, x2, y2);

        return recognize(all);
    }

    public String recognize(List<FontSymbolLookup> all) {
        String str = "";

        if (all.size() == 0)
            return str;

        // bigger first.

        Collections.sort(all, new BiggerFirst(all));

        // big images eat small ones

        for (int k = 0; k < all.size(); k++) {
            FontSymbolLookup kk = all.get(k);
            for (int j = k + 1; j < all.size(); j++) {
                FontSymbolLookup jj = all.get(j);
                if (kk.cross(jj)) {
                    all.remove(jj);
                    j--;
                }
            }
        }

        // sort top/bottom/left/right

        Collections.sort(all, new Left2Right());

        // calculate rows

        {
            int x = all.get(0).x;
            int cx = 0;
            for (FontSymbolLookup s : all) {
                int maxCX = Math.max(cx, s.getWidth());

                // if distance betten end of previous symbol and begining of the
                // current is larger then a char size, then it is a space
                if (s.x - (x + cx) >= maxCX)
                    str += " ";

                // if we drop back, then we have a end of line
                if (s.x < x)
                    str += "\n";
                x = s.x + s.getWidth();
                cx = s.getWidth();
                str += s.fs.fontSymbol;
            }
        }

        return str;
    }

}

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import processing.core.PImage;
import core.DImage;
import FileIO.PDFHelper;
import Interfaces.PixelFilter;

// Author: David Dobervich (this is my edit)
// ANOTHER EDIT.
public class OpticalMarkReaderMain {
    private static String currentFolder = System.getProperty("user.dir") + "/";

    public static void main(String[] args) {
        String pathToPdf = fileChooser();
        System.out.println("Loading pdf at " + pathToPdf);

        /*
         * Your code here to...
         * (1). Load the pdf
         * (2). Loop over its pages
         * (3). Create a DImage from each page and process its pixels
         * (4). Output 2 csv files
         */

        ArrayList<DImage> pages = getPagesFromPDF(pathToPdf);

        applyFilter(pages, pathToPdf, new BWMaskingFilter());
    }

    private static ArrayList<DImage> getPagesFromPDF(String pathToPdf) {
        ArrayList<PImage> pages = PDFHelper.getPImagesFromPdf(pathToPdf);
        ArrayList<DImage> dImages = new ArrayList<DImage>();

        for (int i = 0; i < pages.size(); i++) {
            dImages.add(new DImage(pages.get(i)));
        }

        return dImages;
    }

    private static void applyFilter(ArrayList<DImage> pages, String pathToPdf, NamedPixelFilter filter) {
        for (int i = 0; i < pages.size(); i++) {
            filter.processImage(pages.get(i));
            pages.get(i).getPImage().save(getOutputPath(pathToPdf) + filter.getName() + "/page" + (i + 1) + ".png");
        }
    }

    private static String getOutputPath(String pathToPdf) {
        String[] pathParts = pathToPdf.split("/");
        String pdfName = pathParts[pathParts.length - 1].split("\\.")[0];
        String outputPath = "";
        for (int i = 0; i < pathParts.length - 1; i++) {
            outputPath += pathParts[i] + "/";
        }
        outputPath += "output/" + pdfName + "/";

        return outputPath;
    }

    private static String fileChooser() {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fc = new JFileChooser(userDir);
        int returnVal = fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file.getAbsolutePath();
    }
}

interface NamedPixelFilter extends PixelFilter {
    public String getName();
}

class BWMaskingFilter implements NamedPixelFilter {
    public BWMaskingFilter() {
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                if (pixels[i][j] < 150) {
                    pixels[i][j] = 0;
                } else {
                    pixels[i][j] = 255;
                }
            }
        }

        img.setPixels(pixels);
        return img;
    }

    @Override
    public String getName() {
        return "BWMasking";
    }
}

class DilationFilter implements NamedPixelFilter {
    public DilationFilter() {
    }

    @Override
    public DImage processImage(DImage img) {
        return img;
    }

    @Override
    public String getName() {
        return "Dilation";
    }
}

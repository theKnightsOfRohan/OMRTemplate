import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import processing.core.PImage;
import core.DImage;
import FileIO.PDFHelper;
import FileIO.ReadWrite;
import Interfaces.PixelFilter;

// Author: David Dobervich (this is my edit)
// ANOTHER EDIT.
public class OpticalMarkReaderMain {
    private static String currentFolder = System.getProperty("user.dir") + "/";

    public static void main(String[] args) {
        String pathToPdf = currentFolder + "assets/OfficialOMRSampleDoc.pdf";

        System.out.println("Loading pdf at " + pathToPdf);

        /*
         * Your code here to...
         * (1). Load the pdf
         * (2). Loop over its pages
         * (3). Create a DImage from each page and process its pixels
         * (4). Output 2 csv files
         */

        ArrayList<DImage> pages = getPagesFromPDF(pathToPdf);
        System.out.println(BubbleData.getString());

        applyFilter(pages, pathToPdf, new BWFilter());

        ArrayList<ArrayList<Integer>> markedAnswers = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < pages.size(); i++) {
            ArrayList<Integer> studentAnswer = getMarkedAnswers(pages.get(i));
            System.out.println("Page " + (i + 1) + " marked answers: " + studentAnswer);
            markedAnswers.add(studentAnswer);
        }

        ReadWrite.writeToFile(currentFolder + "assets/output/correctAnswers.txt", markedAnswers);

        ConstructOutputStatisticFiles.perStudentAnalysis(markedAnswers,
                currentFolder + "assets/output/student_analysis.json");
        ConstructOutputStatisticFiles.perQuestionAnalysis(markedAnswers,
                currentFolder + "assets/output/question_analysis.json");
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
        outputPath += "output/";

        return outputPath;
    }

    private static ArrayList<Integer> getMarkedAnswers(DImage image) {
        ArrayList<Integer> markedAnswers = new ArrayList<Integer>();

        short[][] pixels = image.getBWPixelGrid();

        for (int i = 0; i < BubbleData.numQuestions; i++) {
            int questionNum = getDarkestMarkedBubble(pixels, i);
            markedAnswers.add(questionNum);
        }

        return markedAnswers;
    }

    private static int getDarkestMarkedBubble(short[][] pixels, int questionNum) {
        int darkestBubble = -1;
        double darkestValue = 255;

        String logString = "Question " + (questionNum + 1) + ": ";

        for (int i = 0; i < BubbleData.numPossibleAnswers; i++) {
            int r = BubbleData.startR + (int) Math.round(questionNum * BubbleData.deltaR);
            int c = BubbleData.startC + (int) Math.round(i * BubbleData.deltaC);

            double total = 0;
            for (int dr = 0; dr < BubbleData.bubbleSize; dr++) {
                for (int dc = 0; dc < BubbleData.bubbleSize; dc++) {
                    total += pixels[r + dr][c + dc];
                }
            }

            double average = total / (BubbleData.bubbleSize * BubbleData.bubbleSize);

            logString += "Bubble " + (i + 1) + " at (" + c + ", " + r + ") with average: " + average + "; ";

            if (average < darkestValue) {
                darkestValue = average;
                darkestBubble = i + 1;
            }
        }

        logString += "Darkest bubble: " + darkestBubble + " with value: " + darkestValue;
        // System.out.println(logString);

        return darkestBubble;
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

class BWFilter implements NamedPixelFilter {
    public BWFilter() {
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                if (pixels[i][j] < 200) {
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
        return "BWFilter";
    }
}

class BubbleData {
    public static final int bubbleSize = 20;
    public static final int startC = 105;
    public static final int startR = 108;
    public static final int numQuestions = 12;
    public static final int numPossibleAnswers = 5;

    public static final double deltaC = 24.5;
    public static final double deltaR = 48.36;

    public static String getString() {
        return "Bubble size: " + bubbleSize + ", Start C: " + startC + ", Start R: " + startR + ", Delta C: " + deltaC
                + ", Delta R: " + deltaR + ", Num questions: " + numQuestions + ", Num possible answers: "
                + numPossibleAnswers;
    }
}

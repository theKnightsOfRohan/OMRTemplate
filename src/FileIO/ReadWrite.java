package FileIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

public class ReadWrite {
    public static void writeToFile(String filePath, String data) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String filePath, List<Object> data) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (Object line : data) {
                writer.println(line.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String filePath, ArrayList<ArrayList<Integer>> data) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (ArrayList<Integer> line : data) {
                writer.println(line.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> readFileByLine(String filePath) {
        List<String> lines = new java.util.ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static String readFile(String filePath) {
        String data = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while (line != null) {
                data += line + "\n";
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}

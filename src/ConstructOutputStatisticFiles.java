import java.io.File;
import java.util.ArrayList;
import processing.core.PImage;
import core.DImage;
import FileIO.PDFHelper;
import FileIO.ReadWrite;
import Interfaces.PixelFilter;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.pkl.codegen.java.JavaCodeGenerator;

public class ConstructOutputStatisticFiles {
    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
        String studentAnalysisPath = "assets/output/student_analysis.json";
        String questionAnalysisPath = "assets/output/question_analysis.json";

        BubbleData.readConfigValues();

        for (int i = 0; i < 10; i++) {
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for (int j = 0; j < 10; j++) {
                temp.add((int) (Math.random() * 10 + 1));
            }
            data.add(temp);
        }

        perStudentAnalysis(data, studentAnalysisPath);
        perQuestionAnalysis(data, questionAnalysisPath);
    }

    public static void perStudentAnalysis(ArrayList<ArrayList<Integer>> data, String outputFilePath) {
        ArrayList<Integer> correctAnswers = data.get(0);

        JSONObject perStudentAnalysis = new JSONObject();

        JSONObject key = new JSONObject();
        for (int i = 0; i < data.get(0).size(); i++) {
            key.put("" + (i + 1), convertNumberToCapitalLetter(data.get(0).get(i)));
        }

        perStudentAnalysis.put("Key", key);

        for (int student = 1; student < data.size(); student++) {
            JSONObject singleStudentAnalysis = new JSONObject();

            JSONObject answers = new JSONObject();

            int right = 0;

            for (int question = 0; question < data.get(student).size(); question++) {
                boolean correct = data.get(student).get(question) == correctAnswers.get(question);
                if (correct) {
                    right++;
                }
                answers.put("" + (question + 1), getRightOrWrong(correct));
            }

            singleStudentAnalysis.put("Answers", answers);

            singleStudentAnalysis.put("Total Correct", right + "/" + data.get(student).size());
            singleStudentAnalysis.put("Score", (right * 100 / (double) data.get(student).size()) + "%");

            perStudentAnalysis.put("" + student, singleStudentAnalysis);
        }

        ReadWrite.writeToFile(outputFilePath, perStudentAnalysis.toString());
    }

    public static void perQuestionAnalysis(ArrayList<ArrayList<Integer>> data, String outputFilePath) {
        JSONArray perQuestionAnalysis = new JSONArray();

        for (int question = 0; question < data.get(0).size(); question++) {
            JSONObject singleQuestionAnalysis = new JSONObject();

            singleQuestionAnalysis.put("Right Answer", convertNumberToCapitalLetter(data.get(0).get(question)));

            JSONObject wrongAnswers = new JSONObject();

            int right = 0;

            for (int student = 0; student < data.size(); student++) {
                boolean correct = data.get(student).get(question) == data.get(0).get(question);

                if (correct) {
                    right++;
                } else {
                    String letter = convertNumberToCapitalLetter(data.get(student).get(question));

                    if (wrongAnswers.has(letter)) {
                        wrongAnswers.put(letter, wrongAnswers.getInt(letter) + 1);
                    } else {
                        wrongAnswers.put(letter, 1);
                    }
                }
            }

            singleQuestionAnalysis.put("Alternate Answers", wrongAnswers);

            singleQuestionAnalysis.put("Total Correct", right + "/" + data.size());
            singleQuestionAnalysis.put("Question", (question + 1));

            perQuestionAnalysis.put(singleQuestionAnalysis);
        }

        ReadWrite.writeToFile(outputFilePath, perQuestionAnalysis.toString());
    }

    private static String getRightOrWrong(boolean value) {
        return value ? "Right" : "Wrong";
    }

    private static String convertNumberToCapitalLetter(int number) {
        return "" + (char) (number + 65);
    }
}

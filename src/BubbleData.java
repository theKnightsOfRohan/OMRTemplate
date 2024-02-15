import org.pkl.config.java.Config;
import org.pkl.config.java.ConfigEvaluator;
import org.pkl.config.java.JavaType;
import org.pkl.core.ModuleSource;

public class BubbleData {
    public static int bubbleSize;
    public static int startC;
    public static int startR;
    public static int numQuestions;
    public static int numPossibleAnswers;

    public static double deltaC;
    public static double deltaR;

    public static String path;

    // Read the config values from the .pkl file
    public static void readConfigValues() {
        Config config;

        try (ConfigEvaluator evaluator = ConfigEvaluator.preconfigured()) {
            config = evaluator.evaluate(ModuleSource.path("src/BubbleData.pkl"));
        }

        bubbleSize = config.get("bubbleSize").as(int.class);
        startC = config.get("startC").as(int.class);
        startR = config.get("startR").as(int.class);
        numQuestions = config.get("numQuestions").as(int.class);
        numPossibleAnswers = config.get("numPossibleAnswers").as(int.class);
        deltaC = config.get("deltaC").as(double.class);
        deltaR = config.get("deltaR").as(double.class);
        path = config.get("path").as(String.class);
    }

    public static String getString() {
        return "Bubble size: " + bubbleSize + ", Start C: " + startC + ", Start R: " + startR + ", Delta C: " + deltaC
                + ", Delta R: " + deltaR + ", Num questions: " + numQuestions + ", Num possible answers: "
                + numPossibleAnswers + ", Path: " + path;
    }
}

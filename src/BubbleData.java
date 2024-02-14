import org.pkl.config.java.Config;
import org.pkl.config.java.ConfigEvaluator;
import org.pkl.config.java.JavaType;
import org.pkl.core.ModuleSource;

public class BubbleData {
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

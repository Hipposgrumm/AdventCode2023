import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Everything here is written with Java 17.
 * @author Hipposgrumm
 */
public class Main {
    private static final Function<String,String> WORDS_TO_NUMBERS = in -> in
            .replace("one","1")
            .replace("two","2")
            .replace("three","3")
            .replace("four","4")
            .replace("five","5")
            .replace("six","6")
            .replace("seven","7")
            .replace("eight","8")
            .replace("nine","9");
    private static final Function<String,String> WORDS_TO_NUMBERS_REVERSED = ni -> ni
            .replace("enin","9")
            .replace("thgie","8")
            .replace("neves","7")
            .replace("xis","6")
            .replace("evif","5")
            .replace("ruof","4")
            .replace("eerht","3")
            .replace("owt","2")
            .replace("eno","1");

    public static void main(String[] args) throws IOException {
        if (args.length==0) throw new ArrayIndexOutOfBoundsException("Please add a day!");
        String challenge = args[0];
        List<String> input = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "\\inputs\\" + "input"+challenge+".txt"));
        if (challenge.replace("+","").equals("1")) {
            int output = 0;
            for (String l:input) {
                StringBuilder line = new StringBuilder(l);
                List<Character> charList = new ArrayList<>((challenge.endsWith("+")?WORDS_TO_NUMBERS.apply(line.toString()):line).chars().mapToObj(c ->(char)c).toList());
                int builtNum = 0;
                for (Character c:charList) {
                    try {
                        builtNum += Integer.parseInt(c.toString())*10;
                        break;
                    } catch (NumberFormatException ignored) {}
                }
                Collections.reverse(charList);
                new ArrayList<>((challenge.endsWith("+")?WORDS_TO_NUMBERS_REVERSED.apply(line.reverse().toString()):line).chars().mapToObj(c ->(char)c).toList());
                for (Character c:charList) {
                    try {
                        builtNum += Integer.parseInt(c.toString());
                        break;
                    } catch (NumberFormatException ignored) {}
                }
                output+=builtNum;
            }
            System.out.println(output);
        } else {
            throw new ArrayIndexOutOfBoundsException("Nothing exists for that day.");
        }
    }
}

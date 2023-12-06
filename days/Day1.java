package days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Day1 extends AbstractDay {
    private static final Function<String,String> WORDS_TO_NUMBERS = in -> in
            .replace("oneight","18")
            .replace("twone","21")
            .replace("threeight","38")
            .replace("fiveight","58")
            .replace("sevenine","79")
            .replace("eightwo","82")
            .replace("eighthree","83")
            .replace("nineight","98")

            .replace("one","1")
            .replace("two","2")
            .replace("three","3")
            .replace("four","4")
            .replace("five","5")
            .replace("six","6")
            .replace("seven","7")
            .replace("eight","8")
            .replace("nine","9");

    @Override
    public Object run(List<String> input, boolean hard) {
        int output = 0;
        for (String line : input) {
            if (hard) line = WORDS_TO_NUMBERS.apply(line);
            List<Character> charList = new ArrayList<>(line.chars().mapToObj(c -> (char) c).toList());
            int builtNum = 0;
            for (Character c : charList) {
                try {
                    builtNum += Integer.parseInt(c.toString()) * 10;
                    break;
                } catch (NumberFormatException ignored) {
                }
            }
            Collections.reverse(charList);
            for (Character c : charList) {
                try {
                    builtNum += Integer.parseInt(c.toString());
                    break;
                } catch (NumberFormatException ignored) {
                }
            }
            output += builtNum;
        }
        return output;
    }
}

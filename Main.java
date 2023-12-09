import days.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Everything here is written with Java 17.
 * @author Hipposgrumm
 */
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length==0) throw new ArrayIndexOutOfBoundsException("Please add a day!");
        String challenge = args[0];
        boolean hard = challenge.endsWith("+");
        challenge = challenge.replace("+", "");
        List<String> input;
        try {
            // Example
            input = Files.readAllLines(Paths.get(System.getProperty("user.dir")+"\\inputs\\"+"input"+challenge+"_.txt"));
        } catch (IOException e) {
            try {
                if (!hard) throw new IOException();
                // Part 2
                input = Files.readAllLines(Paths.get(System.getProperty("user.dir")+"\\inputs\\"+"input"+challenge+"+.txt"));
            } catch (IOException ee) {
                // Part 1
                input = Files.readAllLines(Paths.get(System.getProperty("user.dir")+"\\inputs\\"+"input"+challenge+".txt"));
            }
        }
        long elapsedStart = System.currentTimeMillis();
        System.out.println(switch (challenge) {
            case "1" -> new Day1().run(input,hard);
            case "2" -> new Day2().run(input,hard);
            case "3" -> new Day3().run(input,hard);
            case "4" -> new Day4().run(input,hard);
            case "5" -> new Day5().run(input,hard);
            case "6" -> new Day6().run(input,hard);
            default -> throw new ArrayIndexOutOfBoundsException("Nothing exists for that day.");
        });
        System.out.println("Challenge "+args[0]+" took "+(System.currentTimeMillis()-elapsedStart)+" ms.");
    }
}

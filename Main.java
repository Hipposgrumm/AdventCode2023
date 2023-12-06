import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Everything here is written with Java 17.
 * @author Hipposgrumm
 */
public class Main {
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
        switch (challenge) {
            case "1" -> {
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
                System.out.println(output);
            }
            case "2" -> {
                int output = 0;
                for (String line : input) {
                    if (hard) {
                        BagOfCubes cubes = new BagOfCubes(line);
                        output += cubes.power();
                        System.out.println("Red=" + cubes.red + " Green=" + cubes.green + " Blue=" + cubes.blue + " Total=" + cubes.power());
                    } else {
                        if (new BagOfCubes(line).isPossible(12, 13, 14))
                            output += Integer.parseInt(line.split(":")[0].replace("Game ", ""));
                    }
                }
                System.out.println(output);
            }
            case "3" -> {
                // Need this otherwise any numbers at the very end of a line don't get processed.
                input = input.stream().map(l -> l+".").toList();

                List<SurroundedNumber> definedNumbers = new ArrayList<>();
                AtomicInteger output = new AtomicInteger();
                for (int y=0;y<input.size();y++) {
                    char[] c = input.get(y).toCharArray();
                    int number = 0;
                    int numberX = 0;
                    int numberY = 0;
                    for (int x=0;x<c.length;x++) {
                        if ("1234567890".contains(Character.toString(c[x]))) {
                            if (number==0) {
                                numberX=x;
                                numberY=y;
                            } else {
                                number *= 10;
                            }
                            number += Integer.parseInt(Character.toString(c[x]));
                        } else {
                            if (number!=0) definedNumbers.add(new SurroundedNumber(number,numberX,numberY));
                            number = 0;
                            numberX=0;
                            numberY=0;
                        }
                    }
                }
                for (int y=0;y<input.size();y++) {
                    char[] c = input.get(y).toCharArray();
                    for (int x=0;x<c.length;x++) {
                        char ch = c[x];
                        if (!".1234567890".contains(Character.toString(ch))) {
                            int finalX = x; // IntelliJ needs these because
                            int finalY = y; // Java is dumb in that regard.
                            List<SurroundedNumber> removeNums = new ArrayList<>();
                            List<Integer> cog = new ArrayList<>();
                            definedNumbers.forEach(n -> {
                                if (n.inRange(finalX, finalY)) {
                                    if (hard && ch=='*') {
                                        cog.add(n.get());
                                    } else {
                                        output.addAndGet(n.get());
                                    }
                                    removeNums.add(n);
                                }
                            });
                            definedNumbers.removeAll(removeNums);
                            if (cog.size()==2 && hard) output.addAndGet(cog.get(0)*cog.get(1));
                        }
                    }
                }
                System.out.println("\n");
                System.out.println(output);
            }
            default -> throw new ArrayIndexOutOfBoundsException("Nothing exists for that day.");
        }
    }

    private static class BagOfCubes {
        // The bag has at most these many...
        private final int red;
        private final int green;
        private final int blue;

        public BagOfCubes(String input) {
            int r = 0;
            int g = 0;
            int b = 0;
            input = input.split(":")[1].strip();
            for (String turn:input.split(";")) {
                for (String batch:turn.split(",")) {
                    batch = batch.strip();
                    int val = Integer.parseInt(batch.split(" ")[0]);
                    switch (batch.split(" ")[1]) {
                        case "red" -> r = Math.max(r, val);
                        case "green" -> g = Math.max(g, val);
                        case "blue" -> b = Math.max(b, val);
                    }
                }
            }
            this.red = r;
            this.green = g;
            this.blue = b;
        }

        public boolean isPossible(int maxR, int maxG, int maxB) {
            return red<=maxR && green<= maxG && blue<=maxB;
        }

        public int power() {
            return red*green*blue;
        }
    }

    private static class SurroundedNumber {
        private final int number;
        private final CoordinatePair pos; // Position of the leftmost number.
        private final int size;

        public SurroundedNumber(int num, int x, int y) {
            this.number = num;
            this.size = Integer.toString(num).length();
            this.pos = new CoordinatePair(x,y);
        }

        public int get() {
            return number;
        }

        public boolean inRange(int x, int y) {
            if (y==pos.y-1 && x>=pos.x-1 && x<=pos.x+size) return true;
            if (y==pos.y+1 && x>=pos.x-1 && x<=pos.x+size) return true;
            return y == pos.y && (x == pos.x - 1 || x == pos.x + size);
        }
    }

    public record CoordinatePair(int x, int y) {}
}

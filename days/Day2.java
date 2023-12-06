package days;

import java.util.List;

public class Day2 extends AbstractDay {
    @Override
    public Object run(List<String> input, boolean hard) {
        int output = 0;
        for (String line : input) {
            if (hard) {
                BagOfCubes cubes = new BagOfCubes(line);
                output += cubes.power();
            } else {
                if (new BagOfCubes(line).isPossible(12, 13, 14))
                    output += Integer.parseInt(line.split(":")[0].replace("Game ", ""));
            }
        }
        return output;
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
}

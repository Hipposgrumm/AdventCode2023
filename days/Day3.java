package days;

import java.util.ArrayList;
import java.util.List;

public class Day3 extends AbstractDay {
    @Override
    public Object run(List<String> input, boolean hard) {
        // Need this otherwise any numbers at the very end of a line don't get processed.
        input = input.stream().map(l -> l+".").toList();

        List<SurroundedNumber> definedNumbers = new ArrayList<>();
        int output = 0;
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
                    List<SurroundedNumber> removeNums = new ArrayList<>();
                    List<Integer> cog = new ArrayList<>();
                    for (SurroundedNumber n:definedNumbers) {
                        if (n.inRange(x, y)) {
                            if (!hard || ch == '*') {
                                cog.add(n.get());
                            }
                            removeNums.add(n);
                        }
                    }
                    // If I remove from the list within the loop I get ConcurrentModificationException.
                    definedNumbers.removeAll(removeNums);
                    if (hard) {
                        if (cog.size()==2) output+=(cog.get(0)*cog.get(1));
                    } else {
                        for (Integer i:cog) {
                            output+=i;
                        }
                    }
                }
            }
        }
        return output;
    }


    private static class SurroundedNumber {
        private final int number;
        private final _Util.Pair<Integer,Integer> pos; // Position of the leftmost number.
        private final int size;

        public SurroundedNumber(int num, int x, int y) {
            this.number = num;
            this.size = Integer.toString(num).length();
            this.pos = new _Util.Pair<>(x,y);
        }

        public int get() {
            return number;
        }

        public boolean inRange(int x, int y) {
            // First  = x
            // Second = y
            if (y==pos.second()-1 && x>=pos.first()-1 && x<=pos.first()+size) return true;
            if (y==pos.first()+1 && x>=pos.first()-1 && x<=pos.first()+size) return true;
            return y == pos.second() && (x == pos.first() - 1 || x == pos.first() + size);
        }
    }
}

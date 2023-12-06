package days;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day4 extends AbstractDay {
    @Override
    public Object run(List<String> input, boolean hard) {
        int output = 0;
        Map<Integer,Integer> cards = new HashMap<>() {{
            for (int c=1;c<=input.size();c++) { // C++ Moment
                put(c,1);
            }
        }};
        for (int card=1;card<=cards.size();card++) {
            String ticket = input.get(card-1);
            List<Integer> scratchedNums = Stream.of(ticket.split("\\|")[0].split(":")[1].split(" ")).filter(s -> s.length()>0).map(i -> Integer.parseInt(i.strip())).toList();
            List<Integer> totalNums = Stream.of(ticket.split("\\|")[1].split(" ")).filter(s -> s.length()>0).map(i -> Integer.parseInt(i.strip())).toList();
            for (int i=0;i<(hard?cards.get(card):1);i++) {
                int val = 0;
                int extraIndex = card;
                for (Integer num : scratchedNums) {
                    if (totalNums.contains(num)) {
                        if (val == 0) {
                            val = 1;
                        } else {
                            val <<= 1;
                        }
                        extraIndex++;
                    }
                }
                for (int ex=card+1;ex<=extraIndex;ex++) {
                    cards.put(ex,cards.get(ex)+1);
                }
                if (!hard) output+=val;
            }
        }
        if (hard) for (Integer i:cards.values()) output+=i;
        return output;
    }
}

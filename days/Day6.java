package days;

import java.util.ArrayList;
import java.util.List;

public class Day6 extends AbstractDay {
    @Override
    public Object run(List<String> input, boolean hard) {
        List<_Util.Pair<Long,Long>> dataPairs = new ArrayList<>() {{
            String[] times = input.get(0).replace(" ",hard?"":" ").replace("Time:","").split(" ");
            String[] distances = input.get(1).replace(" ",hard?"":" ").replace("Distance:","").split(" ");
            for (int t=0,d=0;(t<times.length);t++) {
                if (times[t].length()==0) continue;
                if (distances[d].length()==0) {t--;d++;continue;}
                add(new _Util.Pair<>(Long.parseLong(times[t]),Long.parseLong(distances[d])));
                d++;
            }
        }};
        long output = 1;
        for (_Util.Pair<Long,Long> timeDistance:dataPairs) {
            long possibleWays = 0;
            for (int t=1;t<timeDistance.first();t++) {
                if ((timeDistance.first()-t)*t>timeDistance.second()) possibleWays++;
            }
            if (possibleWays>0) output*=possibleWays;
        }
        return output;
    }
}

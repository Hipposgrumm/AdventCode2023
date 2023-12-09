package days;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Day5 extends AbstractDay {
    private final List<GinormousIterationHelper> seed2soil = new ArrayList<>();
    private final List<GinormousIterationHelper> soil2fertilizer = new ArrayList<>();
    private final List<GinormousIterationHelper> fertilizer2water = new ArrayList<>();
    private final List<GinormousIterationHelper> water2light = new ArrayList<>();
    private final List<GinormousIterationHelper> light2temperature = new ArrayList<>();
    private final List<GinormousIterationHelper> temperature2humidity = new ArrayList<>();
    private final List<GinormousIterationHelper> humidity2location = new ArrayList<>();

    private final BiFunction<Long,Long,Long> THE_GAUNTLET = (start, len) -> {
        long min = Long.MAX_VALUE;
        for (long s=start;s<start+len;s++) {
            // TODO: Actually test part 2 to see if it works. It will take about an hour and a half.
            System.out.print("\rPoint: "+s); // If you're using eclipse, this line breaks.
            long newS = s;
            for (GinormousIterationHelper ih : seed2soil) {
                _Util.Pair<Boolean,Long> result = ih.evaluate(newS);
                if (result.first()) {
                    newS = result.second();
                    break;
                }
            }
            if (s==-1) continue;
            for (GinormousIterationHelper ih : soil2fertilizer) {
                _Util.Pair<Boolean,Long> result = ih.evaluate(newS);
                if (result.first()) {
                    newS = result.second();
                    break;
                }
            }
            if (newS==-1) continue;
            for (GinormousIterationHelper ih : fertilizer2water) {
                _Util.Pair<Boolean,Long> result = ih.evaluate(newS);
                if (result.first()) {
                    newS = result.second();
                    break;
                }
            }
            if (newS==-1) continue;
            for (GinormousIterationHelper ih : water2light) {
                _Util.Pair<Boolean,Long> result = ih.evaluate(newS);
                if (result.first()) {
                    newS = result.second();
                    break;
                }
            }
            if (newS==-1) continue;
            for (GinormousIterationHelper ih : light2temperature) {
                _Util.Pair<Boolean,Long> result = ih.evaluate(newS);
                if (result.first()) {
                    newS = result.second();
                    break;
                }
            }
            if (newS==-1) continue;
            for (GinormousIterationHelper ih : temperature2humidity) {
                _Util.Pair<Boolean,Long> result = ih.evaluate(newS);
                if (result.first()) {
                    newS = result.second();
                    break;
                }
            }
            if (newS==-1) continue;
            for (GinormousIterationHelper ih : humidity2location) {
                _Util.Pair<Boolean,Long> result = ih.evaluate(newS);
                if (result.first()) {
                    newS = result.second();
                    break;
                }
            }
            if (newS==-1) continue;
            if (newS<min) min = newS;

        }
        System.out.println();
        System.out.println();
        return min;
    };

    @Override
    public Object run(List<String> input, boolean hard) {
        // Destination determines what numbers.
        // Source determines where.
        String last = "";
        for (int i=1;i<input.size();i++) {
            String value = input.get(i);
            if (value.equals("")){
                if (i+1<input.size()) last = input.get(i+1);
                continue;
            } else if (value.equals(last)) continue;
            String[] values = value.split(" ");
            (switch (last) {
                case "seed-to-soil map:" -> seed2soil;
                case "soil-to-fertilizer map:" -> soil2fertilizer;
                case "fertilizer-to-water map:" -> fertilizer2water;
                case "water-to-light map:" -> water2light;
                case "light-to-temperature map:" -> light2temperature;
                case "temperature-to-humidity map:" -> temperature2humidity;
                case "humidity-to-location map:" -> humidity2location;
                default -> new ArrayList<GinormousIterationHelper>();
            }).add(new GinormousIterationHelper(Long.parseLong(values[0]),Long.parseLong(values[2]),Long.parseLong(values[1])));
        }
        seed2soil.add(new GinormousIterationHelper(0,Long.MAX_VALUE,0));
        soil2fertilizer.add(new GinormousIterationHelper(0,Long.MAX_VALUE,0));
        fertilizer2water.add(new GinormousIterationHelper(0,Long.MAX_VALUE,0));
        water2light.add(new GinormousIterationHelper(0,Long.MAX_VALUE,0));
        light2temperature.add(new GinormousIterationHelper(0,Long.MAX_VALUE,0));
        temperature2humidity.add(new GinormousIterationHelper(0,Long.MAX_VALUE,0));
        humidity2location.add(new GinormousIterationHelper(0,Long.MAX_VALUE,0));
        AtomicReference<Long> pairFirstHalf = new AtomicReference<>(null);
        return Stream.of(input.get(0).replace("seeds: ", "").split(" "))
                .map(Long::parseLong).map(s -> {
                    if (!hard) return new _Util.Pair<>(s,1L);
                    if (pairFirstHalf.get()==null) {
                        pairFirstHalf.set(s);
                        return null;
                    }
                    Long val = pairFirstHalf.get();
                    pairFirstHalf.set(null);
                    return new _Util.Pair<>(val,s);
                }).filter(Objects::nonNull)
                .map(s -> {
                    System.out.println("Working on Pair ("+s.first()+", "+s.second()+")");
                    return THE_GAUNTLET.apply(s.first(),s.second());
                }).mapToLong(s-> s).min().stream().peek(s->System.out.println()).min().orElse(-1);
    }

    private static class GinormousIterationHelper {
        private final long offsetStartValue;   // Destination Range Start
        private final long offsetLength;       // Range Length
        private final long offsetStartReplace; // Source Range Start

        public GinormousIterationHelper(long offStart, long offLength, long offReplaceStart) {
            this.offsetStartValue = offStart;
            this.offsetLength = offLength;
            this.offsetStartReplace = offReplaceStart;
        }

        public _Util.Pair<Boolean,Long> evaluate(long value) {
            boolean doesOverlap = false;
            if (value>=offsetStartReplace && value<(offsetStartReplace+offsetLength)) {
                doesOverlap = true;
                value+=offsetStartReplace-offsetStartValue;
            }
            return new _Util.Pair<>(doesOverlap,value);
        }
    }
}

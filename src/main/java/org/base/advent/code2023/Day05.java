package org.base.advent.code2023;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.base.advent.util.Text.extractLong;

/**
 * <a href="https://adventofcode.com/2023/day/5">Day 05</a>
 */
public class Day05 implements Function<List<String>, Day05.Locations> {
    public record Locations(long lowest, long reallyLowest) {}
    public record Mapping(Range<Long> src, long delta) {}
    public record Farm(long[] seeds, List<List<Mapping>> almanac) {}

    @Override
    public Locations apply(List<String> input) {
        Farm farm = readAlmanac(input);
        long part1 = Long.MAX_VALUE;
        for (long seed : farm.seeds)
            part1 = Math.min(part1, mapSeeds(Range.of(seed, seed), farm));

        List<Range<Long>> ranges = new ArrayList<>();
        for (int i = 0; i < farm.seeds.length; i += 2)
            ranges.add(Range.of(farm.seeds[i], farm.seeds[i] + farm.seeds[i + 1] - 1L));
        long part2 = Long.MAX_VALUE;
        for (Range<Long> r : ranges) {
            long mappedSeed = mapSeeds(r, farm);
            if (mappedSeed < part2 && mappedSeed > 0)
                part2 = mappedSeed;
        }

        return new Locations(part1, part2);
    }

    private long mapSeeds(Range<Long> seeds, Farm farm) {
        List<Range<Long>> list = new ArrayList<>();
        list.add(seeds);
        for (List<Mapping> mappings : farm.almanac) {
            List<Range<Long>> next = new ArrayList<>();
            list.forEach(seedRange -> {
                for (Mapping m : mappings) {
                    if (m.src.contains(seedRange.getMinimum())) {
                        if (m.src.contains(seedRange.getMaximum()))
                            next.add(Range.of(seedRange.getMinimum() + m.delta, seedRange.getMaximum() + m.delta));
                        else {
                            next.add(Range.of(seedRange.getMinimum() + m.delta, m.src.getMaximum() + m.delta));
                            next.add(Range.of(m.src.getMaximum() + 1L, seedRange.getMaximum()));
                        }
                        break;
                    }
                    else if (m.src.contains(seedRange.getMaximum())) {
                        next.add(Range.of(seedRange.getMinimum(), m.src.getMinimum() - 1L));
                        next.add(Range.of(m.src.getMinimum() + m.delta, seedRange.getMaximum() + m.delta));
                        break;
                    }
                    else if (seedRange.contains(m.src.getMinimum()) && seedRange.contains(m.src.getMaximum())) {
                        next.add(Range.of(seedRange.getMinimum(), m.src.getMinimum() - 1L));
                        next.add(Range.of(m.src.getMinimum() + m.delta, m.src.getMaximum() + m.delta));
                        next.add(Range.of(m.src.getMaximum() + 1L, seedRange.getMaximum()));
                        break;
                    }
                }
            });
            if (!next.isEmpty())
                list = next;
        }

        long min = Long.MAX_VALUE;
        for (Range<Long> r : list)
            min = Math.min(min, r.getMinimum());
        return min;
    }

    private Farm readAlmanac(List<String> input) {
        List<List<Mapping>> almanac = new ArrayList<>();
        List<Mapping> list = null;
        for (int i = 2; i < input.size(); i++) {
            String line = input.get(i);
            if (StringUtils.isBlank(line)) list = null;
            else if (list == null) { // skip title
                list = new ArrayList<>();
                almanac.add(list);
            }
            else {
                long[] nums = extractLong(line);
                list.add(new Mapping(Range.of(nums[1], nums[1] + nums[2]), nums[0] - nums[1]));
            }
        }
        return new Farm(extractLong(input.get(0)), almanac);
    }
}


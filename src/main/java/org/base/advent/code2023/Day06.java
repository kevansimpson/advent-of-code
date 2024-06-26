package org.base.advent.code2023;

import java.util.List;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2023/day/6">Day 06</a>
 */
public class Day06 implements Function<List<Day06.Race>, Day06.Wins> {
    public record Wins(long wins1, long wins2) {}
    public record Race(long time, long distance) {
        public long runFast() {
            long left = 1L;
            while ((left * (time - left)) < distance)
                left += 1L;

            long right = time;
            while ((right * (time - right)) < distance)
                right -= 1L;

            return right - left + 1L;
        }

        public int runFull() {
            int count = 0;
            for (int t = 1; t <= time; t++)
                if (distance < (t * (time - t)))
                    count++;
            return count;
        }
    }

    @Override
    public Wins apply(List<Race> races) {
        long wins1 = 1L;
        for (Race r : races)
            wins1 *= r.runFull();

        return new Wins(wins1, merge(races).runFast());
    }

    private Race merge(List<Race> races) {
        StringBuilder time = new StringBuilder();
        StringBuilder dist = new StringBuilder();
        for (Race r : races) {
            time.append(r.time);
            dist.append(r.distance);
        }
        return new Race(Long.parseLong(time.toString()), Long.parseLong(dist.toString()));
    }
}


package org.base.advent;

import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PuzzleTester {
    private static final String LINE = "%n\t=======================%n";
    private static final String BANNER = LINE + "\t Advent of Code - %d" + LINE;

    public static String readableTime(Duration duration) {
        long nanos = duration.toNanos();
        if (nanos > 1000000L)
            return String.format("%d millis", TimeUnit.NANOSECONDS.toMillis(nanos));
        else if (nanos > 1000L)
            return String.format("%d micros", TimeUnit.NANOSECONDS.toMicros(nanos));
        else
            return String.format("%d nanos", nanos);
    }

    protected static void banner(int year) {
        System.out.printf(BANNER, year);
    }

    protected void testSolutions(Solution<?> solution, Object expected1, Object expected2) {
        String name = solution.getClass().getSimpleName();
        long total = stopwatch(name + " part 1", expected1, solution::solvePart1)
                + stopwatch(name + " part 2", expected2, solution::solvePart2);
        printTime(name + " total", Duration.ofNanos(total));
        System.out.printf("%n\t-----%n");
    }

    private void printTime(String name, Duration duration) {
        System.out.printf("\t%s: %s", name, StringUtils.leftPad(readableTime(duration), 12));
    }

    private long stopwatch(String name, Object expected, Supplier<Object> test) {
        long start = System.nanoTime();
        assertEquals(expected, test.get(), name);
        Duration duration = Duration.of(System.nanoTime() - start, ChronoUnit.NANOS);
        printTime(name, duration);
        return duration.toNanos();
    }
}

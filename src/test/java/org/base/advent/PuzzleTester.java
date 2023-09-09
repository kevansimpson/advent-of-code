package org.base.advent;

import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PuzzleTester implements PuzzleReader {
    private static final String INDENT = StringUtils.repeat(' ', 59);
    private static final String LINE = "%n\t=======================%n";
    private static final String BANNER = LINE + "\t Advent of Code - %d" + LINE;

    protected static void banner(int year) {
        System.out.printf(BANNER, year);
    }

    protected <T, S, A, B> void testSolutions(Function<T, S> solutions,
                                              T input,
                                              Object expected1,
                                              Function<S, A> answer1,
                                              Object expected2,
                                              Function<S, B> answer2) {
        final String name = solutions.getClass().getSimpleName();
        long start = System.nanoTime();
        S solutionRecord = solutions.apply(input);
        assertEquals(expected1, answer1.apply(solutionRecord), name);
        assertEquals(expected2, answer2.apply(solutionRecord), name);
        Duration duration = Duration.of(System.nanoTime() - start, ChronoUnit.NANOS);
        printTime(name + INDENT + name + " total", duration);
        System.out.printf("%n\t-----%n");
    }

    protected <T> void testSolutions(Solution<T> solution,
                                     T input,
                                     Object expected1,
                                     Object expected2) {
        final String name = solution.getClass().getSimpleName();
        final long total =
                stopwatch(name + " part 1", expected1, () -> solution.solvePart1(input))
                + stopwatch(name + " part 2", expected2, () -> solution.solvePart2(input));
        printTime(name + " total", Duration.ofNanos(total));
        System.out.printf("%n\t-----%n");
    }

    private long stopwatch(String name, Object expected, Supplier<Object> test) {
        long start = System.nanoTime();
        assertEquals(expected, test.get(), name);
        Duration duration = Duration.of(System.nanoTime() - start, ChronoUnit.NANOS);
        printTime(name, duration);
        return duration.toNanos();
    }
}

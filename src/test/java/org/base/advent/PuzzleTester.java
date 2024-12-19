package org.base.advent;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.base.advent.ParallelSolution.*;
import static org.base.advent.ParallelSolution.Part.part1;
import static org.base.advent.ParallelSolution.Part.part2;
import static org.base.advent.ParallelSolution.Part.total;
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

    protected <T> void testParallelSolutions(ParallelSolution<T> solution,
                                             T input,
                                             Object expected1,
                                             Object expected2) {
        final String name = solution.getName();
        PuzzleListener listener = new PuzzleListener(name);
        solution.addPropertyChangeListener(listener);
        Pair<Object, Object> results = solution.apply(input);
        assertEquals(expected1, results.getLeft(), name);
        assertEquals(expected2, results.getRight(), name);
        Map<String, Duration> durations = listener.getDurations();
        Duration d1 = durations.get(getParallelKey(name, part1)), d2 = durations.get(getParallelKey(name, part2));
        Duration entire = durations.get(getParallelKey(name, total));
        printTime(name +" part1", d1);
        printTime(name +" part2", d2);
        printTime(name +" total", entire);
        System.out.printf("%n\t-----%n");
    }

    protected <T> void testSolutions(Solution<T> solution,
                                     T input,
                                     Object expected1,
                                     Object expected2) {
        final String name = solution.getClass().getSimpleName();
        final long total =
                stopwatch(name + " part1", expected1, () -> solution.solvePart1(input))
                + stopwatch(name + " part2", expected2, () -> solution.solvePart2(input));
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

    @RequiredArgsConstructor
    private static class PuzzleListener implements PropertyChangeListener {
        Map<String, Long> startTimes = new HashMap<>();
        Map<String, Duration> durations = new HashMap<>();
        CountDownLatch latch = new CountDownLatch(6);
        final String name;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String key = String.valueOf(evt.getOldValue());
            switch (evt.getPropertyName()) {
                case START_EVENT -> startTimes.put(key, System.nanoTime());
                case STOP_EVENT -> durations.put(key, Duration.ofNanos(System.nanoTime() - startTimes.get(key)));
            }
            latch.countDown();
        }

        public Map<String, Duration> getDurations() {
            try {
                latch.await();
                return durations;
            }
            catch (Exception ex) {
                throw new RuntimeException(name, ex);
            }
        }
    }
}

package org.base.advent.util;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility methods.
 */
public class Util {
    /** Utility records to use as solution results. */
    public record MinMaxInt(int min, int max) {}
    public record MinMaxLong(long min, long max) {}

    public static List<String[]> split(final String... input) {
        return Stream.of(input).map(str -> str.split(",")).collect(Collectors.toList());
    }

    // h/t https://www.baeldung.com/java-list-split
    public static List<List<String>> splitByBlankLine(List<String> lines) {
        int[] indexes = // of blank lines
                Stream.of(IntStream.of(-1), IntStream.range(0, lines.size())
                                .filter(i -> lines.get(i).isBlank()), IntStream.of(lines.size()))
                        .flatMapToInt(s -> s).toArray();
        return IntStream.range(0, indexes.length - 1)
                .mapToObj(i -> lines.subList(indexes[i] + 1, indexes[i + 1]))
                .collect(Collectors.toList());
    }

    public static BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++)
            result = result.multiply(BigInteger.valueOf(i));
        return result;
    }

    public static long lowestCommonMultiple(long[] elements) {
        long lcm = 1L;
        long divisor = 2L;
        while (true) {
            int counter = 0;
            boolean divisible = false;
            for (int i = 0; i < elements.length; i++) {
                if (elements[i] == 0L)
                    return 0L;
                else if (elements[i] < 0L)
                    elements[i] *= -1L;

                if (elements[i] == 1L)
                    counter++;

                if (elements[i] % divisor == 0L) {
                    divisible = true;
                    elements[i] /= divisor;
                }
            }

            if (divisible)
                lcm *= divisor;
            else
                divisor += 1L;

            if (counter == elements.length)
                return lcm;
        }
    }

    public static <T> List<List<T>> combinations(List<T> list, int len) {
        List<List<T>> allCombos = new ArrayList<>();
        comboUtil(list, allCombos, new ArrayList<>(), 0, len);
        return allCombos;
    }

    static <T> void comboUtil(List<T> data, List<List<T>> allCombos, List<T> combo,
                              int index, int missing) {
        if (missing == 0)
            allCombos.add(combo);
        else {
            for (int i = index; i <= data.size() - missing; i++) {
                List<T> newCombo;
                if (i == data.size() - missing)
                    newCombo = combo;
                else
                    newCombo = new ArrayList<>(combo);
                newCombo.add(data.get(i));
                comboUtil(data, allCombos, newCombo, i + 1, missing - 1);
            }
        }
    }

    public static int[] reverse(int[] arr) {
        int n = arr.length;
        int[] reversed = new int[n];
        int j = n;
        for (int k : arr) {
            reversed[j - 1] = k;
            j = j - 1;
        }

        return reversed;
    }

    /**
     * Merges two {@link Range} into a single one, unless there's no overlap.
     * The merged <code>Range</code> will usually be the {@link Pair#getLeft()} of returned <code>Pair</code>.
     * If there's no overlap, the <code>Range</code> parameters are returned in sorted order in a {@link Pair}.
     *
     * @param one the first <code>Range</code> parameter.
     * @param two the second <code>Range</code> parameter.
     * @return a pair of <code>Range</code>, one of which will be <code>null</code> unless there's overlap.
     */
    public static Pair<Range<Long>, Range<Long>> merge(Range<Long> one, Range<Long> two) {
        if (one.getMinimum() <= two.getMinimum()) {
            if (one.getMaximum() >= two.getMaximum())       // one contains two
                return Pair.of(one, null);
            else if (one.getMaximum() < two.getMinimum())   // no overlap
                return Pair.of(one, two);
            else                                            // one + two
                return Pair.of(Range.of(one.getMinimum(), two.getMaximum()), null);
        }
        else {
            if (one.getMaximum() <= two.getMaximum())       // two contains one
                return Pair.of(two, null);
            else if (one.getMinimum() > two.getMaximum())   // no overlap
                return Pair.of(two, one);
            else                                            // two + one
                return Pair.of(Range.of(two.getMinimum(), one.getMaximum()), null);
        }
    }

    public static <T> T safeGet(CompletableFuture<T> future) {
        try {
            return future.get();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

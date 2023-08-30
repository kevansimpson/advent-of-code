package org.base.advent.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public static <T> List<List<T>> combinations(List<T> list, int len) {
        if (len == 0) { // can't replace w/ List.of b/c it needs to be modifiable
            return new ArrayList<>(Arrays.asList(new ArrayList<>()));
        }
        else {
            List<List<T>> result = new ArrayList<>();
            for (int i = 0, max = (list.size() - len); i <= max; i++) {
                List<T> foo = new ArrayList<>(list.stream().skip(i + 1).limit(list.size() - 1).toList());
                List<List<T>> sub = combinations(foo, len - 1);
                for (List<T> perm : sub) {
                    List<T> add = new ArrayList<>(Stream.concat(Stream.of(list.get(i)), perm.stream()).toList());
                    result.add(add);
                }
            }
            return result;
        }
    }
}

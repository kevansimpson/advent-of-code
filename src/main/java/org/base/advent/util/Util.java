package org.base.advent.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility methods.
 */
public class Util {
    private static final Pattern integerPattern = Pattern.compile("-?\\d+");

    public static int[] extractInt(String str) {
        return findAll(integerPattern, str).stream().mapToInt(Integer::valueOf).toArray();
    }

    public static List<String> findAll(Pattern pattern, String str) {
        Matcher matcher = pattern.matcher(str);

        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }

        return list;
    }

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

    @SafeVarargs
    public static <T> List<List<T>> permute(T... set) {
        List<List<T>> result = new ArrayList<>();

        //start from an empty list
        result.add(new ArrayList<>());

        for (T t : set) {
            //list of list in current iteration of the array num
            List<List<T>> current = new ArrayList<>();

            for (List<T> l : result) {
                // # of locations to insert is the largest index + 1
                for (int j = 0; j < l.size() + 1; j++) {
                    // + add num[i] to different locations
                    l.add(j, t);

                    List<T> temp = new ArrayList<>(l);
                    current.add(temp);
                    //noinspection SuspiciousListRemoveInLoop
                    l.remove(j);
                }
            }

            result = new ArrayList<>(current);
        }

        return result;
    }
}

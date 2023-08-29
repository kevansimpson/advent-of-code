package org.base.advent.util;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private static final Pattern numberPattern = Pattern.compile("-?\\d+");
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    public static String shiftText(String str, int shift) {
        return Stream.of(str.split("")).map(ch -> {
            if (alphabet.contains(ch))
                return String.valueOf(alphabet.charAt((alphabet.indexOf(ch) + shift) % 26));
            else
                return ch;
        }).collect(Collectors.joining());
    }

    public static int[] extractInt(String str) {
        return findAll(numberPattern, str).stream().mapToInt(Integer::valueOf).toArray();
    }

    public static long[] extractLong(String str) {
        return findAll(numberPattern, str).stream().mapToLong(Long::valueOf).toArray();
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

    public static Set<Character> stringToSet(String str) {
        return new HashSet<>(str.chars().mapToObj(c -> (char) c).toList());
    }

    public static List<String> columns(List<String> rows) {
        return rows.stream().reduce(new ArrayList<>(Collections.nCopies(rows.size(), "")),
                (columns, row) -> {
                    for (int it = 0, max = rows.get(0).length(); it < max; it++)
                        columns.set(it, columns.get(it) + row.charAt(it));
                    return columns;
                },
                (col1, col2) -> new ArrayList<>( // lame!
                        Stream.concat(col1.stream(), col2.stream()).toList()));
    }

    public static BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++)
            result = result.multiply(BigInteger.valueOf(i));
        return result;
    }

    public static <T> List<List<T>> combinations(List<T> list, int len) {
        if (len == 0) {
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

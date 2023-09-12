package org.base.advent.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** String utilities */
public class Text {
    private static final Pattern numberPattern = Pattern.compile("-?\\d+");
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

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

    public static String rotateLeft(String str, int rotations) {
        return str.substring(rotations) + str.substring(0, rotations);
    }

    public static String rotateRight(String str, int rotations) {
        return rotateLeft(str, str.length() - rotations);
    }

    public static String shiftText(String str, int shift) {
        return Stream.of(str.split("")).map(ch -> {
            if (alphabet.contains(ch))
                return String.valueOf(alphabet.charAt((alphabet.indexOf(ch) + shift) % 26));
            else
                return ch;
        }).collect(Collectors.joining());
    }

    public static Set<Character> stringToSet(String str) {
        return new HashSet<>(str.chars().mapToObj(c -> (char) c).toList());
    }

}

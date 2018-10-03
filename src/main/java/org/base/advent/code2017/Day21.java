package org.base.advent.code2017;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * <h2>Part 1</h2>
 * You find a program trying to generate some art. It uses a strange process that involves repeatedly enhancing
 * the detail of an image through a set of rules.
 *
 * The image consists of a two-dimensional square grid of pixels that are either on (#) or off (.). The program
 * always begins with this pattern:
 * <pre>
 * .#.
 * ..#
 * ###
 * </pre>
 *
 * Because the pattern is both 3 pixels wide and 3 pixels tall, it is said to have a size of 3.
 *
 * Then, the program repeats the following process:
 *  - If the size is evenly divisible by 2, break the pixels up into 2x2 squares, and convert each 2x2 square into
 *          a 3x3 square by following the corresponding enhancement rule.
 *  - Otherwise, the size is evenly divisible by 3; break the pixels up into 3x3 squares, and convert each 3x3 square
 *          into a 4x4 square by following the corresponding enhancement rule.
 *
 * Because each square of pixels is replaced by a larger one, the image gains pixels and so its size increases.
 *
 * The artist's book of enhancement rules is nearby (your puzzle input); however, it seems to be missing rules.
 * The artist explains that sometimes, one must rotate or flip the input pattern to find a match. (Never rotate or
 * flip the output pattern, though.) Each pattern is written concisely: rows are listed as single units, ordered
 * top-down, and separated by slashes. For example, the following rules correspond to the adjacent patterns:
 * <pre>
 * ../.#  =  ..
 *           .#
 *
 *                 .#.
 * .#./..#/###  =  ..#
 *                 ###
 *
 *                         #..#
 * #..#/..../#..#/.##.  =  ....
 *                         #..#
 *                         .##.
 * </pre>
 *
 * When searching for a rule to use, rotate and flip the pattern as necessary. For example, all of the following
 * patterns match the same rule:
 * <pre>
 * .#.   .#.   #..   ###
 * ..#   #..   #.#   ..#
 * ###   ###   ##.   .#.
 * </pre>
 *
 * Suppose the book contained the following two rules:
 * <pre>
 * ../.# => ##./#../...
 * .#./..#/### => #..#/..../..../#..#
 * </pre>
 *
 * As before, the program begins with this pattern:
 * <pre>
 * .#.
 * ..#
 * ###
 * </pre>
 *
 * The size of the grid (3) is not divisible by 2, but it is divisible by 3. It divides evenly into a single square;
 * the square matches the second rule, which produces:
 * <pre>
 * #..#
 * ....
 * ....
 * #..#
 * </pre>
 *
 * The size of this enhanced grid (4) is evenly divisible by 2, so that rule is used. It divides evenly into four squares:
 * <pre>
 * #.|.#
 * ..|..
 * --+--
 * ..|..
 * #.|.#
 * </pre>
 *
 * Each of these squares matches the same rule (../.# => ##./#../...), three of which require some flipping and
 * rotation to line up with the rule. The output for the rule is the same in all four cases:
 * <pre>
 * ##.|##.
 * #..|#..
 * ...|...
 * ---+---
 * ##.|##.
 * #..|#..
 * ...|...
 * </pre>
 *
 * Finally, the squares are joined into a new grid:
 * <pre>
 * ##.##.
 * #..#..
 * ......
 * ##.##.
 * #..#..
 * ......
 * </pre>
 *
 * Thus, after 2 iterations, the grid contains 12 pixels that are on.
 *
 * How many pixels stay on after 5 iterations?
 *
 * <h2>Part 2</h2>
 *
 */
public class Day21 implements Solution<Map<String, String>> {

    public static final String START = ".#./..#/###";

    @Override
    public Map<String, String> getInput() throws IOException {
        return rotateFlipKeys(toMap(readLines("/2017/input21.txt")));
    }

    @Override
    public Object solvePart1() throws Exception {
        return countLightsOn(generateArt(START, 5, getInput()));
    }

    @Override
    public Object solvePart2() throws Exception {
        return getInput();
    }

    public int countLightsOn(final String art) {
        return StringUtils.countMatches(art, "#");
    }

    public String generateArt(final String start, final int iterations, final Map<String, String> rules) {
        String input = start;
        for (int i = 0; i < iterations; i++) {
            System.out.println("Iteration: "+ i);
            System.out.println(input);
            System.out.println("--------------------------------------------------------");
            input = resizeGrid(input, rules);
        }

        return input;
    }

    // .split("(?<=\\G.{4})")
    public String resizeGrid(String grid, final Map<String, String> rules) {
        grid = grid.replaceAll("\n", "/");
        final int size = size(grid);
        if ((size % 2) == 0) {
            if (size > 2) {     // this is wrong!  have to split into size/2 rows
                System.out.println("grid = "+ grid);
                final int len = grid.length();
                final String[] upperRow = splitGrid(2, grid.substring(0, len / 2));
                final String[] lowerRow = splitGrid(2, grid.substring(len / 2 + 1));
//                System.out.println(ArrayUtils.toString(upperRow));
//                System.out.println(ArrayUtils.toString(lowerRow));

                final String rsUpper = Stream.of(upperRow).map(g -> resizeGrid(g, rules)).collect(Collectors.joining("/"));
                final String rsLower = Stream.of(lowerRow).map(g -> resizeGrid(g, rules)).collect(Collectors.joining("/"));
//                System.out.println("-----");
//                System.out.println(rsUpper);
//                System.out.println(rsLower);
//                System.out.println("=====");
                return joinGrids(rsUpper, rsLower);
            } else
                return rules.get(grid);
        }
        else if ((size % 3) == 0) {
            if (size > 3) {
                throw new RuntimeException("size > 3");
            }
            else
                return rules.get(grid);
        }
        else
            return rules.get(grid);
    }


    public String[] splitGrid(final int size, final String grid) {
//        System.out.println("splitting "+ grid);
        final String[] rows = grid.split("/");
        final String[] splits = new String[rows.length];
        Arrays.fill(splits, "");

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                splits[y] += (x == 0) ? "" : "/";
//                System.out.println(x * size +" ~ "+ (x * size + size));
                splits[x] += rows[y].substring(x * size, x * size + size);
            }
        }

        System.out.println("splits = "+ ArrayUtils.toString(splits));
        return splits;
    }

    public String joinGrids(final String upperRow, final String lowerRow) {
        final String[] g1 = upperRow.split("/");
        final String[] g2 = lowerRow.split("/");
//        System.out.println("joining:\n"+ upperRow +"\n"+ lowerRow);
//        System.out.println(ArrayUtils.toString(g1));
//        System.out.println(ArrayUtils.toString(g2));
//        System.out.println(g1.length);
//        System.out.println(g2.length);
        return alignGrid(g1) +"\n" + alignGrid(g2);
//        return (g1 +"\n"+ g2).replaceAll("\\n", "/");
    }

    protected String alignGrid(String... row) {
        final int half = row.length / 2;
        final StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < half; i++) {
            bldr.append(row[i]).append(row[i + half]).append("\n");
        }

        return bldr.toString().trim();
    }

    public int size(final String grid) {
        return StringUtils.countMatches(grid, "/") + 1;
    }

    public Map<String, String> rotateFlipKeys(final Map<String, String> rules) {
        final Map<String, String> variations = new HashMap<>();

        for (String input : rules.keySet()) {
            final String output = rules.get(input);
            for (final String variation : toRotations(input))
                variations.put(variation, output);
            for (final String variation : toRotations(flip(input)))
                variations.put(variation, output);
        }

        variations.putAll(rules);

//        variations.keySet().forEach(k -> System.out.println(k +" => "+ variations.get(k)));
        return variations;
    }

    protected String[] toRotations(String input) {
        final String[] rotations = new String[4];
        rotations[0] = input;
        for (int i = 1; i < 4; i++)
            rotations[i] = (input = rotate(input));

        return rotations;
    }

    protected String flip(final String input) {
        return Stream.of(input.split("/")).map(StringUtils::reverse).collect(Collectors.joining("/"));
    }

    protected String rotate(String input) {
        String[] rows = input.split("/");
        final int size = rows.length;
        char[][] grid = new char[size][1];

        for (int i = 0; i < size; i++)
            grid[i] = rows[i].toCharArray();

        char[][] output = rotate(grid);
        String delimiter = "";
        StringBuilder bldr = new StringBuilder();
        for (char[] out : output) {
            bldr.append(delimiter).append(new String(out));
            delimiter = "/";
        }

        return bldr.toString();
    }

    protected char[][] rotate(char[][] input) {
        final int n = input.length;
        final int m = input[0].length;
        char[][] output = new char[m][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                output[j][n - 1 -i] = input[i][j];

        return output;
    }

    public String toGrid(String input) {
        final String clean = input.replaceAll("/", "");
        final StringBuilder builder = new StringBuilder();
        final int size = (int) Math.sqrt(clean.length());

        for (int i = 0; i < size; i++)
            builder.append(clean.substring(i * size, i * size + size)).append("\n");

        return builder.toString();
    }

    public Map<String, String> toMap(final List<String> input) {
        return input.stream().collect(Collectors.toMap(
                        s -> StringUtils.substringBefore(s, " => "),
                        s -> StringUtils.substringAfter(s, " => ")));
    }

    public static class Grid {
        private final String[] sections;

        public Grid(final String... sections) {
            this.sections = sections;
        }

        public String[] sections() {
            return sections;
        }

        public int size() {
            return sections().length;
        }
    }
}

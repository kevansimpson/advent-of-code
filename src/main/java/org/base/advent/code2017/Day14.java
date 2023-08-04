package org.base.advent.code2017;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.*;

/**
 * <a href="https://adventofcode.com/2017/day/14">Day 14</a>
 */
public class Day14 implements Solution<List<String>> {
    private final Day10 day10 = new Day10();

    @Getter
    private final List<String> input =
            readCachedLines("/2017/day14-bit-grid.txt",() -> listBits("uugsqrei"));

    @Override
    public Object solvePart1() {
        return countUsedSquares(getInput());
    }

    @Override
    public Object solvePart2() {
        return countRegions(getInput());
    }

    public int countRegions(final List<String> listBits) {
        final Set<Point> used = toPoints(listBits);
        int regions = 0;

        while (!used.isEmpty()) {
            final Optional<Point> next = used.stream().findFirst();

            buildRegion(next.get(), used);
            ++regions;
        }

        return regions;
    }

    protected void buildRegion(final Point start, final Set<Point> grid) {
        if (grid.contains(start)) {
            grid.remove(start);
            final List<Point> litKids = start.cardinal().stream().filter(grid::contains).toList();
            for (final Point cardinal : litKids)
                buildRegion(cardinal, grid);
        }
    }

    public Set<Point> toPoints(final List<String> listBits) {
        final Set<Point> used = new HashSet<>();
        for (int y = 0; y < 128; y++) {
            final String bits = listBits.get(y);
            debug(bits);
            for (int x = 0; x < 128; x++) {
                if (bits.charAt(x) == '1')
                    used.add(new Point(x, y));
            }
        }

        return used;
    }

    public int countUsedSquares(final List<String> listBits) {
        return listBits.stream().mapToInt(bits -> StringUtils.countMatches(bits, "1")).sum();
    }

    protected List<String> listBits(final String input) {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 128; i++)
            list.add(tastyHighBitWork(input +"-"+ i));

        return list;
    }

    public String tastyHighBitWork(final String input) {
        final String hex = day10.fullHexKnot(input);
        return highEndBits(hex);
    }

    public String highEndBits(final String hex) {
        final StringBuilder bits = new StringBuilder();
        for (final char ch : hex.toCharArray()) {
            bits.append(StringUtils.leftPad(
                    Integer.toBinaryString(Integer.parseInt(String.valueOf(ch), 16)), 4, "0"));
        }

        return bits.toString();
    }
}

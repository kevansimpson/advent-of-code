package org.base.advent.code2017;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.util.Point;
import org.base.advent.Solution;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <h2>Part 1</h2>
 * Suddenly, a scheduled job activates the system's disk defragmenter. Were the situation different, you might sit
 * and watch it for a while, but today, you just don't have that kind of time. It's soaking up valuable system
 * resources that are needed elsewhere, and so the only option is to help it finish its task as soon as possible.
 *
 * The disk in question consists of a 128x128 grid; each square of the grid is either free or used. On this disk,
 * the state of the grid is tracked by the bits in a sequence of knot hashes.
 *
 * A total of 128 knot hashes are calculated, each corresponding to a single row in the grid; each hash contains
 * 128 bits which correspond to individual grid squares. Each bit of a hash indicates whether that square is
 * free (0) or used (1).
 *
 * The hash inputs are a key string (your puzzle input), a dash, and a number from 0 to 127 corresponding to the row.
 * For example, if your key string were flqrgnkx, then the first row would be given by the bits of the knot hash of
 * flqrgnkx-0, the second row from the bits of the knot hash of flqrgnkx-1, and so on until the last row, flqrgnkx-127.
 *
 * The output of a knot hash is traditionally represented by 32 hexadecimal digits; each of these digits correspond
 * to 4 bits, for a total of 4 * 32 = 128 bits. To convert to bits, turn each hexadecimal digit to its equivalent
 * binary value, high-bit first: 0 becomes 0000, 1 becomes 0001, e becomes 1110, f becomes 1111, and so on; a hash
 * that begins with a0c2017... in hexadecimal would begin with 10100000110000100000000101110000... in binary.
 *
 * Continuing this process, the first 8 rows and columns for key flqrgnkx appear as follows, using # to denote used
 * squares, and . to denote free ones:
 * <pre>
 * ##.#.#..-->
 * .#.#.#.#
 * ....#.#.
 * #.#.##.#
 * .##.#...
 * ##..#..#
 * .#...#..
 * ##.#.##.-->
 * |      |
 * V      V
 * </pre>
 *
 * In this example, 8108 squares are used across the entire 128x128 grid.
 *
 * Given your actual key string, how many squares are used?
 *
 * <h2>Part 2</h2>
 * Now, all the defragmenter needs to know is the number of regions. A region is a group of used squares that are all
 * adjacent, not including diagonals. Every used square is in exactly one region: lone used squares form their own
 * isolated regions, while several adjacent squares all count as a single region.
 *
 * In the example above, the following nine regions are visible, each marked with a distinct digit:
 * <pre>
 * 11.2.3..-->
 * .1.2.3.4
 * ....5.6.
 * 7.8.55.9
 * .88.5...
 * 88..5..8
 * .8...8..
 * 88.8.88.-->
 * |      |
 * V      V
 * </pre>
 * Of particular interest is the region marked 8; while it does not appear contiguous in this small view, all of the
 * squares marked 8 are connected when considering the whole 128x128 grid. In total, in this example, 1242 regions
 * are present.
 *
 * How many regions are present given your key string?
 *
 */
public class Day14 implements Solution<List<String>> {

    private final Day10 day10 = new Day10();

    @Override
    public List<String> getInput() throws IOException {
        return readCachedLines("/2017/day14-bit-grid.txt",() -> listBits("uugsqrei"));
    }

    @Override
    public Object solvePart1() throws Exception {
        return countUsedSquares(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return countRegions(getInput());
    }

    public int countRegions(final List<String> listBits) {
        final Set<Point> used = toPoints(listBits);
        int regions = 0;

        while (!used.isEmpty()) {
            final Optional<Point> next = used.stream().findFirst();
            if (!next.isPresent()) break;

            buildRegion(next.get(), used);
            ++regions;
        }

        return regions;
    }

    protected void buildRegion(final Point start, final Set<Point> grid) {
        if (grid.contains(start)) {
            grid.remove(start);
            final List<Point> litKids = start.cardinal().stream().filter(grid::contains).collect(Collectors.toList());
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

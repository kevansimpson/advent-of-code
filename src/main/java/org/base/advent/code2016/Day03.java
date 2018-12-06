package org.base.advent.code2016;

import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * <h2>Part 1</h2>
 * Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture
 * that makes up this part of Easter Bunny HQ. This must be a graphic design department; the walls are
 * covered in specifications for triangles.
 *
 * Or are they?
 *
 * The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of
 * these aren't triangles. You can't help but mark the impossible ones.
 *
 * In a valid triangle, the sum of any two sides must be larger than the remaining side. For example,
 * the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
 *
 * In your puzzle input, how many of the listed triangles are possible?
 *
 * <h2>Part 2</h2>
 * Now that you've helpfully marked up their design documents, it occurs to you that triangles are
 * specified in groups of three vertically. Each set of three numbers in a column specifies a triangle.
 * Rows are unrelated.
 *
 * For example, given the following specification, numbers with the same hundreds digit would be part
 * of the same triangle:
 * <pre>
 *     101 301 501
 *     102 302 502
 *     103 303 503
 *     201 401 601
 *     202 402 602
 *     203 403 603
 * </pre>
 *
 * In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?
 */
public class Day03 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2016/input03.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return countTrianglesByRow(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return countTrianglesByColumn(getInput());
    }

    public long countTrianglesByColumn(final List<String> input) {
        final List<int[]> rows = toIntArray(input).collect(Collectors.toList());
        long valid = 0;
        for (int i = 0, max = input.size(); i < max; i += 3) {
            final int[] row1 = rows.get(i), row2 = rows.get(i + 1), row3 = rows.get(i + 2);
            valid += validTriangle(row1[0], row2[0], row3[0]) ? 1 : 0;
            valid += validTriangle(row1[1], row2[1], row3[1]) ? 1 : 0;
            valid += validTriangle(row1[2], row2[2], row3[2]) ? 1 : 0;
        }

        return valid;
    }

    public long countTrianglesByRow(final List<String> input) {
        return toIntArray(input).filter(this::validTriangle).count();
    }

    Stream<int[]> toIntArray(final List<String> input) {
        return input.stream()
                .map(String::trim)
                .map(str -> Arrays.asList(str.split("\\s+")))
                .map(lst -> lst.stream().mapToInt(Integer::parseInt).toArray());
    }

    boolean validTriangle(int... sides) {
        Arrays.sort(sides);
        return sides.length == 3 && ((sides[0] + sides[1]) > sides[2]);
    }
}

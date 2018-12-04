package org.base.advent.code2018;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.math.NumberUtils.toInt;

/**
 * <h2>Part 1</h2>
 * The Elves managed to locate the chimney-squeeze prototype fabric for Santa's suit (thanks to someone
 * who helpfully wrote its box IDs on the wall of the warehouse in the middle of the night). Unfortunately,
 * anomalies are still affecting them - nobody can even agree on how to cut the fabric.
 *
 * The whole piece of fabric they're working on is a very large square - at least 1000 inches on each side.
 *
 * Each Elf has made a claim about which area of fabric would be ideal for Santa's suit.
 * All claims have an ID and consist of a single rectangle with edges parallel to the edges of the fabric.
 * Each claim's rectangle is defined as follows:
 *
 *  - The number of inches between the left edge of the fabric and the left edge of the rectangle.
 *  - The number of inches between the top edge of the fabric and the top edge of the rectangle.
 *  - The width of the rectangle in inches.
 *  - The height of the rectangle in inches.
 *
 * A claim like #123 @ 3,2: 5x4 means that claim ID 123 specifies a rectangle 3 inches from the left edge,
 * 2 inches from the top edge, 5 inches wide, and 4 inches tall. Visually, it claims the square inches of
 * fabric represented by # (and ignores the square inches of fabric represented by .) in the diagram below:
 * <pre>
 *     ...........
 *     ...........
 *     ...#####...
 *     ...#####...
 *     ...#####...
 *     ...#####...
 *     ...........
 *     ...........
 *     ...........
 * </pre>
 * The problem is that many of the claims overlap, causing two or more claims to cover part of the same areas.
 * For example, consider the following claims:
 * #1 @ 1,3: 4x4
 * #2 @ 3,1: 4x4
 * #3 @ 5,5: 2x2
 *
 * Visually, these claim the following areas:
 * <pre>
 *     ........
 *     ...2222.
 *     ...2222.
 *     .11XX22.
 *     .11XX22.
 *     .111133.
 *     .111133.
 *     ........
 * </pre>
 * The four square inches marked with X are claimed by both 1 and 2.
 * (Claim 3, while adjacent to the others, does not overlap either of them.)
 *
 * If the Elves all proceed with their own plans, none of them will have enough fabric.
 * How many square inches of fabric are within two or more claims?
 *
 * <h2>Part 2</h2>
 * Amidst the chaos, you notice that exactly one claim doesn't overlap by even a single square inch
 * of fabric with any other claim. If you can somehow draw attention to it, maybe the Elves will be
 * able to make Santa's suit after all!
 *
 * For example, in the claims above, only claim 3 is intact after all claims are made.
 *
 * What is the ID of the only claim that doesn't overlap?
 */
public class Day03 implements Solution<Map<Point, List<Integer>>> {

    @Setter(AccessLevel.PACKAGE)
    private List<Claim> claims;
    private Map<Point, List<Integer>> grid;

    @Override
    public Map<Point, List<Integer>> getInput() throws IOException {
        if (grid == null)
            grid = buildGrid(getClaims());

        return grid;
    }

    @Override
    public Object solvePart1() throws Exception {
        return calculateOverlap(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return findAdjacentClaimId(getInput());
    }

    public List<Claim> getClaims() throws IOException {
        if (claims == null) {
            claims = toClaims(readLines("/2018/input03.txt"));
            Collections.reverse(claims);  // look from the end of the list
        }

        return claims;
    }

    public int findAdjacentClaimId(final Map<Point, List<Integer>> grid) throws IOException {
        final List<Point> points = grid.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 1)
                .map(Map.Entry::getKey).collect(Collectors.toList());
        System.out.println(points.size());  // non-overlap count is greater than overlap

        for (final Claim claim : getClaims()) {
            if (CollectionUtils.containsAll(points, claim.getPoints()))
                return claim.getId();
        }

        return -1;
    }

    public long calculateOverlap(final Map<Point, List<Integer>> grid) {
        return grid.values().stream().filter(list -> list.size() > 1).count();
    }

    Map<Point, List<Integer>> buildGrid(final List<Claim> claims) {
        final Map<Point, List<Integer>> grid = new HashMap<>();
        for (final Claim claim : claims)
            claim.getPoints().forEach(point -> {
                final List<Integer> idList = grid.getOrDefault(point, new ArrayList<>());
                idList.add(claim.getId());
                grid.put(point, idList);
            });

        return grid;
    }

    List<Claim> toClaims(final List<String> input) {
        return input.stream().map(this::toClaim).collect(Collectors.toList());
    }

    private static final Pattern CLAIM_PATTERN = // #123 @ 3,2: 5x4
            Pattern.compile("#(\\d+)\\s@\\s(\\d+),(\\d+):\\s(\\d+)x(\\d+)");

    Claim toClaim(final String input) {
        final Matcher m = CLAIM_PATTERN.matcher(input);
        if (m.matches()) {
            final int id = toInt(m.group(1)), l = toInt(m.group(2)), t = toInt(m.group(3)),
                    w = toInt(m.group(4)), h = toInt(m.group(5));
            return new Claim(id, l, t, w, h, listPoints(l, t, w, h));
        }
        else
            throw new RuntimeException("Bad Claim: "+ input);
    }

    private List<Point> listPoints(final int left, final int top, final int width, final int height) {
        final List<Point> points = new ArrayList<>();
        for (int y = -top; y > (-(height + top)); y--) {
            for (int x = left; x < (width + left); x++) {
                points.add(new Point(x, y));
            }
        }

        return points;
    }

    @Data
    @AllArgsConstructor
    public static class Claim {
        private int id;
        private int left;
        private int top;
        private int width;
        private int height;
        private List<Point> points;
    }
}

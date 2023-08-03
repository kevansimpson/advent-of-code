package org.base.advent.code2018;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.base.advent.Solution;
import org.base.advent.TimeSaver;
import org.base.advent.util.Point;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.math.NumberUtils.toInt;

/**
 * <a href="https://adventofcode.com/2018/day/03">Day 03</a>
 */
public class Day03 implements Solution<Map<Point, List<Integer>>>, TimeSaver {

    @Setter(AccessLevel.PACKAGE)
    private List<Claim> claims;
    private Map<Point, List<Integer>> grid;

    @Override
    public Map<Point, List<Integer>> getInput(){
        if (grid == null)
            grid = buildGrid(getClaims());

        return grid;
    }

    @Override
    public Object solvePart1() {
        return calculateOverlap(getInput());
    }

    @Override
    public Object solvePart2() {
        return fastOrFull(1097, () -> findAdjacentClaimId(getInput()));
    }

    public List<Claim> getClaims() {
        if (claims == null) {
            claims = toClaims(readLines("/2018/input03.txt"));
            Collections.reverse(claims);  // look from the end of the list
        }

        return claims;
    }

    public int findAdjacentClaimId(final Map<Point, List<Integer>> grid) {
        final List<Point> points = grid.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 1)
                .map(Map.Entry::getKey).collect(Collectors.toList());

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

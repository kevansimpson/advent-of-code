package org.base.advent.code2018;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.base.advent.TimeSaver;
import org.base.advent.util.Point;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.math.NumberUtils.toInt;

/**
 * <a href="https://adventofcode.com/2018/day/03">Day 03</a>
 */
public class Day03 implements Function<List<String>, Day03.OverlappingClaims>, TimeSaver {
    public record OverlappingClaims(long fabricSqFt, int nonOverlappingClaimId) {}

    @Override
    public Day03.OverlappingClaims apply(List<String> input) {
        List<Claim> claims = toClaims(input);
        Collections.reverse(claims);  // look from the end of the list
        Map<Point, List<Integer>> grid = buildGrid(claims);

        return new OverlappingClaims(calculateOverlap(grid),
                fastOrFull(1097, () -> findAdjacentClaimId(claims, grid)));
    }

    int findAdjacentClaimId(final List<Claim> claims, final Map<Point, List<Integer>> grid) {
        final List<Point> points = grid.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 1)
                .map(Map.Entry::getKey).collect(Collectors.toList());

        for (final Claim claim : claims) {
            if (CollectionUtils.containsAll(points, claim.getPoints()))
                return claim.getId();
        }

        return -1;
    }

    long calculateOverlap(final Map<Point, List<Integer>> grid) {
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

    Claim toClaim(final String input) {
        final Pattern CLAIM_PATTERN = // #123 @ 3,2: 5x4
                Pattern.compile("#(\\d+)\\s@\\s(\\d+),(\\d+):\\s(\\d+)x(\\d+)");
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

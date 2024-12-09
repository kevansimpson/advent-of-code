package org.base.advent.util;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Range;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * Represents a two-dimensional point in Cartesian coordinate space.
 */
@EqualsAndHashCode
public class Point {
    private static final Map<Long, Map<Long, Point>> flyweightPool = Collections.synchronizedMap(new TreeMap<>());

    public static final Point ORIGIN = Point.of(0, 0);
    public static final BiFunction<Point, Point, Long> MANHATTAN_DISTANCE =
            (a, b) -> Math.abs(b.x - a.x) + Math.abs(b.y - a.y);

    public static Map<String, Function<Point, Point>> MOVE_MAP = Map.of(
        "U", pt -> pt.up(1),
        "D", pt -> pt.down(1),
        "L", pt -> pt.left(1),
        "R", pt -> pt.right(1));

    public final long x;
    public final long y;

    public Point(final long x, final long y) {
        this.x = x;
        this.y = y;
    }

    public int ix() {
        return (int) x;
    }
    public int iy() {
        return (int) y;
    }

    @Override
    public String toString() {
        return "["+ this.x +","+ this.y +"]";
    }

    public Point up(final int move) {
        return Point.of(x, y + move);
    }
    public Point down(final int move) {
        return Point.of(x, y - move);
    }
    public Point left(final int move) {
        return Point.of(x - move, y);
    }
    public Point right(final int move) {
        return Point.of(x + move, y);
    }

    public Point move(final char dir) {
        return move(dir, 1);
    }
    public Point move(final char dir, final int delta) {
        return move(String.valueOf(dir), delta);
    }
    public Point move(final long deltaX, final long deltaY) {
        return Point.of(x + deltaX, y + deltaY);
    }
    public Point move(final String dir) {
        return move(dir, 1);
    }
    public Point move(final String dir, final int delta) {
        return switch (dir) {
            case "U", "N", "^" -> up(delta);
            case "D", "S", "v" -> down(delta);
            case "L", "W", "<" -> left(delta);
            case "R", "E", ">" -> right(delta);
            default -> this;
        };
    }

    public long getManhattanDistance() {
        return getManhattanDistance(ORIGIN);
    }

    public long getManhattanDistance(final Point point) {
        return MANHATTAN_DISTANCE.apply(this, point);
    }

    /**
     * Cardinal directional points in clockwise order, starting from directly up (12 o'clock position).
     * @return Cardinal directional points, starting from directly up (12 o'clock position).
     */
    public List<Point> cardinal() {
        final List<Point> cardinal = new ArrayList<>();
        cardinal.add(up(1));
        cardinal.add(right(1));
        cardinal.add(down(1));
        cardinal.add(left(1));

        return cardinal;
    }

    /**
     * Clockwise points, starting from directly up (12 o'clock position).
     * @return Clockwise points, starting from directly up (12 o'clock position).
     */
    public List<Point> surrounding() {
        final List<Point> surrounding = new ArrayList<>();
        surrounding.add(up(1));
        surrounding.add(right(1).up(1));
        surrounding.add(right(1));
        surrounding.add(down(1).right(1));
        surrounding.add(down(1));
        surrounding.add(left(1).down(1));
        surrounding.add(left(1));
        surrounding.add(up(1).left(1));

        return surrounding;
    }

    public static Point of(final long x, final long y) {
        Map<Long, Point> column = flyweightPool.computeIfAbsent(y, key -> Collections.synchronizedMap(new TreeMap<>()));
        return column.computeIfAbsent(x, row -> new Point(x, y));
    }

    public static Point point(final String commaDelimitedValues) {
        final String[] values = commaDelimitedValues.split(",");
        return Point.of(Long.parseLong(values[0]), Long.parseLong(values[1]));
    }

    public static boolean inGrid(Point pt, long width, long height) {
        return Range.of(0L, width - 1).contains(pt.x) && Range.of(0L, height - 1).contains(pt.y);
    }
}

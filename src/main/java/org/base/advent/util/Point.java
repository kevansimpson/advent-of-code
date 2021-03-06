package org.base.advent.util;

import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * Represents a two-dimensional point in Cartesian coordinate space.
 */
@EqualsAndHashCode
public class Point {
    private static final Map<Integer, Map<Integer, Point>> flyweightPool = Collections.synchronizedMap(new TreeMap<>());

    public static final Point ORIGIN = Point.of(0, 0);
    public static final BiFunction<Point, Point, Integer> MANHATTAN_DISTANCE =
            (a, b) -> Math.abs(b.x - a.x) + Math.abs(b.y - a.y);

    public static Map<String, Function<Point, Point>> MOVE_MAP = Map.of(
        "U", pt -> pt.up(1),
        "D", pt -> pt.down(1),
        "L", pt -> pt.left(1),
        "R", pt -> pt.right(1));

    public final int x;
    public final int y;

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
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
    public Point move(final int deltaX, final int deltaY) {
        return Point.of(x + deltaX, y + deltaY);
    }

    public double angle(Point target) { // starts pointing up when cartesian graph is upside down
        double angle = Math.toDegrees(Math.atan2(target.y - y, target.x - x)) + 90.0d;
        return (angle < 0) ? angle + 360 : angle;
    }

    public BigDecimal distance(final Point point) {
        final double dist = Math.hypot(Math.abs(point.y - y), Math.abs(point.x - x));
//        System.out.println(this +" - "+ point +" = "+ dist);
        return BigDecimal.valueOf(dist);
    }

    public int getManhattanDistance() {
        return getManhattanDistance(ORIGIN);
    }

    public int getManhattanDistance(final Point point) {
        return MANHATTAN_DISTANCE.apply(this, point);
    }

    /**
     * Cardinal directional points in clockwise order, starting from directly up (12 o'clock position).
     * @return Cardinal directional points, starting from directly up (12 o'clock position).
     */
    public List<Point> cardinal() {
        final List<Point> surrounding = new ArrayList<>();
        surrounding.add(up(1));
        surrounding.add(right(1));
        surrounding.add(down(1));
        surrounding.add(left(1));

        return surrounding;
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

    public static Point of(final int x, final int y) {
        Map<Integer, Point> column = flyweightPool.computeIfAbsent(y, key -> Collections.synchronizedMap(new TreeMap<>()));
        return column.computeIfAbsent(x, row -> new Point(x, y));
    }

    public static Point point(final String commaDelimitedValues) {
        final String[] values = commaDelimitedValues.split(",");
        return new Point(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
    }
}

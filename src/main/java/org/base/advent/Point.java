package org.base.advent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import lombok.EqualsAndHashCode;


/**
 * Represents a two-dimensional point in Cartesian coordinate space.
 */
@EqualsAndHashCode
public class Point {
	public static final Point ORIGIN = new Point(0, 0);
	public static final BiFunction<Point, Point, Integer> MANHATTAN_DISTANCE =
			(a, b) -> Math.abs(b.x - a.x) + Math.abs(b.y - a.y);

	public final int x;
	public final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "["+ this.x +","+ this.y +"]";
	}

	public Point up(int move) {
		return new Point(x, y + move);
	}
	public Point down(int move) {
		return new Point(x, y - move);
	}
	public Point left(int move) {
		return new Point(x - move, y);
	}
	public Point right(int move) {
		return new Point(x + move, y);
	}

	public int getManhattanDistance() {
		return MANHATTAN_DISTANCE.apply(this, ORIGIN);
	}

	public List<Point> surrounding() {
		List<Point> surrounding = new ArrayList<>();
		surrounding.add(right(1));
		surrounding.add(right(1).up(1));
		surrounding.add(up(1));
		surrounding.add(up(1).left(1));
		surrounding.add(left(1));
		surrounding.add(left(1).down(1));
		surrounding.add(down(1));
		surrounding.add(down(1).right(1));
		return surrounding;
	}

	public static Point point(String commaDelimitedValues) {
		String[] values = commaDelimitedValues.split(",");
		return new Point(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
	}
}

package org.base.advent;

import java.util.ArrayList;
import java.util.List;


public class HexPoint extends Point {

	public static final HexPoint CENTER = new HexPoint(0, 0);

	public HexPoint(final int x, final int y) {
		super(x, y);
	}

	public HexPoint north() {
		return new HexPoint(x, y + 1);
	}
	public HexPoint northwest() {
		return new HexPoint(x - 1, y +1);
	}
	public HexPoint southwest() {
		return new HexPoint(x - 1, y);
	}
	public HexPoint south() {
		return new HexPoint(x, y - 1);
	}
	public HexPoint southeast() {
		return new HexPoint(x + 1, y - 1);
	}
	public HexPoint northeast() {
		return new HexPoint(x + 1, y);
	}

	/**
	 * Clockwise points, starting from directly up (12 o'clock position).
	 *
	 * NW | N |
	 * ---+---+---
	 * SW |   | NE
	 * ---+---+---
	 *    |S  | SE
	 *
	 * @return Clockwise points, starting from directly up (12 o'clock position).
	 */
	@Override
	public List<Point> surrounding() {
		final List<Point> surrounding = new ArrayList<>();
		surrounding.add(north());
		surrounding.add(northeast());
		surrounding.add(southeast());
		surrounding.add(south());
		surrounding.add(southwest());
		surrounding.add(northwest());
		return surrounding;
	}

	// https://www.redblobgames.com/grids/hexagons/#distances-cube
	public static int hexDistance(final Point point) {
		return (Math.abs(point.x) + Math.abs(point.y) + Math.abs(point.x + point.y)) / 2;
	}
}

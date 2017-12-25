package org.base.advent.code2015;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.base.advent.Point;
import org.base.advent.Solution;

/**
 *
 */
public class Day03 implements Solution<String> {

	private static final Integer DELIVERED_PRESENT = Integer.valueOf(1);

	private final Map<Point, Integer> presentCount = new HashMap<Point, Integer>();

	@Override
	public String getInput() throws IOException {
		return readInput("/2015/input03.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		followDirections(getInput(), 0, 1);
		return presentCount.size();
	}

	@Override
	public Object solvePart2() throws Exception {
		presentCount.clear();
		String directions = getInput();
		followDirections(directions, 0, 2);
		followDirections(directions, 1, 2);
		return presentCount.size();
	}

	protected void followDirections(String directions, int startIndex, int increment) {
		// begins by delivering a present to the house at his starting location
		Point position = Point.ORIGIN;
		presentCount.put(position, DELIVERED_PRESENT);

		char[] steps = directions.toCharArray();
		for (int index = startIndex, max = steps.length; index < max; index += increment) {
			switch (steps[index]) {
				case '^':
					position = position.up(1);
					break;
				case 'v':
					position = position.down(1);
					break;
				case '<':
					position = position.left(1);
					break;
				case '>':
					position = position.right(1);
					break;
			}

			presentCount.put(position, DELIVERED_PRESENT);
		}
	}

//	protected Point deliverPresentAt(Map<Point, Integer> presentCount, Point Point) {
//		presentCount.put(Point, DELIVERED_PRESENT);
//		return Point;
//	}
}

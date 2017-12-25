package org.base.advent.code2015;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.base.advent.Point;
import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * Santa is delivering presents to an infinite two-dimensional grid of houses.
 *
 * He begins by delivering a present to the house at his starting location, and then an elf at the North Pole
 * calls him via radio and tells him where to move next. Moves are always exactly one house to the north (^),
 * south (v), east (>), or west (<). After each move, he delivers another present to the house at his new location.
 *
 * However, the elf back at the north pole has had a little too much eggnog, and so his directions are a little off,
 * and Santa ends up visiting some houses more than once. How many houses receive at least one present?
 *
 * For example:
 *
 * 		- > delivers presents to 2 houses: one at the starting location, and one to the east.
 * 	 	- ^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
 * 	 	- ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.

 * <h2>Part 2</h2>
 * The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa, to deliver presents
 * with him.
 *
 * Santa and Robo-Santa start at the same location (delivering two presents to the same starting house), then take
 * turns moving based on instructions from the elf, who is eggnoggedly reading from the same script as the previous
 * year.
 *
 * This year, how many houses receive at least one present?
 *
 * For example:
 *
 * 		- ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
 * 		- ^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
 * 		- ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
 *
 */
public class Day03 implements Solution<String> {

	private static final Integer DELIVERED_PRESENT = 1;

	private final Map<Point, Integer> presentCount = new HashMap<>();

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
		final String directions = getInput();
		followDirections(directions, 0, 2);
		followDirections(directions, 1, 2);
		return presentCount.size();
	}

	protected void followDirections(final String directions, final int startIndex, final int increment) {
		// begins by delivering a present to the house at his starting location
		Point position = Point.ORIGIN;
		presentCount.put(position, DELIVERED_PRESENT);

		final char[] steps = directions.toCharArray();
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
}

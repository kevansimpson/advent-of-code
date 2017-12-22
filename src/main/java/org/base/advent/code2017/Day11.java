package org.base.advent.code2017;

import java.io.IOException;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.HexPoint;
import org.base.advent.Solution;


/**
 * <h2>Part 1</h2>
 * Crossing the bridge, you've barely reached the other side of the stream when a program comes up to you,
 * clearly in distress. "It's my child process," she says, "he's gotten lost in an infinite grid!"
 *
 * Fortunately for her, you have plenty of experience with infinite grids.
 *
 * Unfortunately for you, it's a hex grid.
 *
 * The hexagons ("hexes") in this grid are aligned such that adjacent hexes can be found to the
 * north, northeast, southeast, south, southwest, and northwest:
 *
 *   \ n  /
 * nw +--+ ne
 *   /    \
 * -+      +-
 *   \    /
 * sw +--+ se
 *   / s  \
 *
 * You have the path the child process took. Starting where he started, you need to determine the fewest number
 * of steps required to reach him. (A "step" means to move from the hex you are in to any adjacent hex.)
 *
 * For example:
 *
 * ne,ne,ne is 3 steps away.
 * ne,ne,sw,sw is 0 steps away (back where you started).
 * ne,ne,s,s is 2 steps away (se,se).
 * se,sw,se,sw,sw is 3 steps away (s,s,sw).
 *
 *
 * <h2>Part 2</h2>
 * How many steps away is the furthest he ever got from his starting position?
 *
 */
public class Day11 implements Solution<String> {

	@Override
	public String getInput() throws IOException {
		return readInput("/input11.txt");
	}


	@Override
	public Object solvePart1() throws Exception {
		return countSteps(getInput()).getLeft();
	}

	@Override
	public Object solvePart2() throws Exception {
		return countSteps(getInput()).getRight();
	}

	public Pair<Integer, Integer> countSteps(final String directions) {
		HexPoint point = HexPoint.CENTER;
		final String[] steps = directions.split(",");
		int max = 0;

		for (final String step : steps) {
			switch (step) {
				case "n":
					point = point.north();
					break;
				case "s":
					point = point.south();
					break;
				case "nw":
					point = point.northwest();
					break;
				case "sw":
					point = point.southwest();
					break;
				case "ne":
					point = point.northeast();
					break;
				case "se":
					point = point.southeast();
					break;
			}

			max = Math.max(max, HexPoint.hexDistance(point));
		}

		return Pair.of(HexPoint.hexDistance(point), max);
	}
}

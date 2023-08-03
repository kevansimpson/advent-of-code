package org.base.advent.code2017;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;
import org.base.advent.util.HexPoint;


/**
 * <a href="https://adventofcode.com/2017/day/11">Day 11</a>
 */
public class Day11 implements Solution<String> {

	@Override
	public String getInput(){
		return readInput("/2017/input11.txt");
	}


	@Override
	public Object solvePart1() {
		return countSteps(getInput()).getLeft();
	}

	@Override
	public Object solvePart2() {
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

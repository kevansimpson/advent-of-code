package org.base.advent.code2015;

import org.base.advent.Solution;

/**
 * <a href="https://adventofcode.com/2015/day/01">Day 01</a>
 */
public class Day01 implements Solution<String> {

	@Override
	public String getInput(){
		return readInput("/2015/input01.txt");
	}


	@Override
	public Object solvePart1() {
		return findFloor(getInput());
	}


	@Override
	public Object solvePart2() {
		return firstEntersBasement(getInput());
	}


	public int findFloor(String instructions) {
		final int totalLength = instructions.length();
		instructions = instructions.replaceAll("\\(", "");
		final int down = instructions.length();
		final int up = totalLength - down;
		instructions = instructions.replaceAll("\\)", "");
		final int remaining = instructions.length();
		return (up - down - remaining);
	}

	public int firstEntersBasement(final String instructions) {
		int floor = 0;
		int position = 0;
		for (final char ch : instructions.toCharArray()) {
			++position;
			switch (ch) {
				case ')':
					--floor;
					break;
				case '(':
					++floor;
					break;
			}
			if (floor == -1) break;
		}
		
		return position;
		
	}
}

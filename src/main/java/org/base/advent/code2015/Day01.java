package org.base.advent.code2015;

import java.io.IOException;
import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * Santa was hoping for a white Christmas, but his weather machine's "snow" function is powered by stars,
 * and he's fresh out! To save Christmas, he needs you to collect fifty stars by December 25th.
 *
 * Collect stars by helping Santa solve puzzles. Two puzzles will be made available on each day in the
 * advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 *
 * Here's an easy puzzle to warm you up.
 *
 * Santa is trying to deliver presents in a large apartment building, but he can't find the right floor -
 * the directions he got are a little confusing. He starts on the ground floor (floor 0) and then follows
 * the instructions one character at a time.
 *
 * An opening parenthesis, (, means he should go up one floor, and a closing parenthesis, ), means he should go down one floor.
 *
 * The apartment building is very tall, and the basement is very deep; he will never find the top or bottom floors.
 *
 * For example:
 *
 *  - (()) and ()() both result in floor 0.
 *  - ((( and (()(()( both result in floor 3.
 *  - ))((((( also results in floor 3.
 *  - ()) and ))( both result in floor -1 (the first basement level).
 *  - ))) and )())()) both result in floor -3.
 *
 *  To what floor do the instructions take Santa?
 *
 * <h2>Part 2</h2>
 * Now, given the same instructions, find the position of the first character that causes him to enter the
 * basement (floor -1). The first character in the instructions has position 1, the second character has position 2, and so on.
 *
 * For example:
 *
 *  - ) causes him to enter the basement at character position 1.
 *  - ()()) causes him to enter the basement at character position 5.
 *
 *  What is the position of the character that causes Santa to first enter the basement?
 */
public class Day01 implements Solution<String> {

	@Override
	public String getInput() throws IOException {
		return readInput("/2015/input01.txt");
	}


	@Override
	public Object solvePart1() throws Exception {
		return findFloor(getInput());
	}


	@Override
	public Object solvePart2() throws Exception {
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

package org.base.advent.code2017;

import java.io.IOException;
import java.util.function.Function;
import org.base.advent.Solution;


/**
 * <h2>Part 1</h2>
 * You're standing in a room with "digitization quarantine" written in LEDs along one wall.
 * The only door is locked, but it includes a small interface. "Restricted Area - Strictly No Digitized Users Allowed."
 *
 * It goes on to explain that you may only leave by solving a captcha to prove you're not a human. Apparently, you
 * only get one millisecond to solve the captcha: too fast for a normal human, but it feels like hours to you.
 *
 * The captcha requires you to review a sequence of digits (your puzzle input) and find the sum of all digits that
 * match the next digit in the list. The list is circular, so the digit after the last digit is the first digit in the list.
 *
 * <h2>Part 2</h2>
 * You notice a progress bar that jumps to 50% completion. Apparently, the door isn't yet satisfied, but it did emit a star
 * as encouragement. The instructions change:
 *
 * Now, instead of considering the next digit, it wants you to consider the digit halfway around the circular list.
 * That is, if your list contains 10 items, only include a digit in your sum if the digit 10/2 = 5 steps forward matches
 * it. Fortunately, your list has an even number of elements.
 *
 */
public class Day01 implements Solution<String> {

	public static final Function<String, Integer> INCREMENT_ONE = (str) -> 1;
	public static final Function<String, Integer> INCREMENT_HALF = (str) -> str.length() / 2;

	@Override
	public String getInput() throws IOException {
		return readInput("/2017/input01.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return solve(getInput(), INCREMENT_ONE);
	}

	@Override
	public Object solvePart2() throws Exception {
		return solve(getInput(), INCREMENT_HALF);
	}

	public int solve(String input, Function<String, Integer> incrementer) {
		char[] ch = input.toCharArray();
		int sum = 0;
		int len = ch.length;
		int increment = incrementer.apply(input);

		for (int i = 0; i < len; i++) {
			int ix = (i + increment);

			if (ix >= len)
				ix -= len;

			if (ch[i] == ch[ix])
				sum += Integer.parseInt(""+ ch[i]);

		}

		return sum;
	}
}

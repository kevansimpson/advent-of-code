package org.base.advent.code2017;

import org.base.advent.Solution;

import java.util.function.Function;


/**
 * <a href="https://adventofcode.com/2017/day/01">Day 01</a>
 */
public class Day01 implements Solution<String> {

	public static final Function<String, Integer> INCREMENT_ONE = (str) -> 1;
	public static final Function<String, Integer> INCREMENT_HALF = (str) -> str.length() / 2;

	@Override
	public String getInput(){
		return readInput("/2017/input01.txt");
	}

	@Override
	public Object solvePart1() {
		return solve(getInput(), INCREMENT_ONE);
	}

	@Override
	public Object solvePart2() {
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
				sum += Integer.parseInt(String.valueOf(ch[i]));

		}

		return sum;
	}
}

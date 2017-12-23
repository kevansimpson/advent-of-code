package org.base.advent.code2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.base.advent.Solution;


/**
 * <h2>Part 1</h2>
 * As you walk through the door, a glowing humanoid shape yells in your direction.
 * "You there! Your state appears to be idle. Come help us repair the corruption in this spreadsheet -
 * if we take another millisecond, we'll have to display an hourglass cursor!"
 *
 * The spreadsheet consists of rows of apparently-random numbers. To make sure the recovery process is
 * on the right track, they need you to calculate the spreadsheet's checksum. For each row, determine
 * the difference between the largest value and the smallest value; the checksum is the sum of all of these differences.
 *
 * <h2>Part 2</h2>
 * "Great work; looks like we're on the right track after all. Here's a star for your effort." However, the program
 * seems a little worried. Can programs be worried?
 *
 * &quot;Based on what we're seeing, it looks like all the User wanted is some information about the evenly divisible
 * values in the spreadsheet. Unfortunately, none of us are equipped for that kind of calculation - most of us
 * specialize in bitwise operations.&quot;
 *
 * It sounds like the goal is to find the only two numbers in each row where one evenly divides the other -
 * that is, where the result of the division operation is a whole number. They would like you to find those numbers
 * on each line, divide them, and add up each line's result.
 */
public class Day02 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2017/input02.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return checksum(getInput());
	}

	@Override
	public Object solvePart2() throws Exception {
		return evenlyDivisible(getInput());
	}

	public int checksum(List<String> input) {
		List<List<Integer>> lines = parseLines(input);
		return lines.stream()
			 .mapToInt(values -> ((values.get(values.size() - 1)) - values.get(0)))
			 .sum();
	}

	public int evenlyDivisible(List<String> input) {
		List<List<Integer>> lines = parseLines(input);
		int checksum = 0;

		for (List<Integer> line : lines) {
			Collections.reverse(line);
			outer:
				for (int hi : line)
					for (int lo: line)
						if (hi > lo && hi % lo == 0) {
							checksum += (hi / lo);
							break outer;
						}
		}

		return checksum;
	}

	protected List<List<Integer>> parseLines(List<String> input) {
		Pattern pattern = Pattern.compile("\\s");
		List<List<Integer>> lines = new ArrayList<>();

		for (String line : input) {
			List<Integer> values = pattern.splitAsStream(line).map(Integer::valueOf).sorted().collect(Collectors.toList());
			lines.add(values);
		}

		return lines;
	}
}

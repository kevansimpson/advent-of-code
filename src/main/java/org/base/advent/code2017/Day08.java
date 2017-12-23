package org.base.advent.code2017;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;


/**
 * <h2>Part 1</h2>
 * You receive a signal directly from the CPU. Because of your recent assistance with jump instructions,
 * it would like you to compute the result of a series of unusual register instructions.
 *
 * Each instruction consists of several parts: the register to modify, whether to increase or decrease
 * that register's value, the amount by which to increase or decrease it, and a condition. If the condition
 * fails, skip the instruction without modifying the register. The registers all start at 0. The
 * instructions look like this:
 *
 * b inc 5 if a > 1
 * a inc 1 if b < 5
 * c dec -10 if a >= 1
 * c inc -20 if c == 10
 *
 * These instructions would be processed as follows:
 *
 * Because a starts at 0, it is not greater than 1, and so b is not modified.
 * a is increased by 1 (to 1) because b is less than 5 (it is 0).
 * c is decreased by -10 (to 10) because a is now greater than or equal to 1 (it is 1).
 * c is increased by -20 (to -10) because c is equal to 10.
 * After this process, the largest value in any register is 1.
 *
 * You might also encounter <= (less than or equal to) or != (not equal to). However, the CPU doesn't
 * have the bandwidth to tell you what all the registers are named, and leaves that to you to determine.
 *
 * What is the largest value in any register after completing the instructions in your puzzle input?
 *
 * <h2>Part 2</h2>
 * To be safe, the CPU also needs to know the highest value held in any register during this process
 * so that it can decide how much memory to allocate to these operations. For example, in the above
 * instructions, the highest value ever held was 10 (in register c after the third instruction was evaluated).
 */
public class Day08 implements Solution<List<String>> {

	public static final String HIGHEST = "highestValueDuringInstructionProcessing";

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2017/input08.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return updateRegisters(getInput(), false).values().stream().max(Comparator.naturalOrder()).get();
	}

	@Override
	public Object solvePart2() throws Exception {
		return updateRegisters(getInput(), true).getOrDefault(HIGHEST, -1);
	}

	public Map<String, Integer> updateRegisters(List<String> equations, boolean trackHighest) {
		Map<String, Integer> register = new LinkedHashMap<>();

		for (String instruction : equations) {
			String[] tokens = instruction.split("\\s");
			// 0  1   2  3  4  5 6
			// c dec -10 if a >= 1
			int value = Integer.parseInt(tokens[6]);
			int variable = register.getOrDefault(tokens[4], 0);
			boolean predicate = false;

			switch (tokens[5]) {
				case ">":
					predicate = variable > value;
					break;
				case "<":
					predicate = variable < value;
					break;
				case "<=":
					predicate = variable <= value;
					break;
				case ">=":
					predicate = variable >= value;
					break;
				case "==":
					predicate = variable == value;
					break;
				case "!=":
					predicate = variable != value;
					break;
				default:
					throw new RuntimeException(tokens[5]);
			}

			if (predicate) {
				int newValue = register.getOrDefault(tokens[0], 0) // current
							   + Integer.parseInt(tokens[2]) * (StringUtils.equalsIgnoreCase("inc", tokens[1]) ? 1 : -1); // delta
				register.put(tokens[0], newValue);

				if (trackHighest && newValue > register.getOrDefault(HIGHEST, 0)) {
					register.put(HIGHEST, newValue);
				}
			}
		}

		return register;
	}
}

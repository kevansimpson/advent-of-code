package org.base.advent.code2017;

import org.base.advent.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2017/day/02">Day 02</a>
 */
public class Day02 implements Solution<List<String>> {

	@Override
	public List<String> getInput(){
		return readLines("/2017/input02.txt");
	}

	@Override
	public Object solvePart1() {
		return checksum(getInput());
	}

	@Override
	public Object solvePart2() {
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

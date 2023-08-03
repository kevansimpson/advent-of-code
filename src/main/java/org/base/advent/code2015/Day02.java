package org.base.advent.code2015;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <a href="https://adventofcode.com/2015/day/02">Day 02</a>
 */
public class Day02 implements Solution<List<String>> {

	@Override
	public List<String> getInput(){
		return readLines("/2015/input02.txt");
	}


	@Override
	public Object solvePart1() {
		return wrapPresents(getInput()).getLeft();
	}


	@Override
	public Object solvePart2() {
		return wrapPresents(getInput()).getRight();
	}

	public Pair<Integer, Integer> wrapPresents(final List<String> dimensions) {
		int total = 0;
		int ribbon = 0;

		for (final String dimStr : dimensions) {
			final Present p = new Present();
			p.add(Integer.parseInt(StringUtils.substringBefore(dimStr, "x")));
			p.add(Integer.parseInt(StringUtils.substringBetween(dimStr, "x")));
			p.add(Integer.parseInt(StringUtils.substringAfterLast(dimStr, "x")));
			Collections.sort(p);
			total += p.needsWrappingPaper();
			ribbon += p.needsRibbon();
		}

		return Pair.of(total, ribbon);
	}

	public static class Present extends ArrayList<Integer> {
		private static final long serialVersionUID = -6669661914404297874L;

		public int needsWrappingPaper() {
			// adding an extra of first side for slack
			return (3 * get(0) * get(1)) + (2 * get(1) * get(2)) + (2 * get(2) * get(0));
		}
		
		public int needsRibbon() {
			return (2 * get(0)) + (2 * get(1)) + /*bow*/ (get(0) * get(1) * get(2));
		}
	}
}

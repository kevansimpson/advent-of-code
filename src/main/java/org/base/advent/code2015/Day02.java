package org.base.advent.code2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;


/**
 * <h2>Part 1</h2>
 * The elves are running low on wrapping paper, and so they need to submit an order for more. They have a list of
 * the dimensions (length l, width w, and height h) of each present, and only want to order exactly as much as they need.
 *
 * Fortunately, every present is a box (a perfect right rectangular prism), which makes calculating the required wrapping
 * paper for each gift a little easier: find the surface area of the box, which is 2*l*w + 2*w*h + 2*h*l. The elves also
 * need a little extra paper for each present: the area of the smallest side.
 *
 * For example:
 *
 *  - A present with dimensions 2x3x4 requires 2*6 + 2*12 + 2*8 = 52 square feet of wrapping paper plus 6 square feet
 *  	of slack, for a total of 58 square feet.
 *  - A present with dimensions 1x1x10 requires 2*1 + 2*10 + 2*10 = 42 square feet of wrapping paper plus 1 square foot
 *  	of slack, for a total of 43 square feet.
 *
 * All numbers in the elves' list are in feet. How many total square feet of wrapping paper should they order?
 *
 * <h2>Part 2</h2>
 * The elves are also running low on ribbon. Ribbon is all the same width, so they only have to worry about the length
 * they need to order, which they would again like to be exact.
 *
 * The ribbon required to wrap a present is the shortest distance around its sides, or the smallest perimeter of any
 * one face. Each present also requires a bow made out of ribbon as well; the feet of ribbon required for the perfect
 * bow is equal to the cubic feet of volume of the present. Don't ask how they tie the bow, though; they'll never tell.
 *
 * For example:
 *
 *  - A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap the present plus 2*3*4 = 24 feet of
 *  	ribbon for the bow, for a total of 34 feet.
 *  - A present with dimensions 1x1x10 requires 1+1+1+1 = 4 feet of ribbon to wrap the present plus 1*1*10 = 10 feet of
 *  	ribbon for the bow, for a total of 14 feet.
 *
 * How many total feet of ribbon should they order?
 */
public class Day02 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input02.txt");
	}


	@Override
	public Object solvePart1() throws Exception {
		return wrapPresents(getInput()).getLeft();
	}


	@Override
	public Object solvePart2() throws Exception {
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

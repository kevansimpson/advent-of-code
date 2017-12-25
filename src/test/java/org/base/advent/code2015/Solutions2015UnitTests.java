package org.base.advent.code2015;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Verify answers for all days of 2015 Advent of Code.
 */
public class Solutions2015UnitTests {
	@Test
	public void verifyDay01() throws Exception {
		final Day01 day01 = new Day01();
		assertEquals(74, day01.solvePart1());
		assertEquals(1795, day01.solvePart2());
	}

	@Test
	public void verifyDay02() throws Exception {
		final Day02 day02 = new Day02();
		assertEquals(1588178, day02.solvePart1());
		assertEquals(3783758, day02.solvePart2());
	}

	@Test
	public void verifyDay03() throws Exception {
		final Day03 day03 = new Day03();
		assertEquals(2081, day03.solvePart1());
		assertEquals(2341, day03.solvePart2());
	}

	@Test
	public void verifyDay04() throws Exception {
		final Day04 day04 = new Day04();
		assertEquals(254575L, day04.solvePart1());
		assertEquals(1038736L, day04.solvePart2());
	}

	@Test
	public void verifyDay05() throws Exception {
		final Day05 day05 = new Day05();
		assertEquals(258, day05.solvePart1());
		assertEquals(53, day05.solvePart2());
	}

	@Test
	public void verifyDay06() throws Exception {
		final Day06 day06 = new Day06();
		assertEquals(543903, day06.solvePart1());
		assertEquals(14687245, day06.solvePart2());
	}

	@Test
	public void verifyDay07() throws Exception {
		final Day07 day07 = new Day07();
		assertEquals(46065, day07.solvePart1());
		assertEquals(14134, day07.solvePart2());
	}

	@Test
	public void verifyDay08() throws Exception {
		final Day08 day08 = new Day08();
		assertEquals(5075, day08.solvePart1());
		assertEquals(7310, day08.solvePart2());
	}

	@Test
	public void verifyDay09() throws Exception {
		final Day09 day09 = new Day09();
		assertEquals(16869, day09.solvePart1());
		assertEquals(7284, day09.solvePart2());
	}

//	@Test
//	public void verifyDay10() throws Exception {
//		final Day10 day10 = new Day10();
//		assertEquals(4480, day10.solvePart1());
//		assertEquals("c500ffe015c83b60fad2e4b7d59dabc4", day10.solvePart2());
//	}
//
//	@Test
//	public void verifyDay11() throws Exception {
//		final Day11 day11 = new Day11();
//		assertEquals(643, day11.solvePart1());
//		assertEquals(1471, day11.solvePart2());
//	}

	@Test
	public void verifyDay12() throws Exception {
		final Day12 day12 = new Day12();
		assertEquals(152, day12.solvePart1());
		assertEquals(186, day12.solvePart2());
	}

	@Test
	public void verifyDay13() throws Exception {
		final Day13 day13 = new Day13();
		assertEquals(1504, day13.solvePart1());
//		assertEquals(186, day13.solvePart2());
	}

}

package org.base.advent.code2017;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Verify answers for all days.
 */
public class Solutions2017UnitTests {
	@Test
	public void verifyDay01() throws Exception {
		final Day01 day01 = new Day01();
		assertEquals(1251, day01.solvePart1());
		assertEquals(1244, day01.solvePart2());
	}

	@Test
	public void verifyDay02() throws Exception {
		final Day02 day02 = new Day02();
		assertEquals(41919, day02.solvePart1());
		assertEquals(303, day02.solvePart2());
	}

	@Test
	public void verifyDay03() {
		final Day03 day03 = new Day03();
		assertEquals(438, day03.solvePart1());
		assertEquals(266330L, day03.solvePart2());
	}

	@Test
	public void verifyDay04() throws Exception {
		final Day04 day04 = new Day04();
		assertEquals(337L, day04.solvePart1());
		assertEquals(231L, day04.solvePart2());
	}

	@Test
	public void verifyDay05() throws Exception {
		final Day05 day05 = new Day05();
		assertEquals(318883L, day05.solvePart1());
		assertEquals(23948711L, day05.solvePart2());
	}

	@Test
	public void verifyDay06() throws Exception {
		final Day06 day06 = new Day06();
		assertEquals(12841, day06.solvePart1());
		assertEquals(8038, day06.solvePart2());
	}

	@Test
	public void verifyDay07() throws Exception {
		final Day07 day07 = new Day07();
		assertEquals("hmvwl", day07.solvePart1());
		assertEquals(1853, day07.solvePart2());
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

	@Test
	public void verifyDay10() throws Exception {
		final Day10 day10 = new Day10();
		assertEquals(4480, day10.solvePart1());
		assertEquals("c500ffe015c83b60fad2e4b7d59dabc4", day10.solvePart2());
	}

	@Test
	public void verifyDay11() throws Exception {
		final Day11 day11 = new Day11();
		assertEquals(643, day11.solvePart1());
		assertEquals(1471, day11.solvePart2());
	}

	@Test
	public void verifyDay12() throws Exception {
		final Day12 day12 = new Day12();
		assertEquals(152, day12.solvePart1());
		assertEquals(186, day12.solvePart2());
	}

}

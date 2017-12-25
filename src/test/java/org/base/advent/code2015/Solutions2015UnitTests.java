package org.base.advent.code2015;

import org.base.advent.code2017.Day03;
import org.base.advent.code2017.Day04;
import org.base.advent.code2017.Day05;
import org.base.advent.code2017.Day06;
import org.base.advent.code2017.Day07;
import org.base.advent.code2017.Day08;
import org.base.advent.code2017.Day09;
import org.base.advent.code2017.Day10;
import org.base.advent.code2017.Day11;
import org.base.advent.code2017.Day12;
import org.base.advent.code2017.Day13;
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

	@Test
	public void verifyDay13() throws Exception {
		final Day13 day13 = new Day13();
		assertEquals(1504, day13.solvePart1());
//		assertEquals(186, day13.solvePart2());
	}

}

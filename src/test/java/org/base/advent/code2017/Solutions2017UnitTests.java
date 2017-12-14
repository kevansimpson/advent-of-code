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

}

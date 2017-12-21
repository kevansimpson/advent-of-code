package org.base.advent.code2017;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.base.advent.Point;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for daily puzzle examples.
 */
public class Examples2017UnitTests {

	@Test
	public void testDay01Examples() {
		final Day01 day01 = new Day01();
		assertEquals(3L, day01.solve("1122", Day01.INCREMENT_ONE));
		assertEquals(4L, day01.solve("1111", Day01.INCREMENT_ONE));
		assertEquals(0L, day01.solve("1234", Day01.INCREMENT_ONE));
		assertEquals(9L, day01.solve("91212129", Day01.INCREMENT_ONE));
		// part 2
		assertEquals(6L, day01.solve("1212", Day01.INCREMENT_HALF));
		assertEquals(0L, day01.solve("1221", Day01.INCREMENT_HALF));
		assertEquals(4L, day01.solve("123425", Day01.INCREMENT_HALF));
		assertEquals(12L, day01.solve("123123", Day01.INCREMENT_HALF));
		assertEquals(4L, day01.solve("12131415", Day01.INCREMENT_HALF));
	}

	@Test
	public void testDay02Examples() {
		final Day02 day02 = new Day02();
		List<String> list = Arrays.asList("5 1 9 5", "7 5 3", "2 4 6 8");
		assertEquals(18, day02.checksum(list));
		// part 2
		list = Arrays.asList("5 9 2 8", "9 4 7 3", "3 8 6 5");
		assertEquals(9, day02.evenlyDivisible(list));
	}

	@Test
	public void testDay03Examples() {
		final Day03 day03 = new Day03();
		assertEquals(0, Point.ORIGIN.getManhattanDistance());
		assertEquals(3, (new Point(2, 1)).getManhattanDistance());
		assertEquals(2, (new Point(0, -2)).getManhattanDistance());
		assertEquals(31, (new Point(-15, 16)).getManhattanDistance());
		assertEquals(0, day03.distanceFromOrigin(1));
		assertEquals(3, day03.distanceFromOrigin(12));
		assertEquals(2, day03.distanceFromOrigin(23));
		assertEquals(31, day03.distanceFromOrigin(1024));
		// part 2
		/*
		 * 147  142  133  122   59
		 * 304    5    4    2   57
		 * 330   10    1    1   54
		 * 351   11   23   25   26
		 * 362  747  806--->   ...
		 */
		final int[] expected = { -1, 1, 1, 2, 4, 5, 10, 11, 23, 25, 26, 54, 57, 59, 122, 133, 142, 147, 304, 330, 351, 362, 747, 806 };
		for (int i = 1; i < 24; i++)
			assertEquals("Expected: "+ i, expected[i], day03.spiralSum(day03.toPoint(i)));

		assertEquals(11, day03.firstLargerValue(10));
		assertEquals(54, day03.firstLargerValue(28));
		assertEquals(54, day03.firstLargerValue(38));
		assertEquals(54, day03.firstLargerValue(48));
		assertEquals(806, day03.firstLargerValue(750));
	}

	@Test
	public void testDay04Examples() {
		final Day04 day04 = new Day04();
		assertTrue(day04.isValid("aa bb cc dd ee"));
		assertFalse(day04.isValid("aa bb cc dd aa"));
		assertTrue(day04.isValid("aa bb cc dd aaa"));
		// part 2
		assertTrue(day04.hasAnagrams("abcde fghij"));
		assertFalse(day04.hasAnagrams("abcde xyz ecdab"));
		assertTrue(day04.hasAnagrams("a ab abc abd abf abj"));
		assertTrue(day04.hasAnagrams("iiii oiii ooii oooi oooo"));
		assertFalse(day04.hasAnagrams("oiii ioii iioi iiio"));
	}

	@Test
	public void testDay05Examples() {
		final Day05 day05 = new Day05();
		final List<Integer> instructions = Arrays.asList(0, 3, 0, 1, -3);
		assertEquals(5, day05.jumpsToEscape(instructions, Day05.NORMAL_JUMP));
		// part 2
		final List<Integer> instructions2 = Arrays.asList(0, 3, 0, 1, -3);
		assertEquals(10, day05.jumpsToEscape(instructions2, Day05.STRANGE_JUMP));
	}

	@Test
	public void testDay06Examples() {
		final Day06 day06 = new Day06();
		final List<Integer> instructions = Arrays.asList(0, 2, 7, 0);
		assertEquals(Arrays.asList(2, 4, 1, 2), day06.distributeBlocks(instructions));
		assertEquals(Arrays.asList(3, 1, 2, 3), day06.distributeBlocks(Arrays.asList(2, 4, 1, 2)));
		assertEquals(Arrays.asList(0, 2, 3, 4), day06.distributeBlocks(Arrays.asList(3, 1, 2, 3)));
		assertEquals(Arrays.asList(1, 3, 4, 1), day06.distributeBlocks(Arrays.asList(0, 2, 3, 4)));
		assertEquals(Arrays.asList(2, 4, 1, 2), day06.distributeBlocks(Arrays.asList(1, 3, 4, 1)));
		assertEquals(5, (int) day06.distributionsUntilDuplicate(instructions).getLeft());
		// part 2
		assertEquals(4, (int) day06.distributionsUntilDuplicate(Arrays.asList(2, 4, 1, 2)).getLeft());
		assertEquals(4, (int) day06.distributionsUntilDuplicate(day06.distributionsUntilDuplicate(instructions).getRight()).getLeft());
	}

	@Test
	public void testDay07Examples() {
		final Day07 day07 = new Day07();
		final List<String> input = Arrays.asList(
				"pbga (66)",
				"xhth (57)",
				"ebii (61)",
				"havc (66)",
				"ktlj (57)",
				"fwft (72) -> ktlj, cntj, xhth",
				"qoyq (66)",
				"padx (45) -> pbga, havc, qoyq",
				"tknk (41) -> ugml, padx, fwft",
				"jptl (61)",
				"ugml (68) -> gyxo, ebii, jptl",
				"gyxo (61)",
				"cntj (57)");
		final List<Day07.Tower> towers = day07.parseTowers(input);
		assertEquals("tknk", day07.findBottomTower(towers));
		// part 2
		assertEquals(60, day07.findMisweightedTowerSimple(towers));
	}

	@Test
	public void testDay08Examples() {
		final Day08 day08 = new Day08();
		final List<String> input = Arrays.asList("b inc 5 if a > 1", "a inc 1 if b < 5", "c dec -10 if a >= 1", "c inc -20 if c == 10");

		Map<String, Integer> register = day08.updateRegisters(input, false);
		assertNotNull(register);
		assertEquals(1, (int) register.getOrDefault("a", 0));
		assertFalse(register.containsKey("b"));
		assertEquals(0, (int) register.getOrDefault("b", 0));
		assertEquals(-10, (int) register.getOrDefault("c", 0));
		assertEquals(1, (int) register.values().stream().max(Comparator.naturalOrder()).get());
		// part 2
		register = day08.updateRegisters(input, true);
		assertNotNull(register);
		assertEquals(1, (int) register.getOrDefault("a", 0));
		assertFalse(register.containsKey("b"));
		assertEquals(0, (int) register.getOrDefault("b", 0));
		assertEquals(-10, (int) register.getOrDefault("c", 0));
		assertEquals(10, (int) register.getOrDefault(Day08.HIGHEST, 0));
	}

	@Test
	public void testDay09Examples() {
		final Day09 day09 = new Day09();
		assertEquals(0, day09.countGroups("<>"));					// empty garbage.
		assertEquals(0, day09.countGroups("<random characters>"));	// garbage containing random characters.
		assertEquals(0, day09.countGroups("<<<<>"));					// because the extra < are ignored.
		assertEquals(0, day09.countGroups("<{!>}>"));				// because the first > is canceled.
		assertEquals(0, day09.countGroups("<!!>"));					// because the second ! is canceled, allowing the > to terminate the garbage.
		assertEquals(0, day09.countGroups("<!!!>>"));				// because the second ! and the first > are canceled.
		assertEquals(0, day09.countGroups("<{o\"i!a,<{i<a>"));		// which ends at the first >.

		assertEquals(1, day09.countGroups("{}"));
		assertEquals(3, day09.countGroups("{{{}}}"));
		assertEquals(3, day09.countGroups("{{},{}}"));
		assertEquals(6, day09.countGroups("{{{},{},{{}}}}"));
		assertEquals(1, day09.countGroups("{<{},{},{{}}>}"));
		assertEquals(1, day09.countGroups("{<a>,<a>,<a>,<a>}"));
		assertEquals(5, day09.countGroups("{{<a>},{<a>},{<a>},{<a>}}"));
		assertEquals(2, day09.countGroups("{{<!>},{<!>},{<!>},{<a>}}"));

		assertEquals(1, day09.score("{}"));									// score of 1.
		assertEquals(6, day09.score("{{{}}}"));								// score of 1 + 2 + 3 = 6.
		assertEquals(5, day09.score("{{},{}}"));								// score of 1 + 2 + 2 = 5.
		assertEquals(16, day09.score("{{{},{},{{}}}}"));						// score of 1 + 2 + 3 + 3 + 3 + 4 = 16.
		assertEquals(1, day09.score("{<a>,<a>,<a>,<a>}"));					// score of 1.
		assertEquals(9, day09.score("{{<ab>},{<ab>},{<ab>},{<ab>}}"));		// score of 1 + 2 + 2 + 2 + 2 = 9.
		assertEquals(9, day09.score("{{<!!>},{<!!>},{<!!>},{<!!>}}"));		// score of 1 + 2 + 2 + 2 + 2 = 9.
		assertEquals(3, day09.score("{{<a!>},{<a!>},{<a!>},{<ab>}}"));		// score of 1 + 2 = 3.

		assertEquals(0, day09.countGarbage("<>"));
		assertEquals(17, day09.countGarbage("<random characters>"));
		assertEquals(3, day09.countGarbage("<<<<>"));
		assertEquals(2, day09.countGarbage("<{!>}>"));
		assertEquals(0, day09.countGarbage("<!!>"));
		assertEquals(0, day09.countGarbage("<!!!>>"));
		assertEquals(10, day09.countGarbage("<{o\"i!a,<{i<a>"));
	}

	@Test
	public void testDay10Examples() {
		final Day10 day10 = new Day10();

		final Day10.Result result = day10.twist(day10.circularList(5), new int[] {3,4,1,5}, 5, 0, 0);
		assertEquals(3, (int) result.getCircularList().get(0));
		assertEquals(4, (int) result.getCircularList().get(1));
		assertEquals(4, result.getPosition());
		assertEquals(4, result.getSkipSize());
		// part 2
		assertEquals("49,44,50,44,51", day10.convertToASCII("1,2,3"));
		final int[] expected = {49,44,50,44,51,17,31,73,47,23};
		final int[] actual = day10.concoctMagicSequence(day10.convertToASCII("1,2,3"));
		for (int i = 0; i < expected.length; i++)
			assertEquals(expected[i], actual[i]);

		final int[] sparse = {65, 27, 9, 1, 4, 3, 40, 50, 91, 7, 6, 0, 2, 5, 68, 22};
		int xor = 0;
		for (final int val : sparse)
			xor ^= val;

		assertEquals(64, xor);
		assertEquals("a2582a3a0e66e6e86e3812dcb672a272", day10.toHexidecimal(day10.fullKnot("", 256)));
		assertEquals("33efeb34ea91902bb2f59c9920caa6cd", day10.toHexidecimal(day10.fullKnot("AoC 2017", 256)));
		assertEquals("3efbe78a8d82f29979031a4aa0b16a9d", day10.toHexidecimal(day10.fullKnot("1,2,3", 256)));
		assertEquals("63960835bcdc130f0b66d7ff4f6a5a8e", day10.toHexidecimal(day10.fullKnot("1,2,4", 256)));
	}
}

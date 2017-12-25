package org.base.advent.code2015;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * Santa needs help figuring out which strings in his text file are naughty or nice.
 *
 * A nice string is one with all of the following properties:
 *
 *  - It contains at least three VOWELS (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
 *  - It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
 *  - It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
 *
 * For example:
 *  - ugknbfddgicrmopn is nice because it has at least three VOWELS (u...i...o...), a double
 *  	letter (...dd...), and none of the disallowed substrings.
 *  - aaa is nice because it has at least three VOWELS and a double letter, even though the letters used by
 *  	different rules overlap.
 *  - jchzalrnumimnmhp is naughty because it has no double letter.
 *  - haegwjzuvuyypxyu is naughty because it contains the string xy.
 *  - dvszwmarrgswjxmb is naughty because it contains only one vowel.
 *
 * How many strings are nice?
 *
 * <h2>Part 2</h2>
 * Realizing the error of his ways, Santa has switched to a better model of determining whether a string is naughty
 * or nice. None of the old rules apply, as they are all clearly ridiculous.
 *
 * Now, a nice string is one with all of the following properties:
 *
 *  - It contains a pair of any two letters that appears at least twice in the string without overlapping, like
 * xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
 *  - It contains at least one letter which repeats with exactly one letter between them, like xyx,
 *  abcdefeghi (efe), or even aaa.
 *
 * For example:
 *  - qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj) and a letter that repeats with exactly
 *  	one letter between them (zxz).
 *  - xxyxx is nice because it has a pair that appears twice and a letter that repeats with one between, even though
 *  	the letters used by each rule overlap.
 *  - uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single letter between them.
 *  - ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo), but no pair that appears twice.
 *
 * How many strings are nice under these new rules?
 *
 */
public class Day05 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input05.txt");
	}


	@Override
	public Object solvePart1() throws Exception {
		return countNiceStrings(getInput());
	}


	@Override
	public Object solvePart2() throws Exception {
		return countNicerStrings(getInput());
	}

	private static final Pattern VOWELS = Pattern.compile("[aeiou]", Pattern.DOTALL);
	private static final Pattern DOUBLE_LETTERS = Pattern.compile("([a-z])\\1+", Pattern.DOTALL);
	private static final Pattern BAD_STRINGS = Pattern.compile("(ab|cd|pq|xy)", Pattern.DOTALL);
	private static final Pattern NON_OVERLAPPING_LETTER_PAIRS = Pattern.compile("([a-z]{2}).*\\1+", Pattern.DOTALL);
	private static final Pattern SANDWICH_LETTERS = Pattern.compile("([a-z]).\\1+", Pattern.DOTALL);

	public int countNiceStrings(final List<String> input) {
		int matches = 0;
		for (final String str : input) {
			if (countMatches(str, VOWELS) >= 3)
				if (countMatches(str, DOUBLE_LETTERS) >= 1)
					if (countMatches(str, BAD_STRINGS) <= 0)
						++matches;
		}

		return matches;
	}

	public int countNicerStrings(final List<String> input) {
		int matches = 0;
		for (final String str : input) {
			if (countMatches(str, NON_OVERLAPPING_LETTER_PAIRS) >= 1)
				if (countMatches(str, SANDWICH_LETTERS) >= 1)
					++matches;
		}

		return matches;
	}
	
	protected int countMatches(final String input, final Pattern pattern) {
		int count = 0;
		final Matcher matcher = pattern.matcher(input);
		
		while (matcher.find()) {
			count++;
		}

		return count;
	}
}

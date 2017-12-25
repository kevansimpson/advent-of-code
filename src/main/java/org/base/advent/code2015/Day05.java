package org.base.advent.code2015;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.base.advent.Solution;

/**
 *
 */
public class Day05 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input05.txt");
	}


	@Override
	public Object solvePart1() throws Exception {
		return null;
	}


	@Override
	public Object solvePart2() throws Exception {
		return null;
	}


	public void solvePuzzle1() throws Exception {
		Pattern vowels = Pattern.compile("[aeiou]", Pattern.DOTALL);
		Pattern doubleLetters = Pattern.compile("([a-z])\\1+", Pattern.DOTALL);
		Pattern badStrings = Pattern.compile("(ab|cd|pq|xy)", Pattern.DOTALL);

		List<String> input = getInput();
		int matches = 0;
 
		for (String str : input) {
			if (countMatches(str, vowels) >= 3) {
				if (countMatches(str, doubleLetters) >= 1) {
					if (countMatches(str, badStrings) <= 0) {
						++matches;
					}
				}
			}
		}
		System.out.println("Number of 'good' words: "+ matches);
	}

	public void solvePuzzle2() throws Exception {
		Pattern nonOverlappingLetterPairs = Pattern.compile("([a-z]{2}).*\\1+", Pattern.DOTALL);
		Pattern sandwichLetters = Pattern.compile("([a-z]).\\1+", Pattern.DOTALL);

		List<String> input = getInput();
		int matches = 0;
 
		for (String str : input) {
			if (countMatches(str, nonOverlappingLetterPairs) >= 1) {
				if (countMatches(str, sandwichLetters) >= 1) {
					++matches;
				}
			}
		}
		System.out.println("Number of 'good' words with better model: "+ matches);
	}
	
	protected int countMatches(String input, Pattern pattern) {
		int count = 0;
		Matcher matcher = pattern.matcher(input);
		
		while (matcher.find()) {
			count++;
		}

		return count;
	}
}

package org.base.advent.code2017;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.base.advent.Solution;


/**
 * <h2>Part 1</h2>
 * A new system policy has been put in place that requires all accounts to use a passphrase instead of
 * simply a password. A passphrase consists of a series of words (lowercase letters) separated by spaces.
 *
 * To ensure security, a valid passphrase must contain no duplicate words.
 *
 * For example:
 *
 * aa bb cc dd ee is valid.
 * aa bb cc dd aa is not valid - the word aa appears more than once.
 * aa bb cc dd aaa is valid - aa and aaa count as different words.
 *
 * The system's full passphrase list is available as your puzzle input. How many passphrases are valid?
 *
 * <h2>Part 2</h2>
 * For added security, yet another system policy has been put in place. Now, a valid passphrase must
 * contain no two words that are anagrams of each other - that is, a passphrase is invalid if any
 * word's letters can be rearranged to form any other word in the passphrase.
 *
 * For example:
 *
 * abcde fghij is a valid passphrase.
 * abcde xyz ecdab is not valid - the letters from the third word can be rearranged to form the first word.
 * a ab abc abd abf abj is a valid passphrase, because all letters need to be used when forming another word.
 * iiii oiii ooii oooi oooo is valid.
 * oiii ioii iioi iiio is not valid - any of these words can be rearranged to form any other word.
 *
 * Under this new system policy, how many passphrases are valid?
 */
public class Day04 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2017/input04.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return countValidPassphrases(getInput(), this::isValid);
	}

	@Override
	public Object solvePart2() throws Exception {
		return countValidPassphrases(getInput(), this::hasAnagrams);
	}

	public long countValidPassphrases(List<String> input, Predicate<String> predicate) {
		return input.parallelStream().filter(predicate).count();
	}

	public boolean isValid(String passphrase) {
		String[] tokens = passphrase.split("\\s");
		return tokens.length == Stream.of(tokens).collect(Collectors.toSet()).size();
	}

	public boolean hasAnagrams(String passphrase) {
		String[] tokens = passphrase.split("\\s");
		return tokens.length == Stream.of(tokens).map(this::sortString).collect(Collectors.toSet()).size();
	}

}

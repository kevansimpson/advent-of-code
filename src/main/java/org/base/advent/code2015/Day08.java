package org.base.advent.code2015;

import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * Space on the sleigh is limited this year, and so Santa will be bringing his list as a digital copy. He needs to
 * know how much space it will take up when stored.
 *
 * It is common in many programming languages to provide a way to escape special characters in strings. For example,
 * C, JavaScript, Perl, Python, and even PHP handle special characters in very similar ways.
 *
 * However, it is important to realize the difference between the number of characters in the code representation of
 * the string literal and the number of characters in the in-memory string itself.
 *
 * For example:
 *  - "" is 2 characters of code (the two double quotes), but the string contains zero characters.
 *  - "abc" is 5 characters of code, but 3 characters in the string data.
 *  - "aaa\"aaa" is 10 characters of code, but the string itself contains six "a" characters and a single, escaped
 *      quote character, for a total of 7 characters in the string data.
 *  - "\x27" is 6 characters of code, but the string itself contains just one - an apostrophe ('), escaped using
 *      hexadecimal notation.
 *
 * Santa's list is a file that contains many double-quoted string literals, one on each line. The only escape sequences
 * used are \\ (which represents a single backslash), \" (which represents a lone double-quote character), and \x plus
 * two hexadecimal characters (which represents a single character with that ASCII code).
 *
 * Disregarding the whitespace in the file, what is the number of characters of code for string literals minus the
 * number of characters in memory for the values of the strings in total for the entire file?
 *
 * For example, given the four strings above, the total number of characters of string code (2 + 5 + 10 + 6 = 23)
 * minus the total number of characters in memory for string values (0 + 3 + 7 + 1 = 11) is 23 - 11 = 12.
 *
 * <h2>Part 2</h2>
 * Now, let's go the other way. In addition to finding the number of characters of code, you should now encode each
 * code representation as a new string and find the number of characters of the new encoded representation, including
 * the surrounding double quotes.
 *
 * For example:
 *  - "" encodes to "\"\"", an increase from 2 characters to 6.
 *  - "abc" encodes to "\"abc\"", an increase from 5 characters to 9.
 *  - "aaa\"aaa" encodes to "\"aaa\\\"aaa\"", an increase from 10 characters to 16.
 *  - "\x27" encodes to "\"\\x27\"", an increase from 6 characters to 11.
 *
 * Your task is to find the total number of characters to represent the newly encoded strings minus the number of
 * characters of code in each original string literal. For example, for the strings above, the total encoded length
 * (6 + 9 + 16 + 11 = 42) minus the characters in the original code representation (23, just like in the first part
 * of this puzzle) is 42 - 23 = 19.
 *
 */
public class Day08 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input08.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return totalCharacters(getInput());
	}

	@Override
	public Object solvePart2() throws Exception {
		return totalEncryptedCharacters(getInput());
	}


	public int totalCharacters(final List<String> directions) {
		int inMemory = 0;
		for (String line : directions) {
			line = StringUtils.chop(line.trim()).substring(1);
			inMemory += 2;
			inMemory += computeInMemory(line);
		}

		return inMemory;
	}

	public int totalEncryptedCharacters(final List<String> directions) {
		int encrypted = 0;
		for (String line : directions) {
			line = StringUtils.chop(line.trim()).substring(1);
			encrypted += 4;	// escape surrounding quotes
			encrypted += computeEncrypted(line);
		}

		return encrypted;
	}

	int computeInMemory(final String line) {
		final char[] chars = line.toCharArray();
		int count = 0;
		int flag = 0;

        for (final char ch : chars) {
            switch (ch) {
                case '\\':
                    if (flag == 1) {
                        count += 1;    // don't dbl count
                        flag = 0;    // reset flag
                    } else
                        flag = 1;
                    break;
                case '"':
                    if (flag == 1) {
                        count += 1;    // don't dbl count
                    }
                    flag = 0;    // reset flag
                    break;
                case 'x':
                    flag = flag == 1 ? 2 : 0;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                    switch (flag) {
                        case 3: {    // found escaped hex
                            count += 3;
                            flag = 0;    // reset flag
                            break;
                        }
                        case 2:
                            flag = 3;
                            break;
                        default:
                            flag = 0;    // reset flag
                    }
                    break;
                default:
            }
        }

		return count;
	}

	int computeEncrypted(final String line) {
		final char[] chars = line.toCharArray();
		int count = 0;
		int flag = 0;

        for (final char ch : chars) {
            switch (ch) {
                case '\\':
                    if (flag == 1) {
                        count += 2;    // escape
                        flag = 0;    // reset flag
                    } else
                        flag = 1;
                    break;
                case '"':
                    if (flag == 1) {
                        count += 2;    // escape
                    }
                    flag = 0;    // reset flag
                    break;
                case 'x':
                    flag = flag == 1 ? 2 : 0;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                    switch (flag) {
                        case 3: {    // found escaped hex
                            count += 1;
                            flag = 0;    // reset flag
                            break;
                        }
                        case 2:
                            flag = 3;
                            break;
                        default:
                            flag = 0;    // reset flag
                    }
                    break;
                default:
            }
        }

		return count;
	}
}

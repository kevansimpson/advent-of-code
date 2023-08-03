package org.base.advent.code2015;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.util.List;

/**
 * <a href="https://adventofcode.com/2015/day/08">Day 08</a>
 */
public class Day08 implements Solution<List<String>> {

	@Override
	public List<String> getInput(){
		return readLines("/2015/input08.txt");
	}

	@Override
	public Object solvePart1() {
		return totalCharacters(getInput());
	}

	@Override
	public Object solvePart2() {
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

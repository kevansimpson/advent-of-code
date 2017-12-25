package org.base.advent.code2015;

import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

/**
 *
 */
public class Day08 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input08.txt");
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
		List<String> directions = getInput();

		int inMemory = 0;
		for (String line : directions) {
			line = StringUtils.chop(line.trim()).substring(1);
			inMemory += 2;
			inMemory += computeInMemory(line);
		}

		// < 1564
		// > 1327
		System.out.println("Total characters in memory: "+ inMemory);
	}

	public void solvePuzzle2() throws Exception {
		List<String> directions = getInput();

		int encrypted = 0;
		for (String line : directions) {
			line = StringUtils.chop(line.trim()).substring(1);
			encrypted += 4;	// escape surrounding quotes
			encrypted += computeEncrypted(line);
		}

		// > 980
		System.out.println("Total encrypted characters: "+ encrypted);
	}

	int computeInMemory(String line) {
		char[] chars = line.toCharArray();
		int count = 0;
		int len = chars.length;
		int flag = 0;

		for (int i = 0; i < len; i++) {
			char ch = chars[i];
			switch (ch) {
				case '\\':
					if (flag == 1) {
						count += 1;	// don't dbl count
						flag = 0;	// reset flag
					}
					else
						flag = 1;
					break;
				case '"':
					if (flag == 1) {
						count += 1;	// don't dbl count
					}
					flag = 0;	// reset flag
					break;
				case 'x':
					if (flag == 1)
						flag = 2;
					else
						flag = 0;	// reset flag
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
						case 3: {	// found escaped hex
							count += 3;
							flag = 0;	// reset flag
							break;
						}
						case 2:
							flag = 3;
							break;
						default:
							flag = 0;	// reset flag
					}
					break;
				default:
			}
		}

		return count;
	}

	int computeEncrypted(String line) {
		char[] chars = line.toCharArray();
		int count = 0;
		int len = chars.length;
		int flag = 0;

		for (int i = 0; i < len; i++) {
			char ch = chars[i];
			switch (ch) {
				case '\\':
					if (flag == 1) {
						count += 2;	// escape
						flag = 0;	// reset flag
					}
					else
						flag = 1;
					break;
				case '"':
					if (flag == 1) {
						count += 2;	// escape
					}
					flag = 0;	// reset flag
					break;
				case 'x':
					if (flag == 1)
						flag = 2;
					else
						flag = 0;	// reset flag
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
						case 3: {	// found escaped hex
							count += 1;
							flag = 0;	// reset flag
							break;
						}
						case 2:
							flag = 3;
							break;
						default:
							flag = 0;	// reset flag
					}
					break;
				default:
			}
		}

		return count;
	}
}

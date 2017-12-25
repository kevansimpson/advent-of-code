package org.base.advent.code2015;

import java.util.regex.Pattern;

/**
 *
 */
public class Day11 {

	private static Pattern disallowed = Pattern.compile("[^iol]+", Pattern.CASE_INSENSITIVE);

	public void solvePuzzle1() throws Exception {
		String pswd = nextValidPassword("vzbxkghb");
		System.out.println("After vzbxkghb Santa's next password is "+ pswd);
	}

	public void solvePuzzle2() throws Exception {
		String pswd = nextValidPassword("vzbxkghb");
		String next = nextValidPassword(pswd);
		System.out.println("After "+ pswd +" Santa's next password is "+ next);
	}

	String nextValidPassword(String input) {
		String pswd = nextPassword(input);
		while (!isValidPassword(pswd)) {
			pswd = nextPassword(pswd);
		}
		
		return pswd;
	}

	String nextPassword(String input) {
		char[] ltrs = input.toCharArray();

		for (int i = ltrs.length - 1; i >= 0; i--) {
			char nextLtr = nextLetter(ltrs[i]);
			ltrs[i] = nextLtr;
			if (nextLtr != 'a')
				break;
		}

		return new String(ltrs);
	}

	char nextLetter(char ltr) {
		char next = (ltr == 'z') ? 'a' : (char) (1 + ((int) ltr));

		switch (next) {
			case 'i':
				return 'j';
			case 'l':
				return 'm';
			case 'o':
				return 'p';
			default:
				return next;
		}
	}

	boolean isValidPassword(String pswd) {
		return !isDisallowed(pswd) && hasNonOverlappingPairs(pswd) && hasSequence(pswd);
	}

	boolean hasSequence(String pswd) {
		int count = 1;
		int current = 0;
		char[] ltrs = pswd.toCharArray();
		for (int i = 0; i < ltrs.length; i++) {
			char ch = ltrs[i];
//			System.out.println(ch);
			if (i == 0) {
//				current = (int) ch;
//				System.out.println("current = "+ current);
			}
			else if (((int) ch) == (current + 1)) {
				count += 1;
//				current = (int) ch;
//				System.out.println("count = "+ count);
				if (count >= 3)
					return true;
			}
			else {
				count = 1;
//				System.out.println("reset");
			}
			current = (int) ch;
		}

		return false;
	}

	boolean isDisallowed(String pswd) {
		return !disallowed.matcher(pswd).matches();
	}

	boolean hasNonOverlappingPairs(String pswd) {
		String[] split = ("Q"+pswd+"Q").split("([a-z])\\1");
		return split.length >= 3;
	}
}

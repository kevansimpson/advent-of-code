package org.base.advent.code2015;

import java.security.MessageDigest;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class Day04 {

	public void solvePuzzle1() throws Exception {
		System.out.println("First hash = "+ solveFor("00000"));
	}

	public void solvePuzzle2() throws Exception {
		System.out.println("Second hash = "+ solveFor("000000"));
	}

	protected long solveFor(String prefix) throws Exception {
		String input = "bgvyzdsv";
		long index = 1;
		for (; index < Long.MAX_VALUE; index += 1l) {
			String result = convertToMD5ThenHex(input + String.valueOf(index));
			if (StringUtils.startsWith(result, prefix)) {
				return index;
			}
		}
		
		return -1;
//		System.out.println("converted = "+ convertToMD5ThenHex(input));
	}

	protected String convertToMD5ThenHex(String text) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
//			String encrypted = new String(digest.digest(text.getBytes()));
			return bytesToHex(digest.digest(text.getBytes()));
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}

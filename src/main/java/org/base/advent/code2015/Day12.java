package org.base.advent.code2015;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.base.advent.Solution;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 */
public class Day12 implements Solution<String> {

	private static Pattern numbers = Pattern.compile("([-\\d]+)");


	@Override
	public String getInput() throws IOException {
		return readInput("/2015/input12.txt");
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
		int sum = 0;
		Matcher m = numbers.matcher(getInput());
		while (m.find()) {
			sum += Integer.parseInt(m.group());
		}

		System.out.println("The sum of all numbers is "+ sum);
	}

	public void solvePuzzle2() throws Exception {
		String input = getInput();
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(input);
		int sum = sum(obj);
		System.out.println("The sum of all numbers is "+ sum);
	}

	int sum(JSONObject obj) {
		int sum = 0;
		
		for (Object key : obj.keySet()) {
			Object val = obj.get(key);
			if (!(key instanceof String))
				throw new RuntimeException(String.valueOf(key));
//			System.out.println(key +" ==> "+ val);
			if (val instanceof JSONObject) {
				sum += sum((JSONObject) val);
			}
			else if (val instanceof JSONArray) {
				sum += sum((JSONArray) val);
			}
			else if (val instanceof String) {
				String str = (String) val;
				if ("red".equalsIgnoreCase(str))
					return 0;
			}
			else
				sum += Integer.parseInt(String.valueOf(val));
		}

		return sum;
	}

	int sum(JSONArray arr) {
		int sum = 0;
		
		for (Object elem : arr) {
			if (elem instanceof JSONObject) {
				sum += sum((JSONObject) elem);
			}
			else if (elem instanceof JSONArray) {
				sum += sum((JSONArray) elem);
			}
			else if (!(elem instanceof String)) {
				sum += Integer.parseInt(String.valueOf(elem));
			}
		}

		return sum;
	}
}

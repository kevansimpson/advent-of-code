package org.base.advent.code2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ObjectUtils;
import org.base.advent.Solution;

/**
 *
 */
public class Day16 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input16.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return null;
	}

	@Override
	public Object solvePart2() throws Exception {
		return null;
	}


	private static final String INDEX = "INDEX";

	public void solvePuzzle1() throws Exception {
		List<String> sueList = getInput();
		Map<String, Integer> tickerTape = buildTickerTape();

		List<Map<String, Integer>> potentials = new ArrayList<>();
		for (int index = 0; index < sueList.size(); index++) {
			Map<String, Integer> attr = parseSue(sueList.get(index));
			if (hasSameAttr(tickerTape, attr)) {
//				System.out.println("Potential match: "+ attr);
				potentials.add(attr);
			}
		}

		System.out.println("The index of Aunt Sue is "+ potentials.get(0).get(INDEX));
	}

	public void solvePuzzle2() throws Exception {
		List<String> sueList = getInput();
		Map<String, Integer> tickerTape = buildTickerTape();

		List<Map<String, Integer>> potentials = new ArrayList<>();
		for (int index = 0; index < sueList.size(); index++) {
			Map<String, Integer> attr = parseSue(sueList.get(index));
			if (hasSameRanges(tickerTape, attr)) {
//				System.out.println("Potential match: "+ attr);
				potentials.add(attr);
			}
		}

		System.out.println("The index of Aunt Sue with outdated retroencabulator is "+ potentials.get(0).get(INDEX));
	}

	boolean hasSameRanges(Map<String, Integer> attr1, Map<String, Integer> attr2) {
		return satisfiesTicker(attr1, attr2) && reverseTicker(attr2, attr1);
	}

	boolean satisfiesTicker(Map<String, Integer> tickerTape, Map<String, Integer> attr) {
		for (String key : tickerTape.keySet()) {
			switch (key) {
				case INDEX:
					continue;
				case "trees":
				case "cats":
					if (attr.containsKey(key) && ObjectUtils.compare(tickerTape.get(key), attr.get(key)) >= 0) {
//						System.out.println("XXX "+ key +" = "+ tickerTape.get(key) +", "+ attr.get(key) +" --> "+ ObjectUtils.compare(tickerTape.get(key), attr.get(key)));
						return false;
					}
					break;
				case "pomeranians":
				case "goldfish":
					if (attr.containsKey(key) && ObjectUtils.compare(tickerTape.get(key), attr.get(key)) <= 0) {
//						System.out.println("XXX "+ key +" = "+ tickerTape.get(key) +", "+ attr.get(key) +" --> "+ ObjectUtils.compare(tickerTape.get(key), attr.get(key)));
						return false;
					}
					break;
				default:
					if (attr.containsKey(key) && !Objects.equals(tickerTape.get(key), attr.get(key))) {
//						System.out.println("XXX "+ key +" = "+ tickerTape.get(key) +", "+ attr.get(key) +" --> "+ ObjectUtils.compare(tickerTape.get(key), attr.get(key)));
						return false;
					}
			}
		}
		
		return true;
	}

	boolean reverseTicker(Map<String, Integer> attr, Map<String, Integer> tickerTape) {
		for (String key : attr.keySet()) {
			switch (key) {
				case INDEX:
				case "trees":
				case "cats":
				case "pomeranians":
				case "goldfish":
					continue;
				default:
					if (tickerTape.containsKey(key) && !Objects.equals(attr.get(key), tickerTape.get(key))) {
//						System.out.println("ZZZ "+ key +" = "+ attr.get(key) +", "+ tickerTape.get(key) +" --> "+ ObjectUtils.compare(attr.get(key), tickerTape.get(key)));
						return false;
					}
			}
		}
		
		return true;
	}

	boolean hasSameAttr(Map<String, Integer> attr1, Map<String, Integer> attr2) {
		return hasSameValues(attr1, attr2) && hasSameValues(attr2, attr1);
	}

	boolean hasSameValues(Map<String, Integer> attr1, Map<String, Integer> attr2) {
		for (String key : attr1.keySet()) {
			if (INDEX.equals(key)) continue;

			if (attr2.containsKey(key) && !Objects.equals(attr1.get(key), attr2.get(key)))
				return false;
		}

		return true;
	}

	Map<String, Integer> buildTickerTape() {
		Map<String, Integer> tickerTape = new HashMap<>();
		tickerTape.put("children", 3);
		tickerTape.put("cats", 7);
		tickerTape.put("samoyeds", 2);
		tickerTape.put("pomeranians", 3);
		tickerTape.put("akitas", 0);
		tickerTape.put("vizslas", 0);
		tickerTape.put("goldfish", 5);
		tickerTape.put("trees", 3);
		tickerTape.put("cars", 2);
		tickerTape.put("perfumes", 1);

		return tickerTape;
	}

	private static Pattern parser = Pattern.compile(
			"\\w+ (\\d+): (\\w+): (\\d+), (\\w+): (\\d+), (\\w+): (\\d+)", Pattern.DOTALL);
	Map<String, Integer> parseSue(String sue) {
		Map<String, Integer> attr = new HashMap<>();
		Matcher m = parser.matcher(sue);
		if (m.matches()) {
			attr.put(INDEX, Integer.parseInt(m.group(1)));
			attr.put(m.group(2), Integer.parseInt(m.group(3)));
			attr.put(m.group(4), Integer.parseInt(m.group(5)));
			attr.put(m.group(6), Integer.parseInt(m.group(7)));
		}
		else {
			System.err.println("No match: "+ sue);
		}
		return attr;
	}
}

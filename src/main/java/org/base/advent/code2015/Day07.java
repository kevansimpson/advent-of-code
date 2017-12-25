package org.base.advent.code2015;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.base.advent.Solution;

/**
 *
 */
public class Day07 implements Solution<List<String>> {

	private static Pattern parser = Pattern.compile("(.+)\\s->\\s([a-z]+)", Pattern.DOTALL);

	private Map<String, Integer> valueMap = new HashMap<>();

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input07.txt");
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
		Map<String, String[]> circuitMap = buildCircuitMap(directions);
		System.out.println("The signal provided to a = "+ calculate(circuitMap, "a"));
	}

	public void solvePuzzle2() throws Exception {
		// clear and override wire "b"
		valueMap.clear();
		valueMap.put("b", 46065);
		List<String> directions = getInput();
		Map<String, String[]> circuitMap = buildCircuitMap(directions);
		System.out.println("The signal provided to a after override to b = "+ calculate(circuitMap, "a"));
	}

	Map<String, String[]> buildCircuitMap(List<String> directions) {
		Map<String, String[]> circuitMap = new HashMap<String, String[]>();
		for (String directive : directions) {
			Matcher matcher = parser.matcher(directive);
			if (matcher.matches()) {
				circuitMap.put(matcher.group(2), matcher.group(1).split("\\s"));
			}
			else {
				System.err.println("No match: "+ directive);
			}
		}

		return circuitMap;
	}

	void debug(Map<String, String[]> circuitMap) {
		for (String key : circuitMap.keySet())
			System.err.println(key +" = "+ ArrayUtils.toString(circuitMap.get(key)));
	}

	protected int calculate(Map<String, String[]> circuitMap, String wire) {
		String[] logic = circuitMap.get(wire);
		if (!valueMap.containsKey(wire)) {
			if (logic == null) {
//				debug(circuitMap);
//				System.out.println("wire = "+ wire);
				if (NumberUtils.isNumber(wire))
					return Integer.parseInt(wire);
				else
					throw new NullPointerException(wire);
			}
			else {
			switch (logic.length) {
				case 1: // literal value OR undocumented variable
					if (NumberUtils.isNumber(logic[0]))
						valueMap.put(wire, Integer.parseInt(logic[0]));
					else
						valueMap.put(wire, calculate(circuitMap, logic[0]));
					break;
				case 2: // bitwise complement, the ~ operator is NOT appropriate
					if (StringUtils.equals("NOT", logic[0]))
						valueMap.put(wire, (65535 - calculate(circuitMap, logic[1])));
					break;
				case 3: // equation
					switch (logic[1]) {
						case "AND":
							if (NumberUtils.isNumber(logic[2]))
								valueMap.put(wire, calculate(circuitMap, logic[0]) & NumberUtils.toInt(logic[2]));
							else
								valueMap.put(wire, calculate(circuitMap, logic[0]) & calculate(circuitMap, logic[2]));
							break;
						case "OR":
							valueMap.put(wire, calculate(circuitMap, logic[0]) | calculate(circuitMap, logic[2]));
							break;
						case "LSHIFT":
							valueMap.put(wire, calculate(circuitMap, logic[0]) << NumberUtils.toInt(logic[2]));
							break;
						case "RSHIFT":
							valueMap.put(wire, calculate(circuitMap, logic[0]) >>> NumberUtils.toInt(logic[2]));
							break;
					}
			}
			}
		}

		return valueMap.get(wire);
	}

	protected int get(Map<String, Integer> circuitMap, String key) {
		return circuitMap.containsKey(key) ? circuitMap.get(key).intValue() : 0;
	}
}

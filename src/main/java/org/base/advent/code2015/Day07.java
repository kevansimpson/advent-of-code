package org.base.advent.code2015;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.base.advent.Solution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/07">Day 07</a>
 */
public class Day07 implements Solution<List<String>> {

    private static final Pattern parser = Pattern.compile("(.+)\\s->\\s([a-z]+)", Pattern.DOTALL);

    private final Map<String, Integer> valueMap = new HashMap<>();

    @Override
    public List<String> getInput(){
        return readLines("/2015/input07.txt");
    }

    @Override
    public Object solvePart1() {
        return signalA(getInput());
    }

    @Override
    public Object solvePart2() {
        return overrideSignalA(getInput());
    }


    public int signalA(final List<String> directions) {
        return calculate(buildCircuitMap(directions), "a");
    }

    public int overrideSignalA(final List<String> directions) {
        // clear and override wire "b"
        valueMap.clear();
        valueMap.put("b", 46065);
        return calculate(buildCircuitMap(directions), "a");
    }

    protected Map<String, String[]> buildCircuitMap(final List<String> directions) {
        final Map<String, String[]> circuitMap = new HashMap<>();
        for (final String directive : directions) {
            final Matcher matcher = parser.matcher(directive);
            if (matcher.matches()) {
                circuitMap.put(matcher.group(2), matcher.group(1).split("\\s"));
            }
            else {
                throw new RuntimeException("No match: "+ directive);
            }
        }

        return circuitMap;
    }

    protected int calculate(final Map<String, String[]> circuitMap, final String wire) {
        final String[] logic = circuitMap.get(wire);
        if (!valueMap.containsKey(wire)) {
            if (logic == null) {
                if (NumberUtils.isDigits(wire))
                    return Integer.parseInt(wire);
                else
                    throw new NullPointerException(wire);
            }
            else {
                switch (logic.length) {
                    case 1: // literal value OR undocumented variable
                        if (NumberUtils.isDigits(logic[0]))
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
                                if (NumberUtils.isDigits(logic[2]))
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

    protected int get(final Map<String, Integer> circuitMap, final String key) {
        return circuitMap.getOrDefault(key, 0);
    }
}

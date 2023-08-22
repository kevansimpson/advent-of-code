package org.base.advent.code2015;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

/**
 * <a href="https://adventofcode.com/2015/day/07">Day 07</a>
 */
public class Day07 implements Function<List<String>, Day07.SignalOverride> {
    private static final Pattern parser = Pattern.compile("(.+)\\s->\\s([a-z]+)", Pattern.DOTALL);

    public record SignalOverride(int signalA, int overrideA) {}

    @Override
    public SignalOverride apply(final List<String> directions) {
        final Map<String, Integer> valueMap = new HashMap<>();
        int a = calculate(valueMap, buildCircuitMap(directions), "a");
        // clear and override wire "b"
        valueMap.clear();
        valueMap.put("b", a);
        return new SignalOverride(a, calculate(valueMap, buildCircuitMap(directions), "a"));
    }

    Map<String, String[]> buildCircuitMap(final List<String> directions) {
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

    int calculate(final Map<String, Integer> valueMap,
                  final Map<String, String[]> circuitMap,
                  final String wire) {
        final String[] logic = circuitMap.get(wire);
        if (!valueMap.containsKey(wire)) {
            if (logic == null) {
                if (isDigits(wire))
                    return parseInt(wire);
                else
                    throw new NullPointerException(wire);
            }
            else {
                switch (logic.length) {
                    case 1 -> { // literal value OR undocumented variable
                        if (isDigits(logic[0]))
                            valueMap.put(wire, parseInt(logic[0]));
                        else
                            valueMap.put(wire, calculate(valueMap, circuitMap, logic[0]));
                    }
                    case 2 -> { // bitwise complement, the ~ operator is NOT appropriate
                        if (StringUtils.equals("NOT", logic[0]))
                            valueMap.put(wire, (65535 - calculate(valueMap, circuitMap, logic[1])));
                    }
                    case 3 -> { // equation
                        switch (logic[1]) {
                            case "AND" -> {
                                if (isDigits(logic[2]))
                                    valueMap.put(wire, calculate(valueMap, circuitMap, logic[0]) & toInt(logic[2]));
                                else
                                    valueMap.put(wire,
                                            calculate(valueMap, circuitMap, logic[0]) &
                                                    calculate(valueMap, circuitMap, logic[2]));
                            }
                            case "OR" ->
                                    valueMap.put(wire,
                                            calculate(valueMap, circuitMap, logic[0]) |
                                                    calculate(valueMap, circuitMap, logic[2]));
                            case "LSHIFT" ->
                                    valueMap.put(wire, calculate(valueMap, circuitMap, logic[0]) << toInt(logic[2]));
                            case "RSHIFT" ->
                                    valueMap.put(wire, calculate(valueMap, circuitMap, logic[0]) >>> toInt(logic[2]));
                        }
                    }
                }
            }
        }

        return valueMap.get(wire);
    }
}

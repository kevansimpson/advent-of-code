package org.base.advent.code2015;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.base.advent.Solution;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>Part 1</h2>
 * This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates! Unfortunately, little Bobby
 * is a little under the recommended age range, and he needs help assembling the circuit.
 *
 * Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535).
 * A signal is provided to each wire by a gate, another wire, or some specific value. Each wire can only get a
 * signal from one source, but can provide its signal to multiple destinations. A gate provides no signal until all
 * of its inputs have a signal.
 *
 * The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect wires
 * x and y to an AND gate, and then connect its output to wire z.
 *
 * For example:
 *  - 123 -> x means that the signal 123 is provided to wire x.
 *  - x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
 *  - p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
 *  - NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.
 *
 * Other possible gates include OR (bitwise OR) and RSHIFT (right-shift). If, for some reason, you'd like to emulate
 * the circuit instead, almost all programming languages (for example, C, JavaScript, or Python) provide operators
 * for these gates.
 *
 * In little Bobby's kit's instructions booklet (provided as your puzzle input),
 * what signal is ultimately provided to wire a?
 *
 * <h2>Part 2</h2>
 * Now, take the signal you got on wire a, override wire b to that signal, and reset the other wires (including wire a).
 * What new signal is ultimately provided to wire a?
 */
public class Day07 implements Solution<List<String>> {

    private static final Pattern parser = Pattern.compile("(.+)\\s->\\s([a-z]+)", Pattern.DOTALL);

    private final Map<String, Integer> valueMap = new HashMap<>();

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input07.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return signalA(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
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

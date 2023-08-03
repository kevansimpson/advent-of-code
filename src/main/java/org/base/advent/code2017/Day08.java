package org.base.advent.code2017;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <a href="https://adventofcode.com/2017/day/08">Day 08</a>
 */
public class Day08 implements Solution<List<String>> {

    public static final String HIGHEST = "highestValueDuringInstructionProcessing";

    @Override
    public List<String> getInput(){
        return readLines("/2017/input08.txt");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
	@Override
    public Object solvePart1() {
        return updateRegisters(getInput(), false).values().stream().max(Comparator.naturalOrder()).get();
    }

    @Override
    public Object solvePart2() {
        return updateRegisters(getInput(), true).getOrDefault(HIGHEST, -1);
    }

    public Map<String, Integer> updateRegisters(List<String> equations, boolean trackHighest) {
        Map<String, Integer> register = new LinkedHashMap<>();

        for (String instruction : equations) {
            String[] tokens = instruction.split("\\s");
            // 0  1   2  3  4  5 6
            // c dec -10 if a >= 1
            int value = Integer.parseInt(tokens[6]);
            int variable = register.getOrDefault(tokens[4], 0);
            boolean predicate = false;

            switch (tokens[5]) {
                case ">":
                    predicate = variable > value;
                    break;
                case "<":
                    predicate = variable < value;
                    break;
                case "<=":
                    predicate = variable <= value;
                    break;
                case ">=":
                    predicate = variable >= value;
                    break;
                case "==":
                    predicate = variable == value;
                    break;
                case "!=":
                    predicate = variable != value;
                    break;
                default:
                    throw new RuntimeException(tokens[5]);
            }

            if (predicate) {
                int newValue = register.getOrDefault(tokens[0], 0) // current
                               + Integer.parseInt(tokens[2]) * (StringUtils.equalsIgnoreCase("inc", tokens[1]) ? 1 : -1); // delta
                register.put(tokens[0], newValue);

                if (trackHighest && newValue > register.getOrDefault(HIGHEST, 0)) {
                    register.put(HIGHEST, newValue);
                }
            }
        }

        return register;
    }
}

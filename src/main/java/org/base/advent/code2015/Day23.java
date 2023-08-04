package org.base.advent.code2015;

import lombok.Getter;
import org.base.advent.Solution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://adventofcode.com/2015/day/23">Day 23</a>
 */
public class Day23 implements Solution<List<String>> {
    @Getter
    private final List<String> input = readLines("/2015/input23.txt");

    @Override
    public Object solvePart1() {
        return solve(getInput(), 0, 0);
    }

    @Override
    public Object solvePart2() {
        return solve(getInput(), 1, 0);
    }

    public int solve(final List<String> instructions, final int a, final int b) {
        final Map<String, Integer> registers = new HashMap<>();
        registers.put("a", a);
        registers.put("b", b);
        followInstructions(registers, instructions, 0);
        return registers.get("b");
    }
    
    protected void followInstructions(final Map<String, Integer> registers, final List<String> instructions, final int index) {
        if (index < 0 || index >= instructions.size())
            return;

        final String[] tokens = instructions.get(index).replace(",", "").split("\\s");
        switch (tokens[0]) {
            case "hlf" -> {
                final Integer hlf = registers.get(tokens[1]);
                registers.put(tokens[1], hlf / 2);
                followInstructions(registers, instructions, index + 1);
            }
            case "tpl" -> {
                final Integer tpl = registers.get(tokens[1]);
                registers.put(tokens[1], tpl * 3);
                followInstructions(registers, instructions, index + 1);
            }
            case "inc" -> {
                final Integer inc = registers.get(tokens[1]);
                registers.put(tokens[1], inc + 1);
                followInstructions(registers, instructions, index + 1);
            }
            case "jmp" -> followInstructions(registers, instructions, index + Integer.parseInt(tokens[1]));
            case "jie" -> {
                final Integer jie = registers.get(tokens[1]);
                if ((jie % 2) == 0)
                    followInstructions(registers, instructions, index + Integer.parseInt(tokens[2]));
                else
                    followInstructions(registers, instructions, index + 1);
            }
            case "jio" -> {
                final Integer jio = registers.get(tokens[1]);
                if (jio == 1)
                    followInstructions(registers, instructions, index + Integer.parseInt(tokens[2]));
                else
                    followInstructions(registers, instructions, index + 1);
            }
        }
    }
}

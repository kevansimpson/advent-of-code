package org.base.advent.code2024;

import org.base.advent.ParallelSolution;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static org.base.advent.util.Text.extractInt;
import static org.base.advent.util.Text.extractLong;

/**
 * <a href="https://adventofcode.com/2024/day/17">Day 17</a>
 */
public class Day17 extends ParallelSolution<List<String>> {
    public Day17(ExecutorService pool) {
        super(pool);
    }

    @Override
    public Object solvePart1(List<String> input) {
        return new Program(input).run();
    }

    @Override
    public Object solvePart2(List<String> input) {
        Program p = new Program(input);
        int[] opCodes = p.opCodes;
        String target = Arrays.toString(opCodes)
                .replaceAll(" ", "")
                .replace("[", "").
                replace("]", "");

        List<Long> answers = new ArrayList<>();
        recurse(1L, target, opCodes, answers);

        return answers.stream().min(Comparator.naturalOrder()).orElse(-1L);
    }

    void recurse(long a, String target, int[] codes, List<Long> answers) {
        for (long add = 0; add < 8L; add++) {
            long next = add + a;
            Program p = new Program(next, 0L, 0L, codes);
            String out = p.run();
            if (out.length() > target.length())
                return;

            if (target.equals(out)) {
                answers.add(next);
            }
            else if (target.endsWith(out)) {
                recurse(next * 8, target, codes, answers);
            }
        }
    }

    private static class Program {
        long a, b, c;
        int[] opCodes;
        List<Long> output = new ArrayList<>();

        public Program(List<String> input) {
            this(extractLong(input.get(0))[0], extractLong(input.get(1))[0], extractLong(input.get(2))[0],
                    extractInt(input.get(4)));
        }

        public Program(long a, long b, long c, int[] codes) {
            this.a = a;
            this.b = b;
            this.c = c;
            opCodes = codes;
        }

        public String run() {
            int pointer = 0;
            while (pointer < opCodes.length) {
                switch (opCodes[pointer]) {
                    // adv
                    case 0 -> {
                        a /= pow(combo(opCodes[pointer + 1]));
                        pointer += 2;
                    }
                    // bxl
                    case 1 -> {
                        b ^= opCodes[pointer + 1];
                        pointer += 2;
                    }
                    // bst
                    case 2 -> {
                        b = combo(opCodes[pointer + 1]) % 8L;
                        pointer += 2;
                    }
                    // jnz
                    case 3 -> {
                        if (a == 0)
                            pointer += 2;
                        else
                            pointer = opCodes[pointer + 1];
                    }
                    // bxc
                    case 4 -> {
                        b ^= c;
                        pointer += 2;
                    }
                    // out
                    case 5 -> {
                        output.add(combo(opCodes[pointer + 1]) % 8L);
                        pointer += 2;
                    }
                    // bdv
                    case 6 -> {
                        b =  a / pow(combo(opCodes[pointer + 1]));
                        pointer += 2;
                    }
                    // cdv
                    case 7 -> {
                        c = a / pow(combo(opCodes[pointer + 1]));
                        pointer += 2;
                    }
                }
            }

            return output.stream().map(String::valueOf).collect(Collectors.joining(","));
        }

        long combo(int operand) {
            return switch (operand) {
                case 4 -> a;
                case 5 -> b;
                case 6 -> c;
                case 7 -> throw new RuntimeException("Invalid program!");
                // 0,1,2,3
                default -> operand;
            };
        }

        long pow(long power) {
            return (power == 0L) ? 1L : 2L * pow(power - 1L);
        }
    }
}


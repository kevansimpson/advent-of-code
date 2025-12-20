package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.ParallelSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.lang.Long.parseLong;

/**
 * <a href="https://adventofcode.com/2025/day/6">Day 6</a>
 */
public class Day06 extends ParallelSolution<List<String>> {
    public Day06(ExecutorService pool) {
        super(pool);
    }

    @Override
    public Object solvePart1(List<String> input) {
        List<List<Long>> problems = new ArrayList<>();
        int size = input.size(), probCount = input.get(0).trim().split("\\s+").length;
        for (int i = 0; i < probCount; i++)
            problems.add(new ArrayList<>());

        for (int line = 0; line < size - 1; line++) {
            String[] nums = input.get(line).trim().split("\\s+");
            for (int i = 0; i < nums.length; i++)
                problems.get(i).add(parseLong(nums[i]));
        }

        List<Long> answers = new ArrayList<>();
        String[] ops = input.get(size - 1).trim().split("\\s+");
        for (int i = 0; i < probCount; i++) {
            if ("+".equals(ops[i]))
                answers.add(problems.get(i).stream().reduce(0L, Long::sum));
            else
                answers.add(problems.get(i).stream().reduce(1L, (l1, l2) -> l1 * l2));
        }

        return answers.stream().mapToLong(Long::longValue).sum();

    }

    @Override
    public Object solvePart2(List<String> input) {
        Stack<Character> ops = new Stack<>();
        Stack<StringBuilder> problems = new Stack<>();
        List<Long> answers = new ArrayList<>();
        int len = input.stream().mapToInt(String::length).max().orElseThrow();
        for (int i = 0; i < len; i++) {
            StringBuilder column = new StringBuilder();
            for (int line = 0; line < input.size(); line++) {
                char ch = input.get(line).charAt(i);
                switch (ch) {
                    case ' ': break;
                    case '+':
                    case '*': {
                        ops.push(ch);
                        break;
                    }
                    default:
                        column.append(ch);
                }
            }
            if (column.isEmpty()) {
                answers.add(calculate(ops, problems));
            }
            else
                problems.push(column);
        }

        answers.add(calculate(ops, problems));
        return answers.stream().mapToLong(Long::longValue).sum();
    }

    long calculate(Stack<Character> ops, Stack<StringBuilder> problems) {
        char op = ops.pop();
        boolean add = (op == '+');
        long answer = add ? 0L : 1L;
        while (!problems.isEmpty()) {
            long num = parseLong(problems.pop().toString());
            if (add)
                answer += num;
            else
                answer *= num;
        }

        return answer;
    }
}


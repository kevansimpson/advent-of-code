package org.base.advent.code2022;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.base.advent.util.Util.extractInt;

/**
 * <a href="https://adventofcode.com/2022/day/11">Day 11</a>
 */
public class Day11 implements Solution<List<String>> {

    @Override
    public Object solvePart1(final List<String> input) {
        final List<Monkey> barrel1 = readMonkeyNotes(input);
        play20Rounds(barrel1);
        return mostActive(barrel1);
    }

    @Override
    public Object solvePart2(final List<String> input) {
        final List<Monkey> barrel2 = readMonkeyNotes(input);
        for (int i = 1; i <= 10000; i++) {
            barrel2.forEach(m -> quickMonkey(m, barrel2));
        }
        return mostActive(barrel2);
    }

    void play20Rounds(List<Monkey> monkeys) {
        for (int r = 1; r <= 20; r++)
            monkeys.forEach(m -> m.takeTurn(monkeys));
    }

    void quickMonkey(Monkey monkey, List<Monkey> allMonkeys) {
        List<Long> next = new ArrayList<>(monkey.items);
        monkey.items.clear();
        next.forEach(item -> {
            monkey.totalInspected.incrementAndGet();
            long worry = monkey.evalOperation(item) % divisorProduct(allMonkeys);
            if (worry % monkey.test == 0L)
                allMonkeys.get(monkey.testPass).items.add(worry);
            else
                allMonkeys.get(monkey.testFail).items.add(worry);
        });
    }

    long mostActive(List<Monkey> monkeys) {
        long[] seen = monkeys.stream().mapToLong(it -> it.totalInspected.get()).toArray();
        Arrays.sort(seen);
        return seen[seen.length - 1] * seen[seen.length - 2];
    }

    private long divisorProduct(List<Monkey> monkeys) {
        return monkeys.stream().mapToLong(Monkey::test).reduce(1L, (a, b) -> a * b);
    }

    private List<Monkey> readMonkeyNotes(List<String> notes) {
        return ListUtils.partition(notes, 7).stream().map(lines ->
                new Monkey(
                        extractInt(lines.get(0))[0],
                        Arrays.stream(extractInt(lines.get(1)))
                                .mapToLong(Long::valueOf).boxed().collect(Collectors.toList()),
                        StringUtils.substringAfter(lines.get(2), "new = "),
                        (long) extractInt(lines.get(3))[0],
                        extractInt(lines.get(4))[0],
                        extractInt(lines.get(5))[0],
                        new AtomicLong(0L))).collect(Collectors.toList());
    }

    public record Monkey(int position, List<Long> items, String operation,
                         Long test, int testPass, int testFail, AtomicLong totalInspected) {

        public void takeTurn(List<Monkey> allMonkeys) {
            items.forEach(worry -> {
                totalInspected.incrementAndGet();
                long current = evalOperation(worry) / 3L;
                if (current % test == 0L)
                    allMonkeys.get(testPass).items.add(current);
                else
                    allMonkeys.get(testFail).items.add(current);
            });
            items.clear();
        }

        long evalOperation(long worry) {
            String[] ops = operation.split(" ");
            long l = eval(worry, ops[0]);
            long r = eval(worry, ops[2]);
            return switch (ops[1]) {
                case "+" -> l + r;
                case "*" -> l * r;
                default -> throw new IllegalStateException("Unexpected value: " + ops[1]);
            };
        }

        private Long eval(long worry, String operand) {
            if ("old".equals(operand))
                return worry;
            else
                return Long.valueOf(operand);
        }
    }
}

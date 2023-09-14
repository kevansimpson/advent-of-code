package org.base.advent.code2018;

import org.base.advent.Solution;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.base.advent.util.Text.azInt;

/**
 * <a href="https://adventofcode.com/2018/day/7">Day 7</a>
 */
public class Day07 implements Solution<List<String>> {
    @Override
    public Object solvePart1(List<String> input) {
        return orderInstructions(readInstructions(input));
    }

    @Override
    public Object solvePart2(List<String> input) {
        return elfLabor(readInstructions(input));
    }

    int elfLabor(Map<String, List<String>> instructions) {
        int steps = -1;
        Map<String, AtomicInteger> busy = new HashMap<>();

        while (!instructions.isEmpty() || !busy.isEmpty()) {
            steps++; // increment step, decrement elf tasks
            busy.values().forEach(AtomicInteger::decrementAndGet);

            List<String> working = new ArrayList<>(busy.keySet());
            for (String elf : working) {
                if (busy.get(elf).get() == 0) {
                    removeStep(elf, instructions);
                    busy.remove(elf);
                }
            }

            List<String> done = done(instructions);
            for (String d : done)
                if (busy.size() < 5 && !busy.containsKey(d))
                    busy.put(d, new AtomicInteger(60 + azInt(d.charAt(0))));
        }

        return steps;
    }

    String orderInstructions(Map<String, List<String>> instructions) {
        StringBuilder order = new StringBuilder();
        while (!instructions.isEmpty()) {
            List<String> done = done(instructions);
            String d = done.get(0);
            removeStep(d, instructions);
            order.append(d);
        }

        return order.toString();
    }

    List<String> done(Map<String, List<String>> instructions) {
        return instructions.entrySet().stream()
                .filter(e -> e.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
    }

    void removeStep(String step, Map<String, List<String>> instructions) {
        instructions.remove(step);
        for (List<String> pre : instructions.values())
            pre.remove(step);

    }

    Map<String, List<String>> readInstructions(List<String> input) {
        Set<String> all = new HashSet<>();
        Map<String, List<String>> instructions = new HashMap<>();
        for (String row : input) {
            String[] steps = row.split(" ");
            String step = steps[7], req = steps[1];
            all.add(step);
            all.add(req);
            if (instructions.containsKey(step))
                instructions.get(step).add(req);
            else
                instructions.put(step, new ArrayList<>(List.of(req)));
        }

        all.stream().filter(it -> !instructions.containsKey(it))
                .forEach(it -> instructions.put(it, new ArrayList<>()));
        return instructions;
    }
}
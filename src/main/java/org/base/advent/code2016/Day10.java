package org.base.advent.code2016;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2016/day/10">Day 10</a>
 */
public class Day10 implements Function<List<String>, Pair<Integer, Integer>> {
    record Instruction(String botId, String low, String high) {}
    record ChipFactory(Map<String, List<Integer>> initialBots,
                       Map<String, Instruction> instructions) {}

    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        ChipFactory factory = inspectMicrochips(input);
        Map<String, List<Integer>> bots = new ConcurrentHashMap<>(factory.initialBots);
        int[] botNumber = new int[] {0};
        while (bots.values().stream().anyMatch(it -> it.size() > 1)) {
            bots.forEach((k, v) -> {
                if (v.size() == 2) {
                    if (v.contains(17) && v.contains(61))
                        botNumber[0] = Integer.parseInt(k.substring(3));

                    List<Integer> ab = new ArrayList<>(v);
                    Collections.sort(ab);
                    bots.put(k, new ArrayList<>());
                    Instruction instr = factory.instructions.get(k);
                    if (bots.containsKey(instr.low))
                        bots.get(instr.low).add(ab.get(0));
                    else
                        bots.put(instr.low, new ArrayList<>(List.of(ab.get(0))));

                    if (bots.containsKey(instr.high))
                        bots.get(instr.high).add(ab.get(1));
                    else
                        bots.put(instr.high, new ArrayList<>(List.of(ab.get(1))));
                }
            });

        }

        return Pair.of(botNumber[0],
                bots.get("output0").get(0) * bots.get("output1").get(0) * bots.get("output2").get(0));
    }

    ChipFactory inspectMicrochips(List<String> input) {
        Map<String, List<Integer>> initialBots = new TreeMap<>();
        Map<String, Instruction> instructions = new LinkedHashMap<>();

        for (String line : input) {
            String[] bits = line.split(" ");
            if ("value".equals(bits[0])) {
                String key = bits[4] + bits[5];
                int chip = Integer.parseInt(bits[1]);
                if (initialBots.containsKey(key))
                    initialBots.get(key).add(chip);
                else
                    initialBots.put(key, new ArrayList<>(List.of(chip)));
            }
            else {
                Instruction inst = new Instruction(
                        bits[0] + bits[1], bits[5] + bits[6], bits[10] + bits[11]);
                instructions.put(inst.botId, inst);
            }
        }
        return new ChipFactory(initialBots, instructions);
    }
}
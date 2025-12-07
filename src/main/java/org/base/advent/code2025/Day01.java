package org.base.advent.code2025;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

/**
 * <a href="https://adventofcode.com/2025/day/1">Day 1</a>
 */
public class Day01 implements Function<List<String>, Pair<Integer, Integer>> {
    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        int dial = 50, pointsAtZero = 0, passesZero = 0;
        for (String instr : input) {
            String turn = instr.substring(0, 1);
            int clicks = parseInt(instr.substring(1));
            int dir = "L".equals(turn) ? -1 : 1;
            passesZero += clicks / 100;
            for (int i = 0; i < clicks % 100; i++) {
                dial += dir;
                if (dial == 100) dial = 0;
                else if (dial == -1) dial = 99;

                if (dial == 0)
                    passesZero++;
            }
            if (dial == 0)
                pointsAtZero++;
        }
        return Pair.of(pointsAtZero, passesZero);
    }
}


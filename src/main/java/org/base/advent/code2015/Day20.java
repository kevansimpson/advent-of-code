package org.base.advent.code2015;


import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2015/day/20">Day 20</a>
 */
public class Day20 implements Function<Integer, Day20.LowestHouseNumbers> {

    private static final int MAX = 1000000;

    public record LowestHouseNumbers(int low1, int low2) {}

    @Override
    public LowestHouseNumbers apply(final Integer target) {
        final int[] houses1 = new int[MAX];
        final int[] houses2 = new int[MAX];

        for (int elf = 1; elf < MAX; elf++) {
            int count = 0;
            for (int visited = elf; visited < MAX; visited += elf) {
                houses1[visited] += elf * 10;
                // new rules
                if (count++ < 50) {
                    houses2[visited] += elf * 11;
                }
            }
        }

        return new LowestHouseNumbers(findTargetHouse(houses1, target), findTargetHouse(houses2, target));
    }

    int findTargetHouse(final int[] houses, final int target) {
        int answer = 0;
        for (int i = 0; i < MAX; i++) {
            if (houses[i] >= target) {
                answer = i;
                break;
            }
        }

        return answer;
    }
}

package org.base.advent.code2015;


import org.base.advent.Solution;

/**
 * <a href="https://adventofcode.com/2015/day/20">Day 20</a>
 */
public class Day20 implements Solution<Integer> {

    private static final int MAX = 1000000;

    @Override
    public Integer getInput() {
        return 34000000;
    }

    @Override
    public Object solvePart1() {
        return findLowestHouse();
    }

    @Override
    public Object solvePart2() {
        return findLowestWithNewRules();
    }

    public int findLowestHouse() {
        final int[] houses = new int[MAX];

        for (int elf = 1; elf < MAX; elf++) {
            for (int visited = elf; visited < MAX; visited += elf) {
                houses[visited] += elf * 10;
            }
        }

        return findTargetHouse(houses);
    }

    public int findLowestWithNewRules() {
        final int[] houses = new int[MAX];

        for (int elf = 1; elf < MAX; elf++) {
            int count = 0;
            for (int visited = elf; visited < MAX; visited += elf) {
                houses[visited] += elf * 11;
                count += 1;
                if (count >= 50)
                    break;
            }
        }

        return findTargetHouse(houses);
    }

    protected int findTargetHouse(final int[] houses) {
        int answer = 0;
        for (int i = 0; i < MAX; i++) {
            if (houses[i] >= getInput()) {
                answer = i;
                break;
            }
        }

        return answer;
    }
}

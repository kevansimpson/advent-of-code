package org.base.advent.code2015;


import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * To keep the Elves busy, Santa has them deliver some presents by hand, door-to-door. He sends them down a street
 * with infinite houses numbered sequentially: 1, 2, 3, 4, 5, and so on.
 *
 * Each Elf is assigned a number, too, and delivers presents to houses based on that number:
 *
 * The first Elf (number 1) delivers presents to every house: 1, 2, 3, 4, 5, ....
 * The second Elf (number 2) delivers presents to every second house: 2, 4, 6, 8, 10, ....
 * Elf number 3 delivers presents to every third house: 3, 6, 9, 12, 15, ....
 *
 * There are infinitely many Elves, numbered starting with 1. Each Elf delivers presents equal to ten times his or
 * her number at each house.
 *
 * The first house gets 10 presents: it is visited only by Elf 1, which delivers 1 * 10 = 10 presents. The fourth
 * house gets 70 presents, because it is visited by Elves 1, 2, and 4, for a total of 10 + 20 + 40 = 70 presents.
 *
 * What is the lowest house number of the house to get at least as many presents as the number in your puzzle input?
 *
 * <h2>Part 2</h2>
 * The Elves decide they don't want to visit an infinite number of houses. Instead, each Elf will stop after
 * delivering presents to 50 houses. To make up for it, they decide to deliver presents equal to eleven times
 * their number at each house.
 *
 * With these changes, what is the new lowest house number of the house to get at least as many presents as the
 * number in your puzzle input?
 *
 */
public class Day20 implements Solution<Integer> {

    private static int MAX = 1000000;

    @Override
    public Integer getInput() {
        return 34000000;
    }

    @Override
    public Object solvePart1() throws Exception {
        return findLowestHouse();
    }

    @Override
    public Object solvePart2() throws Exception {
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

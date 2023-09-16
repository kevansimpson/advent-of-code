package org.base.advent.code2018;

import org.base.advent.Solution;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * <a href="https://adventofcode.com/2018/day/9">Day 9</a>
 */
public class Day09 implements Solution<Day09.MarbleGame> {
    public record MarbleGame(int players, int lastMarble) {}

    @Override
    public Object solvePart1(MarbleGame game) {
        return playGame(game);
    }

    @Override
    public Object solvePart2(MarbleGame game) {
        return playGame(new MarbleGame(game.players, 100 * game.lastMarble));
    }

    public long playGame(MarbleGame game) {
        long[] scores = new long[game.players];
        Deque<Integer> circle = new ArrayDeque<>();
        circle.add(0);

        for (int marble = 1; marble < game.lastMarble + 1; marble++) {
            if ((marble % 23) == 0) {
                rotate(circle, -7);
                scores[marble % game.players] += marble + circle.pop();
            }
            else {
                rotate(circle, 2);
                circle.addLast(marble);
            }
        }

        Arrays.sort(scores);
        return scores[game.players - 1];
    }

    void rotate(Deque<Integer> circle, int dist) {
        if (dist > 0)
            for (int i = 0; i < dist; i++)
                circle.addFirst(circle.removeLast());
        else
            for (int i = 0; i < Math.abs(dist) - 1; i++)
                circle.addLast(circle.removeFirst());
    }
}
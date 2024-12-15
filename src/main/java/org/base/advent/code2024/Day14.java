package org.base.advent.code2024;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.base.advent.util.Text.extractLong;

/**
 * <a href="https://adventofcode.com/2024/day/14">Day 14</a>
 */
@AllArgsConstructor
public class Day14 implements Function<List<String>, Pair<Integer, Integer>> {
    private final int width;
    private final int height;

    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        List<Robot> bots = findRobots(input);
        bots.forEach(b -> b.move(100, width, height));
        int safetyFactor = safetyFactor(bots);
        int tree = 0; // examples
        if (width > 100 && height > 100) // file input
            tree = findTree(bots);

        return Pair.of(safetyFactor, tree);
    }

    private int findTree(List<Robot> bots) {
        // start after the first 100 seconds + 0-index offset
        for (int s = 101; s < Integer.MAX_VALUE; s++) {
            Set<Point> image = new HashSet<>();
            int[][] finalPositions = new int[height][width];
            bots.forEach(b -> {
                b.move(1, width, height);
                finalPositions[-b.position.iy()][b.position.ix()]++;
                image.add(new Point(-b.position.iy(), b.position.ix()));
            });
            if (image.size() == bots.size()) {
                print(finalPositions, s);
                return s;
            }
        }

        return -1;
    }

    private void print(int[][] finalPositions, int index) {
        System.out.println("\n"+ StringUtils.leftPad("  "+ index, width, "=") +"\n");
        for (int[] row : finalPositions)
            System.out.println(
                    Arrays.toString(row)
                            .replaceAll(", ", "")
                            .replaceAll("0", " "));
    }

    private int safetyFactor(List<Robot> bots) {
        int safetyFactor = 1, midX = width / 2, midY = height / 2;
        int[] quadrants = {0, 0, 0, 0};
        bots.forEach(b -> {
            if (b.position.ix() < midX) {
                if (b.position.iy() > -midY)
                    quadrants[0]++;
                else if (b.position.iy() < -midY)
                    quadrants[1]++;
            }
            else if (b.position.ix() > midX) {
                if (b.position.iy() > -midY)
                    quadrants[2]++;
                else if (b.position.iy() < -midY)
                    quadrants[3]++;
            }
        });

        for (int q : quadrants)
            safetyFactor *= Math.max(1, q);
        return safetyFactor;
    }

    private List<Robot> findRobots(List<String> input) {
        return input.parallelStream().map(line -> {
            long[] nums = extractLong(line);
            return new Robot(new Point(nums[0], -nums[1]), new Point(nums[2], -nums[3]));
        }).toList();
    }

    @AllArgsConstructor
    private static class Robot {
        Point position;
        Point velocity;

        public void move(int seconds, int width, int height) {
            for (int s = 0; s < seconds; s++) {
                long newX = position.x + velocity.x;
                long newY = position.y + velocity.y;
                if (newX < 0) // out of bounds horizontally
                    newX += width;
                else if (newX >= width) // out of bounds horizontally
                    newX -= width;
                if (newY > 0) // out of bounds vertically
                    newY -= height;
                else if (newY <= -height) // out of bounds vertically
                    newY += height;

                position = new Point(newX, newY);
            }
        }
    }
}


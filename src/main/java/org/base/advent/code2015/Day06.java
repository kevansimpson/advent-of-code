package org.base.advent.code2015;

import lombok.Getter;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/06">Day 06</a>
 */
public class Day06 implements Solution<List<String>> {
    @Getter
    private final List<String> input = readLines("/2015/input06.txt");

    @Override
    public Object solvePart1() {
        return countLightsOn(getInput());
    }

    @Override
    public Object solvePart2() {
        return totalBrightness(getInput());
    }

    private enum Cmd {on, off, toggle}  // case-sensitive, to match input

    private static final Pattern PARSER = Pattern.compile(
            "(toggle|turn on|turn off)\\s([\\d,]+)\\sthrough\\s([\\d,]+)", Pattern.DOTALL);

    public int countLightsOn(final List<String> directions)  {
        final boolean[][] lightGrid = new boolean[1000][1000];

        for (final String directive : directions) {
            final Matcher matcher = PARSER.matcher(directive);
            if (matcher.matches()) {
                final Cmd cmd = parseCmd(matcher.group(1));
                final Point start = Point.point(matcher.group(2));
                final Point end = Point.point(matcher.group(3));
                
                for (int x = start.ix(); x <= end.x; x++) {
                    for (int y = start.iy(); y <= end.y; y++) {
                        switch (cmd) {
                            case on -> lightGrid[x][y] = true;
                            case off -> lightGrid[x][y] = false;
                            case toggle -> lightGrid[x][y] = !lightGrid[x][y];
                        }
                    }
                }
            }
            else {
                throw new RuntimeException("No match: "+ directive);
            }
        }
        
        int lightsOn = 0;
        for (int x = 0; x < 1000; x++) {
            for (int y = 0; y < 1000; y++) {
                if (lightGrid[x][y]) ++lightsOn;
            }
        }
        
        return lightsOn;
    }

    public int totalBrightness(final List<String> directions) {
        final int[][] lightGrid = new int[1000][1000];

        for (final String directive : directions) {
            final Matcher matcher = PARSER.matcher(directive);
            if (matcher.matches()) {
                final Cmd cmd = parseCmd(matcher.group(1));
                final Point start = Point.point(matcher.group(2));
                final Point end = Point.point(matcher.group(3));
                
                for (int x = start.ix(); x <= end.x; x++) {
                    for (int y = start.iy(); y <= end.y; y++) {
                        final int value = lightGrid[x][y];
                        switch (cmd) {
                            case on -> lightGrid[x][y] = value + 1;
                            case off -> lightGrid[x][y] = Math.max(value - 1, 0);
                            case toggle -> lightGrid[x][y] = value + 2;
                        }
                    }
                }
            }
            else {
                throw new RuntimeException("No match: "+ directive);
            }
        }
        
        int lightsOn = 0;
        for (int x = 0; x < 1000; x++) {
            for (int y = 0; y < 1000; y++) {
                lightsOn += lightGrid[x][y];
            }
        }
        
        // Wrong answer #1: 14190930 (too low, caused by not accounting for minimum brightness of 0)
        return lightsOn;
    }

    protected Cmd parseCmd(final String text) {
        return Cmd.valueOf(text.replace("turn ", "").trim());
    }
}

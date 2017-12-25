package org.base.advent.code2015;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.base.advent.Point;
import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * Because your neighbors keep defeating you in the holiday house decorating contest year after year, you've decided
 * to deploy one million lights in a 1000x1000 grid.
 *
 * Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to display
 * the ideal lighting configuration.
 *
 * Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are at 0,0, 0,999,
 * 999,999, and 999,0. The instructions include whether to turn on, turn off, or toggle various inclusive ranges
 * given as coordinate pairs. Each coordinate pair represents opposite corners of a rectangle, inclusive; a
 * coordinate pair like 0,0 through 2,2 therefore refers to 9 lights in a 3x3 square. The lights all start turned off.
 *
 * To defeat your neighbors this year, all you have to do is set up your lights by doing the instructions Santa
 * sent you in order.
 *
 * For example:
 *  - turn on 0,0 through 999,999 would turn on (or leave on) every light.
 *  - toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones that were on, and
 *      turning on the ones that were off.
 *  - turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
 *
 * After following the instructions, how many lights are lit?
 *
 * <h2>Part 2</h2>
 * You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from
 * Ancient Nordic Elvish.
 *
 * The light grid you bought actually has individual brightness controls; each light can have a brightness of zero
 * or more. The lights all start at zero.
 *
 * The phrase turn on actually means that you should increase the brightness of those lights by 1.
 * The phrase turn off actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.
 * The phrase toggle actually means that you should increase the brightness of those lights by 2.
 *
 * What is the total brightness of all lights combined after following Santa's instructions?
 *
 * For example:
 *  - turn on 0,0 through 0,0 would increase the total brightness by 1.
 *  - toggle 0,0 through 999,999 would increase the total brightness by 2000000.
 *
 */
public class Day06 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input06.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return countLightsOn(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
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
                
                for (int x = start.x; x <= end.x; x++) {
                    for (int y = start.y; y <= end.y; y++) {
                        switch (cmd) {
                            case on:
                                lightGrid[x][y] = true;
                                break;
                            case off:
                                lightGrid[x][y] = false;
                                break;
                            case toggle:
                                lightGrid[x][y] = !lightGrid[x][y];
                                break;
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
                
                for (int x = start.x; x <= end.x; x++) {
                    for (int y = start.y; y <= end.y; y++) {
                        final int value = lightGrid[x][y];
                        switch (cmd) {
                            case on:
                                lightGrid[x][y] = value + 1;
                                break;
                            case off:
                                lightGrid[x][y] = Math.max(value - 1, 0);
                                break;
                            case toggle:
                                lightGrid[x][y] = value + 2;
                                break;
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

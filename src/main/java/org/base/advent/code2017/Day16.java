package org.base.advent.code2017;

import org.apache.commons.lang3.ArrayUtils;
import org.base.advent.Solution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * <h2>Part 1</h2>
 * You come upon a very unusual sight; a group of programs here appear to be dancing.
 *
 * There are sixteen programs in total, named a through p. They start by standing in a line: a stands in position 0,
 * b stands in position 1, and so on until p, which stands in position 15.
 *
 * The programs' dance consists of a sequence of dance moves:
 *
 *  - Spin, written sX, makes X programs move from the end to the front, but maintain their order otherwise.
 *          (For example, s3 on abcde produces cdeab).
 *  - Exchange, written xA/B, makes the programs at positions A and B swap places.
 *  - Partner, written pA/B, makes the programs named A and B swap places.
 *
 * For example, with only five programs standing in a line (abcde), they could do the following dance:
 *
 *  - s1, a spin of size 1: eabcd.
 *  - x3/4, swapping the last two programs: eabdc.
 *  - pe/b, swapping programs e and b: baedc.
 *
 * After finishing their dance, the programs end up in order baedc.
 *
 * You watch the dance for a while and record their dance moves (your puzzle input). In what order are the programs
 * standing after their dance?
 *
 * <h2>Part 2</h2>
 * Now that you're starting to get a feel for the dance moves, you turn your attention to the dance as a whole.
 *
 * Keeping the positions they ended up in from their previous dance, the programs perform it again and again:
 * including the first dance, a total of one billion (1000000000) times.
 *
 * In the example above, their second dance would begin with the order baedc, and use the same dance moves:
 *
 *  - s1, a spin of size 1: cbaed.
 *  - x3/4, swapping the last two programs: cbade.
 *  - pe/b, swapping programs e and b: ceadb.
 *
 * In what order are the programs standing after their billion dances?
 *
 */
public class Day16 implements Solution<List<String>> {

    private static final int BILLION = 1000000000;

    @Override
    public List<String> getInput() throws IOException {
        return Stream.of(readInput("/2017/input16.txt").split(",")).collect(Collectors.toList());
    }

    @Override
    public Object solvePart1() throws Exception {
        return doALittleDance("abcdefghijklmnop", getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return danceMarathon("abcdefghijklmnop", getInput());
    }

    public String danceMarathon(String start, final List<String> steps) {
        final List<String> polkaDot = new ArrayList<>(); // again?

        for (int i = 0; i < BILLION; i++) {
            start = doALittleDance(start, steps);
            if (!polkaDot.contains(start))
                polkaDot.add(start);
            else
                break;
        }

        return polkaDot.get(BILLION % polkaDot.size() -1);
    }

    public String doALittleDance(final String start, final List<String> steps) {
        char[] programs = start.toCharArray();
        final int len = programs.length;

        for (final String step : steps) {
            switch (step.substring(0, 1)) {
                case "s":   // sX
                    programs = spin(programs, len, Integer.parseInt(step.substring(1)));
                    break;
                case "x":   // xA/B
                    programs = exchange(programs,
                            Stream.of(step.substring(1).split("/")).mapToInt(Integer::parseInt).toArray());
                    break;
                case "p":   // pA/B
                    programs = partner(programs, step.substring(1).split("/"));
                    break;
                default:
                    throw new RuntimeException("Funky Step: "+ step);
            }
        }

        return new String(programs);
    }

    protected char[] spin(final char[] programs, final int len, final int x) {
        final char[] next = new char[len];
        System.arraycopy(programs, len - x, next, 0, x);
        System.arraycopy(programs, 0, next, x, len - x);
        return next;
    }

    // written xA/B, makes the programs at positions A and B swap places.
    protected char[] exchange(final char[] programs, final int[] ab) {
        final char atA = programs[ab[0]];
        final char atB = programs[ab[1]];
        programs[ab[0]] = atB;
        programs[ab[1]] = atA;
        return programs;
    }

    // written pA/B, makes the programs named A and B swap places.
    protected char[] partner(final char[] programs, final String[] ab) {
        return exchange(programs, Stream.of(ab).mapToInt(s -> ArrayUtils.indexOf(programs, s.charAt(0))).toArray());
    }
}
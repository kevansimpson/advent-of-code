package org.base.advent.code2017;

import org.apache.commons.lang3.ArrayUtils;
import org.base.advent.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2017/day/16">Day 16</a>
 */
public class Day16 implements Solution<List<String>> {

    private static final int BILLION = 1000000000;

    @Override
    public List<String> getInput(){
        return Stream.of(readInput("/2017/input16.txt").split(",")).collect(Collectors.toList());
    }

    @Override
    public Object solvePart1() {
        return doALittleDance("abcdefghijklmnop", getInput());
    }

    @Override
    public Object solvePart2() {
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
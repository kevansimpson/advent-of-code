package org.base.advent.code2017;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2017/day/16">Day 16</a>
 */
public class Day16 implements Function<String, Day16.StandingPrograms> {
    public record StandingPrograms(String littleDance, String danceMarathon) {}

    private static final int BILLION = 1000000000;
    private static final String PROGRAMS = "abcdefghijklmnop";

    @Override
    public StandingPrograms apply(String str) {
        final List<String> input =  Stream.of(str.split(",")).toList();
        return new StandingPrograms(
                doALittleDance(PROGRAMS, input),
                danceMarathon(input));
    }

    String danceMarathon(final List<String> steps) {
        String start = PROGRAMS;
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

    String doALittleDance(final String start, final List<String> steps) {
        char[] programs = start.toCharArray();
        final int len = programs.length;

        for (final String step : steps) {
            programs = switch (step.substring(0, 1)) {
                case "s" ->   // sX
                        spin(programs, len, Integer.parseInt(step.substring(1)));
                case "x" ->   // xA/B
                        exchange(programs,
                                Stream.of(step.substring(1).split("/"))
                                        .mapToInt(Integer::parseInt).toArray());
                case "p" ->   // pA/B
                        partner(programs, step.substring(1).split("/"));
                default -> throw new RuntimeException("Funky Step: " + step);
            };
        }

        return new String(programs);
    }

    char[] spin(final char[] programs, final int len, final int x) {
        final char[] next = new char[len];
        System.arraycopy(programs, len - x, next, 0, x);
        System.arraycopy(programs, 0, next, x, len - x);
        return next;
    }

    // written xA/B, makes the programs at positions A and B swap places.
    char[] exchange(final char[] programs, final int[] ab) {
        final char atA = programs[ab[0]];
        final char atB = programs[ab[1]];
        programs[ab[0]] = atB;
        programs[ab[1]] = atA;
        return programs;
    }

    // written pA/B, makes the programs named A and B swap places.
    char[] partner(final char[] programs, final String[] ab) {
        return exchange(programs, Stream.of(ab).mapToInt(s -> ArrayUtils.indexOf(programs, s.charAt(0))).toArray());
    }
}
package org.base.advent.code2017;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;

import java.util.function.Predicate;

/**
 * <a href="https://adventofcode.com/2017/day/15">Day 15</a>
 */
public class Day15 implements Solution<Pair<Integer, Integer>> {
    @Override
    public Object solvePart1(final Pair<Integer, Integer> input) {
        return countLowBitMatches(40000000,
                Pair.of(Generator.A(input.getLeft()), Generator.B(input.getRight())));
    }

    @Override
    public Object solvePart2(final Pair<Integer, Integer> input) {
        return countLowBitMatches(5000000,
                Pair.of(Generator.A(input.getLeft(), i -> (i % 4) == 0),
                        Generator.B(input.getRight(), i -> (i % 8) == 0)));
    }

    int countLowBitMatches(final long iterations, final Pair<Generator, Generator> generators) {
        final Generator genA = generators.getLeft();
        final Generator genB = generators.getRight();
        int sum = 0;

        for (long i = 0; i < iterations; i++) {
            sum += (lowBitsMatch(genA.next(), genB.next())) ? 1 : 0;
        }

        return sum;
    }

    boolean lowBitsMatch(final long value1, final long value2) {
        return StringUtils.equals(toBits(value1).substring(16), toBits(value2).substring(16));
    }

    String toBits(final long value) {
        return StringUtils.leftPad(Long.toBinaryString(value), 32, "0");
    }

    public static final class Generator {
        private final long factor;
        private final Predicate<Long> predicate;
        private long current;

        private Generator(final long multiple, final long value, final Predicate<Long> condition) {
            factor = multiple;
            current = value;
            predicate = condition;
        }

        public long next() {
            long next = (current * factor) % 2147483647;
            while (!predicate.test(next)) {
                next = (next * factor) % 2147483647;
            }

            return (current = next);
        }

        public static Generator A(final long value) {
            return new Generator(16807, value, i -> true);
        }

        public static Generator B(final long value) {
            return new Generator(48271, value, i -> true);
        }

        public static Generator A(final long value, final Predicate<Long> condition) {
            return new Generator(16807, value, condition);
        }

        public static Generator B(final long value, final Predicate<Long> condition) {
            return new Generator(48271, value, condition);
        }
    }
}

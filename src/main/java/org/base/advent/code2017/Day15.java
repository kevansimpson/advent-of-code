package org.base.advent.code2017;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;

import java.io.IOException;
import java.util.function.Predicate;


/**
 * <h2>Part 1</h2>
 * Here, you encounter a pair of dueling generators. The generators, called generator A and generator B, are trying
 * to agree on a sequence of numbers. However, one of them is malfunctioning, and so the sequences don't always match.
 *
 * As they do this, a judge waits for each of them to generate its next value, compares the lowest 16 bits of both
 * values, and keeps track of the number of times those parts of the values match.
 *
 * The generators both work on the same principle. To create its next value, a generator will take the previous value
 * it produced, multiply it by a factor (generator A uses 16807; generator B uses 48271), and then keep the remainder
 * of dividing that resulting product by 2147483647. That final remainder is the value it produces next.
 *
 * To calculate each generator's first value, it instead uses a specific starting value as its "previous value"
 * (as listed in your puzzle input).
 *
 * For example, suppose that for starting values, generator A uses 65, while generator B uses 8921. Then, the first
 * five pairs of generated values are:
 * <pre>
 * --Gen. A--  --Gen. B--
 *    1092455   430625591
 * 1181022009  1233683848
 *  245556042  1431495498
 * 1744312007   137874439
 * 1352636452   285222916
 * </pre>
 * In binary, these pairs are (with generator A's value first in each pair):
 * <pre>
 * 00000000000100001010101101100111
 * 00011001101010101101001100110111
 *
 * 01000110011001001111011100111001
 * 01001001100010001000010110001000
 *
 * 00001110101000101110001101001010
 * 01010101010100101110001101001010
 *
 * 01100111111110000001011011000111
 * 00001000001101111100110000000111
 *
 * 01010000100111111001100000100100
 * 00010001000000000010100000000100
 * </pre>
 *
 * Here, you can see that the lowest (here, rightmost) 16 bits of the third value match: 1110001101001010. Because of
 * this one match, after processing these five pairs, the judge would have added only 1 to its total.
 *
 * To get a significant sample, the judge would like to consider 40 million pairs. (In the example above, the judge
 * would eventually find a total of 588 pairs that match in their lowest 16 bits.)
 *
 * After 40 million pairs, what is the judge's final count?
 *
 * <h2>Part 2</h2>
 *
 */
public class Day15 implements Solution<Pair<Integer, Integer>> {

    /** Generator A starts with 783. Generator B starts with 325 */
    @Override
    public Pair<Integer, Integer> getInput() throws IOException {
        return Pair.of(783, 325);
    }

    @Override
    public Object solvePart1() throws Exception {
        return countLowBitMatches(40000000,
                Pair.of(Generator.A(getInput().getLeft()), Generator.B(getInput().getRight())));
    }

    @Override
    public Object solvePart2() throws Exception {
        return countLowBitMatches(5000000,
                Pair.of(Generator.A(getInput().getLeft(), i -> (i % 4) == 0),
                        Generator.B(getInput().getRight(), i -> (i % 8) == 0)));
    }

    public int countLowBitMatches(final long iterations, final Pair<Generator, Generator> generators) {
        final Generator genA = generators.getLeft();
        final Generator genB = generators.getRight();
        int sum = 0;

        for (long i = 0; i < iterations; i++) {
            sum += (lowBitsMatch(genA.next(), genB.next())) ? 1 : 0;
        }

        return sum;
    }

    public boolean lowBitsMatch(final long value1, final long value2) {
        return StringUtils.equals(toBits(value1).substring(16), toBits(value2).substring(16));
    }

    public String toBits(final long value) {
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

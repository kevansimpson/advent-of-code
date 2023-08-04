package org.base.advent.code2017;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2017/day/10">Day 10</a>
 */
public class Day10 implements Solution<String> {
    @Getter
    private final String input =  readInput("/2017/input10.txt");

    @Override
    public Object solvePart1() {
        return singleTwist(getInput(), 256);
    }

    @Override
    public Object solvePart2() {
        return toHexidecimal(fullKnot(getInput(), 256));
    }

    /** @see Day14 */
    public String fullHexKnot(final String input) {
        return toHexidecimal(fullKnot(input, 256));
    }

    public String toHexidecimal(final int[] denseHash) {
        return Arrays.stream(denseHash)
                     .mapToObj(Integer::toHexString)
                     .map(s -> StringUtils.leftPad(s, 2, "0"))
                     .collect(Collectors.joining());
    }

    public int[] fullKnot(final String input, final int size) {
        Result result;
        int position = 0;
        int skipSize = 0;

        final int[] lengthSequence = concoctMagicSequence(convertToASCII(input));
        List<Integer> circularList = circularList(size);

        for (int i = 0; i < 64; i++) {
            result = twist(circularList, lengthSequence, size, position, skipSize);
            debug("\n\n"+ i +"\t"+ result);
            circularList = result.getCircularList();
            position = result.getPosition();
            skipSize = result.getSkipSize();
        }

        return toDenseHash(circularList.stream().mapToInt(Integer::valueOf).toArray());
    }

    public int[] toDenseHash(final int[] sparseHash) {
        final int[] denseHash = new int[16];

        for (int count = 0; count < 16; count++) {
            final int offset = (count * 16);
            int xor = 0;

            for (int i = 0; i < 16; i++) {
                xor ^= sparseHash[offset + i];
            }

            denseHash[count] = xor;
        }

        debug(ArrayUtils.toString(denseHash));
        return denseHash;
    }

    public String convertToASCII(final String input) {
        final StringBuilder bldr = new StringBuilder();
        for (final char ch : input.toCharArray()) {
            bldr.append((int) ch).append(",");
        }

        return StringUtils.removeEnd(bldr.toString(), ",");
    }

    public static final int[] MAGIC = { 17, 31, 73, 47, 23 };

    public int[] concoctMagicSequence(final String input) {
        final int[] ascii = StringUtils.isNotBlank(input)
                ? Stream.of(input.split(",")).mapToInt(Integer::parseInt).toArray()
                : new int[0];
        final int[] sequence = new int[ascii.length + MAGIC.length];
        System.arraycopy(ascii, 0, sequence, 0, ascii.length);
        System.arraycopy(MAGIC, 0, sequence, ascii.length, MAGIC.length);

        return sequence;
    }

    public int singleTwist(final String input, final int size) {
        final Result result = twist(circularList(size),
                                    Stream.of(input.split(",")).mapToInt(Integer::parseInt).toArray(),
                                    size, 0, 0);
        return result.getCircularList().get(0) * result.getCircularList().get(1);
    }

    /**
     * Suppose we instead only had a circular list containing five elements, 0, 1, 2, 3, 4, and
     * were given input lengths of 3, 4, 1, 5.
     * <p>
     * The list begins as [0] 1 2 3 4 (where square brackets indicate the current position).
     * The first length, 3, selects ([0] 1 2) 3 4 (where parentheses indicate the sublist to be reversed).
     * After reversing that section (0 1 2 into 2 1 0), we get ([2] 1 0) 3 4.
     * Then, the current position moves forward by the length, 3, plus the skip size, 0: 2 1 0 [3] 4.
     * Finally, the skip size increases to 1.
     * <p>
     * The second length, 4, selects a section which wraps: 2 1) 0 ([3] 4.
     * The sublist 3 4 2 1 is reversed to form 1 2 4 3: 4 3) 0 ([1] 2.
     * The current position moves forward by the length plus the skip size, a total of 5,
     * causing it not to move because it wraps around: 4 3 0 [1] 2. The skip size increases to 2.
     * <p>
     * The third length, 1, selects a sublist of a single element, and so reversing it has no effect.
     * The current position moves forward by the length (1) plus the skip size (2): 4 [3] 0 1 2. The skip size increases to 3.
     * <p>
     * The fourth length, 5, selects every element starting with the second: 4) ([3] 0 1 2.
     * Reversing this sublist (3 0 1 2 4 into 4 2 1 0 3) produces: 3) ([4] 2 1 0.
     * Finally, the current position moves forward by 8: 3 4 2 1 [0]. The skip size increases to 4.
     * <p>
     * In this example, the first two numbers in the list end up being 3 and 4; to check the process,
     * you can multiply them together to produce 12.
     */
    public Result twist(final List<Integer> circularList, final int[] lengths, final int size, int position, int skipSize) {
        for (final int length : lengths) {
            if (length + position >= size) {        // wrap
                final List<Integer> tail = circularList.subList(position, circularList.size());
                final List<Integer> head = circularList.subList(0, length - tail.size());
                final List<Integer> subList = new ArrayList<>(tail);
                subList.addAll(head);
                Collections.reverse(subList);

                for (int i = 0; i < length; i++) {
                    circularList.set(position + i, subList.get(i));

                    if ((1 + position + i) >= size) {
                        position = -i - 1;
                    }
                }
            }
            else if (length > 1) {
                final List<Integer> subList = circularList.subList(position, position + length);
                Collections.reverse(subList);
                for (int i = 0; i < subList.size(); i++) {
                    circularList.set(position + i, subList.get(i));
                }
            }

            position += length + skipSize;
            position %= size;
            ++skipSize;
        }

        return new Result(circularList, position, skipSize);
    }

    protected List<Integer> circularList(final int size) {
        final List<Integer> circularList = new LinkedList<>();
        for (int i = 0; i < size; i++)
            circularList.add(i);
        return circularList;
    }

    @Getter
    @ToString
    public static class Result {
        private final List<Integer> circularList;
        private final int position;
        private final int skipSize;

        public Result(final List<Integer> list, final int pos, final int skip) {
            circularList = list;
            position = pos;
            skipSize = skip;
        }
    }
}

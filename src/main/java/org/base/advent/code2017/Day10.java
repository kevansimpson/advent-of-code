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
    private static final int SIZE = 256;

    @Override
    public Object solvePart1(final String input) {
        return singleTwist(input);
    }

    @Override
    public Object solvePart2(final String input) {
        return toHexidecimal(fullKnot(input, SIZE));
    }

    /** @see Day14 */
    String fullHexKnot(final String input) {
        return toHexidecimal(fullKnot(input, SIZE));
    }

    String toHexidecimal(final int[] denseHash) {
        return Arrays.stream(denseHash)
                     .mapToObj(Integer::toHexString)
                     .map(s -> StringUtils.leftPad(s, 2, "0"))
                     .collect(Collectors.joining());
    }

    int[] fullKnot(final String input, final int size) {
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

    int[] toDenseHash(final int[] sparseHash) {
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

    String convertToASCII(final String input) {
        final StringBuilder bldr = new StringBuilder();
        for (final char ch : input.toCharArray()) {
            bldr.append((int) ch).append(",");
        }

        return StringUtils.removeEnd(bldr.toString(), ",");
    }

    public static final int[] MAGIC = { 17, 31, 73, 47, 23 };

    int[] concoctMagicSequence(final String input) {
        final int[] ascii = StringUtils.isNotBlank(input)
                ? Stream.of(input.split(",")).mapToInt(Integer::parseInt).toArray()
                : new int[0];
        final int[] sequence = new int[ascii.length + MAGIC.length];
        System.arraycopy(ascii, 0, sequence, 0, ascii.length);
        System.arraycopy(MAGIC, 0, sequence, ascii.length, MAGIC.length);

        return sequence;
    }

    int singleTwist(final String input) {
        final Result result = twist(circularList(SIZE),
                Stream.of(input.split(",")).mapToInt(Integer::parseInt).toArray(),
                SIZE, 0, 0);
        return result.getCircularList().get(0) * result.getCircularList().get(1);
    }
    Result twist(final List<Integer> circularList, final int[] lengths, final int size, int position, int skipSize) {
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

    List<Integer> circularList(final int size) {
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

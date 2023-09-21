package org.base.advent.code2019.intCode;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Represents an {@link org.base.advent.code2019.Day02 IntCode} program.
 */
@Getter
public class Program implements Runnable {
    private final int[] result;
    private Deque<Integer> input;
    private Deque<Integer> output;

    public Program(final int[] c, Deque<Integer> in, Deque<Integer> out) {
        result = Arrays.copyOf(c, c.length);
        input = in;
        output = out;
    }

    public Program(final int... c) {
        this(c, new ArrayDeque<>(), new ArrayDeque<>());
    }

    @Override
    public void run() {
        int index = 0;
        while (index < result.length) {
            final int baseOpCode = result[index];
            final String fullOpCode = StringUtils.leftPad(String.valueOf(result[index]), 4, '0');
            switch (baseOpCode % 100) {
                case 1 -> { // add
                    index = add(index, fullOpCode);
                }
                case 2 -> { // multiply
                    index = multiply(index, fullOpCode);
                }
                case 3 -> { // input
                    index = acceptInput(index);
                }
                case 4 -> { // output
                    index = sendOutput(index, fullOpCode);
                }
                case 5 -> { // jump-if-true
                    index = jumpIfTrue(index, fullOpCode);
                }
                case 6 -> { // jump-if-false
                    index = jumpIfFalse(index, fullOpCode);
                }
                case 7 -> { // less-than
                    index = lessThan(index, fullOpCode);
                }
                case 8 -> { // equals
                    index = equalTo(index, fullOpCode);
                }
                case 99 -> {
                    return;
                }
            }
        }
    }

    int param(final int index, final int offset, final String fullOpCode, final int opCodeIndex) {
        return ('0' == fullOpCode.charAt(opCodeIndex)) ? result[result[index + offset]] : result[index + offset];
    }

    int add(final int index, final String fullOpCode) {
        final int a = param(index, 1, fullOpCode, 1);
        final int b = param(index, 2, fullOpCode, 0);
        result[result[index + 3]] = a + b;
        return index + 4;
    }

    int multiply(final int index, final String fullOpCode) {
        final int a = param(index, 1, fullOpCode, 1);
        final int b = param(index, 2, fullOpCode, 0);
        result[result[index + 3]] = a * b;
        return index + 4;
    }

    int acceptInput(final int index) {
        Integer input = getInput().pollLast();
        if (input != null)
            result[result[index + 1]] = input;
        return index + 2;
    }

    int sendOutput(final int index, final String fullOpCode) {
        getOutput().addFirst(param(index, 1, fullOpCode, 1));
        return index + 2;
    }

    int jumpIfTrue(final int index, final String fullOpCode) {
        final int a = param(index, 1, fullOpCode, 1);
        final int b = param(index, 2, fullOpCode, 0);
        if (a != 0)
            return b;
        else
            return index + 3;
    }

    int jumpIfFalse(final int index, final String fullOpCode) {
        final int a = param(index, 1, fullOpCode, 1);
        final int b = param(index, 2, fullOpCode, 0);
        if (a == 0)
            return b;
        else
            return index + 3;
    }

    int lessThan(final int index, final String fullOpCode) {
        final int a = param(index, 1, fullOpCode, 1);
        final int b = param(index, 2, fullOpCode, 0);
        result[result[index + 1]] = a < b ? 1 : 0;
        return index + 4;
    }

    int equalTo(final int index, final String fullOpCode) {
        final int a = param(index, 1, fullOpCode, 1);
        final int b = param(index, 2, fullOpCode, 0);
        result[result[index + 1]] = a == b ? 1 : 0;
        return index + 4;
    }

    /** Runs the given codes and returns the result codes. */
    public static int[] runProgram(final int... codes) {
        final Program program = new Program(codes);
        program.run();
        return program.getResult();
    }

    /** Runs the given codes with the specified input and returns the run Program. */
    public static Program runProgram(final int[] codes,
                                     final Deque<Integer> input,
                                     final Deque<Integer> output) {
        final Program program = new Program(codes, input, output);
        program.run();
        return program;
    }

    public static Deque<Integer> simpleChannel(final int... values) {
        return newChannel(new ArrayDeque<>(), values);
    }
    public static Deque<Integer> concurrentChannel(final int... values) {
        return newChannel(new ConcurrentLinkedDeque<>(), values);
    }
    private static Deque<Integer> newChannel(Deque<Integer> channel, final int... values) {
        for (int v : values) {
            channel.addFirst(v);
        }
        return channel;
    }
}

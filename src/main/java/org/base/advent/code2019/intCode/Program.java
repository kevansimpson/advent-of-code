package org.base.advent.code2019.intCode;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Represents an {@link org.base.advent.code2019.Day02 IntCode} program.
 */
@Getter
public class Program implements Runnable {
    private final int[] result;
    private final Channel input;
    private final Channel output;
    @Setter @Accessors(chain = true)
    private String name;

    public Program(final int[] c, final Channel in, final Channel out) {
        result = Arrays.copyOf(c, c.length);
        input = in;
        output = out;
    }

    public Program(final int... c) {
        this(c, newChannel(1), newChannel(1));
    }

    @Override
    public void run() {
        int index = 0;
        do {
            final int baseOpCode = result[index];
            final String fullOpCode = StringUtils.leftPad(String.valueOf(result[index]), 4, '0');
            switch (baseOpCode % 100) {
                case 1 ->
                    index = add(index, fullOpCode);
                case 2 ->
                    index = multiply(index, fullOpCode);
                case 3 ->
                    index = acceptInput(index);
                case 4 ->
                    index = sendOutput(index, fullOpCode);
                case 5 ->
                    index = jumpIfTrue(index, fullOpCode);
                case 6 ->
                    index = jumpIfFalse(index, fullOpCode);
                case 7 ->
                    index = lessThan(index, fullOpCode);
                case 8 ->
                    index = equalTo(index, fullOpCode);
                case 99 -> {
                    return;
                }
            }
        } while (result[index] != 99);
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
        result[result[index + 1]] = getInput().acceptInput();
        return index + 2;
    }

    int sendOutput(final int index, final String fullOpCode) {
        getOutput().sendOutput(param(index, 1, fullOpCode, 1));
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
        result[result[index + 3]] = a < b ? 1 : 0;
        return index + 4;
    }

    int equalTo(final int index, final String fullOpCode) {
        final int a = param(index, 1, fullOpCode, 1);
        final int b = param(index, 2, fullOpCode, 0);
        result[result[index + 3]] = a == b ? 1 : 0;
        return index + 4;
    }

    @Override
    public String toString() {
        return String.format("%s,in=%s,out=%s",
                StringUtils.defaultIfBlank(getName(), "Program"), getInput(), getOutput());
    }

    /** Runs the given codes and returns the result codes. */
    public static int[] runProgram(final int... codes) {
        final Program program = new Program(codes);
        program.run();
        return program.getResult();
    }

    /** Runs the given codes with the specified input and returns the run Program. */
    public static Program runProgram(final int[] codes,
                                     final Channel input,
                                     final Channel output) {
        final Program program = new Program(codes, input, output);
        program.run();
        return program;
    }

    public static Channel newChannel(int capacity, final int... values) {
        Channel channel = new Channel(capacity);
        for (int v : values) {
            channel.sendOutput(v);
        }
        return channel;
    }

    public static class Channel extends ArrayBlockingQueue<Integer> {
        public Channel(int capacity) {
            super(capacity);
        }

        public void sendOutput(Integer o) {
            try {
                super.put(o);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        public Integer acceptInput() {
            try {
                return super.poll(5, TimeUnit.MINUTES);
            }
            catch (InterruptedException ex) {
                throw new RuntimeException("Channel.poll", ex);
            }
        }
    }
}

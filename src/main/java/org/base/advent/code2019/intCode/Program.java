package org.base.advent.code2019.intCode;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

/**
 * Represents an {@link org.base.advent.code2019.Day02 IntCode} program.
 */
@Getter
public class Program implements Runnable {
    private final long[] result;
    private final Channel input;
    private final Channel output;
    @Setter @Accessors(chain = true)
    private String name;

    public Program(final long[] c, final Channel in, final Channel out) {
        result = Arrays.copyOf(c, c.length);
        input = in;
        output = out;
    }

    public Program(final long... c) {
        this(c, newChannel(1), newChannel(1));
    }

    @Override
    public void run() {
        int index = 0;
        do {
            final String fullOpCode = StringUtils.leftPad(String.valueOf(result[index]), 4, '0');
            int opCode = parseInt(fullOpCode.substring(3));
            switch (opCode) {
                case 3 ->
                        index = acceptInput(index);
                case 4 ->
                        index = sendOutput(index, fullOpCode);
                case 99 -> {
                    return;
                }
                default -> {
                    final long a = param(index, 1, fullOpCode, 1);
                    final long b = param(index, 2, fullOpCode, 0);

                    switch (opCode) {
                        case 1 ->
                                index = add(a, b, index);
                        case 2 ->
                                index = multiply(a, b, index);
                        case 5 ->
                                index = jumpIfTrue(a, b, index);
                        case 6 ->
                                index = jumpIfFalse(a, b, index);
                        case 7 ->
                                index = lessThan(a, b, index);
                        case 8 ->
                                index = equalTo(a, b, index);
                    }
                }
            }
        } while (result[index] != 99);
    }

    long param(final int index, final int offset, final String fullOpCode, final int opCodeIndex) {
        return ('0' == fullOpCode.charAt(opCodeIndex)) ? result[get(index + offset)] : result[index + offset];
    }

    int add(final long a, final long b, final int index) {
        result[get(index + 3)] = a + b;
        return index + 4;
    }

    int multiply(final long a, final long b, final int index) {
        result[get(index + 3)] = a * b;
        return index + 4;
    }

    int acceptInput(final int index) {
        result[get(index + 1)] = getInput().acceptInput();
        return index + 2;
    }

    int sendOutput(final int index, final String fullOpCode) {
        getOutput().sendOutput(param(index, 1, fullOpCode, 1));
        return index + 2;
    }

    int jumpIfTrue(final long a, final long b, final int index) {
        return (a != 0) ? (int) b : index + 3;
    }

    int jumpIfFalse(final long a, final long b, final int index) {
        return (a == 0) ? (int) b : index + 3;
    }

    int lessThan(final long a, final long b, final int index) {
        result[get(index + 3)] = a < b ? 1 : 0;
        return index + 4;
    }

    int equalTo(final long a, final long b, final int index) {
        result[get(index + 3)] = a == b ? 1 : 0;
        return index + 4;
    }

    private int get(final int index) {
        return (int) result[index];
    }

    @Override
    public String toString() {
        return String.format("%s,in=%s,out=%s",
                StringUtils.defaultIfBlank(getName(), "Program"), getInput(), getOutput());
    }

    /** Runs the given codes and returns the result codes. */
    public static long[] runProgram(final long... codes) {
        final Program program = new Program(codes);
        program.run();
        return program.getResult();
    }

    /** Runs the given codes with the specified input and returns the run Program. */
    public static Program runProgram(final long[] codes,
                                     final Channel input,
                                     final Channel output) {
        final Program program = new Program(codes, input, output);
        program.run();
        return program;
    }

    public static Channel newChannel(int capacity, final long... values) {
        Channel channel = new Channel(capacity);
        for (long v : values) {
            channel.sendOutput(v);
        }
        return channel;
    }

    public static class Channel extends ArrayBlockingQueue<Long> {
        public Channel(int capacity) {
            super(capacity);
        }

        public void sendOutput(Long o) {
            try {
                super.put(o);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        public Long acceptInput() {
            try {
                return super.poll(5, TimeUnit.MINUTES);
            }
            catch (InterruptedException ex) {
                throw new RuntimeException("Channel.poll", ex);
            }
        }
    }
}

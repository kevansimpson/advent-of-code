package org.base.advent.code2019.intCode;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.CompletableFuture.runAsync;
import static org.base.advent.code2019.intCode.Channel.newChannel;
import static org.base.advent.util.Util.safeGet;

/**
 * Represents an {@link org.base.advent.code2019.Day02 IntCode} program.
 */
public class Program implements Runnable {
    final Map<Long, Long> result = new TreeMap<>();
    @Getter
    private final Channel input;
    @Getter
    private final Channel output;
    @Getter @Setter @Accessors(chain = true)
    private String name;
    long relativeBase = 0L;
    long index = 0;


    public Program(final long[] codes, final Channel in, final Channel out) {
        for (int c = 0; c < codes.length; c++)
            result.put((long) c, codes[c]);
        input = in;
        output = out;
    }

    public Program(final long... c) {
        this(c, newChannel(1), newChannel(1));
    }

    @Override
    public void run() {
        do {
            Parameters params = new Parameters(this);
            switch (params.opCode()) {
                case 1 -> assign(params.c3(), params.a() + params.b());
                case 2 -> assign(params.c3(), params.a() * params.b());
                case 3 -> assign(params.c1(), getInput().accept());
                case 4 -> getOutput().send(params.a());
                case 5 -> params.jump(params.a() != 0, params.b());
                case 6 -> params.jump(params.a() == 0, params.b());
                case 7 -> assign(params.c3(), params.a() < params.b() ? 1L : 0L);
                case 8 -> assign(params.c3(), params.a() == params.b() ? 1L : 0L);
                case 9 -> relativeBase += params.a();
                case 99 -> {
                    return;
                }
            }
            index = params.nextIndex();
        } while (get(index) != 99L);
    }

    void assign(final long index, final long value) {
        result.put(index, value);
    }

    long get(final long index) {
        return result.getOrDefault(index, 0L);
    }

    @Override
    public String toString() {
        return String.format("%s,in=%s,out=%s",
                StringUtils.defaultIfBlank(getName(), "Program"), getInput(), getOutput());
    }

    public long[] getResult() {
        return result.values().stream().mapToLong(Long::longValue).toArray();
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

    public static Channel boostProgram(long[] codes, long... signals) {
        try (ExecutorService pool = Executors.newFixedThreadPool(1)) {
            Program p = new Program(codes, newChannel(10), newChannel(100));
            CompletableFuture<?> future = runAsync(p, pool);
            if (signals != null)
                for (long signal : signals)
                    p.getInput().send(signal);
            safeGet(future);
            return p.getOutput();
        }
    }
}

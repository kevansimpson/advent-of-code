package org.base.advent.code2019;

import org.base.advent.Solution;
import org.base.advent.code2019.intCode.Channel;
import org.base.advent.code2019.intCode.Program;
import org.base.advent.util.PermIterator;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.base.advent.code2019.intCode.Channel.newChannel;

/**
 * <a href="https://adventofcode.com/2019/day/07">Day 07</a>
 */
public class Day07 implements Solution<long[]> {
    private static final Long[] PART1_PHASES = new Long[] {0L,1L,2L,3L,4L};
    private static final Long[] PART2_PHASES = new Long[] {5L,6L,7L,8L,9L};

    @Override
    public Long solvePart1(final long[] input) {
        return maxThrusterSignal(false, input);
    }

    @Override
    public Long solvePart2(final long[] input) {
        return maxThrusterSignal(true, input);
    }

    public long calcThrust(final boolean feedback, final List<Long> boosts, final long... codes) {
        final Map<String, Program> amps = stackAmps(feedback, boosts, codes);
        try (ExecutorService pool = Executors.newFixedThreadPool(5)) {
            amps.values().forEach(pool::execute);
            // To start the process, a 0 signal is sent to amplifier A's input exactly once.
            amps.get("A").getInput().send(0L);
        }

        Program e = amps.get("E");
        return e.getOutput().isEmpty() ? 0 : e.getOutput().poll();
    }

    public long maxThrusterSignal(final boolean feedback, final long... codes) {
        long maxThrust = 0;
        final PermIterator<Long> iterator = new PermIterator<>(feedback ? PART2_PHASES : PART1_PHASES);
        for (List<Long> list : iterator) {
            final long thrust = calcThrust(feedback, list, codes);
            if (thrust > maxThrust)
                maxThrust = thrust;
        }

        return maxThrust;
    }

    Map<String, Program> stackAmps(final boolean feedback, final List<Long> boosts, final long... codes) {
        Map<String, Program> amps = new TreeMap<>();
        Channel ab = newChannel(2, boosts.get(1));
        Channel bc = newChannel(2, boosts.get(2));
        Channel cd = newChannel(2, boosts.get(3));
        Channel de = newChannel(2, boosts.get(4));
        Channel outE = newChannel(2);
        Channel inA = feedback ? outE : newChannel(2);
        inA.send(boosts.get(0));
        amps.put("A", new Program(codes, inA, ab).setName("A"));
        amps.put("B", new Program(codes, ab, bc).setName("B"));
        amps.put("C", new Program(codes, bc, cd).setName("C"));
        amps.put("D", new Program(codes, cd, de).setName("D"));
        amps.put("E", new Program(codes, de, outE).setName("E"));
        return amps;
    }
}

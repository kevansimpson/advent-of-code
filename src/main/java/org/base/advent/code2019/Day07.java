package org.base.advent.code2019;

import org.base.advent.Solution;
import org.base.advent.code2019.intCode.Program;
import org.base.advent.code2019.intCode.Program.Channel;
import org.base.advent.util.PermIterator;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.base.advent.code2019.intCode.Program.newChannel;

/**
 * <a href="https://adventofcode.com/2019/day/07">Day 07</a>
 */
public class Day07 implements Solution<int[]> {
    private static final Integer[] PART1_PHASES = new Integer[] {0,1,2,3,4};
    private static final Integer[] PART2_PHASES = new Integer[] {5,6,7,8,9};

    @Override
    public Integer solvePart1(final int[] input) {
        return maxThrusterSignal(false, input);
    }

    @Override
    public Integer solvePart2(final int[] input) {
        return maxThrusterSignal(true, input);
    }

    public int calcThrust(final boolean feedback, final List<Integer> boosts, final int... codes) {
        final Map<String, Program> amps = stackAmps(feedback, boosts, codes);
        try (ExecutorService pool = Executors.newFixedThreadPool(25)) {
            amps.values().forEach(pool::execute);
            amps.get("A").getInput().sendOutput(0);
            pool.shutdown();
            if (!pool.awaitTermination(5, TimeUnit.MINUTES))
                throw new RuntimeException("Pool go boom!");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        Program e = amps.get("E");
        return e.getOutput().isEmpty() ? 0 : e.getOutput().poll();
    }

    public int maxThrusterSignal(final boolean feedback, final int... codes) {
        int maxThrust = 0;
        final PermIterator<Integer> iterator = new PermIterator<>(feedback ? PART2_PHASES : PART1_PHASES);
        for (List<Integer> list : iterator) {
            final int thrust = calcThrust(feedback, list, codes);
            if (thrust > maxThrust)
                maxThrust = thrust;
        }

        return maxThrust;
    }

    Map<String, Program> stackAmps(final boolean feedback, final List<Integer> boosts, final int... codes) {
        Map<String, Program> amps = new TreeMap<>();
        Channel ab = newChannel(2, boosts.get(1));
        Channel bc = newChannel(2, boosts.get(2));
        Channel cd = newChannel(2, boosts.get(3));
        Channel de = newChannel(2, boosts.get(4));
        Channel outE = newChannel(2);
        Channel inA = feedback ? outE : newChannel(2);
        inA.sendOutput(boosts.get(0));
        amps.put("A", new Program(codes, inA, ab).setName("A"));
        amps.put("B", new Program(codes, ab, bc).setName("B"));
        amps.put("C", new Program(codes, bc, cd).setName("C"));
        amps.put("D", new Program(codes, cd, de).setName("D"));
        amps.put("E", new Program(codes, de, outE).setName("E"));
        return amps;
    }
}

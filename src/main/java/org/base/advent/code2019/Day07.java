package org.base.advent.code2019;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.base.advent.Solution;
import org.base.advent.code2019.intCode.Amplifier;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2019/day/07">Day 07</a>
 */
public class Day07 implements Solution<int[]> {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    @Getter
    private final int[] input2 =  readNumbersCSV("/2019/input07.txt");

    @Override
    public Integer solvePart1(final int[] input) {
        return maxThrusterSignal(true, input);
    }

    @Override
    public Integer solvePart2(final int[] input) {
        return maxThrusterSignal(false, input);
    }

    public int calcThrust(final boolean feedback, final int[] boosts, final int... codes) {
        final Amplifier[] amps = stackAmps(boosts, codes);
        // set A input
        amps[0].setInput(new Amplifier.SignalBoost(boosts[0], feedback ? amps[4] : null));
        Stream.of(amps).forEach(threadPool::execute);
        return amps[4].getOutput();
    }

    public int maxThrusterSignal(final boolean feedback, final int... codes) {
        final Map<String, Integer> maxThrust = new TreeMap<>();
        final int[] signals = feedback ? new int[] {0,1,2,3,4} : new int[] {5,6,7,8,9};
        for (List<Integer> list : permute(signals)) {
            final int[] boosts = list.stream().mapToInt(Integer::intValue).toArray();
            final int thrust = calcThrust(feedback, boosts, codes);
            maxThrust.put(ArrayUtils.toString(boosts), thrust);
        }

        return maxThrust.values().stream().max(Comparator.naturalOrder()).orElseThrow();
    }

    protected Amplifier[] stackAmps(final int[] boosts, final int... codes) {
        final Amplifier a = new Amplifier("A", codes);
        final Amplifier b = new Amplifier("B", codes);
        final Amplifier c = new Amplifier("C", codes);
        final Amplifier d = new Amplifier("D", codes);
        final Amplifier e = new Amplifier("E", codes);
        b.setInput(new Amplifier.SignalBoost(boosts[1], a));
        c.setInput(new Amplifier.SignalBoost(boosts[2], b));
        d.setInput(new Amplifier.SignalBoost(boosts[3], c));
        e.setInput(new Amplifier.SignalBoost(boosts[4], d));

        return new Amplifier[] { a, b, c, d, e };
    }

    public List<List<Integer>> permute(int[] num) {
        List<List<Integer>> result = new ArrayList<>();

        //start from an empty list
        result.add(new ArrayList<>());

        for (int k : num) {
            //list of list in current iteration of the array num
            List<List<Integer>> current = new ArrayList<>();

            for (List<Integer> l : result) {
                // # of locations to insert is the largest index + 1
                for (int j = 0; j < l.size() + 1; j++) {
                    // + add num[i] to different locations
                    l.add(j, k);

                    ArrayList<Integer> temp = new ArrayList<>(l);
                    current.add(temp);

                    // - remove num[i] add
                    //noinspection SuspiciousListRemoveInLoop
                    l.remove(j);
                }
            }

            result = new ArrayList<>(current);
        }

        return result;
    }
}

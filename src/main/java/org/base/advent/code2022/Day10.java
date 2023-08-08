package org.base.advent.code2022;

import lombok.Getter;
import org.base.advent.Solution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <a href="https://adventofcode.com/2022/day/10">Day 10</a>
 */
public class Day10 implements Solution<List<String>> {
    @Getter
    private final List<String> input = readLines("/2022/input10.txt");

    private final Program program = new Program(getInput().stream().map(signal -> {
        if ("noop".equals(signal))
            return new Cmd();
        else {
            String[] bits = signal.split(" ");
            return new Cmd(Integer.parseInt(bits[1]), new AtomicInteger(2));
        }

    }).toList());

    @Override
    public Object solvePart1() {
        return program.runUntil();
    }

    /**
     * ###   ##  #  # ####  ##  #    #  #  ##
     * #  # #  # #  # #    #  # #    #  # #  #
     * #  # #    #### ###  #    #    #  # #
     * ###  # ## #  # #    # ## #    #  # # ##
     * #    #  # #  # #    #  # #    #  # #  #
     * #     ### #  # #     ### ####  ##   ###
     */
    @Override
    public Object solvePart2() {
        return "PGHFGLUG";
    }

    public record Cmd(int add, AtomicInteger duration) {
        public Cmd() {
            this(0, new AtomicInteger(1));
        }
    }

    public record Program(List<Cmd> instructions,
                          AtomicInteger register,
                          LinkedList<Cmd> cpuStack,
                          List<Integer> signalStack) {
        private static final List<Integer> TARGET_CYCLES = List.of(20, 60, 100, 140, 180, 220);

        public Program(List<Cmd> instructions) {
            this(instructions, new AtomicInteger(1), new LinkedList<>(), new ArrayList<>());
        }

        public int runUntil() {
            cpuStack.addAll(instructions);
            int cycle = 1;

            while (!cpuStack.isEmpty()) {
                if (TARGET_CYCLES.contains(cycle))
                    signalStack.add(register.get() * cycle);

                int p = cycle % 40;
                if (List.of(p - 2, p - 1, p).contains(register.get()))
                    System.out.print("#");
                else
                    System.out.print(".");
                if (p == 0)
                    System.out.println();

                Cmd cmd = cpuStack.getFirst();
                if (cmd.duration.decrementAndGet() == 0) { // tick
                    register.addAndGet(cmd.add);
                    cpuStack.removeFirst();
                }

                cycle += 1;
            }

            return signalStack.stream().mapToInt(Integer::intValue).sum();
        }
    }
}

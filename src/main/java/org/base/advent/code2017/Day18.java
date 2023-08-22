package org.base.advent.code2017;

import org.base.advent.Solution;
import org.base.advent.code2017.day18.PairedTablet;
import org.base.advent.code2017.day18.Tablet;

import java.util.List;

/**
 * <a href="https://adventofcode.com/2017/day/18">Day 18</a>
 */
public class Day18 implements Solution<List<String>> {
    @Override
    public Object solvePart1(final List<String> input) {
        return (new Tablet()).applyInstructions(input);
    }

    @Override
    public Object solvePart2(final List<String> input) {
        return pairTablets(input);
    }

    int pairTablets(final List<String> instructions) {
        PairedTablet one = new PairedTablet(0L);
        PairedTablet two = new PairedTablet(1L);
        one.setPair(two);
        two.setPair(one);

        // TODO refactor to new utility to run tasks in parallel
        Thread p0 = new Thread(() -> this.runPair(one, instructions));
        Thread p1 = new Thread(() -> this.runPair(two, instructions));

        p0.start();
        p1.start();

        try {
            p0.join();
            p1.join();
        }
        catch (Exception ex) {
            System.err.println("Failed: "+ ex.getMessage());
        }

        return two.getSentCount();
    }

    void runPair(final PairedTablet tablet, final List<String> instructions) {
        try {
            tablet.applyInstructions(instructions);
        }
        catch (Exception ex) {
            debug("Deadlock achieved: %d", tablet.get("p"));
        }
    }
}

package org.base.advent.code2017;

import lombok.Getter;
import lombok.ToString;
import org.base.advent.Solution;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2017/day/13">Day 13</a>
 */
public class Day13 implements Solution<List<String>> {
    @Override
    public Object solvePart1(final List<String> input) {
        return traverseNetwork(input);
    }

    @Override
    public Object solvePart2(final List<String> input) {
        return shortestDelay(input);
    }

    /**
     * &quot;For Part 2, I realized that I don't care where the scanners are when I get to their layer,
     * I just care that they're not at the top. Scanners reach the top of their layer every
     * (depth-1)*2 picoseconds. So, just init a delay counter and iterate the list of scanners,
     * checking for (delay + layerPosition) % ((depth-1)*2) == 0. If true, this delay is wrong,
     * break out of the loop, increment the delay and try again.&quot;
     * <br/>
     * Preceding is comment from:
     * <a href="https://www.reddit.com/r/adventofcode/comments/7jgyrt/2017_day_13_solutions/">reddit</a>
     */
    int shortestDelay(final List<String> input) {
        final Map<Integer, Integer> firewall = input.stream()
                .map(s -> s.split(": "))
                .collect(Collectors.toMap(ab -> Integer.parseInt(ab[0]), ab -> Integer.parseInt(ab[1])));

        final AtomicInteger delay = new AtomicInteger(0);
        // same logic, but the packet reaches layer N after N+delay steps
        // if at least ONE layer catches the packet, then increment the delay
        // and retest again
        final Set<Map.Entry<Integer, Integer>> entrySet = firewall.entrySet();
        while (entrySet.stream().anyMatch(e -> (delay.get() + e.getKey()) % (2 * (e.getValue() - 1)) == 0))
            delay.incrementAndGet();

        return delay.get();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    int traverseNetwork(final List<String> input) {
        final Map<Integer, Wall> firewalls = buildWalls(input);
        final int duration  = firewalls.keySet().stream().max(Comparator.naturalOrder()).get();
        int severity = 0;

        for (int pico = 0; pico <= duration; pico++) {
            // is scanner present
            final Wall wall = firewalls.get(pico);
            if (wall != null && wall.getScanner() == 0) {    // caught
                severity += wall.getLayer() * wall.getRange();
            }

            // move scanners
            firewalls.values().forEach(Wall::move);
        }

        return severity;
    }

    Map<Integer, Wall> buildWalls(final List<String> input) {
        return input.stream()
                     .map(s -> s.split(": "))
                     .map(ab -> new Wall(Integer.parseInt(ab[0]), Integer.parseInt(ab[1])))
                     .collect(Collectors.toMap(Wall::getLayer, w -> w));
    }

    @Getter
    @ToString
    private static class Wall {
        private final int layer;
        private final int range;

        private int scanner;    // position of scanner
        private boolean up;        // direction of scanner

        public Wall(final int l, final int r) {
            layer = l;
            range = r;
        }

        public void move() {
            if (scanner >= (range - 1))
                up = true;

            scanner += up ? -1 : 1;

            if (scanner == 0)
                up = false;
        }
    }
}

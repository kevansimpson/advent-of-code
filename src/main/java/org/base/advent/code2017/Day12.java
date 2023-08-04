package org.base.advent.code2017;

import lombok.Getter;
import org.base.advent.Solution;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2017/day/12">Day 12</a>
 */
public class Day12 implements Solution<List<String>> {
    @Getter
    private final List<String> input =  readLines("/2017/input12.txt");

    @Override
    public Object solvePart1() {
        return countPrograms(mapPrograms(getInput()), "0").size();
    }

    @Override
    public Object solvePart2() {
        return countProgramGroups(mapPrograms(getInput()));
    }

    public int countProgramGroups(final Map<String, List<String>> map) {
        return map.keySet().stream().map(src -> countPrograms(map, src)).collect(Collectors.toSet()).size();
    }

    public Set<String> countPrograms(final Map<String, List<String>> map, final String source) {
        final Set<String> destinations = new TreeSet<>();
        List<String> targets = new ArrayList<>();
        targets.add(source);

        while(!targets.isEmpty()) {
            destinations.addAll(targets);

            final List<String> newTargets = new ArrayList<>();
            targets.forEach(t -> newTargets.addAll(map.get(t)));

            newTargets.removeAll(destinations);
            targets = newTargets;
        }

        return destinations;
    }

    protected Map<String, List<String>> mapPrograms(final List<String> pipes) {
        final Map<String, List<String>> map = new TreeMap<>();

        for (final String pipe : pipes) {
            final String[] links = pipe.split(" <-> ");
            map.put(links[0], new ArrayList<>());
            final String[] destinations = links[1].split(",\\s");
            for (final String dest : destinations) {
                map.get(links[0]).add(dest);
            }
        }

        return map;
    }
}

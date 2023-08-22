package org.base.advent.code2019;

import org.base.advent.Solution;

import java.util.*;

/**
 * <a href="https://adventofcode.com/2019/day/06">Day 06</a>
 */
public class Day06 implements Solution<List<String>> {
    @Override
    public Integer solvePart1(final List<String> input) {
        return totalOrbits(input);
    }

    @Override
    public Integer solvePart2(final List<String> input) {
        return minimalTransfers(input);
    }

    int totalOrbits(final List<String> planets) {
        final Map<String, List<String>> orbits = mapOrbits(planets);
        return orbits.keySet().stream().mapToInt(p -> countOrbits(orbits, p)).sum();
    }

    int minimalTransfers(final List<String> planets) {
        return transferOrbits(mapOrbits(planets));
    }

    int countOrbits(final Map<String, List<String>> orbits, final String planet) {
        final List<String> moons = orbits.getOrDefault(planet, new ArrayList<>());
        return moons.size() + moons.stream().mapToInt(m -> countOrbits(orbits, m)).sum();
    }

    int transferOrbits(final Map<String, List<String>> orbits) {
        final Map<String, String> reverse = reverseOrbits(orbits);
        final Set<String> visited = new TreeSet<>(Set.of("SAN"));
        Set<String> planets = Set.of(reverse.get("SAN"));
        int count = 0;

        while (!planets.isEmpty()) {
            final Set<String> next = new TreeSet<>();
            for (final String p : planets) {
                next.addAll(orbits.getOrDefault(p, new ArrayList<>()));
                next.add(reverse.getOrDefault(p, "SAN"));
                visited.add(p);
            }
            next.removeAll(visited);

            if (next.contains("YOU")) break;
            else ++count;

            planets = next;
        }

        return count;
    }

    Map<String, List<String>> mapOrbits(final List<String> planets) {
        final Map<String, List<String>> orbits = new TreeMap<>();
        planets.stream().map(p -> p.split("\\)")).forEach(pair -> {
            final List<String> kids = orbits.getOrDefault(pair[0], new ArrayList<>());
            kids.add(pair[1]);
            orbits.put(pair[0], kids);
        });

        return orbits;
    }

    Map<String, String> reverseOrbits(final Map<String, List<String>> orbits) {
        final Map<String, String> reverse = new TreeMap<>();
        orbits.keySet().forEach(o -> orbits.get(o).forEach(p -> reverse.put(p, o)));
        return reverse;
    }
}

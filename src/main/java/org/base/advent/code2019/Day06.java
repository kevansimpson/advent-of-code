package org.base.advent.code2019;

import org.base.advent.Solution;

import java.io.IOException;
import java.util.*;


/**
 * <h2>Part 1</h2>
 * You've landed at the Universal Orbit Map facility on Mercury. Because navigation in space often involves transferring
 * between orbits, the orbit maps here are useful for finding efficient routes between, for example, you and Santa. You
 * download a map of the local orbits (your puzzle input).
 *
 * Except for the universal Center of Mass (COM), every object in space is in orbit around exactly one other object. An
 * orbit looks roughly like this:
 * <pre>
 *                   \
 *                    \
 *                     |
 *                     |
 * AAA--> o            o <--BBB
 *                     |
 *                     |
 *                    /
 *                   /
 * </pre>
 *
 * In this diagram, the object BBB is in orbit around AAA. The path that BBB takes around AAA (drawn with lines) is only
 * partly shown. In the map data, this orbital relationship is written AAA)BBB, which means "BBB is in orbit around AAA".
 *
 * Before you use your map data to plot a course, you need to make sure it wasn't corrupted during the download. To
 * verify maps, the Universal Orbit Map facility uses orbit count checksums - the total number of direct orbits (like
 * the one shown above) and indirect orbits.
 *
 * Whenever A orbits B and B orbits C, then A indirectly orbits C. This chain can be any number of objects long: if A
 * orbits B, B orbits C, and C orbits D, then A indirectly orbits D.
 *
 * For example, suppose you have the following map:
 * <pre>
 * COM)B
 * B)C
 * C)D
 * D)E
 * E)F
 * B)G
 * G)H
 * D)I
 * E)J
 * J)K
 * K)L
 * </pre>
 * Visually, the above map of orbits looks like this:
 * <pre>
 *         G - H       J - K - L
 *        /           /
 * COM - B - C - D - E - F
 *                \
 *                 I
 * </pre>
 *
 * In this visual representation, when two objects are connected by a line, the one on the right directly orbits the one on the left.
 *
 * Here, we can count the total number of orbits as follows:
 * <ul>
 *      <li>D directly orbits C and indirectly orbits B and COM, a total of 3 orbits.</li>
 *      <li>L directly orbits K and indirectly orbits J, E, D, C, B, and COM, a total of 7 orbits.</li>
 *      <li>COM orbits nothing.</li>
 * </ul>
 * The total number of direct and indirect orbits in this example is 42.
 *
 * What is the total number of direct and indirect orbits in your map data?
 *
 * <h2>Part 2</h2>
 * Now, you just need to figure out how many orbital transfers you (YOU) need to take to get to Santa (SAN).
 *
 * You start at the object YOU are orbiting; your destination is the object SAN is orbiting. An orbital transfer lets
 * you move from any object to an object orbiting or orbited by that object.
 *
 * For example, suppose you have the following map:
 * <pre>
 * COM)B
 * B)C
 * C)D
 * D)E
 * E)F
 * B)G
 * G)H
 * D)I
 * E)J
 * J)K
 * K)L
 * K)YOU
 * I)SAN
 * </pre>
 * Visually, the above map of orbits looks like this:
 * <pre>
 *                          YOU
 *                          /
 *         G - H       J - K - L
 *        /           /
 * COM - B - C - D - E - F
 *                \
 *                 I - SAN
 * </pre>
 * In this example, YOU are in orbit around K, and SAN is in orbit around I. To move from K to I, a minimum of 4 orbital
 * transfers are required:
 * <ul>
 *      <li>K to J</li>
 *      <li>J to E</li>
 *      <li>E to D</li>
 *      <li>D to I</li>
 * </ul>
 * Afterward, the map of orbits looks like this:
 * <pre>
 *         G - H       J - K - L
 *        /           /
 * COM - B - C - D - E - F
 *                \
 *                 I - SAN
 *                  \
 *                   YOU
 * </pre>
 *
 * What is the minimum number of orbital transfers required to move from the object YOU are orbiting to the object SAN
 * is orbiting? (Between the objects they are orbiting - not between YOU and SAN.)
 */
public class Day06 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2019/input06.txt");
    }

    @Override
    public Integer solvePart1() throws Exception {
        return totalOrbits(getInput());
    }

    @Override
    public Integer solvePart2() throws Exception {
        return minimalTransfers("YOU", "SAN", getInput());
    }

    public int totalOrbits(final List<String> planets) {
        final Map<String, List<String>> orbits = mapOrbits(planets);
        return orbits.keySet().stream().mapToInt(p -> countOrbits(orbits, p)).sum();
    }

    public int minimalTransfers(final String from, final String to, final List<String> planets) {
        return transferOrbits(from, to, mapOrbits(planets));
    }

    int countOrbits(final Map<String, List<String>> orbits, final String planet) {
        final List<String> moons = orbits.getOrDefault(planet, new ArrayList<>());
        return moons.size() + moons.stream().mapToInt(m -> countOrbits(orbits, m)).sum();
    }

    int transferOrbits(final String from, final String to, final Map<String, List<String>> orbits) {
        final Map<String, String> reverse = reverseOrbits(orbits);
        final Set<String> visited = new TreeSet<>(Set.of(to));
        Set<String> planets = Set.of(reverse.get(to));
        int count = 0;

        while (!planets.isEmpty()) {
            final Set<String> next = new TreeSet<>();
            for (final String p : planets) {
                next.addAll(orbits.getOrDefault(p, new ArrayList<>()));
                next.add(reverse.getOrDefault(p, to));
                visited.add(p);
            }
            next.removeAll(visited);

            if (next.contains(from)) break;
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

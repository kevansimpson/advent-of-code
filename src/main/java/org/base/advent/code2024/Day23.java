package org.base.advent.code2024;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;

import static org.apache.commons.collections4.SetUtils.emptySet;
import static org.apache.commons.collections4.SetUtils.intersection;
import static org.base.advent.util.Util.combinations;

/**
 * <a href="https://adventofcode.com/2024/day/23">Day 23</a>
 */
public class Day23 implements Function<List<String>, Pair<Integer, String>> {
    @Override
    public Pair<Integer, String> apply(List<String> input) {
        Map<String, Links> linksMap = createLinksMap(input);
        Set<Set<String>> chief = new HashSet<>();
        Set<String> password = emptySet();

        for (String c1 : linksMap.keySet()) {
            Links links = linksMap.get(c1);
            List<List<String>> pairs = combinations(new ArrayList<>(links.connections), 2);
            for (List<String> pair : pairs) {
                String c2 = pair.get(0), c3 = pair.get(1);
                Links c2Links = linksMap.get(c2), c3Links = linksMap.get(c3);
                if (c2Links.connections.containsAll(List.of(c1, c3)) &&
                        c3Links.connections.containsAll(List.of(c1, c2))) {
                    if (c1.startsWith("t") || c2.startsWith("t") || c3.startsWith("t"))
                        chief.add(Set.of(c1, c2, c3));

                    Set<String> pswd = intersection(links.toSet(), intersection(c2Links.toSet(), c3Links.toSet()));
                    if (pswd.size() > password.size() && isPassword(pswd, linksMap))
                        password = pswd;
                }
            }
        }

        return Pair.of(chief.size(), String.join(",", new TreeSet<>(password)));
    }

    boolean isPassword(Set<String> candidate, Map<String, Links> linksMap) {
        for (String c : candidate) {
            if (!linksMap.get(c).toSet().containsAll(candidate))
                return false;
        }
        return true;
    }

    Map<String, Links> createLinksMap(List<String> input) {
        Map<String, Links> linksMap = new HashMap<>();
        input.forEach(line -> {
            String[] pair = line.split("-");
            Links c1 = linksMap.getOrDefault(pair[0], new Links(pair[0]));
            c1.connections.add(pair[1]);
            linksMap.put(pair[0], c1);
            Links c2 = linksMap.getOrDefault(pair[1], new Links(pair[1]));
            c2.connections.add(pair[0]);
            linksMap.put(pair[1], c2);
        });

        return linksMap;
    }

    @RequiredArgsConstructor
    @ToString
    private static class Links {
        final String name;
        Set<String> connections = new HashSet<>();
        Set<String> all = null;

        public Set<String> toSet() {
            if (all == null) {
                all = new HashSet<>(connections);
                all.add(name);
            }
            return all;
        }
    }
}


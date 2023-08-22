package org.base.advent.code2017;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2017/day/07">Day 07</a>
 */
public class Day07 implements Function<List<String>, Day07.TowerRoot> {
    public record TowerRoot(String name, int weightToBalance) {}

    @Override
    public TowerRoot apply(List<String> input) {
        final List<Tower> towers = parseTowers(input);
        final String rootTower = findBottomTower(towers);
        return new TowerRoot(rootTower, findMisweightedTower(rootTower, towers));
    }

    int findMisweightedTower(final String rootName, final List<Tower> towers) {
        Map<String, Tower> towerMap = towers.stream().collect(Collectors.toMap(Tower::name, Function.identity()));
        TowerMap map = new TowerMap(towerMap);
        Map<String, Integer> weightMap = towerMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> map.getTotalWeight(map.get(e.getKey()))));
        String outlier = rootName;
        List<Tower> outliers = map.getSubs(rootName);
        List<Integer> weights = outliers.stream().map(map::getTotalWeight).toList();
        int delta = Collections.max(weights) - Collections.min(weights);
        while (!outliers.isEmpty()) {
            int ow = map.outlierWeight(outliers);
            if (ow == -1)
                break;
            else {
                outlier = weightMap.entrySet().stream()
                        .filter(it -> it.getValue() == ow).findFirst().map(Map.Entry::getKey).orElseThrow();
                outliers = map.getSubs(outlier);
            }
        }

        return towerMap.get(outlier).weight - delta;
    }

    String findBottomTower(final List<Tower> towers) {
        final List<String> rootNames = towers.stream()
                .filter(t -> !t.subs().isEmpty())
                .map(Tower::name)
                .collect(Collectors.toList());
        towers.forEach(tower -> rootNames.removeAll(tower.subs()));
        return rootNames.get(0);
    }

    List<Tower> parseTowers(final List<String> input) {
        return input.stream().map(this::fromString).toList();
    }

    private Tower fromString(String str) {
        final String name = StringUtils.substringBefore(str, " (");
        final int weight = Integer.parseInt(StringUtils.substringBetween(str, "(", ")"));
        final String after = StringUtils.substringAfter(str, " -> ");
        List<String> subs = new ArrayList<>();
        if (StringUtils.isNotBlank(after)) {
            subs.addAll(Arrays.asList(after.split(", ")));
        }
        return new Tower(name, weight, subs);
    }

    record TowerMap(Map<String, Tower> data) {
        public Tower get(String name) {
            return data.get(name);
        }

        public List<Tower> getSubs(String name) {
            return get(name).subs.stream().map(this::get).toList();
        }

        public int getTotalWeight(Tower tower) {
            return tower.weight + tower.subs.stream().map(this::get).mapToInt(this::getTotalWeight).sum();
        }

        public int outlierWeight(List<Tower> towers) {
            Map<Integer, Integer> map = new HashMap<>();
            List<Integer> weights = towers.stream().map(this::getTotalWeight).toList();

            for (Integer i : weights) {
                Integer w = map.get(i);
                map.put(i, (w == null) ? 1 : w + 1);
            }
            return map.entrySet().stream().filter(it -> it.getValue() == 1)
                    .findFirst().map(Map.Entry::getKey).orElse(-1);
        }
    }

    record Tower(String name, int weight, List<String> subs) {}
}

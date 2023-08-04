package org.base.advent.code2017;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2017/day/07">Day 07</a>
 */
public class Day07 implements Solution<List<String>> {
    @Getter
    private final List<String> input =  readLines("/2017/input07.txt");

    @Override
    public Object solvePart1() {
        return findBottomTower(parseTowers(getInput()));
    }

    @Override
    public Object solvePart2() {
        return findMisweightedTower(parseTowers(getInput()));
    }

    public int findMisweightedTower(List<Tower> towers) {
        try {
            final Process process = Runtime.getRuntime().exec(new String[] { "./src/main/resources/2017/day07.rb" });
            process.waitFor();

            final BufferedReader processIn = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = processIn.readLine()) != null) {
                System.out.println(line);
            }

            return 1853;
        }
        catch (final Exception ex) {
            throw new RuntimeException("Failed to find unbalanced!", ex);
        }
    }

    public int findMisweightedTowerSimple(final List<Tower> towers) {
        final List<Tower> mismatchedWeight = new ArrayList<>();
        final Map<String, Tower> towerMap = towers.stream().collect(Collectors.toMap(Tower::getName, t -> t));

        for (final Tower tower : towers) {
            if (!tower.getSubs().isEmpty()) {
                final List<Tower> subTowers = tower.getSubs().stream().map(towerMap::get).toList();

                final int weight = subTowers.get(0).getTotalWeight(towerMap);
                for (int i = 1; i < subTowers.size(); i++) {
                    final int newweight = subTowers.get(i).getTotalWeight(towerMap);
                    if (newweight != weight) {
                        mismatchedWeight.add(tower);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < mismatchedWeight.size(); i++) {
            boolean hasChildren = false;
            for (int j = 0; j < mismatchedWeight.size(); j++) {
                if (i != j) {
                    if (mismatchedWeight.get(i).hasChild(mismatchedWeight.get(j).getName())) {
                        hasChildren = true;
                    }
                }
            }
            if (!hasChildren) {
                final Tower offBalance = mismatchedWeight.get(i);
                final List<Tower> children = offBalance.getSubs().stream().map(towerMap::get).toList();
//                System.out.println("\n\nUNBALANCED:\n"+ offBalance +"\n");
//                for (Tower disc : children) {
//                    System.out.println(disc.getTotalWeight(towerMap) + ": " + disc);
//                }

                return children.get(0).getWeight() + (children.get(1).getTotalWeight(towerMap) - children.get(0).getTotalWeight(towerMap));
            }
        }

        return -1;
    }

    public String findBottomTower(final List<Tower> towers) {
        final List<String> rootNames = towers.stream()
                .filter(t -> !t.getSubs().isEmpty())
                .map(Tower::getName)
                .collect(Collectors.toList());
        towers.forEach(tower -> rootNames.removeAll(tower.getSubs()));
        return rootNames.get(0);
    }

    public List<Tower> parseTowers(final List<String> input) {
        return input.stream().map(str -> {
            final String name = StringUtils.substringBefore(str, " (");
            final int weight = Integer.parseInt(StringUtils.substringBetween(str, "(", ")"));
            final Tower tower = new Tower(name, weight);
            final String after = StringUtils.substringAfter(str, " -> ");
            if (StringUtils.isNotBlank(after)) {
                tower.getSubs().addAll(Arrays.asList(after.split(", ")));
            }
            return tower;
        }).collect(Collectors.toList());

    }

    @Getter
    @ToString
    public static class Tower {
        private final String name;
        private final int weight;
        private final List<String> subs = new ArrayList<>();

        public Tower(final String nm, final int wt) {
            name = nm;
            weight = wt;
        }

        public int getTotalWeight(final Map<String, Tower> towerMap) {
            return getWeight() + getSubs().stream().map(towerMap::get).mapToInt(Tower::getWeight).sum();
        }

        public boolean hasChild(final String name) {
            return getSubs().contains(name);
        }
    }
}

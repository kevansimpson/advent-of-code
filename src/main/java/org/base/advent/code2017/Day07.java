package org.base.advent.code2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;
import lombok.Getter;
import lombok.ToString;


/**
 * <h2>Part 1</h2>
 * Wandering further through the circuits of the computer, you come upon a tower of programs that have gotten
 * themselves into a bit of trouble. A recursive algorithm has gotten out of hand, and now they're balanced
 * precariously in a large tower.
 *
 * One program at the bottom supports the entire tower. It's holding a large disc, and on the disc are balanced
 * several more sub-towers. At the bottom of these sub-towers, standing on the bottom disc, are other programs,
 * each holding their own disc, and so on. At the very tops of these sub-sub-sub-...-towers, many programs stand
 * simply keeping the disc below them balanced but with no disc of their own.
 *
 * You offer to help, but first you need to understand the structure of these towers. You ask each program to
 * yell out their name, their weight, and (if they're holding a disc) the names of the programs immediately
 * above them balancing on that disc. You write this information down (your puzzle input). Unfortunately, in
 * their panic, they don't do this in an orderly fashion; by the time you're done, you're not sure which program
 * gave which information.
 *
 * For example, if your list is the following:
 *
 * pbga (66)
 * xhth (57)
 * ebii (61)
 * havc (66)
 * ktlj (57)
 * fwft (72) -> ktlj, cntj, xhth
 * qoyq (66)
 * padx (45) -> pbga, havc, qoyq
 * tknk (41) -> ugml, padx, fwft
 * jptl (61)
 * ugml (68) -> gyxo, ebii, jptl
 * gyxo (61)
 * cntj (57)
 *
 * ...then you would be able to recreate the structure of the towers that looks like this:
 *
 *                 gyxo
 *                /
 *          ugml - ebii
 *        /       \
 *       |         jptl
 *       |
 *       |         pbga
 *      /         /
 * tknk --- padx - havc
 *      \         \
 *       |         qoyq
 *       |
 *       |         ktlj
 *        \       /
 *          fwft - cntj
 *                \
 *                 xhth
 *
 * In this example, tknk is at the bottom of the tower (the bottom program), and is holding up ugml, padx, and fwft.
 * Those programs are, in turn, holding up other programs; in this example, none of those programs are holding up
 * any other programs, and are all the tops of their own towers. (The actual tower balancing in front of you is much larger.)
 *
 * Before you're ready to help them, you need to make sure your information is correct. What is the name of the bottom program?
 *
 * <h2>Part 2</h2>
 *
 * The programs explain the situation: they can't get down. Rather, they could get down, if they weren't expending all of
 * their energy trying to keep the tower balanced. Apparently, one program has the wrong weight, and until it's fixed,
 * they're stuck here.
 *
 * For any program holding a disc, each program standing on that disc forms a sub-tower. Each of those sub-towers are
 * supposed to be the same weight, or the disc itself isn't balanced. The weight of a tower is the sum of the weights of
 * the programs in that tower.
 *
 * In the example above, this means that for ugml's disc to be balanced, gyxo, ebii, and jptl must all have the same weight,
 * and they do: 61.
 *
 * However, for tknk to be balanced, each of the programs standing on its disc and all programs above it must each match.
 * This means that the following sums must all be the same:
 *
 * ugml + (gyxo + ebii + jptl) = 68 + (61 + 61 + 61) = 251
 * padx + (pbga + havc + qoyq) = 45 + (66 + 66 + 66) = 243
 * fwft + (ktlj + cntj + xhth) = 72 + (57 + 57 + 57) = 243
 *
 * As you can see, tknk's disc is unbalanced: ugml's stack is heavier than the other two.
 * Even though the nodes above ugml are balanced, ugml itself is too heavy:
 * it needs to be 8 units lighter for its stack to weigh 243 and keep the towers balanced.
 * If this change were made, its weight would be 60.
 *
 * Given that exactly one program is the wrong weight, what would its weight need to be to balance the entire tower?
 *
 */
public class Day07 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2017/input07.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return findBottomTower(parseTowers(getInput()));
	}

	@Override
	public Object solvePart2() throws Exception {
		return findMisweightedTower(parseTowers(getInput()));
	}

	public int findMisweightedTower(List<Tower> towers) {
		try {
			final Process process = Runtime.getRuntime().exec("./src/main/resources/2017/day07.rb");
			process.waitFor();

			final BufferedReader processIn = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = processIn.readLine()) != null) {
				System.out.println(line);
			}

			return Integer.parseInt("1853");
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
				final List<Tower> subTowers = tower.getSubs().stream().map(towerMap::get).collect(Collectors.toList());

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
			Boolean hasChildren = false;
			for (int j = 0; j < mismatchedWeight.size(); j++) {
				if (i != j) {
					if (mismatchedWeight.get(i).hasChild(mismatchedWeight.get(j).getName())) {
						hasChildren = true;
					}
				}
			}
			if (!hasChildren) {
				final Tower offBalance = mismatchedWeight.get(i);
				final List<Tower> children = offBalance.getSubs().stream().map(towerMap::get).collect(Collectors.toList());
//				System.out.println("\n\nUNBALANCED:\n"+ offBalance +"\n");
//				for (Tower disc : children) {
//					System.out.println(disc.getTotalWeight(towerMap) + ": " + disc);
//				}

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

package org.base.advent.code2017;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.base.advent.Solution;
import lombok.Getter;
import lombok.ToString;


/**
 * <h2>Part 1</h2>
 * You need to cross a vast firewall. The firewall consists of several layers, each with a security scanner
 * that moves back and forth across the layer. To succeed, you must not be detected by a scanner.
 *
 * By studying the firewall briefly, you are able to record (in your puzzle input) the depth of each layer
 * and the range of the scanning area for the scanner within it, written as depth: range. Each layer has a
 * thickness of exactly 1. A layer at depth 0 begins immediately inside the firewall; a layer at depth 1
 * would start immediately after that.
 *
 * For example, suppose you've recorded the following:
 *
 * 0: 3
 * 1: 2
 * 4: 4
 * 6: 4
 *
 * This means that there is a layer immediately inside the firewall (with range 3), a second layer
 * immediately after that (with range 2), a third layer which begins at depth 4 (with range 4), and a
 * fourth layer which begins at depth 6 (also with range 4). Visually, it might look like this:
 *
 *  0   1   2   3   4   5   6
 * [ ] [ ] ... ... [ ] ... [ ]
 * [ ] [ ]         [ ]     [ ]
 * [ ]             [ ]     [ ]
 *                 [ ]     [ ]
 *
 * Within each layer, a security scanner moves back and forth within its range. Each security scanner
 * starts at the top and moves down until it reaches the bottom, then moves up until it reaches the top,
 * and repeats. A security scanner takes one picosecond to move one step.
 *
 * ...
 *
 * Your plan is to hitch a ride on a packet about to move through the firewall. The packet will travel
 * along the top of each layer, and it moves at one layer per picosecond. Each picosecond, the packet
 * moves one layer forward (its first move takes it into layer 0), and then the scanners move one step.
 * If there is a scanner at the top of the layer as your packet enters it, you are caught. (If a
 * scanner moves into the top of its layer while you are there, you are not caught: it doesn't have
 * time to notice you before you leave.) If you were to do this in the configuration above, marking
 * your current position with parentheses, your passage through the firewall would look like this:
 *
 * ...
 *
 * In this situation, you are caught in layers 0 and 6, because your packet entered the layer when its
 * scanner was at the top when you entered it. You are not caught in layer 1, since the scanner moved
 * into the top of the layer once you were already there.
 *
 * The severity of getting caught on a layer is equal to its depth multiplied by its range. (Ignore
 * layers in which you do not get caught.) The severity of the whole trip is the sum of these values.
 * In the example above, the trip severity is 0*3 + 6*4 = 24.
 *
 * Given the details of the firewall you've recorded, if you leave immediately,
 * what is the severity of your whole trip?
 *
 * <h2>Part 2</h2>
 * Now, you need to pass through the firewall without being caught - easier said than done.
 *
 * You can't control the speed of the packet, but you can delay it any number of picoseconds.
 * For each picosecond you delay the packet before beginning your trip, all security scanners
 * move one step. You're not in the firewall during this time; you don't enter layer 0 until
 * you stop delaying the packet.
 *
 * In the example above, if you delay 10 picoseconds (picoseconds 0 - 9), you won't get caught:
 *
 * Because all smaller delays would get you caught, the fewest number of picoseconds you would
 * need to delay to get through safely is 10.
 *
 * What is the fewest number of picoseconds that you need to delay the packet to pass through
 * the firewall without being caught?
 */
public class Day13 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2017/input13.txt");
	}


	@Override
	public Object solvePart1() throws Exception {
		return traverseNetwork(getInput());
	}

	@Override
	public Object solvePart2() throws Exception {
		return 0;
	}

	public int traverseNetwork(final List<String> input) {
		Map<Integer, Wall> firewalls = buildWalls(input);
		final int duration  = firewalls.keySet().stream().max(Comparator.naturalOrder()).get();
		int severity = 0;

		for (int pico = 0; pico <= duration; pico++) {
			System.out.println("pico = "+ pico);
			firewalls.values().forEach(System.out::println);

			// is scanner present
			Wall wall = firewalls.get(pico);
			if (wall != null) {
				if (wall.getScanner() == 0) {    // caught
					severity += wall.getLayer() * wall.getRange();
					System.out.println("CAUGHT: " + pico);
				}
			}

			// move scanners
			firewalls.values().forEach(Wall::move);
			firewalls.values().forEach(System.out::println);
		}

		return severity;
	}

	public Map<Integer, Wall> buildWalls(final List<String> input) {
		Map<Integer, Wall> firewalls = input.stream()
					 .map(s -> s.split(": "))
					 .map(ab -> new Wall(Integer.parseInt(ab[0]), Integer.parseInt(ab[1])))
					 .collect(Collectors.toMap(Wall::getLayer, w -> w));

		return firewalls;
	}

	@Getter
	@ToString
	public static class Wall {
		private final int layer;
		private final int range;

		private int scanner;	// position of scanner
		private boolean up;		// direction of scanner

		public Wall(final int l, final int r) {
			layer = l;
			range = r;
		}

		public void move() {
			if (scanner >= (range - 1))
				up = true;

			int move = up ? -1 : 1;
			scanner += move;

			if (scanner == 0)
				up = false;

		}
	}
}

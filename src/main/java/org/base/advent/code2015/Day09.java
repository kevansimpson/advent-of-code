package org.base.advent.code2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.base.advent.Solution;

/**
 *
 */
public class Day09 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input09.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return null;
	}

	@Override
	public Object solvePart2() throws Exception {
		return null;
	}

	private static Pattern parser = Pattern.compile("(.+)\\sto\\s(.+)\\s=\\s(\\d+)", Pattern.DOTALL);

	private Set<String> locations = new HashSet<>();
	private Map<List<String>, Integer> distanceMap = new HashMap<>();
	private Map<String, Integer> jumpDistanceMap;

	public void solvePuzzle1() throws Exception {
		List<String> directions = getInput();
		jumpDistanceMap = buildDistanceMap(directions);
//		debug(jumpDistanceMap);
		
		List<String> permutation = new ArrayList<>();
//		List<String> locationList = Arrays.asList("a", "b", "c", "d");
		List<String> locationList = new ArrayList<>(this.locations);
		buildAllPaths(locationList, permutation);
		
		int lowest = Integer.MAX_VALUE;
		for (List<String> path : distanceMap.keySet()) {
			Integer dist = distanceMap.get(path);
			if (dist < lowest) {
				lowest = dist;
			}
		}
		System.out.println("The shortest path is: "+ lowest);
	}

	public void solvePuzzle2() throws Exception {
		int longest = 0;
		for (List<String> path : distanceMap.keySet()) {
			Integer dist = distanceMap.get(path);
			if (dist > longest) {
				longest = dist;
			}
		}
		System.out.println("The longest path is: "+ longest);
	}

	int calculateDistance(List<String> path) {
		int dist = 0;
		for (int i = 0; i < path.size() - 1; i++) {
			String key = key(path.get(i), path.get(i + 1));
			Integer step = jumpDistanceMap.get(key);
			if (step == null)
				throw new NullPointerException(key);
			else
				dist += step.intValue();
		}

		return dist;
	}

	void buildAllPaths(List<String> available, List<String> permutation) {
		if (available.size() == 0) {
//			System.out.println(permutation);
			distanceMap.put(permutation, calculateDistance(permutation));
			return;
		}

		for (int i = 0; i < available.size(); i++) {
			String loc = available.get(i);
			List<String> remaining = new ArrayList<>();
			remaining.addAll(available.subList(0, i));
			remaining.addAll(available.subList(i + 1, available.size()));
			
			List<String> newPerm = new ArrayList<>(permutation);
			newPerm.add(loc);
			buildAllPaths(remaining, newPerm);
		}
		
	}

	String key(String loc1, String loc2) {
		return "JUMP-"+ Arrays.asList(loc1, loc2).toString();
	}

	Map<String, Integer> buildDistanceMap(List<String> directions) {
		Map<String, Integer> distanceMap = new HashMap<>();
		for (String directive : directions) {
			Matcher matcher = parser.matcher(directive);
			if (matcher.matches()) {
				String city1 = matcher.group(1);
				String city2 = matcher.group(2);
				Integer distance = Integer.parseInt(matcher.group(3));
				locations.add(city1);
				locations.add(city2);
				distanceMap.put(key(city1, city2), distance);
				distanceMap.put(key(city2, city1), distance);
			}
			else {
				System.err.println("No match: "+ directive);
			}
		}

		return distanceMap;
	}

	void debug(Map<String, Integer> map) {
		for (String key : map.keySet()) {
			System.out.println(ArrayUtils.toString(key) +" ==> "+ map.get(key));
		}
	}
}

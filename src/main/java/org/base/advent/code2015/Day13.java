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
public class Day13 implements Solution<List<String>> {
	private static final String ME = "ME";

	//Alice would gain 2 happiness units by sitting next to Bob.
	//Alice would gain 26 happiness units by sitting next to Carol.
	private static Pattern parser = Pattern.compile(
			"(.+)\\swould\\s(.+)\\s(\\d+).*to\\s(.+)\\.", Pattern.DOTALL);

	private Set<String> people = new HashSet<>();
	private Map<List<String>, Integer> distanceMap = new HashMap<>();
	private Map<String, Integer> jumpDistanceMap;


	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input13.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return null;
	}

	@Override
	public Object solvePart2() throws Exception {
		return null;
	}


	public void solvePuzzle1() throws Exception {
		List<String> directions = getInput();
		jumpDistanceMap = buildDistanceMap(directions);
		
		List<String> permutation = new ArrayList<>();
		List<String> peopleList = new ArrayList<>(this.people);
		buildAllPaths(peopleList, permutation);
		
		int longest = Integer.MIN_VALUE;
		List<String> optimal = null;
		for (List<String> path : distanceMap.keySet()) {
			Integer dist = distanceMap.get(path);
			if (dist > longest) {
				longest = dist;
				optimal = path;
			}
		}
		
		// > 725 - didn't account for 0-last deltas
		System.out.println("The optimal seating arrangement is: "+ optimal);
		System.out.println("The total change in happiness is: "+ longest);	// 733
	}

	public void solvePuzzle2() throws Exception {
		distanceMap.clear();
		jumpDistanceMap.clear();
		people.clear();
		people.add(ME);
		this.solvePuzzle1();
	}

	int calculateDistance(List<String> path) {
		int last = path.size() - 1;
		int dist = getDelta(path.get(last), path.get(0));
		dist += getDelta(path.get(0), path.get(last));
		for (int i = 0; i < last; i++) {
			String person1 = path.get(i);
			String person2 = path.get(i + 1);
			dist += getDelta(person1, person2);
			dist += getDelta(person2, person1);
		}

		return dist;
	}

	int getDelta(String person1, String person2) {
		if (Arrays.asList(person1, person2).contains(ME))
			return 0;

		String key = key(person1, person2);
		Integer step = jumpDistanceMap.get(key);
		if (step == null)
			throw new NullPointerException(key);
		else
			return step.intValue();
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
				//Alice would gain 2 happiness units by sitting next to Bob.
				//Alice would gain 26 happiness units by sitting next to Carol.
				String person1 = matcher.group(1), person2 = matcher.group(4);
				Integer distance = Integer.parseInt(matcher.group(3));
				people.add(person1);
				people.add(person2);
				distanceMap.put(key(person1, person2), (distance * ("gain".equals(matcher.group(2)) ? 1 : -1)));
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
	void debugL(Map<List<String>, Integer> map) {
		for (List<String> key : map.keySet()) {
			System.out.println(key.toString() +" ==> "+ map.get(key));
		}
	}
}

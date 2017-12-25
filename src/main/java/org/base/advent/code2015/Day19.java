package org.base.advent.code2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

/**
 *
 */
public class Day19 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input19.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return null;
	}

	@Override
	public Object solvePart2() throws Exception {
		return null;
	}


	Set<String> molecules = new HashSet<>();
	String medicine;
	int shortestPath = Integer.MAX_VALUE;

	public void solvePuzzle1() throws Exception {
		List<String> replacements = getInput();
		medicine = replacements.remove(replacements.size() - 1);

		Map<String, List<String>> rmap = buildReplacementMap(replacements);
		applyAllReplacements(rmap);
		System.out.println("Total # of molecules is "+ molecules.size());
	}

	/**
	 * Adapted from solution found on https://www.reddit.com/r/adventofcode/comments/3xflz8/day_19_solutions/
	 * 
	 * All of the rules are of one of the following forms:
	 * α => βγ
	 * α => βRnγAr
	 * α => βRnγYδAr
	 * α => βRnγYδYεAr
	 * 
	 * As Rn, Ar, and Y are only on the left side of the equation, one merely only needs to compute
	 * 
	 * 			#NumSymbols - #Rn - #Ar - 2 * #Y - 1
	 * 
	 * Subtract of #Rn and #Ar because those are just extras. 
	 * Subtract two times #Y because we get rid of the Ys and the extra elements following them. 
	 * Subtract one because we start with "e".
	 * 
	 */
	public void solvePuzzle2() throws Exception {
		List<String> replacements = getInput();
		medicine = replacements.remove(replacements.size() - 1);

		int upper = 0;
		for (char ch : medicine.toCharArray()) {
			if (Character.isUpperCase(ch))
				upper += 1;
		}
		
		shortestPath = upper - countOccurrences(medicine, "Rn") - countOccurrences(medicine, "Ar") - 2 * countOccurrences(medicine, "Y") - 1;
		System.out.println("Shortest path is "+ shortestPath);
	}

	int countOccurrences(String str, String x) {
		int count = 0;
		for (int index = str.indexOf(x); index >= 0; index = str.indexOf(x, index + 1), ++count) { /* no op */ }
		return count;
	}

	void applyAllReplacements(Map<String, List<String>> rmap) {
		for (Entry<String, List<String>> replacement : rmap.entrySet()) {
			Set<String> uniqueMolecules = applyReplacement(medicine, replacement);
			molecules.addAll(uniqueMolecules);
		}
	}

	Set<String> applyReplacement(String chain, Entry<String, List<String>> replacement) {
		Set<String> uniqueMolecules = new HashSet<>();
		for (String repl : replacement.getValue()) {
			int start = 0, index = 0;
//			System.out.println("repl = "+ repl +" - "+ chain.indexOf(replacement.getKey(), start));
			while ((index = chain.indexOf(replacement.getKey(), start)) >= 0) {
				String variation = chain.substring(0, index) + repl +
						chain.substring(index + replacement.getKey().length());
				uniqueMolecules.add(variation);
				start = index + replacement.getKey().length();
//				System.out.println("new = "+ variation +" - "+ start);
			}
		}

		return uniqueMolecules;
	}

	Map<String, List<String>> buildReplacementMap(List<String> replacements) {
		Map<String, List<String>> rmap = new HashMap<>();
		for (String repl : replacements) {
			if (StringUtils.isBlank(repl)) continue;
			String[] tokens = repl.split("\\s");
			if (!rmap.containsKey(tokens[0])) {
				rmap.put(tokens[0], new ArrayList<>());
			}
			rmap.get(tokens[0]).add(tokens[2]);
		}
		return rmap;
	}
}
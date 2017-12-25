package org.base.advent.code2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

/**
 *
 */
public class Day24 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input24.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return null;
	}

	@Override
	public Object solvePart2() throws Exception {
		return null;
	}


	private static Function<List<String>, int[]> toIntArray =
			ls -> ls.stream().mapToInt(Integer::parseInt).toArray();
//	private static Function<List<String>, List<Integer>> toIntList =
//			ls -> ls.stream().map(Integer::parseInt).collect(Collectors.toList());
//	private static Function<List<Integer>, Integer> sum =
//			ls -> ls.stream().mapToInt(i -> i).sum();
	
	List<Integer> answer = null;
	private int smallestGroup = Integer.MAX_VALUE;
	private long lowestQE = Long.MAX_VALUE;

	public void solvePuzzle1() throws Exception {
		solveFor(3);
	}

	public void solvePuzzle2() throws Exception {
		solveFor(4);
	}

	int expectedSum = -1;
	void solveFor(int numCompartments) throws Exception {
		smallestGroup = Integer.MAX_VALUE;
		lowestQE = Long.MAX_VALUE;

		long ts = System.currentTimeMillis();
		List<String> containers = getInput();
		int[] cs = toIntArray.apply(containers);

		findSmallest(cs, numCompartments);
		solve(containers, 0, new ArrayList<>(), 0);
		
		debug(answer, " in ", (System.currentTimeMillis() - ts), "ms");
		info("Lowest QE for ", numCompartments, " compartments is ", lowestQE);

	}
	
	void findSmallest(int[] containers, int numCompartments) {
		ArrayUtils.reverse(containers);
		int max = 2 << containers.length - 1;
		expectedSum = Arrays.stream(containers).sum() / numCompartments;

		for (int i = 0; i < max; i++) {
			int[] ia = toArray(i, containers);
			int is = Arrays.stream(ia).sum();
			if (ia.length <= 0 || is != expectedSum) continue;

			if (ia.length < smallestGroup) {
				smallestGroup = ia.length;
				lowestQE = calcQE(ia);;
				return;
			}
		}
	}
	void solve(List<String> containers, int index, List<Integer> permutation, int currentSum) {
		if (currentSum == expectedSum) {
			if (permutation.size() < smallestGroup) {
				lowestQE = permutation.stream().mapToLong(Long::valueOf).reduce((a,b) -> a * b).getAsLong();
				smallestGroup = permutation.size();
				answer = permutation;
			}
			else if (permutation.size() == smallestGroup) {
				long qe = permutation.stream().mapToLong(Long::valueOf).reduce((a,b) -> a * b).getAsLong();
				if (qe < lowestQE) {
					lowestQE = qe;
					answer = permutation;
				}
			}
			return;
		}

		if (index >= containers.size() || permutation.size() >= smallestGroup)
			return;

		for (int i = 0; i < containers.size(); i++) {
			int value = Integer.parseInt(containers.get(i));
			if (permutation.contains(value)) continue;
			List<Integer> next = new ArrayList<>(permutation);
			next.add(value);
			solve(containers, 1 + index, next, value + currentSum);
		}
	}

	long calcQE(int[] ints) {
		return Arrays.stream(ints).mapToLong(l -> l).reduce((l1, l2) -> l1 * l2).getAsLong();
	}

	int[] toArray(int flag, int... cs) {
		Set<Integer> set = new TreeSet<>();
		String rbin = StringUtils.reverse(Integer.toBinaryString(flag));
		for (int i = 0; i < rbin.length(); i += 1)
			if ('1' == rbin.charAt(i))
				set.add(cs[i]);

		return set.stream().mapToInt(e -> e.intValue()).toArray();
	}

	Set<Integer> toSet(int[] cs) {
		Set<Integer> set = new TreeSet<>();
		for (int i : cs) set.add(i);
		return set;
	}
	
	void debug(Object... args) {
//		System.out.println(StringUtils.join(args));
	}
	void info(Object... args) {
		System.out.println(StringUtils.join(args));
	}

}
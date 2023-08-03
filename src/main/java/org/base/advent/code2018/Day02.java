package org.base.advent.code2018;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.base.advent.Solution;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <a href="https://adventofcode.com/2018/day/02">Day 02</a>
 */
public class Day02 implements Solution<List<String>> {

    public enum Match {
        none, two, three, both
    }

    @Override
    public List<String> getInput(){
        return readLines("/2018/input02.txt");
    }

    @Override
    public Object solvePart1() {
        return checksum(getInput());
    }

    @Override
    public Object solvePart2() {
        return findPrototype(getInput());
    }

    public String findPrototype(final List<String> input) {
        final int size = input.size();
        final LevenshteinDistance distance = new LevenshteinDistance(2);

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (distance.apply(input.get(i), input.get(j)) == 1)
                    return removeDiff(input.get(i), input.get(j));
            }
        }

        return null;
    }

    private String removeDiff(final String text1, final String text2) {
        return StringUtils.remove(text1, text1.charAt(StringUtils.indexOfAnyBut(text1, text2)));
    }

    public int checksum(final List<String> input) {
        final int[] cksum = new int[2];
        input.stream().map(this::countPairsAndTriples).forEach(match -> {
            switch (match) {
                case two:
                    cksum[0] += 1; break;
                case three:
                    cksum[1] += 1; break;
                case both:
                    cksum[0] += 1;
                    cksum[1] += 1;
                    break;
                default:
                    // nothing
            }
        });

        return cksum[0] * cksum[1];
    }

    public Match countPairsAndTriples(final String text) {
        final Set<Character> set = new HashSet<>();
        for (char c : text.toCharArray()) {
            set.add(c);
        }

        final Set<Integer> counts = new HashSet<>();
        set.forEach(c -> counts.add(StringUtils.countMatches(text, c)));

        return (counts.contains(2))
                ? (counts.contains(3)) ? Match.both : Match.two
                : (counts.contains(3)) ? Match.three : Match.none;
    }
}

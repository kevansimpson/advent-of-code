package org.base.advent.code2023;

import java.util.*;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2023/day/04">Day 04</a>
 */
public class Day04 implements Function<List<String>, Day04.Scratchcards> {
    public record Card(Set<Integer> winners, List<Integer> numbers) {}
    public record Scratchcards(int score, int count) {}

    @Override
    public Scratchcards apply(List<String> input) {
        List<Card> cards = readCards(input);
        int sum = 0, count = 0;
        int[] counts = new int[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            counts[i]++;
            count += counts[i];
            int winners = 0;
            for (int w : card.numbers)
                if (card.winners.contains(w))
                    winners++;
            if (winners > 0) {
                sum += Math.pow(2.0, winners - 1.0);
                for (int j = i + 1; j <= i + winners; j++)
                    counts[j] += counts[i];
            }
        }

        return new Scratchcards(sum, count);
    }

    private List<Card> readCards(List<String> input) {
        List<Card> cards = new ArrayList<>();
        for (String line : input) {
            int start = line.indexOf(":") + 2;
            int mid = line.indexOf(" | ");
            cards.add(new Card(splitToInt(line.substring(start, mid), new TreeSet<>()),
                    splitToInt(line.substring(mid + 3), new ArrayList<>())));
        }
        return cards;
    }
    private <T extends Collection<Integer>> T splitToInt(String str, T collection) {
        for (String s : str.trim().split("\\s+"))
            collection.add(Integer.parseInt(s));
        return collection;
    }
}


package org.base.advent.code2023;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;

import static org.base.advent.util.Text.charCounts;

/**
 * <a href="https://adventofcode.com/2023/day/7">Day 07</a>
 */
public class Day07 implements Function<List<String>, Day07.CardSums> {
    public record CardSums(long sum, long wildSum) {}

    private static final String CARDS = "23456789TJQKA";
    private static final String WILDS = "J23456789TQKA";

    @Override
    public CardSums apply(List<String> input) {
        List<CamelCards> cards = dealCards(input);
        List<CamelCards> wilds = new ArrayList<>(cards);
        cards.sort(new CamelComparator(CARDS, this::rank));
        wilds.sort(new CamelComparator(WILDS, this::wildRank));
        return new CardSums(sum(cards), sum(wilds));
    }

    private long sum(List<CamelCards> cards) {
        long sum = 0L;
        for (int i = 0; i < cards.size(); i++)
            sum += ((long) i + 1) * cards.get(i).bid;
        return sum;
    }

    private String rank(CamelCards cards) {
        return cards.rank;
    }

    private String wildRank(CamelCards cards) {
        if (cards.hand.contains("J")) {
            Map<Character, Integer> newHand = new HashMap<>(cards.counts);
            int jokers = newHand.remove('J');
            if (newHand.isEmpty())
                return cards.rank;
            else {
                Map.Entry<Character, Integer> max = null;
                for (Map.Entry<Character, Integer> e : newHand.entrySet())
                    if (max == null || e.getValue() > max.getValue())
                        max = e;
                newHand.put(max.getKey(), max.getValue() + jokers);
                return format(newHand.values());
            }
        }
        return cards.rank;
    }

    private List<CamelCards> dealCards(List<String> input) {
        List<CamelCards> cards = new ArrayList<>();
        for (String line : input) {
            String[] arr = line.split(" ");
            cards.add(new CamelCards(arr[0], Long.parseLong(arr[1])));
        }
        return cards;
    }

    private static class CamelCards {
        private final String hand;
        private final long bid;
        private final Map<Character, Integer> counts;
        private final String rank;

        public CamelCards(String h, long b) {
            hand = h;
            bid = b;
            counts = charCounts(hand);
            rank = format(counts.values());
        }
    }

    @AllArgsConstructor
    private static class CamelComparator implements Comparator<CamelCards> {
        private final String cardRanks;
        private final Function<CamelCards, String> ranker;

        @Override
        public int compare(CamelCards o1, CamelCards o2) {
            int comp = ranker.apply(o1).compareTo(ranker.apply(o2));
            if (comp == 0) {
                for (int index = 0; index < o1.hand.length(); index++) {
                    int ix1 = cardRanks.indexOf(o1.hand.charAt(index));
                    int ix2 = cardRanks.indexOf(o2.hand.charAt(index));
                    if (ix1 < ix2)
                        return -1;
                    else if (ix1 > ix2)
                        return 1;
                }
            }
            return comp;
        }
    }

    private static String format(Collection<Integer> counts) {
        List<Integer> list = new ArrayList<>(counts);
        list.sort(Comparator.reverseOrder());
        StringBuilder bldr = new StringBuilder();
        for (Integer i : list)
            bldr.append(i);
        return StringUtils.rightPad(bldr.toString(), 5, "0");
    }
}


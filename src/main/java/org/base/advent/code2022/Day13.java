package org.base.advent.code2022;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.function.Predicate.not;

/**
 * <a href="https://adventofcode.com/2022/day/13">Day 13</a>
 */
public class Day13 implements Function<List<String>, Day13.DistressSignal> {
    public record DistressSignal(int indicesSum, int decoderKey) {}

    private static final List<List<Object>> PART2_SIGNALS =
            Stream.of("[[2]]", "[[6]]").map(Day13::parse).toList();

    @Override
    public DistressSignal apply(final List<String> input) {
        final List<List<Object>> nested = input.stream().filter(not(String::isBlank)).map(Day13::parse).toList();
        return new DistressSignal(sumIndices(nested), decoderKey(nested));
    }

    int sumIndices(final List<List<Object>> nested) {
        int result = 0;
        List<List<Object>> copy = new ArrayList<>(nested);
        for (int i = 0, max = copy.size() - 1; i < max; i += 2)
            if (compareValues(copy.get(i), copy.get(i + 1)) < 0)
                result += ((i + 2) / 2);
        return result;
    }

    int decoderKey(final List<List<Object>> nested) {
        List<List<Object>> part2 = new ArrayList<>(PART2_SIGNALS);
        part2.addAll(nested);

        List<String> sorted = part2.stream().sorted(this::compareValues).map(Object::toString).toList();
        return (sorted.indexOf("[[[2]]]") + 1) * (sorted.indexOf("[[[6]]]") + 1);
    }

    int compareValues(final List<Object> left, final List<Object> right) {
        Iterator<Object> lit = left.iterator();
        Iterator<Object> rit = right.iterator();
        while (lit.hasNext() && rit.hasNext()) {
            int comp = compareValues(lit.next(), rit.next());
            if (comp != 0)
                return comp;
        }

        if (!lit.hasNext())
            return (rit.hasNext()) ? -1 : 0;
        else
            return 1;
    }

    @SuppressWarnings("unchecked")
    int compareValues(final Object left, final Object right) {
        if (left instanceof Integer il) {
            if (right instanceof Integer ir)
                return il - ir;
            else
                return compareValues(List.of(il), right);
        }
        else if (right instanceof Integer ir)
            return compareValues(left, List.of(ir));
        else
            return compareValues((List<Object>) left, (List<Object>) right);
    }

    private static List<Object> parse(final String nested) {
        Deque<List<Object>> stack = new ArrayDeque<>();
        stack.push(new ArrayList<>());
        StringBuilder str = new StringBuilder();

        for (char ch : nested.toCharArray()) {
            switch (ch) {
                case '[' -> stack.push(new ArrayList<>());
                case ']' -> {
                    List<Object> top = stack.pop();
                    if (!str.isEmpty()) {
                        top.add(Integer.parseInt(str.toString()));
                        str = new StringBuilder();
                    }
                    requireNonNull(stack.peek()).add(top);
                }
                case ',' -> {
                    if (!str.isEmpty()) {
                        requireNonNull(stack.peek()).add(Integer.parseInt(str.toString()));
                        str = new StringBuilder();
                    }
                }
                default -> str.append(ch);
            }
        }

        return stack.peek();
    }
}

package org.base.advent.code2022;

import lombok.Getter;
import org.base.advent.Solution;
import org.base.advent.util.SafeLazyInitializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * <a href="https://adventofcode.com/2022/day/13">Day 13</a>
 */
public class Day13 implements Solution<List<String>> {
    private static final List<List<Object>> PART2_SIGNALS =
            Stream.of("[[2]]", "[[6]]").map(Day13::parse).toList();

    @Getter
    private final List<String> input = readLines("/2022/input13.txt");
    private final SafeLazyInitializer<List<List<Object>>> nested = new SafeLazyInitializer<>(() ->
        getInput().stream().filter(not(String::isBlank)).map(Day13::parse).toList());

    @Override
    public Object solvePart1() {
        int result = 0;
        List<List<Object>> copy = new ArrayList<>(nested.get());
        for (int i = 0, max = copy.size() - 1; i < max; i += 2)
            if (compareValues(copy.get(i), copy.get(i + 1)) < 0)
                result += ((i + 2) / 2);
        return result;
    }

    @Override
    public Object solvePart2() {
        List<List<Object>> part2 = new ArrayList<>(PART2_SIGNALS);
        part2.addAll(nested.get());

        List<String> sorted = part2.stream().sorted(this::compareValues).map(Object::toString).toList();
        return (sorted.indexOf("[[[2]]]") + 1) * (sorted.indexOf("[[[6]]]") + 1);
    }

    int compareValues(List<Object> left, List<Object> right) {
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
    int compareValues(Object left, Object right) {
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

    private static List<Object> parse(String nested) {
        Stack<List<Object>> stack = new Stack<>();
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
                    stack.peek().add(top);
                }
                case ',' -> {
                    if (!str.isEmpty()) {
                        stack.peek().add(Integer.parseInt(str.toString()));
                        str = new StringBuilder();
                    }
                }
                default -> str.append(ch);
            }
        }

        return stack.peek();
    }
}

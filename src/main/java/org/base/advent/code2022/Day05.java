package org.base.advent.code2022;

import org.base.advent.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.base.advent.util.Util.splitByBlankLine;

/**
 * <a href="https://adventofcode.com/2022/day/05">Day 05</a>
 */
public class Day05 implements Function<List<String>, Day05.MovedCrate> {
    public record MovedCrate(String top, String rearranged) {}

    @Override
    public MovedCrate apply(final List<String> strings) {
        List<List<String>> split = splitByBlankLine(strings);
        List<String> stacks = split.get(0).subList(0, split.get(0).size() - 1);
        final Puzzle puzzle = new Puzzle(split, stacks,
                Arrays.stream(Util.extractInt(split.get(0).get(stacks.size()))).max().orElse(-1),
                split.get(1).stream().map(Util::extractInt).toList());
        return new MovedCrate(moveCrates(puzzle, this::topCrates), moveCrates(puzzle, this::rearrangeCrates));
    }

    void topCrates(final Crate crate) {
        for (int s = 0; s < crate.qty; s++)
            if (!crate.from.isEmpty())
                crate.to.push(crate.from.pop());
    }

    void rearrangeCrates(final Crate crate) {
        Stack<Character> stack = new Stack<>();
        for (int s = 0; s < crate.qty; s++)
            if (!crate.from.isEmpty())
                stack.push(crate.from.pop());

        while (!stack.isEmpty())
            crate.to.push(stack.pop());
    }

     String moveCrates(final Puzzle puzzle, final CrateMover mover) {
        List<Stack<Character>> crates = scanCrates(puzzle);
        puzzle.moves.forEach(crate -> mover.accept(
                new Crate(crate[0], crates.get(crate[1] - 1), crates.get(crate[2] - 1))));
        return crates.stream()
                .map(s -> String.valueOf(s.peek()))
                .collect(Collectors.joining());
    }

    private List<Stack<Character>> scanCrates(Puzzle p) {
        List<Stack<Character>> list = new ArrayList<>();
        for (int i = 0, max = p.numStacks - 1; i <= max; i++) {
            Stack<Character> stack = new Stack<>();
            int column = 1 + i * 4;
            for (int c = p.stacks.size() - 1; c >= 0; c--) {
                String line = p.stacks.get(c);
                if (line.length() >= column && line.charAt(column) != ' ')
                    stack.push(line.charAt(column));
            }
            list.add(stack);
        }
        return list;
    }

    public record Crate(int qty, Stack<Character> from, Stack<Character> to) {}

    public interface CrateMover extends Consumer<Crate> {}

    public record Puzzle(List<List<String>> split,
                         List<String> stacks,
                         int numStacks,
                         List<int[]> moves) {}
}

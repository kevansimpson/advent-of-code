package org.base.advent.code2016;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.PermIterator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Integer.*;
import static org.base.advent.util.Text.rotateLeft;
import static org.base.advent.util.Text.rotateRight;

/**
 * <a href="https://adventofcode.com/2016/day/21">Day 21</a>
 */
public class Day21 implements Function<List<String>, Pair<String, String>> {
    @Override
    public Pair<String, String> apply(List<String> input) {
        return Pair.of(unscramble("abcdefgh", input), reverseScramble(input));
    }

    String reverseScramble(List<String> input) {
        PermIterator<Character> iterator = new PermIterator<>(
                new Character[] {'a','b','c','d','e','f','g','h'});
        for (List<Character> perm : iterator) {
            String pswd = perm.stream().map(String::valueOf).collect(Collectors.joining());
            if ("fbgdceah".equals(unscramble(pswd, input)))
                return pswd;
        }

        return null;
    }

    String unscramble(String pswd, final List<String> operations) {
        String unscrambled = pswd;
        for (String operation : operations) {
            String[] ops = operation.split(" ");
            switch (ops[0]) {
                case "swap" -> {
                    if ("position".equals(ops[1]))
                        unscrambled = swapPositions(unscrambled, ops);
                    else
                        unscrambled = swapLetters(unscrambled, ops);
                }
                case "rotate" -> {
                    switch (ops[1]) {
                        case "left" -> unscrambled = rotateLeft(unscrambled, parseInt(ops[2]));
                        case "right" -> unscrambled = rotateRight(unscrambled, parseInt(ops[2]));
                        default -> {
                            int steps = unscrambled.indexOf(ops[6]);
                            unscrambled = rotateRight(unscrambled, steps + 1 + ((steps >= 4 ? 1 : 0)));
                        }
                    }
                }
                case "reverse" ->
                    unscrambled = reversePositions(unscrambled, ops);
                default ->
                    unscrambled = movePositions(unscrambled, ops);
            }
        }

        return unscrambled;
    }

    String movePositions(String pswd, String[] ops) {
        int p1 = parseInt(ops[2]), p2 = parseInt(ops[5]);
        String base = pswd.substring(0, p1) + pswd.substring(p1 + 1);
        return base.substring(0, p2) +
                pswd.charAt(p1) +
                base.substring(p2);
    }

    String reversePositions(String pswd, String[] ops) {
        int p1 = parseInt(ops[2]), p2 = parseInt(ops[4]);
        String xy = StringUtils.reverse(pswd.substring(p1, p2 + 1));
        return pswd.substring(0, p1) + xy + pswd.substring(p2 + 1);
    }

    String swapLetters(String pswd, String[] ops) {
        String l1 = ops[2], l2 = ops[5];
        int p1 = pswd.indexOf(l1), p2 = pswd.indexOf(l2);
        int min = min(p1, p2), max = max(p1, p2);
        return pswd.substring(0, min) +
                pswd.charAt(max) +
                pswd.substring(min + 1, max) +
                pswd.charAt(min) +
                pswd.substring(max + 1);
    }

    String swapPositions(String pswd, String[] ops) {
        int p1 = parseInt(ops[2]), p2 = parseInt(ops[5]);
        int min = min(p1, p2), max = max(p1, p2);
        return pswd.substring(0, min) +
                pswd.charAt(max) +
                pswd.substring(min + 1, max) +
                pswd.charAt(min) +
                pswd.substring(max + 1);
    }
}
package org.base.advent.code2024;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.base.advent.util.Text.findAll;

/**
 * <a href="https://adventofcode.com/2024/day/3">Day 3</a>
 */
public class Day03 implements Function<String, Day03.MulProducts> {
    public record MulProducts(int all, int enabled) {}

    private static final Pattern MUL_REGEX = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
    private static final Pattern DO_OR_NOT_REGEX = Pattern.compile("do\\(\\)|don't\\(\\)");

    @Override
    public MulProducts apply(String input) {
        int all = 0, enabled = 0;
        boolean doMul = true;
        int index = 0;
        Matcher m = MUL_REGEX.matcher(input);
        while (m.find(index)) {
            List<String> matches = findAll(DO_OR_NOT_REGEX, input.substring(index, m.end()));
            for (String doDont : matches) {
                if (doDont.equals("do()"))
                    doMul = true;
                else if (doDont.equals("don't()"))
                    doMul = false;
            }

            int mul = Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2));
            all += mul;
            if (doMul)
                enabled += mul;

            index = m.end();
        }

        return new MulProducts(all, enabled);
    }
}


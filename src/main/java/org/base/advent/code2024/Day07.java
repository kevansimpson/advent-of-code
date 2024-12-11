package org.base.advent.code2024;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2024/day/7">Day 7</a>
 */
public class Day07 implements Function<List<String>, Pair<Long, Long>> {
    public record RopeBridge(long calibration, long concatenation) {}

    private static final List<BiFunction<Long, Long, Long>> OPERATORS =
            List.of(Long::sum, (a, b) -> a * b);

    @Override
    public Pair<Long, Long> apply(List<String> input) {
        long calibration = 0L, concatenation = 0L;
        List<BiFunction<Long, Long, Long>> withConcatenation = new ArrayList<>(OPERATORS);
        withConcatenation.add((a, b) -> Long.parseLong(String.format("%d%d", a, b)));
        for (String equation : input) {
            long[] values = Text.extractLong(equation);
            if (canBeCalibrated(values, OPERATORS)) {
                calibration += values[0];
            }
            else if (canBeCalibrated(values, withConcatenation))
                concatenation += values[0];
        }
        concatenation += calibration;
        return Pair.of(calibration, concatenation);
    }

    private boolean canBeCalibrated(long[] values, List<BiFunction<Long, Long, Long>> operators) {
        for (int i = 0; i < operators.size(); i++) {
            if (canBeCalibrated(values, 0L, 1, i, operators))
                return true;
        }
        return false;
    }

    private boolean canBeCalibrated(long[] values, long sum, int index, int op,
                                    List<BiFunction<Long, Long, Long>> operators) {
        if (sum == values[0] && index == values.length) {
            return true;
        }
        else if (index < values.length) {
            long newSum = operators.get(op).apply(sum, values[index]);
            for (int i = 0; i < operators.size(); i++) {
                if (canBeCalibrated(values, newSum, index + 1, i, operators))
                    return true;
            }
        }
        return false;
    }
}


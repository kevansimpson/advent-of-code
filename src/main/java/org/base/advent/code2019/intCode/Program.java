package org.base.advent.code2019.intCode;

import lombok.Getter;

import java.util.Arrays;

/**
 * Represents an {@link org.base.advent.code2019.Day02 IntCode} program.
 */
@Getter
public class Program {
    private final int[] codes;
    private final int[] result;
    private int index = 0;

    public Program(final int... c) {
        codes = c;
        result = Arrays.copyOf(c, c.length);
    }

    public void run() {
        while (index < result.length) {
            switch (result[index]) {
                case 1:
                    result[result[index + 3]] = result[result[index + 1]] + result[result[index + 2]];
                    index += 4;
                    break;
                case 2:
                    result[result[index + 3]] = result[result[index + 1]] * result[result[index + 2]];
                    index += 4;
                    break;
                case 99:
                    return;
            }
        }
    }
}

package org.base.advent.code2019.intCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Represents an {@link org.base.advent.code2019.Day02 IntCode} program.
 */
@Getter
public class Program {
    private final int[] codes;
    private final int[] result;
    private int index = 0;
    private int input = 0;
    @Setter(AccessLevel.PRIVATE)
    private int output = 0;

    public Program(final int... c) {
        codes = c;
        result = Arrays.copyOf(c, c.length);
    }

    public Program(final int userInput, final int... c) {
        this(c);
        input = userInput;
    }

    int param(final int offset, final String fullOpCode, final int opCodeIndex) {
        try {
            return ('0' == fullOpCode.charAt(opCodeIndex)) ? result[result[index + offset]] : result[index + offset];
        }
        catch (Exception ex) {
            return opCodeIndex;
        }
    }
    public void run() {
        while (index < result.length) {
            final int baseOpCode = result[index];
            final String fullOpCode = StringUtils.leftPad(String.valueOf(result[index]), 5, '0');
            final int param1 = param(1, fullOpCode, 2);
            final int param2 = param(2, fullOpCode, 1);
            switch (baseOpCode % 100) {
                case 1: // add
                    result[result[index + 3]] = param1 + param2;
                    index += 4;
                    break;
                case 2: // multiply
                    result[result[index + 3]] = param1 * param2;
                    index += 4;
                    break;
                case 3: // input
                    if ('0' == fullOpCode.charAt(2))
                        result[result[index + 1]] = getInput();
                    else result[index + 1] = getInput();
                    index += 2;
                    break;
                case 4: // output
                    final int output = param1; //result[result[index + 1]];
                    setOutput(output);
                    index += 2;
                    break;
                case 5: // jump-if-true
                    if (param1 != 0) index = param2;
                    else index += 3;
                    break;
                case 6: // jump-if-false
                    if (param1 == 0) index = param2;
                    else index += 3;
                    break;
                case 7: // less-than
                    result[result[index + 3]] = param1 < param2 ? 1 : 0;
                    index += 4;
                    break;
                case 8: // equals
                    result[result[index + 3]] = param1 == param2 ? 1 : 0;
                    index += 4;
                    break;
                case 99:
                    return;
            }
        }
    }

    /** Runs the given codes and returns the result. */
    public static int[] runProgram(final int... codes) {
        final Program program = new Program(codes);
        program.run();
        return program.getResult();
    }

    /** Runs the given codes with the specified input and returns the run Program. */
    public static Program runProgram(final Supplier<Integer> input, final int... codes) {
        final Program program = new Program(input.get(), codes);
        program.run();
        return program;
    }
}

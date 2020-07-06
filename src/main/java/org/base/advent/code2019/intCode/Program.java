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

//    static int FOO = 0;
    int param(final int offset, final String fullOpCode, final int opCodeIndex) {
//        if (++FOO > 50) throw new RuntimeException("STOP");
//        System.out.printf("FOO => fullOpCode=%s, opCodeIndex=%d%n", fullOpCode, opCodeIndex);
//        System.out.printf("BAR => index=%d, offset=%d, rlen=%d%n", index, offset, result.length);

        try {
            return ('0' == fullOpCode.charAt(opCodeIndex)) ? result[result[index + offset]] : result[index + offset];
        }
        catch (Exception ex) {
            System.out.printf("FOOX => fullOpCode=%s, opCodeIndex=%d%n", fullOpCode, opCodeIndex);
            System.out.printf("BARX => index=%d, offset=%d, rlen=%d%n", index, offset, result.length);
            return 0;
        }
    }
    public void run() {
        while (index < result.length) {
            final int baseOpCode = result[index];
            final String fullOpCode = StringUtils.leftPad(String.valueOf(result[index]), 5, '0');
            final int param1 = param(1, fullOpCode, 2);
//            System.out.println("PARAM1 => "+ param1);
            switch (baseOpCode % 100) {
                case 1:
                    result[result[index + 3]] = param1 + param(2, fullOpCode, 1);
                    index += 4;
                    break;
                case 2:
                    final int param2 = param(2, fullOpCode, 1);
//                    System.out.println("PARAM2 => "+ param2);
                    result[result[index + 3]] = param1 * param2;
                    index += 4;
                    break;
                case 3:
                    System.out.println("INPUT => "+ param1);
                    System.out.println("INPUTX=> "+ result[index + 1]);
//                    result[param1] = getInput();
                    if ('0' == fullOpCode.charAt(2))
                        result[result[index + 1]] = getInput();
                    else
                        result[index + 1] = getInput();
                    index += 2;
                    break;
                case 4:
                    final int output = result[result[index + 1]];
                    setOutput(output);
                    index += 2;
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

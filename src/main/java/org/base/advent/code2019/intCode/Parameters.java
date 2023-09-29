package org.base.advent.code2019.intCode;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.leftPad;

public class Parameters {
    private final Program program;
    private final String fullOpCode;
    private final int opCode;
    private long next;

    public Parameters(final Program p) {
        program = p;
        fullOpCode = leftPad(valueOf(program.get(program.index)), 5, '0');
        opCode = parseInt(fullOpCode.substring(3));
        next = program.index + switch (opCode()) {
            case 1,2,7,8 -> 4L;
            case 3,4,9 -> 2L;
            case 5,6 -> 3L;
            default -> throw new IllegalStateException(fullOpCode +" @ "+ program.index);
        };
    }

    public void jump(boolean condition, long distance) {
        if (condition)
            next = distance;
    }

    public long nextIndex() {
        return next;
    }

    public int opCode() {
        return opCode;
    }

    public long a() {
        return read(1, 2);
    }

    public long b() {
        return read(2, 1);
    }

    public long c1() {
        return write(1, 2);
    }
    public long c3() {
        return write(3, 0);
    }

    private long read(final long offset, final int opCodeIndex) {
        return switch (fullOpCode.charAt(opCodeIndex)) {
            case '0' -> program.get(get(offset));
            case '1' -> get(offset);
            case '2' -> program.get(get(offset) + program.relativeBase);
            default -> throw new RuntimeException("index");
        };
    }

    private long write(final long offset, final int opCodeIndex) {
        return switch (fullOpCode.charAt(opCodeIndex)) {
            case '0', '1' -> get(offset);
            case '2' -> get(offset) + program.relativeBase;
            default -> throw new RuntimeException("index");
        };
    }

    private long get(final long offset) {
        return program.get(program.index + offset);
    }
}

package org.base.advent.code2015;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.base.advent.Solution;

/**
 * <h2>Part 1</h2>
 * Little Jane Marie just got her very first computer for Christmas from some unknown benefactor. It comes
 * with instructions and an example program, but the computer itself seems to be malfunctioning. She's curious
 * what the program does, and would like you to help her run it.
 *
 * The manual explains that the computer supports two registers and six instructions (truly, it goes on to remind
 * the reader, a state-of-the-art technology). The registers are named a and b, can hold any non-negative integer,
 * and begin with a value of 0. The instructions are as follows:
 *
 *  - hlf r sets register r to half its current value, then continues with the next instruction.
 *  - tpl r sets register r to triple its current value, then continues with the next instruction.
 *  - inc r increments register r, adding 1 to it, then continues with the next instruction.
 *  - jmp offset is a jump; it continues with the instruction offset away relative to itself.
 *  - jie r, offset is like jmp, but only jumps if register r is even ("jump if even").
 *  - jio r, offset is like jmp, but only jumps if register r is 1 ("jump if one", not odd).
 *
 * All three jump instructions work with an offset relative to that instruction. The offset is always written with
 * a prefix + or - to indicate the direction of the jump (forward or backward, respectively). For example, jmp +1
 * would simply continue with the next instruction, while jmp +0 would continuously jump back to itself forever.
 *
 * The program exits when it tries to run an instruction beyond the ones defined.
 *
 * For example, this program sets a to 2, because the jio instruction causes it to skip the tpl instruction:
 * inc a
 * jio a, +2
 * tpl a
 * inc a
 *
 * What is the value in register b when the program in your puzzle input is finished executing?
 *
 * <h2>Part 2</h2>
 * The unknown benefactor is very thankful for releasi-- er, helping little Jane Marie with her computer.
 * Definitely not to distract you, what is the value in register b after the program is finished executing
 * if register a starts as 1 instead?
 *
 */
public class Day23 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input23.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return solve(getInput(), 0, 0);
    }

    @Override
    public Object solvePart2() throws Exception {
        return solve(getInput(), 1, 0);
    }

    public int solve(final List<String> instructions, final int a, final int b) {
        final Map<String, Integer> registers = new HashMap<>();
        registers.put("a", a);
        registers.put("b", b);
        followInstructions(registers, instructions, 0);
        return registers.get("b");
    }
    
    protected void followInstructions(final Map<String, Integer> registers, final List<String> instructions, final int index) {
        if (index < 0 || index >= instructions.size())
            return;

        final String[] tokens = instructions.get(index).replace(",", "").split("\\s");
        switch (tokens[0]) {
            case "hlf":
                final Integer hlf = registers.get(tokens[1]);
                registers.put(tokens[1], hlf / 2);
                followInstructions(registers, instructions, index + 1);
                break;
            case "tpl":
                final Integer tpl = registers.get(tokens[1]);
                registers.put(tokens[1], tpl * 3);
                followInstructions(registers, instructions, index + 1);
                break;
            case "inc":
                final Integer inc = registers.get(tokens[1]);
                registers.put(tokens[1], inc + 1);
                followInstructions(registers, instructions, index + 1);
                break;
            case "jmp":
                followInstructions(registers, instructions, index + Integer.parseInt(tokens[1]));
                break;
            case "jie":
                final Integer jie = registers.get(tokens[1]);
                if ((jie % 2) == 0)
                    followInstructions(registers, instructions, index + Integer.parseInt(tokens[2]));
                else
                    followInstructions(registers, instructions, index + 1);
                break;
            case "jio":
                final Integer jio = registers.get(tokens[1]);
                if (jio == 1)
                    followInstructions(registers, instructions, index + Integer.parseInt(tokens[2]));
                else
                    followInstructions(registers, instructions, index + 1);
                break;
        }
    }
}

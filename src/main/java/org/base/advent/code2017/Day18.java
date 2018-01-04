package org.base.advent.code2017;

import lombok.Getter;
import org.apache.commons.lang3.math.NumberUtils;
import org.base.advent.Solution;

import java.io.IOException;
import java.util.*;


/**
 * <h2>Part 1</h2>
 * You discover a tablet containing some strange assembly code labeled simply "Duet". Rather than bother the sound
 * card with it, you decide to run the code yourself. Unfortunately, you don't see any documentation, so you're left
 * to figure out what the instructions mean on your own.
 *
 * It seems like the assembly is meant to operate on a set of registers that are each named with a single letter and
 * that can each hold a single integer. You suppose each register should start with a value of 0.
 *
 * There aren't that many instructions, so it shouldn't be hard to figure out what they do. Here's what you determine:
 * <pre>
 *     - snd X plays a sound with a frequency equal to the value of X.
 *     - set X Y sets register X to the value of Y.
 *     - add X Y increases register X by the value of Y.
 *     - mul X Y sets register X to the result of multiplying the value contained in register X by the value of Y.
 *     - mod X Y sets register X to the remainder of dividing the value contained in register X by the value of Y
 *              (that is, it sets X to the result of X modulo Y).
 *     - rcv X recovers the frequency of the last sound played, but only when the value of X is not zero.
 *              (If it is zero, the command does nothing.)
 *     - jgz X Y jumps with an offset of the value of Y, but only if the value of X is greater than zero.
 *              (An offset of 2 skips the next instruction, an offset of -1 jumps to the previous instruction, and so on.)
 * </pre>
 *
 * Many of the instructions can take either a register (a single letter) or a number. The value of a register is the
 * integer it contains; the value of a number is that number.
 *
 * After each jump instruction, the program continues with the instruction to which the jump jumped. After any other
 * instruction, the program continues with the next instruction. Continuing (or jumping) off either end of the program
 * terminates it.
 *
 * For example:
 * <pre>
 * set a 1
 * add a 2
 * mul a a
 * mod a 5
 * snd a
 * set a 0
 * rcv a
 * jgz a -1
 * set a 1
 * jgz a -2
 * </pre>
 * <pre>
 *  - The first four instructions set a to 1, add 2 to it, square it, and then set it to itself modulo 5,
 *      resulting in a value of 4.
 *  - Then, a sound with frequency 4 (the value of a) is played.
 *  - After that, a is set to 0, causing the subsequent rcv and jgz instructions to both be skipped
 *      (rcv because a is 0, and jgz because a is not greater than 0).
 *   Finally, a is set to 1, causing the next jgz instruction to activate, jumping back two instructions to
 *      another jump, which jumps again to the rcv, which ultimately triggers the recover operation.
 * </pre>
 *
 * At the time the recover operation is executed, the frequency of the last sound played is 4.
 *
 * What is the value of the recovered frequency (the value of the most recently played sound) the first time
 * a rcv instruction is executed with a non-zero value?
 *
 * <h2>Part 2</h2>
 * As you congratulate yourself for a job well done, you notice that the documentation has been on the back of the
 * tablet this entire time. While you actually got most of the instructions correct, there are a few key differences.
 * This assembly code isn't about sound at all - it's meant to be run twice at the same time.
 *
 * Each running copy of the program has its own set of registers and follows the code independently - in fact,
 * the programs don't even necessarily run at the same speed. To coordinate, they use the send (snd) and
 * receive (rcv) instructions:
 *
 *  - snd X sends the value of X to the other program. These values wait in a queue until that program is ready
 *          to receive them. Each program has its own message queue, so a program can never receive a message it sent.
 *  - rcv X receives the next value and stores it in register X. If no values are in the queue, the program waits
 *          for a value to be sent to it. Programs do not continue to the next instruction until they have received a
 *          value. Values are received in the order they are sent.
 *
 * Each program also has its own program ID (one 0 and the other 1); the register p should begin with this value.
 *
 * For example:
 * <pre>
 * snd 1
 * snd 2
 * snd p
 * rcv a
 * rcv b
 * rcv c
 * rcv d
 * </pre>
 *
 * Both programs begin by sending three values to the other. Program 0 sends 1, 2, 0; program 1 sends 1, 2, 1.
 * Then, each program receives a value (both 1) and stores it in a, receives another value (both 2) and stores it in b,
 * and then each receives the program ID of the other program (program 0 receives 1; program 1 receives 0) and
 * stores it in c. Each program now sees a different value in its own copy of register c.
 *
 * Finally, both programs try to rcv a fourth time, but no data is waiting for either of them, and they reach a
 * deadlock. When this happens, both programs terminate.
 *
 * It should be noted that it would be equally valid for the programs to run at different speeds; for example,
 * program 0 might have sent all three values and then stopped at the first rcv before program 1 executed even
 * its first instruction.
 *
 * Once both of your programs have terminated (regardless of what caused them to do so),
 * how many times did program 1 send a value?
 *
 */
public class Day18 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2017/input18.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return applyInstructions(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return getInput();
    }

    /*
     *     - snd X plays a sound with a frequency equal to the value of X.
     *     - set X Y sets register X to the value of Y.
     *     - add X Y increases register X by the value of Y.
     *     - mul X Y sets register X to the result of multiplying the value contained in register X by the value of Y.
     *     - mod X Y sets register X to the remainder of dividing the value contained in register X by the value of Y
     *              (that is, it sets X to the result of X modulo Y).
     *     - rcv X recovers the frequency of the last sound played, but only when the value of X is not zero.
     *              (If it is zero, the command does nothing.)
     *     - jgz X Y jumps with an offset of the value of Y, but only if the value of X is greater than zero.
     *              (An offset of 2 skips the next instruction, an offset of -1 jumps to the previous instruction, and so on.)
     */
    public long applyInstructions(final List<String> instructions) {
        final Tablet tablet = new Tablet();
        int i = 0;
        for (;i < instructions.size();) {
            if (!tablet.getRecoveredSounds().isEmpty()) break;  // we've recovered a sound, stop!
            final String step = instructions.get(i);
//            System.out.println(step);
            final String[] strings = step.split("\\s");
            final String X = strings[1];
            switch (strings[0]) {
                case "snd":
                    tablet.getSoundsPlayed().push(tablet.getRegisters().get(X));
                    break;
                case "set":
                    tablet.setValue(X, strings[2]);
                    break;
                case "add":
                    tablet.addValue(X, strings[2]);
                    break;
                case "mul":
                    tablet.multiplyValue(X, strings[2]);
                    break;
                case "mod":
                    tablet.moduloValue(X, strings[2]);
                    break;
                case "rcv":
                    tablet.receiveValue(X);
                    break;
                case "jgz":
                    if ((!NumberUtils.isDigits(X) && tablet.getRegisters().getOrDefault(X, 0L) > 0)
                        ||
                        NumberUtils.toLong(X, 0) > 0) {
                        // do jump b/c X > 0
                        final long offset = Long.parseLong(strings[2]);
                        if (offset + i < 0 || offset + i >= instructions.size())
                            break;
                        else
                            i += offset;

                        continue;
                    }
                    break;
                default:

                    break;
            }
            ++i;
        }

        debug("%s", tablet.getRecoveredSounds());
        debug("%s", tablet.getSoundsPlayed());
        return tablet.getRecoveredSounds().get(0);
    }

    @Getter
    private static class Tablet {
        private final Map<String, Long> registers = new TreeMap<>();
        private final Stack<Long> soundsPlayed = new Stack<>();
        private final List<Long> recoveredSounds = new ArrayList<>();

        public void addValue(final String register, final String value) {
            final long val = getRegisters().getOrDefault(register, 0L);
            setValue(register, val + Long.parseLong(value));
        }

        public void moduloValue(final String register, final String value) {
            final long val = getRegisters().getOrDefault(register, 0L);

            try {
                setValue(register, val % Long.parseLong(value));
            }
            catch (final NumberFormatException nfex) {
                setValue(register, val % getRegisters().getOrDefault(value, 0L));
            }
        }

        public void multiplyValue(final String register, final String value) {
            final long val = getRegisters().getOrDefault(register, 0L);

            try {
                setValue(register, val * Long.parseLong(value));
            }
            catch (final NumberFormatException nfex) {
                setValue(register, val * getRegisters().getOrDefault(value, 0L));
            }
        }

        public void receiveValue(final String register) {
            final long val = getRegisters().getOrDefault(register, 0L);
            if (val != 0)
                getRecoveredSounds().add(getSoundsPlayed().peek());
        }

        public void setValue(final String register, final long value) {
            getRegisters().put(register, value);
        }
        public void setValue(final String register, final String value) {
            try {
                setValue(register, Integer.parseInt(value));
            }
            catch (final NumberFormatException nfex) {
                setValue(register, getRegisters().getOrDefault(value, 0L));
            }

        }
    }
}

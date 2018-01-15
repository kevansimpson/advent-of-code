package org.base.advent.code2017.day18;

import lombok.Getter;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

/**
 * Base tablet for {@link org.base.advent.code2017.Day18} solution.
 */
@Getter
public class Tablet {
    private final Map<String, Long> registers = new TreeMap<>();
    private final Stack<Long> soundsPlayed = new Stack<>();
    private final List<Long> recoveredSounds = new ArrayList<>();

    public long applyInstructions(final List<String> instructions) throws Exception {
        int i = 0;
        for (;i < instructions.size();) {
            if (!getRecoveredSounds().isEmpty())
                break;  // we've recovered a sound, stop!

            final String[] strings = instructions.get(i).split("\\s");
            final String X = strings[1];
            final Long Y = strings.length <= 2 ? 0L
                    : (NumberUtils.toLong(strings[2], get(strings[2])));
            switch (strings[0]) {
                case "snd":
                    listen(X);
                    break;
                case "set":
                    set(X, Y);
                    break;
                case "add":
                    add(X, Y);
                    break;
                case "mul":
                    multiply(X, Y);
                    break;
                case "mod":
                    modulo(X, Y);
                    break;
                case "rcv":
                    receive(X);
                    break;
                case "jgz":
                    if (NumberUtils.toLong(X, get(X)) > 0) {
                        // do jump b/c X > 0
                        if (Y + i < 0 || Y + i >= instructions.size())
                            break;  // jumped out of instructions
                        else
                            i += Y; // might as well...

                        continue;
                    }
                    break;
                default:

                    break;
            }
            ++i;
        }

        return getRecoveredSounds().get(0);
    }

    public void add(final String register, final long value) {
        set(register, get(register) + value);
    }

    public long get(final String register) {
        return getRegisters().getOrDefault(register, 0L);
    }

    public void listen(final String register) throws Exception {
        getSoundsPlayed().push(get(register));
    }

    public void modulo(final String register, final long value) {
        set(register, get(register) % value);
    }

    public void multiply(final String register, final long value) {
        set(register, get(register) * value);
    }

    public void receive(final String register) {
        final long val = getRegisters().getOrDefault(register, 0L);
        if (val != 0)
            getRecoveredSounds().add(getSoundsPlayed().peek());
    }

    public void set(final String register, final long value) {
        getRegisters().put(register, value);
    }

}

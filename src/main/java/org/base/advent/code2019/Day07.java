package org.base.advent.code2019;

import org.apache.commons.lang3.ArrayUtils;
import org.base.advent.Solution;
import org.base.advent.code2019.intCode.Amplifier;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;


/**
 * <h2>Part 1</h2>
 * Based on the navigational maps, you're going to need to send more power to your ship's thrusters to reach Santa in
 * time. To do this, you'll need to configure a series of amplifiers already installed on the ship.
 *
 * There are five amplifiers connected in series; each one receives an input signal and produces an output signal. They
 * are connected such that the first amplifier's output leads to the second amplifier's input, the second amplifier's
 * output leads to the third amplifier's input, and so on. The first amplifier's input value is 0, and the last
 * amplifier's output leads to your ship's thrusters.
 * <pre>
 *     O-------O  O-------O  O-------O  O-------O  O-------O
 * 0 ->| Amp A |->| Amp B |->| Amp C |->| Amp D |->| Amp E |-> (to thrusters)
 *     O-------O  O-------O  O-------O  O-------O  O-------O
 * </pre>
 *
 * The Elves have sent you some Amplifier Controller Software (your puzzle input), a program that should run on your
 * existing Intcode computer. Each amplifier will need to run a copy of the program.
 *
 * When a copy of the program starts running on an amplifier, it will first use an input instruction to ask the
 * amplifier for its current phase setting (an integer from 0 to 4). Each phase setting is used exactly once, but the
 * Elves can't remember which amplifier needs which phase setting.
 *
 * The program will then call another input instruction to get the amplifier's input signal, compute the correct output
 * signal, and supply it back to the amplifier with an output instruction. (If the amplifier has not yet received an
 * input signal, it waits until one arrives.)
 *
 * Your job is to find the largest output signal that can be sent to the thrusters by trying every possible combination
 * of phase settings on the amplifiers. Make sure that memory is not shared or reused between copies of the program.
 *
 * For example, suppose you want to try the phase setting sequence 3,1,2,4,0, which would mean setting amplifier A to
 * phase setting 3, amplifier B to setting 1, C to 2, D to 4, and E to 0. Then, you could determine the output signal
 * that gets sent from amplifier E to the thrusters with the following steps:
 * <ul>
 *     <li>Start the copy of the amplifier controller software that will run on amplifier A. At its first input
 *         instruction, provide it the amplifier's phase setting, 3. At its second input instruction, provide it the
 *         input signal, 0. After some calculations, it will use an output instruction to indicate the amplifier's
 *         output signal.</li>
 *     <li>Start the software for amplifier B. Provide it the phase setting (1) and then whatever output signal was
 *         produced from amplifier A. It will then produce a new output signal destined for amplifier C.</li>
 *     <li>Start the software for amplifier C, provide the phase setting (2) and the value from amplifier B, then
 *         collect its output signal.</li>
 *     <li>Run amplifier D's software, provide the phase setting (4) and input value, and collect its output signal.</li>
 *     <li>Run amplifier E's software, provide the phase setting (0) and input value, and collect its output signal.</li>
 * </ul>
 *
 * The final output signal from amplifier E would be sent to the thrusters. However, this phase setting sequence may not
 * have been the best one; another sequence might have sent a higher signal to the thrusters.
 *
 * Try every combination of phase settings on the amplifiers. What is the highest signal that can be sent to the
 * thrusters?
 *
 * <h2>Part 2</h2>
 * It's no good - in this configuration, the amplifiers can't generate a large enough output signal to produce the
 * thrust you'll need. The Elves quickly talk you through rewiring the amplifiers into a feedback loop:
 * <pre>
 *       O-------O  O-------O  O-------O  O-------O  O-------O
 * 0 -+->| Amp A |->| Amp B |->| Amp C |->| Amp D |->| Amp E |-.
 *    |  O-------O  O-------O  O-------O  O-------O  O-------O |
 *    |                                                        |
 *    '--------------------------------------------------------+
 *                                                             |
 *                                                             v
 *                                                      (to thrusters)
 * </pre>
 *
 * Most of the amplifiers are connected as they were before; amplifier A's output is connected to amplifier B's input,
 * and so on. However, the output from amplifier E is now connected into amplifier A's input. This creates the feedback
 * loop: the signal will be sent through the amplifiers many times.
 *
 * In feedback loop mode, the amplifiers need totally different phase settings: integers from 5 to 9, again each used
 * exactly once. These settings will cause the Amplifier Controller Software to repeatedly take input and produce output
 * many times before halting. Provide each amplifier its phase setting at its first input instruction; all further
 * input/output instructions are for signals.
 *
 * Don't restart the Amplifier Controller Software on any amplifier during this process. Each one should continue
 * receiving and sending signals until it halts.
 *
 * All signals sent or received in this process will be between pairs of amplifiers except the very first signal and the
 * very last signal. To start the process, a 0 signal is sent to amplifier A's input exactly once.
 *
 * Eventually, the software on the amplifiers will halt after they have processed the final loop. When this happens, the
 * last output signal from amplifier E is sent to the thrusters. Your job is to find the largest output signal that can
 * be sent to the thrusters using the new phase settings and feedback loop arrangement.
 *
 * Try every combination of the new phase settings on the amplifier feedback loop. What is the highest signal that can
 * be sent to the thrusters?
 */
public class Day07 implements Solution<int[]> {

    private final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    @Override
    public int[] getInput() throws IOException {
        return readNumbersCSV("/2019/input07.txt");
    }

    @Override
    public Integer solvePart1() throws Exception {
        return maxThrusterSignal(true, getInput());
    }

    @Override
    public Integer solvePart2() throws Exception {
        return maxThrusterSignal(false, getInput());
    }

    public int calcThrust(final boolean feedback, final int[] boosts, final int... codes) {
        final Amplifier[] amps = stackAmps(boosts, codes);
        // set A input
        amps[0].setInput(new Amplifier.SignalBoost(boosts[0], feedback ? amps[4] : null));
        Stream.of(amps).forEach(a -> threadPool.execute(a));
        return amps[4].getOutput();
    }

    public int maxThrusterSignal(final boolean feedback, final int... codes) {
        final Map<String, Integer> maxThrust = new TreeMap<>();
        final int[] signals = feedback ? new int[] {0,1,2,3,4} : new int[] {5,6,7,8,9};
        for (List<Integer> list : permute(signals)) {
            final int[] boosts = list.stream().mapToInt(Integer::intValue).toArray();
            final int thrust = calcThrust(feedback, boosts, codes);
            maxThrust.put(ArrayUtils.toString(boosts), thrust);
        }

        return maxThrust.values().stream().max(Comparator.naturalOrder()).orElseThrow();
    }

    protected Amplifier[] stackAmps(final int[] boosts, final int... codes) {
        final Amplifier a = new Amplifier("A", codes);
        final Amplifier b = new Amplifier("B", codes);
        final Amplifier c = new Amplifier("C", codes);
        final Amplifier d = new Amplifier("D", codes);
        final Amplifier e = new Amplifier("E", codes);
        b.setInput(new Amplifier.SignalBoost(boosts[1], a));
        c.setInput(new Amplifier.SignalBoost(boosts[2], b));
        d.setInput(new Amplifier.SignalBoost(boosts[3], c));
        e.setInput(new Amplifier.SignalBoost(boosts[4], d));

        return new Amplifier[] { a, b, c, d, e };
    }

    public List<List<Integer>> permute(int[] num) {
        List<List<Integer>> result = new ArrayList<>();

        //start from an empty list
        result.add(new ArrayList<>());

        for (int i = 0; i < num.length; i++) {
            //list of list in current iteration of the array num
            List<List<Integer>> current = new ArrayList<>();

            for (List<Integer> l : result) {
                // # of locations to insert is largest index + 1
                for (int j = 0; j < l.size()+1; j++) {
                    // + add num[i] to different locations
                    l.add(j, num[i]);

                    ArrayList<Integer> temp = new ArrayList<>(l);
                    current.add(temp);

                    //System.out.println(temp);

                    // - remove num[i] add
                    l.remove(j);
                }
            }

            result = new ArrayList<>(current);
        }

        return result;
    }
}

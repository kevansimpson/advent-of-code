package org.base.advent.code2018;

import org.base.advent.Solution;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;


/**
 * <h2>Part 1</h2>
 * "We've detected some temporal anomalies," one of Santa's Elves at the Temporal Anomaly Research and
 * Detection Instrument Station tells you. She sounded pretty worried when she called you down here.
 * "At 500-year intervals into the past, someone has been changing Santa's history!"
 *
 * "The good news is that the changes won't propagate to our time stream for another 25 days, and we have
 * a device" - she attaches something to your wrist - "that will let you fix the changes with no such
 * propagation delay. It's configured to send you 500 years further into the past every few days; that was
 * the best we could do on such short notice."
 *
 * "The bad news is that we are detecting roughly fifty anomalies throughout time; the device will indicate
 * fixed anomalies with stars. The other bad news is that we only have one device and you're the best person
 * for the job! Good lu--" She taps a button on the device and you suddenly feel like you're falling.
 *
 * After feeling like you've been falling for a few minutes, you look at the device's tiny screen.
 * "Error: Device must be calibrated before first use. Frequency drift detected. Cannot maintain destination
 * lock." Below the message, the device shows a sequence of changes in frequency (your puzzle input). A value
 * like +6 means the current frequency increases by 6; a value like -3 means the current frequency decreases by 3.
 *
 * For example, if the device displays frequency changes of +1, -2, +3, +1,
 * then starting from a frequency of zero, the following changes would occur:
 *
 *  - Current frequency  0, change of +1; resulting frequency  1.
 *  - Current frequency  1, change of -2; resulting frequency -1.
 *  - Current frequency -1, change of +3; resulting frequency  2.
 *  - Current frequency  2, change of +1; resulting frequency  3.
 *
 * Starting with a frequency of zero, what is the resulting frequency after all of the changes in frequency
 * have been applied?
 *
 * <h2>Part 2</h2>
 * You notice that the device repeats the same frequency change list over and over. To calibrate the device,
 * you need to find the first frequency it reaches twice.
 *
 * For example, using the same list of changes above, the device would loop as follows:
 *
 *  - Current frequency  0, change of +1; resulting frequency  1.
 *  - Current frequency  1, change of -2; resulting frequency -1.
 *  - Current frequency -1, change of +3; resulting frequency  2.
 *  - Current frequency  2, change of +1; resulting frequency  3.
 *                                          (At this point, the device continues from the start of the list.)
 *  - Current frequency  3, change of +1; resulting frequency  4.
 *  - Current frequency  4, change of -2; resulting frequency  2, which has already been seen.
 *
 * In this example, the first frequency reached twice is 2. Note that your device might need to repeat its
 * list of frequency changes many times before a duplicate frequency is found, and that duplicates might be
 * found while in the middle of processing the list.
 */
public class Day01 implements Solution<List<Integer>> {

    @Override
    public List<Integer> getInput() throws IOException {
        return readNumbers("/2018/input01.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return sum(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return findDuplicateFrequency(getInput());
    }

    public int sum(final List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).sum();
    }

    public int findDuplicateFrequency(final List<Integer> list) {
        final Set<Integer> frequencies = new TreeSet<>();
        int sum = 0;
        frequencies.add(sum);

        while (true) {
            for (int value : list) {
                sum += value;
                if (frequencies.contains(sum))
                    return sum;
                else
                    frequencies.add(sum);
            }
        }
    }
}

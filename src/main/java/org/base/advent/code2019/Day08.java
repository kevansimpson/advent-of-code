package org.base.advent.code2019;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Stream;


/**
 * <h2>Part 1</h2>
 * The Elves' spirits are lifted when they realize you have an opportunity to reboot one of their Mars rovers, and so
 * they are curious if you would spend a brief sojourn on Mars. You land your ship near the rover.
 *
 * When you reach the rover, you discover that it's already in the process of rebooting! It's just waiting for someone
 * to enter a BIOS password. The Elf responsible for the rover takes a picture of the password (your puzzle input) and
 * sends it to you via the Digital Sending Network.
 *
 * Unfortunately, images sent via the Digital Sending Network aren't encoded with any normal encoding; instead, they're
 * encoded in a special Space Image Format. None of the Elves seem to remember why this is the case. They send you the
 * instructions to decode it.
 *
 * Images are sent as a series of digits that each represent the color of a single pixel. The digits fill each row of
 * the image left-to-right, then move downward to the next row, filling rows top-to-bottom until every pixel of the
 * image is filled.
 *
 * Each image actually consists of a series of identically-sized layers that are filled in this way. So, the first digit
 * corresponds to the top-left pixel of the first layer, the second digit corresponds to the pixel to the right of that
 * on the same layer, and so on until the last digit, which corresponds to the bottom-right pixel of the last layer.
 *
 * For example, given an image 3 pixels wide and 2 pixels tall, the image data 123456789012 corresponds to the following
 * image layers:
 * <pre>
 * Layer 1: 123
 *          456
 *
 * Layer 2: 789
 *          012
 * </pre>
 * The image you received is 25 pixels wide and 6 pixels tall.
 *
 * To make sure the image wasn't corrupted during transmission, the Elves would like you to find the layer that
 * contains the fewest 0 digits. On that layer, what is the number of 1 digits multiplied by the number of 2 digits?
 *
 * <h2>Part 2</h2>
 */
public class Day08 implements Solution<String> {

    @Override
    public String getInput() throws IOException {
        return readInput("/2019/input08.txt");
    }

    @Override
    public Integer solvePart1() throws Exception {
        return zeroLayer(getInput());
    }

    @Override
    public String solvePart2() throws Exception {
        return drawImage(getInput());
    }

    public String drawImage(final String rawImage, final int... dims) {
        final String[] layers = toLayers(rawImage, dims);
        char[] decoded = layers[0].toCharArray();

        for (int i = 1; i < layers.length; i++) {
            String layer = layers[i];
            for (int p = 0; p < layer.length(); p++) {
                char top_p = decoded[p];
                char current_p = layer.charAt(p);

                // "0 is black, 1 is white, and 2 is transparent."
                if (top_p == '2') {
                    decoded[p] = current_p;
                }
            }
        }
        return new String(decoded);
    }

    public int zeroLayer(final String image, final int... dims) {
        //noinspection OptionalGetWithoutIsPresent
        return Stream.of(toLayers(image, dims))
                .min(Comparator.comparingInt(l -> StringUtils.countMatches(l, "0")))
                .stream()
                .mapToInt(str -> StringUtils.countMatches(str, "1") * StringUtils.countMatches(str, "2"))
                .min().getAsInt();
    }

    String[] toLayers(final String image, final int... dims) {
        final int w = ArrayUtils.getLength(dims) > 0 ? dims[0] : 25;
        final int h = ArrayUtils.getLength(dims) > 1 ? dims[1] : 6;

        return image.split(String.format("(?<=\\G.{%d})", (w * h)));
    }
}

package org.base.advent.code2019;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2019/day/08">Day 08</a>
 */
public class Day08 implements Solution<String> {
    @Getter
    private final String input =  readInput("/2019/input08.txt");

    @Override
    public Integer solvePart1() {
        return zeroLayer(getInput());
    }

    @Override
    public String solvePart2() {
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

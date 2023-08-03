package org.base.advent;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reads puzzles.
 */
public interface PuzzleReader extends Helpers {

    default List<String> readCachedLines(final String filename, final Supplier<List<String>> computer) {
        return cache() ? readLines(filename) : computer.get();
    }

    default String readInput(final String filename) {
        try {
            return IOUtils.resourceToString(filename, Charset.defaultCharset());
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    default List<String> readLines(final String filename) {
        return IOUtils.readLines(
                Objects.requireNonNull(getClass().getResourceAsStream(filename)), Charset.defaultCharset());
    }

    default List<Integer> readNumbers(final String filename) {
        return readLines(filename).stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    default List<String[]> readCSVLines(final String filename) {
        return readLines(filename).stream().map(line -> line.split("\\s*,\\s*")).collect(Collectors.toList());
    }

    default int[] readNumbersCSV(final String filename) {
        return Stream.of(readInput(filename).split("\\s*,\\s*")).mapToInt(Integer::parseInt).toArray();
    }
}

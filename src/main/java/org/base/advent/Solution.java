package org.base.advent;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Supplier;


/**
 * Identifies two part solutions from an Advent of Code problem.
 */
public interface Solution<T> extends Helpers {

    T getInput() throws IOException ;

    Object solvePart1() throws Exception;
    Object solvePart2() throws Exception;

    default String readCachedInput(final String filename, final Supplier<String> computer) throws IOException {
        return cache() ? readInput(filename) : computer.get();
    }

    default List<String> readCachedLines(final String filename, final Supplier<List<String>> computer) throws IOException {
        return cache() ? readLines(filename) : computer.get();
    }

    default String readInput(final String filename) throws IOException {
        return IOUtils.resourceToString(filename, Charset.defaultCharset());
    }

    default List<String> readLines(final String filename) throws IOException {
        return IOUtils.readLines(getClass().getResourceAsStream(filename), Charset.defaultCharset());
    }
}

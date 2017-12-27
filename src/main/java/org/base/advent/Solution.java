package org.base.advent;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


/**
 * Identifies two part solutions from an Advent of Code problem.
 */
public interface Solution<T> extends Helpers {

    T getInput() throws IOException ;

    Object solvePart1() throws Exception;
    Object solvePart2() throws Exception;

    default String readInput(final String filename) throws IOException {
        return IOUtils.resourceToString(filename, Charset.defaultCharset());
    }

    default List<String> readLines(final String filename) throws IOException {
        return IOUtils.readLines(getClass().getResourceAsStream(filename), Charset.defaultCharset());
    }
}

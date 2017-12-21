package org.base.advent;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;


/**
 * Identifies two part solutions from an Advent of Code problem.
 */
public interface Solution<T> {

	T getInput() throws IOException ;

	Object solvePart1() throws Exception;
	Object solvePart2() throws Exception;

	default String readInput(final String filename) throws IOException {
		return IOUtils.resourceToString(filename, Charset.defaultCharset());
	}

	default List<String> readLines(final String filename) throws IOException {
		return IOUtils.readLines(getClass().getResourceAsStream(filename), Charset.defaultCharset());
	}

	default String sortString(final String input) {
		final char tempArray[] = input.toCharArray();
		Arrays.sort(tempArray);
		return new String(tempArray);
	}

	default boolean debug() {
		return false;
	}

	default void debug(final String message, final Object... args) {
		if (debug())
			//noinspection UseOfSystemOutOrSystemErr
			System.out.println(String.format(message, args));
	}
}

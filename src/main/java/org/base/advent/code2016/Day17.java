package org.base.advent.code2016;

import org.base.advent.util.Point;
import org.base.advent.util.ReusableDigest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static org.base.advent.util.HashAtIndex.hexCharToInt;
import static org.base.advent.util.Point.inGrid;

/**
 * <a href="https://adventofcode.com/2016/day/17">Day 17</a>
 */
public class Day17 implements Function<String, Day17.VaultPaths> {
    public record VaultPaths(String shortest, int longest) {}

    record VaultAttempt(Point position, String path) {}

    private static final String UDLR = "UDLR";
    private final ReusableDigest digest = new ReusableDigest();

    @Override
    public VaultPaths apply(String input) {
        final Point vaultInGrid = Point.of(7, 7);
        final List<VaultAttempt> attempts = new ArrayList<>();
        attempts.add(new VaultAttempt(Point.of(1, 1), input));
        String shortest = null, longest = null;

        while (!attempts.isEmpty()) {
            final List<VaultAttempt> nextWave = new ArrayList<>(attempts);
            attempts.clear();

            for (VaultAttempt attempt : nextWave) {
                String udlr = digest.hashHex(attempt.path).substring(0, 4);
                Point pt = attempt.position;
                if (pt.equals(vaultInGrid)) {
                    if (shortest == null || attempt.path.length() < shortest.length())
                        shortest = attempt.path;

                    if (longest == null || attempt.path.length() > longest.length())
                        longest = attempt.path;

                    continue;
                }

                // vault grid is upside down, so are we
                Point[] moves = new Point[]{pt.down(2), pt.up(2), pt.left(2), pt.right(2)};
                for (int i = 0; i < 4; i++) {
                    if (inGrid(moves[i], 8, 8) && hexCharToInt(udlr.charAt(i)) > 10) { // open
                        attempts.add(new VaultAttempt(moves[i], attempt.path + UDLR.charAt(i)));
                    }
                }
            }
        }

        return new VaultPaths(
                requireNonNull(shortest).substring(input.length()),
                requireNonNull(longest).length() - input.length());
    }
}

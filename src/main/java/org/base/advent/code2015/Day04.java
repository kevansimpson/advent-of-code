package org.base.advent.code2015;

import org.base.advent.util.HashAtIndex;

import java.util.Objects;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.base.advent.util.HashAtIndex.nextWith;

/**
 * <a href="https://adventofcode.com/2015/day/04">Day 04</a>
 */
public class Day04 implements Function<String, Day04.HashedPswdIndexes> {
    public record HashedPswdIndexes(long first, long second) {}

    @Override
    public HashedPswdIndexes apply(String input) {
        HashAtIndex hai = new HashAtIndex(input, null, 1L);
        HashAtIndex first = nextWith(hai, hash -> startsWith(hash, "00000"));
        HashAtIndex second = nextWith(Objects.requireNonNull(first),
                hash -> startsWith(hash, "000000"));
        return new HashedPswdIndexes(first.index(), Objects.requireNonNull(second).index());
    }
}

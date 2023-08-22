package org.base.advent.code2022;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import static org.base.advent.util.Util.stringToSet;

/**
 * <a href="https://adventofcode.com/2022/day/06">Day 06</a>
 */
public class Day06 implements Solution<String> {
    @Override
    public Object solvePart1(final String input) {
        return findMarker(input, 4);
    }

    @Override
    public Object solvePart2(final String input) {
        return findMarker(input, 14);
    }

    int findMarker(final String str, final int packetSize) {
        for (int ix = 0; ix < str.length() - 1; ix++) {
            String packet = StringUtils.substring(str, ix, ix + packetSize);
            if (packet.length() == packetSize && stringToSet(packet).size() == packetSize)
                return ix + packetSize;
        }
        return -1;
    }
}

package org.base.advent.code2022;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Solution;

import static org.base.advent.util.Util.stringToSet;

/**
 * <a href="https://adventofcode.com/2022/day/06">Day 06</a>
 */
public class Day06 implements Solution<String> {
    @Getter
    private final String input = readInput("/2022/input06.txt");

    @Override
    public Object solvePart1() {
        return findMarker(input, 4);
    }

    @Override
    public Object solvePart2() {
        return findMarker(input, 14);
    }

    public int findMarker(String str, int packetSize) {
        for (int ix = 0; ix < str.length() - 1; ix++) {
            String packet = StringUtils.substring(str, ix, ix + packetSize);
            if (packet.length() == packetSize && stringToSet(packet).size() == packetSize)
                return ix + packetSize;
        }
        return -1;
    }
}

package org.base.advent.code2016;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.base.advent.util.Text.shiftText;

/**
 * <a href="https://adventofcode.com/2016/day/4">Day 4</a>
 */
public class Day04 implements Function<List<String>, Day04.RoomSectorInfo> {
    private static final Pattern REGEX = Pattern.compile("([\\-a-z]+)-(\\d+)\\[([a-z]+)]");

    public record RoomSectorInfo(int realSectorSum, int northPoleSector) {}
    public record Room(String name, int sectorId, String checksum) {}

    @Override
    public RoomSectorInfo apply(List<String> input) {
        int real = 0, northPoleRoomId = -1;
        for (Room room : scanRooms(input)) {
            if (isReal(room))
                real += room.sectorId;

            if (shiftText(room.name, room.sectorId).contains("northpole"))
                northPoleRoomId = room.sectorId;
        }

        return new RoomSectorInfo(real, northPoleRoomId);
    }

    boolean isReal(Room room) {
        List<Pair<Character, Integer>> pairs =
                room.name.replaceAll("-", "").chars()
                        .mapToObj(ch -> Pair.of((char) ch, StringUtils.countMatches(room.name, (char) ch)))
                        .distinct()
                        .sorted((o1, o2) -> {
                            int comp = o2.getRight() - o1.getRight();
                            return comp == 0 ? o1.getLeft() - o2.getLeft() : comp;
                        })
                        .toList();
        String code = pairs.stream().limit(5)
                .map(it -> String.valueOf(it.getKey())).collect(Collectors.joining());

        return code.equals(room.checksum);
    }

    List<Room> scanRooms(List<String> input) {
        return input.stream().map(scan -> {
            final Matcher matcher = REGEX.matcher(scan);
            if (matcher.matches())
                return new Room(matcher.group(1), Integer.parseInt(matcher.group(2)), matcher.group(3));
            else
                throw new RuntimeException("failed to scan room: "+ scan);
        }).toList();
    }
}

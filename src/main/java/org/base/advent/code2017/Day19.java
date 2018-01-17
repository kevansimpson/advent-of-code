package org.base.advent.code2017;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Point;
import org.base.advent.Solution;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <h2>Part 1</h2>
 * Somehow, a network packet got lost and ended up here. It's trying to follow a routing diagram (your puzzle input),
 * but it's confused about where to go.
 *
 * Its starting point is just off the top of the diagram. Lines (drawn with |, -, and +) show the path it needs to
 * take, starting by going down onto the only line connected to the top of the diagram. It needs to follow this path
 * until it reaches the end (located somewhere within the diagram) and stop there.
 *
 * Sometimes, the lines cross over each other; in these cases, it needs to continue going the same direction, and only
 * turn left or right when there's no other option. In addition, someone has left letters on the line; these also don't
 * change its direction, but it can use them to keep track of where it's been. For example:
 * <pre>
 *     |
 *     |  +--+
 *     A  |  C
 * F---|----E|--+
 *     |  |  |  D
 *     +B-+  +--+
 * </pre>
 *
 * Given this diagram, the packet needs to take the following path:
 *
 *  - Starting at the only line touching the top of the diagram, it must go down, pass through A, and continue
 *          onward to the first +.
 *  - Travel right, up, and right, passing through B in the process.
 *  - Continue down (collecting C), right, and up (collecting D).
 *  - Finally, go all the way left through E and stopping at F.
 *
 * Following the path to the end, the letters it sees on its path are ABCDEF.
 *
 * The little packet looks up at you, hoping you can help it find the way. What letters will it see (in the order it would
 * see them) if it follows the path? (The routing diagram is very wide; make sure you view it without line wrapping.)
 *
 * <h2>Part 2</h2>
 *
 */
public class Day19 implements Solution<List<Point>> {

    private Map<Point, String> grid;

    @Override
    public List<Point> getInput() throws IOException {
        final List<String> input = readLines("/2017/input19.txt");
        grid = buildGrid(input);
        return followPath(grid);
    }

    @Override
    public Object solvePart1() throws Exception {
        return toLetters(getInput(), grid);
    }

    @Override
    public Object solvePart2() throws Exception {
        return getInput().size();
    }

    public List<Point> followPath(final Map<Point, String> grid) {
        final Point start = findStart(grid);
        final List<Point> path = new ArrayList<>();
        path.add(start);

        Point next = start;
        int dX, dY;
        while (next != null) {
            final List<Point> cardinal = next.cardinal().stream()
                    .filter(pt -> grid.get(pt) != null)
                    .filter(pt -> !path.contains(pt))
                    .collect(Collectors.toList());

            if (cardinal.isEmpty())
                break;
            else if (cardinal.size() == 1) {
                Point point = cardinal.get(0);
                dX = point.x - next.x;
                dY = point.y - next.y;
                while (point != null) {
                    final String marker = grid.get(point);
                    if (marker == null)
                        break;
                    else if (StringUtils.equals("+", marker)) {
                        debug("turn %s", point);
                        path.add(point);
                        next = point;
                        break;
                    }
                    else {
                        debug("add  %s", point);
                        path.add(point);
                        point = point.right(dX).up(dY);
                    }
                }
            }
        }

        return path;
    }

    public String toLetters(final List<Point> path, final Map<Point, String> grid) {
        return path.stream()
                .map(grid::get)
                .collect(Collectors.joining())
                .replaceAll("-", "")
                .replaceAll("\\|", "")
                .replaceAll("\\+", "");
    }

    public Point findStart(final Map<Point, String> grid) {
        return grid.entrySet().stream().filter(e -> e.getKey().y == 0).findFirst().get().getKey();
    }

    public Map<Point, String> buildGrid(final List<String> points) {
        final Map<Point, String> grid = new HashMap<>();

        for (int y = 0; y < points.size(); y++) {
            final char[] line = points.get(y).toCharArray();
            for (int x = 0; x < line.length; x++) {
                if (line[x] != ' ')
                    grid.put(new Point(x, -y), String.valueOf(line[x]));
            }
        }

        return grid;
    }

}

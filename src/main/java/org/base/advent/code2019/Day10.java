package org.base.advent.code2019;

import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <h2>Part 1</h2>
 * You fly into the asteroid belt and reach the Ceres monitoring station. The Elves here have an emergency: they're
 * having trouble tracking all of the asteroids and can't be sure they're safe.
 *
 * The Elves would like to build a new monitoring station in a nearby area of space; they hand you a map of all of the
 * asteroids in that region (your puzzle input).
 *
 * The map indicates whether each position is empty (.) or contains an asteroid (#). The asteroids are much smaller than
 * they appear on the map, and every asteroid is exactly in the center of its marked position. The asteroids can be
 * described with X,Y coordinates where X is the distance from the left edge and Y is the distance from the top edge
 * (so the top-left corner is 0,0 and the position immediately to its right is 1,0).
 *
 * Your job is to figure out which asteroid would be the best place to build a new monitoring station. A monitoring
 * station can detect any asteroid to which it has direct line of sight - that is, there cannot be another asteroid
 * exactly between them. This line of sight can be at any angle, not just lines aligned to the grid or diagonally. The
 * best location is the asteroid that can detect the largest number of other asteroids.
 *
 * For example, consider the following map:
 * <pre>
 * .#..#
 * .....
 * #####
 * ....#
 * ...##
 * </pre>
 *
 * The best location for a new monitoring station on this map is the highlighted asteroid at 3,4 because it can
 * detect 8 asteroids, more than any other location. (The only asteroid it cannot detect is the one at 1,0; its view of
 * this asteroid is blocked by the asteroid at 2,2.) All other asteroids are worse locations; they can detect 7 or fewer
 * other asteroids. Here is the number of other asteroids a monitoring station on each asteroid could detect:
 * <pre>
 * .7..7
 * .....
 * 67775
 * ....7
 * ...87
 * </pre>
 *
 * Here is an asteroid (#) and some examples of the ways its line of sight might be blocked. If there were another
 * asteroid at the location of a capital letter, the locations marked with the corresponding lowercase letter would be
 * blocked and could not be detected:
 * <pre>
 * #.........
 * ...A......
 * ...B..a...
 * .EDCG....a
 * ..F.c.b...
 * .....c....
 * ..efd.c.gb
 * .......c..
 * ....f...c.
 * ...e..d..c
 * </pre>
 *
 * Find the best location for a new monitoring station. How many other asteroids can be detected from that location?
 *
 * <h2>Part 2</h2>
 * Once you give them the coordinates, the Elves quickly deploy an Instant Monitoring Station to the location and
 * discover the worst: there are simply too many asteroids.
 *
 * The only solution is complete vaporization by giant laser.
 *
 * Fortunately, in addition to an asteroid scanner, the new monitoring station also comes equipped with a giant rotating
 * laser perfect for vaporizing asteroids. The laser starts by pointing up and always rotates clockwise, vaporizing any
 * asteroid it hits.
 *
 * If multiple asteroids are exactly in line with the station, the laser only has enough power to vaporize one of them
 * before continuing its rotation. In other words, the same asteroids that can be detected can be vaporized, but if
 * vaporizing one asteroid makes another one detectable, the newly-detected asteroid won't be vaporized until the laser
 * has returned to the same position by rotating a full 360 degrees.
 *
 * For example, consider the following map, where the asteroid with the new monitoring station (and laser) is marked X:
 * <pre>
 * .#....#####...#..
 * ##...##.#####..##
 * ##...#...#.#####.
 * ..#.....X...###..
 * ..#.#.....#....##
 * </pre>
 * The first nine asteroids to get vaporized, in order, would be:
 * <pre>
 * .#....###24...#..
 * ##...##.13#67..9#
 * ##...#...5.8####.
 * ..#.....X...###..
 * ..#.#.....#....##
 * </pre>
 *
 * Note that some asteroids (the ones behind the asteroids marked 1, 5, and 7) won't have a chance to be vaporized until
 * the next full rotation. The laser continues rotating; the next nine to be vaporized are:
 * <pre>
 * .#....###.....#..
 * ##...##...#.....#
 * ##...#......1234.
 * ..#.....X...5##..
 * ..#.9.....8....76
 * </pre>
 * The next nine to be vaporized are then:
 * <pre>
 * .8....###.....#..
 * 56...9#...#.....#
 * 34...7...........
 * ..2.....X....##..
 * ..1..............
 * </pre>
 *
 * Finally, the laser completes its first full rotation (1 through 3), a second rotation (4 through 8), and vaporizes
 * the last asteroid (9) partway through its third rotation:
 * <pre>
 * ......234.....6..
 * ......1...5.....7
 * .................
 * ........X....89..
 * .................
 * </pre>
 *
 * In the large example above (the one with the best monitoring station location at 11,13):
 * <ul>
 *     <li>The 1st asteroid to be vaporized is at 11,12.</li>
 *     <li>The 2nd asteroid to be vaporized is at 12,1.</li>
 *     <li>The 3rd asteroid to be vaporized is at 12,2.</li>
 *     <li>The 10th asteroid to be vaporized is at 12,8.</li>
 *     <li>The 20th asteroid to be vaporized is at 16,0.</li>
 *     <li>The 50th asteroid to be vaporized is at 16,9.</li>
 *     <li>The 100th asteroid to be vaporized is at 10,16.</li>
 *     <li>The 199th asteroid to be vaporized is at 9,6.</li>
 *     <li>The 200th asteroid to be vaporized is at 8,2.</li>
 *     <li>The 201st asteroid to be vaporized is at 10,9.</li>
 *     <li>The 299th and final asteroid to be vaporized is at 11,1.</li>
 * </ul>
 *
 * The Elves are placing bets on which will be the 200th asteroid to be vaporized. Win the bet by determining which
 * asteroid that will be; what do you get if you multiply its X coordinate by 100 and then add its Y coordinate?
 * (For example, 8,2 becomes 802.)
 */
public class Day10 implements Solution<String[]> {

    @Override
    public String[] getInput() throws IOException {
        return readLines("/2019/input10.txt").toArray(new String[36]);
    }

    @Override
    public Integer solvePart1() throws Exception {
        return maxAsteroids(getInput());
    }

    @Override
    public Integer solvePart2() throws Exception {
        final Point _200th = vaporize(getInput());
        return _200th.x * 100 + _200th.y;
    }

    public int maxAsteroids(final String... rows) {
        return mapVisibleAsteroids(rows).values().stream().max(Comparator.naturalOrder()).orElse(-1);
    }

    public Point vaporize(final String... rows) {
        final Map<Point, Integer> asteroids = mapVisibleAsteroids(rows);
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        final Point deathStar = asteroids.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();
        final List<Point> vaporized = new ArrayList<>();

        while (vaporized.size() < 200) {
            final Map<Double, Point> visible = countVisibleAsteroids(deathStar, asteroids.keySet());
            for (double rad : visible.keySet()) {
                final Point gone = visible.get(rad);
                vaporized.add(gone);
                asteroids.remove(gone);
            }
        }

        return vaporized.get(199);
    }

    Map<Point, Integer> mapVisibleAsteroids(final String... rows) {
        final Set<Point> asteroids = scanAsteroids(rows);
        return asteroids.stream().collect(
                Collectors.toMap(pt -> pt, (Point pt) -> countVisibleAsteroids(pt, asteroids).size()));
    }

    /*
.#..#       [1,0]             [4,0]
.....
##### [0,2] [1,2] [2,2] [3,2] [4,2]
....#                         [4,3]
...##                   [3,4] [4,4]
     */
    Map<Double, Point> countVisibleAsteroids(final Point asteroid, final Set<Point> space) {
        final List<Point> graph = new ArrayList<>(space);
        graph.remove(asteroid);
        graph.sort(Comparator.comparing(a -> a.distance(asteroid)));
        final Map<Double, Point> angles = new TreeMap<>();
        for (final Point point : graph) {
            final double angle = asteroid.angle(point);
            if (!angles.containsKey(angle)) angles.put(angle, point);
        }

        return angles;
    }


    Set<Point> scanAsteroids(final String... rows) {
        final Set<Point> asteroids = new LinkedHashSet<>();
        for (int y = 0; y < rows.length; y++) {
            final String row = rows[y];
            for (int x = 0; x < row.length(); x++) {
                if (row.charAt(x) == '#') asteroids.add(Point.of(x, y));
            }
        }

        return asteroids;
    }
}

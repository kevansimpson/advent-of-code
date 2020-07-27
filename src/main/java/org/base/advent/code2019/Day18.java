package org.base.advent.code2019;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;
import org.base.advent.util.Node;
import org.base.advent.util.NodeContext;
import org.base.advent.util.Point;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <h2>Part 1</h2>
 * As you approach Neptune, a planetary security system detects you and activates a giant tractor beam on Triton! You
 * have no choice but to land.
 *
 * A scan of the local area reveals only one interesting feature: a massive underground vault. You generate a map of the
 * tunnels (your puzzle input). The tunnels are too narrow to move diagonally.
 *
 * Only one entrance (marked @) is present among the open passages (marked .) and stone walls (#), but you also detect
 * an assortment of keys (shown as lowercase letters) and doors (shown as uppercase letters). Keys of a given letter
 * open the door of the same letter: a opens A, b opens B, and so on. You aren't sure which key you need to disable the
 * tractor beam, so you'll need to collect all of them.
 *
 * For example, suppose you have the following map:
 * <pre>
 * #########
 * #b.A.@.a#
 * #########
 * </pre>
 * Starting from the entrance (@), you can only access a large door (A) and a key (a). Moving toward the door doesn't
 * help you, but you can move 2 steps to collect the key, unlocking A in the process:
 * <pre>
 * #########
 * #b.....@#
 * #########
 * </pre>
 * Then, you can move 6 steps to collect the only other key, b:
 * <pre>
 * #########
 * #@......#
 * #########
 * </pre>
 * So, collecting every key took a total of 8 steps.
 *
 * Here is a larger example:
 * <pre>
 * ########################
 * #f.D.E.e.C.b.A.@.a.B.c.#
 * ######################.#
 * #d.....................#
 * ########################
 * </pre>
 * The only reasonable move is to take key a and unlock door A:
 * <pre>
 * ########################
 * #f.D.E.e.C.b.....@.B.c.#
 * ######################.#
 * #d.....................#
 * ########################
 * </pre>
 * Then, do the same with key b:
 * <pre>
 * ########################
 * #f.D.E.e.C.@.........c.#
 * ######################.#
 * #d.....................#
 * ########################
 * </pre>
 * ...and the same with key c:
 * <pre>
 * ########################
 * #f.D.E.e.............@.#
 * ######################.#
 * #d.....................#
 * ########################
 * </pre>
 * Now, you have a choice between keys d and e. While key e is closer, collecting it now would be slower in the long run than collecting key d first, so that's the best choice:
 * <pre>
 * ########################
 * #f...E.e...............#
 * ######################.#
 * #@.....................#
 * ########################
 * </pre>
 * Finally, collect key e to unlock door E, then collect key f, taking a grand total of 86 steps.
 *
 * How many steps is the shortest path that collects all of the keys?
 *
 * <h2>Part 2</h2>
 *
 */
public class Day18 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2019/input18.txt");
    }

    @Override
    public Long solvePart1() throws Exception {
        return collectKeys(getInput(), Integer.MAX_VALUE).getLeft();
    }

    @Override
    public String solvePart2() throws Exception {
        return getInput().get(0);
    }

    private static final String GATE = "@";
    private static final String OPEN = ".";
    private static final String WALL = "#";

    /*                 11111111112222
             012345678901234567890123
        0    ########################
        1    #f.D.E.e.C.b.A.@.a.B.c.#   Entrance @ (15,1)
        2    ######################.#
        3    #d.....................#
        4    ########################
     */
    public Pair<Long, String[]> collectKeys(final List<String> input, final int ceiling) {
        final Vault vault = mapVault(input);
        vault.display();

        final VaultContext context = new VaultContext(vault.getEntrance(), vault, new ArrayList<>(), new TreeSet<>());
        context.getRecentlyVisited().add(vault.getEntrance());

        final Node<Point, VaultContext> entrance = new Node().setContext(context);
        List<Node<Point, VaultContext>> nodeList = new ArrayList<>();
        nodeList.add(entrance);
        Node<Point, VaultContext> shortest = null;

        int foo = 0;
        while (!nodeList.isEmpty() && ++foo < ceiling) {
            List<Node<Point, VaultContext>> nextNodes = new ArrayList<>();
            for (final Node<Point, VaultContext> node : nodeList) {
//                System.out.println(node.getDepth());
                final List<Point> surrounding = node.getContext().getNext();
//                System.out.println(node.getContext().getLocation() +" ==> "+ surrounding);

                for (final Point neighbor : surrounding) {
                    final VaultContext newContext = node.getContext().newContext(neighbor);
                    final Node<Point, VaultContext> next = node.add(newContext);

//                    final Path newPath = path.copy().add(neighbor);
                    final String space = node.getContext().getVault().getVault().get(neighbor);
                    if (StringUtils.isAllLowerCase(space) && !newContext.getOpenGates().contains(space.toUpperCase())) {
//                        System.out.println("KEY => "+ space +" @ "+ neighbor);
                        newContext.getOpenGates().add(space.toUpperCase());
                        newContext.getRecentlyVisited().clear();
                        newContext.getRecentlyVisited().add(neighbor);
                    }

                    if (newContext.getOpenGates().size() == vault.keysToFind()) {
                        System.out.println(next);
                        if (shortest == null || next.getDepth() < shortest.getDepth()) {
                            shortest = next;
                        }
                        else next.detach();
                    }
                    else if ((shortest == null || next.getDepth() < shortest.getDepth()) && next.getDepth() < ceiling) {
                        nextNodes.add(next);
//                        System.out.println("adding => "+ next);
                    }
                    else
                        next.detach();
                }

                nodeList = nextNodes;
            }
        }

        System.out.println("RESULT => "+ shortest);
        return Pair.of(shortest.getDepth() - 1, null);
    }

    @Accessors(chain = true)
    @Data
    private static class VaultContext implements NodeContext<Point> {
        private final Point location;
        private final Vault vault;
        private final List<Point> recentlyVisited;
        private final Set<String> openGates;

        public boolean hasVisited(final Point point) {
            if (getRecentlyVisited() != null)
                if (getRecentlyVisited().contains(point)) return true;

//            if (get)
            getRecentlyVisited().clear();
            getRecentlyVisited().add(getLocation());

            return false;
        }

        @Override
        public List<Point> getNext() {
            return getLocation().cardinal().stream()
                    .filter(pt -> isOpenSpace(pt) && !getRecentlyVisited().contains(pt))
                    .collect(Collectors.toList());
        }

        @Override
        public VaultContext newContext(Point next) {
            final List<Point> newVisited = new ArrayList<>(getRecentlyVisited());
            newVisited.add(next);
            return new VaultContext(next, getVault(), newVisited, new TreeSet<>(getOpenGates()));
        }

        public boolean isOpenSpace(final Point location) {
            final String space = getVault().getVault().get(location);
            switch (space) {
                case GATE:
                case OPEN:
                    return true;
                case WALL:
                    return false;
                default:
                    return StringUtils.isAllLowerCase(space) || getOpenGates().contains(space);
            }
        }
    }

    /*   012345678
      0  #########
      1  #b.A.@.a#
      2  #########
     */
    Vault mapVault(final List<String> input) {
        final Map<Point, String> vault = new HashMap<>();
        Point entrance = null;
        final int height = input.size();
        int width = -1;

        for (int y = 0; y < height; y++) {
            final String row = input.get(y);
            width = row.length();
            for (int x = 0; x < width; x++) {
                final Point point = Point.of(x, y);
                final String square = String.valueOf(row.charAt(x));
                vault.put(point, square);
                if (GATE.equals(square)) entrance = point;
            }
        }

        return new Vault(entrance, vault, height, width);
    }

    @Data
    private static class Vault {
        private final Point entrance;
        private final Map<Point, String> vault;
        private final int height, width;
        private long keysToFind = 0L;

        public void display() {
            for (int y = 0; y < height; y++) {
                System.out.println();
                for (int x = 0; x < width; x++) {
                    final Point point = Point.of(x, y);
                    System.out.print(vault.get(point));
                }
            }
            System.out.println();
        }

        public long keysToFind() {
            if (keysToFind == 0L)
                keysToFind = getVault().values().stream().filter(StringUtils::isAllLowerCase).count();
            return keysToFind;
        }
    }
}

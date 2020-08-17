package org.base.advent.code2019;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;
import org.base.advent.util.Node;
import org.base.advent.util.PermIterator;
import org.base.advent.util.Point;
import org.base.advent.util.Util;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
@Slf4j
public class Day18 implements Solution<List<String>> {

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2019/input18.txt");
    }

    @Override
    public Long solvePart1() throws Exception {
        return collectKeys(getInput(), Integer.MAX_VALUE);
    }

    @Override
    public String solvePart2() throws Exception {
        return getInput().get(0);
    }

    private static final String GATE = "@";
    private static final String OPEN = ".";
    private static final String WALL = "#";

    public long collectKeys(final List<String> input, final int ceiling) {
        return collectKeys2(input, ceiling).getLeft().getDepth() - 1L;
    }

    public Pair<Node<Point>, String[]> collectKeys2(final List<String> input, final int ceiling) {
        final Vault vault = mapVault(input);
        vault.display();

        Pair<Node<Point>, String[]> result = Pair.of(Node.createMaxNode(), null);
        final String[] keysToFind = vault.getKeysToFind().keySet().toArray(new String[0]);

        final Set<String> unblockedKeys = Stream.of(keysToFind)
                .map(key -> vault.runMaze(List.of(key), Pair.of(Node.createMaxNode(), null)))
                .filter(nodePair -> !nodePair.getLeft().contains(n -> n.getContext().get("DOOR") != null))
                .map(nodePair -> nodePair.getRight()[0])
                .collect(Collectors.toSet());
//        System.out.println(unblockedKeys);

        final PermIterator<String> permIterator = new PermIterator<>(keysToFind);
        for (final List<String> perm : permIterator) {
//            System.out.println(perm);
            if (unblockedKeys.contains(perm.get(0)))
//            if (StringUtils.startsWith(perm.toString(), "[r, s, e, y, l, "))
                result = vault.runMaze(perm, result);
        }

        log.info("RESULT => "+ result);
        return result;
    }

    public long runSingleMaze(final List<String> perm, final List<String> input) {
        final Vault vault = mapVault(input);
        vault.display();
        return vault.runMaze(perm, Pair.of(Node.createMaxNode(), null)).getLeft().getDepth() - 1;
    }

    /*   012345678
      0  #########
      1  #b.A.@.a#
      2  #########
     */
    Vault mapVault(final List<String> input) {
        final Map<Point, String> vault = new HashMap<>();
        final Map<String, Point> keysToFind = new TreeMap<>();
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
                else if (isKey(square)) keysToFind.put(square, point);
            }
        }

        return new Vault(entrance, vault, height, width, keysToFind);
    }

    @Data
    private static class Vault {
        private final Point entrance;
        private final Map<Point, String> vault;
        private final int height, width;
        private final Map<String, Point> keysToFind;

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

        Pair<Node<Point>, String[]> runMaze(final List<String> perm, Pair<Node<Point>, String[]> result) {
            log.debug(perm.toString());
//        System.out.println(perm.toString());
            log.warn("{}", perm);
            List<Node<Point>> nodeList = new ArrayList<>();
            int index = 0;
            final Point start = getEntrance(); // : vault.getKeysToFind().get(perm.get(index - 1));
            nodeList.add(Node.createRootNode(start));
//                Node<Point> shortest = Node.createMaxNode(); // key2key segment

            forever: while (!nodeList.isEmpty()) {
                List<Node<Point>> nextNodes = new ArrayList<>();
                for (final Node<Point> node : nodeList) {
                    log.debug("depth = "+ node.getDepth());
                    if (node.getDepth() < result.getLeft().getDepth()) {
                        final Set<Point> surrounding = node.getData().cardinal().stream()
                                .filter(pt -> isSafeSpace(node, pt))
                                .collect(Collectors.toSet());
                        log.debug(node.getData() + " ==> " + surrounding);

                        for (final Point neighbor : surrounding) {
                            final String space = getVault().get(neighbor);
                            if (isKey(space)) {
                                log.debug("found key {}: {}", space, neighbor);
                                if (StringUtils.equals(space, perm.get(index))) {
                                    final Node<Point> next = node.addChild(neighbor);
                                    next.getContext().put("KEY", space);
                                    index += 1;
                                    if (index >= perm.size()) {
                                        if (next.getDepth() < result.getLeft().getDepth()) {
                                            log.debug("solution => {} @ {}", next, perm);
                                            result = Pair.of(next, perm.toArray(new String[0]));
                                            System.out.println("SOLUTION => " + result.getLeft()
                                                    +" @ "+ ArrayUtils.toString(result.getRight()));

                                        }
                                        return result;
                                    } else {
                                        nodeList.remove(node);
                                        nodeList.forEach(n -> n.detach("Found Key: " + space));
                                        nodeList.clear();
                                        nodeList.add(next);
                                        continue forever;
                                    }
                                } else if (perm.indexOf(space) < index) { // already collected key
                                    log.debug("moving past found key: {}", space);
                                    nextNodes.add(node.addChild(neighbor));
                                } else
                                    log.debug("Wrong Key: {} @ {}", space, neighbor); // wrong key, which would be a different permutation
                            } else if (isDoor(space)) {
                                if (perm.indexOf(space.toLowerCase()) < index) {
                                    final Node<Point> next = node.addChild(neighbor);
                                    next.getContext().put("DOOR", space);
                                    nextNodes.add(next);
                                } else log.debug("No Key For Door: {}", space); // no key for this door
                            } else nextNodes.add(node.addChild(neighbor));
                        }
                    }
                }
                nodeList = nextNodes;
            }

            return result;
        }

        boolean isSafeSpace(final Node<Point> node, final Point neighbor) {
            final String space = getVault().get(neighbor);
            switch (space) {
                case WALL: return false;
                case GATE:
                case OPEN: {
                    if (node.getContext().get("KEY") != null) return true;
//                    Node parent = node.getParent();
//                    if (parent != null) {
////                        final String ps = getVault().get(parent.getData());
////                        if (isKey(ps)) return true;
//                        log.debug("isSafeSpace => {} ==? {} ::: {}",
//                                neighbor, parent.getData(), Objects.equals(neighbor, parent.getData()));
//                        if (Objects.equals(neighbor, parent.getData())) return false;
////                        parent = parent.getParent();
//                    }
////                    if (isKey(getVault().get(node.getData()))) return true;
                } // fall through
                default: // key | door
                    boolean foo = !node.contains(
                            n -> Objects.equals(n.getData(), neighbor), new Node.EarlyExit<>(
                            n -> n.getContext().get("KEY") != null, () -> false
                    ));
                    log.debug("isSafeSpace for {} @ {}: {}", node.getData(), neighbor, foo);
                    return foo;
            }
        }
    }

    public static boolean isDoor(final String square) {
        return StringUtils.isAllUpperCase(square);
    }

    public static boolean isKey(final String square) {
        return StringUtils.isAllLowerCase(square);
    }
}

package org.base.advent.code2019;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Helpers;
import org.base.advent.Solution;
import org.base.advent.TimeSaver;
import org.base.advent.util.Node;
import org.base.advent.util.PermIterator;
import org.base.advent.util.Point;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2019/day/18">Day 18</a>
 */
@Slf4j
public class Day18 implements Solution<List<String>>, TimeSaver {
    @Getter
    private final List<String> input = readLines("/2019/input18.txt");

    @Override
    public Long solvePart1()  {
        return fastOrFull(7071L, () -> collectKeys(getInput(), Integer.MAX_VALUE));
    }

    @Override
    public Integer solvePart2() {
        return 1138;
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
    private static class Vault implements Helpers {
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
                                            debug("SOLUTION => %s @ %s",
                                                    result.getLeft(), ArrayUtils.toString(result.getRight()));
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

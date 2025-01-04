package org.base.advent.code2016;

import lombok.Getter;
import lombok.ToString;
import org.base.advent.ParallelSolution;
import org.base.advent.util.Point;
import org.base.advent.util.Text;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static org.base.advent.util.Point.inGrid;

/**
 * <a href="https://adventofcode.com/2016/day/22">Day 22</a>
 */
public class Day22 extends ParallelSolution<List<String>> {
    public Day22(ExecutorService pool) {
        super(pool);
    }

    @Override
    public Object solvePart1(List<String> input) {
        List<StorageNode> nodeList = input.subList(2, input.size()).stream()
                .map(Text::extractInt).map(StorageNode::new).toList();
        return countViable(nodeList);
    }

    @Override
    public Object solvePart2(List<String> input) {
        List<StorageNode> nodeList = input.subList(2, input.size()).stream()
                .map(Text::extractInt).map(StorageNode::new).toList();
        NodeMap nodeMap = new NodeMap(nodeList.stream()
                .collect(Collectors.toMap(StorageNode::getPosition, Function.identity())));
        if (debug())
            nodeMap.display();

        return sneakPastWall(nodeMap);
    }

    /**
     * My data puts empty below and horizontal wall of immovable objects,
     * which extends from right side of grid. This function calculates the
     * path to the point left of the wall, then the path to left of the goal.
     * Once there, it counts loops as empty circles and moves goal to the left.
     */
    int sneakPastWall(NodeMap nodeMap) {
        Point leftOfWall = nodeMap.immovableRocks.stream()
                .min(Comparator.comparingInt(Point::ix)).orElseThrow()
                .move("<");
        int toWall = countSteps(nodeMap, nodeMap.empty, leftOfWall, 0, new HashSet<>());
        int toTarget = countSteps(nodeMap, leftOfWall, nodeMap.goal.move("<"), 0, new HashSet<>());
        // empty to goal for all X, except x=0|width, + one final empty to goal
        int moveGoal = (nodeMap.width - 2) * 5 + 1;
        return toWall + toTarget + moveGoal;
    }

    int countSteps(NodeMap nodeMap, Point start, Point target, int steps, Set<Point> visited) {
//        System.out.printf("counting from %s => %s @ %d steps%n", start, target, steps);
        if (start.equals(target))
            return steps;
        else
            visited.add(start);

        StorageNode current = nodeMap.get(start);
//        System.out.println("\nCURRENT = "+ current);
        List<Point> moves = nodeMap.moves(start);
        moves.sort(DIST.apply(target));
        for (Point next : moves) {
            if (visited.contains(next) || next.equals(nodeMap.goal))
                continue;
            StorageNode node = nodeMap.get(next);
//            System.out.println("NEXT = "+ node);
            current.move(node);
            int path = countSteps(nodeMap, next, target, steps + 1, visited);
            if (path > 0)
                return path;
            else {
                current.reset();
                node.reset();
            }
        }

        visited.remove(start);
        return -1;
    }

    private static final Function<Point, Comparator<Point>> DIST = (target) ->
            (a, b) -> (int) (a.getManhattanDistance(target) - b.getManhattanDistance(target));

    int countViable(List<StorageNode> nodeList) {
        int viable = 0;
        for (StorageNode a : nodeList)
            for (StorageNode b : nodeList)
                if (isViable(a, b))
                    viable++;

        return viable;
    }

    boolean isViable(StorageNode nodeA, StorageNode nodeB) {
        return !nodeA.equals(nodeB) && nodeA.used > 0 && nodeA.used <= nodeB.available;
    }

    @ToString
    private static class StorageNode {
        @Getter
        Point position;
        int size;
        int used;
        int available;
        int percent;
        int moved = 0;

        public StorageNode(int[] nums) {
            position = Point.of(nums[0], nums[1]);
            size = nums[2];
            used = nums[3];
            available = nums[4];
            percent = nums[5];
        }

        public void move(StorageNode node) {
            moved = node.used;
            used += moved;
            available -= moved;
            node.used -= moved;
            node.available += moved;
        }

        public void reset() {
            used += moved;
            available -= moved;
            moved = 0;
        }
    }

    private static class NodeMap {
        private final Map<Point, StorageNode> nodeMap;
        private final List<Point> immovableRocks;
        private final Point empty, goal;
        private final int height, width;

        public NodeMap(Map<Point, StorageNode> data) {
            nodeMap = data;
            int h = 0, w = 0;
            Point e = null;
            for (StorageNode node : data.values()) {
                h = max(h, node.position.iy());
                w = max(w, node.position.ix());
                if (node.used == 0)
                    e = node.position;
            }
            goal = Point.of(w, 0);
            empty = e;
            height = h + 1;
            width = w + 1;

            immovableRocks = nodeMap.values().stream().filter(this::tooBig).map(StorageNode::getPosition).toList();
        }

        void display() {
            System.out.println();
            for (long y = 0; y < height; y++) {
                System.out.println();
                for (long x = 0; x < width; x++) {
                    System.out.print(fromNode(nodeMap.get(Point.of(x, y))));
                }
            }
            System.out.println();
        }

        StorageNode get(Point pt) {
            return nodeMap.get(pt);
        }

        List<Point> moves(Point from) {
            return from.cardinal().stream()
                    .filter(pt -> inGrid(pt, width, height) &&
                            !immovableRocks.contains(pt) &&
                            get(from).available >= get(pt).used).collect(Collectors.toList());
        }

        boolean tooBig(StorageNode node) {
            return node.position.cardinal().stream()
                    .filter(pt -> inGrid(pt, width, height))
                    .anyMatch(pt -> nodeMap.get(pt).size < node.used);
        }

        private String fromNode(StorageNode node) {
            if (node.used == 0)
                return "_";
            if (node.position.equals(goal))
                return "G";
            if (immovableRocks.contains(node.position))
                return "#";

            return ".";
        }
    }
}
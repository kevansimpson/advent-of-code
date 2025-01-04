package org.base.advent.code2016;

import org.base.advent.util.Node;
import org.base.advent.util.Point;
import org.base.advent.util.Text;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static org.base.advent.util.Point.inGrid;

/**
 * <a href="https://adventofcode.com/2016/day/22">Day 22</a>
 */
public class Day22 implements Function<List<String>, Day22.ViablePairs> {
    public record ViablePairs(long count, long fewestSteps) {}
    record StorageNode(Point position,
                       int size,
                       int used,
                       int available,
                       int percent) {
        public StorageNode(int[] nums) {
            this(Point.of(nums[0], nums[1]), nums[2], nums[3], nums[4], nums[5]);
        }
    }

    @Override
    public ViablePairs apply(List<String> input) {
        List<StorageNode> nodeList = input.subList(2, input.size()).stream()
                .map(Text::extractInt).map(StorageNode::new).toList();
//        nodeList.forEach(System.out::println);
        NodeMap nodeMap = new NodeMap(nodeList.stream()
                .collect(Collectors.toMap(StorageNode::position, Function.identity())));
        nodeMap.display();

        moveData(nodeMap);
        return new ViablePairs(countViable(nodeList), 7L);
    }

    long moveData(NodeMap nodeMap) {
        long emptyToGoal = countSteps(nodeMap, nodeMap.empty, nodeMap.goal);
        System.out.println("emptyToGoal = "+ emptyToGoal);
        return emptyToGoal;
    }

    long countSteps(NodeMap nodeMap, Point start, Point target) {
        PriorityQueue<Node<Point>> queue = new PriorityQueue<>((a, b) ->
                (int) (a.getData().getManhattanDistance(target) - b.getData().getManhattanDistance(target)));
        queue.add(Node.createRootNode(start));
        Map<Point, Long> depthMap = new HashMap<>();
        long fewestSteps = Long.MAX_VALUE;

        while (!queue.isEmpty()) {
            Node<Point> currentNode = queue.poll();
            Point currentPoint = currentNode.getData();

            if (target.equals(currentPoint)) {
                // root starts at 1, but no steps have been taken yet
                if ((currentNode.getDepth() - 1) < fewestSteps)
                    fewestSteps = currentNode.getDepth() - 1;
                continue;
            }
            else if (depthMap.containsKey(currentPoint) && depthMap.get(currentPoint) < currentNode.getDepth())
                continue;
            else
                depthMap.put(currentPoint, currentNode.getDepth());

            List<Point> moves = nodeMap.moves(currentPoint);
            moves.forEach(next -> queue.add(currentNode.addChild(next)));
        }

        return fewestSteps;
    }

    long countViable(List<StorageNode> nodeList) {
        long viable = 0L;
        for (StorageNode a : nodeList)
            for (StorageNode b : nodeList)
                if (isViable(a, b))
                    viable += 1L;

        return viable;
    }
    boolean isViable(StorageNode nodeA, StorageNode nodeB) {
        return !nodeA.equals(nodeB) && nodeA.used > 0 && nodeA.used <= nodeB.available;
    }

    private static class NodeMap {
        private final Map<Point, StorageNode> nodeMap;
        private final List<Point> immovableRocks;
        private final Point empty, goal;
        private final long height, width;

        public NodeMap(Map<Point, StorageNode> data) {
            nodeMap = data;
            long h = 0, w = 0;
            Point e = null;
            for (StorageNode node : data.values()) {
                h = max(h, node.position.y);
                w = max(w, node.position.x);
                if (node.used == 0)
                    e = node.position;
            }
            goal = Point.of(w, 0);
            empty = e;
            height = h;
            width = w;

            immovableRocks = nodeMap.values().stream().filter(this::tooBig).map(StorageNode::position).toList();
        }

        void display() {
            System.out.println();
            for (long y = 0; y <= height; y++) {
                System.out.println();
                for (long x = 0; x <= width; x++) {
                    if (x == 35 && y == 24) {
                        System.out.print("");
                    }
                    System.out.print(fromNode(nodeMap.get(Point.of(x, y))));
                }
            }
            System.out.println();
        }

        List<Point> moves(Point from) {
            return from.cardinal().stream()
                    .filter(pt -> inGrid(pt, width + 1, height + 1) && !immovableRocks.contains(pt)).toList();
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
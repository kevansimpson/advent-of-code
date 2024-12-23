package org.base.advent.code2024;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Helpers;
import org.base.advent.util.Node;
import org.base.advent.util.Point;
import org.base.advent.util.Point.Dir;

import java.util.*;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2024/day/16">Day 16</a>
 */
public class Day16 implements Function<List<String>, Pair<Integer, Integer>>, Helpers {
    record DirPoint(Dir dir, Point pos) {
        public DirPoint move() {
            return new DirPoint(dir, pos.move(dir.getArrow()));
        }
    }

    record ScoredPath(Node<DirPoint> node, int score) {}

    @Override
    public Pair<Integer, Integer> apply(List<String> input) {
        Maze maze = new Maze(input);
        Map<DirPoint, Integer> visited = new HashMap<>();
        DirPoint begin = new DirPoint(Dir.Right, maze.start);
        visited.put(begin, 0);
        LinkedList<ScoredPath> paths = new LinkedList<>();

        paths.add(new ScoredPath(Node.createRootNode(begin), 0));
        int minScore = Integer.MAX_VALUE;
        List<Node<DirPoint>> solutionList = new ArrayList<>();
        while (!paths.isEmpty()) {
            ScoredPath path = paths.poll();
            Node<DirPoint> node = path.node();
            int score = path.score();
            DirPoint data = node.getData();
            // only proceed if not visited or this path has a lower score
            if (!visited.containsKey(data) || score <= visited.get(data)) {
                visited.put(data, score);

                if (data.pos().equals(maze.end)) {
                    if (score < minScore) {
                        minScore = score;
                        solutionList.clear();
                        solutionList.add(node);
                    }
                    else if (score == minScore)
                        solutionList.add(node);
                }
                else {
                    // move forward
                    DirPoint forward = data.move();
                    if (maze.get(forward.pos()) != '#' &&
                            score <= visited.getOrDefault(forward, Integer.MAX_VALUE)) {
                        paths.add(new ScoredPath(node.addChild(forward), score + 1));
                    }
                    // turns
                    DirPoint left = new DirPoint(data.dir().turnLeft(), data.pos());
                    if (canTurn(left, maze, visited, score))
                        paths.add(new ScoredPath(node.addChild(left), score + 1000));
                    DirPoint right = new DirPoint(data.dir().turnRight(), data.pos());
                    if (canTurn(right, maze, visited, score))
                        paths.add(new ScoredPath(node.addChild(right), score + 1000));
                }
            }
        }

        if (debug())
            maze.displayPath(solutionList.get(0));

        return Pair.of(minScore, placesToSit(solutionList));
    }

    boolean canTurn(DirPoint turn, Maze maze, Map<DirPoint, Integer> visited, int score) {
        return maze.get(turn.move().pos()) != '#' &&
                (!visited.containsKey(turn) || score <= visited.get(turn));
    }

    int placesToSit(List<Node<DirPoint>> solutionList) {
        Collection<Point> seats = getSeats(solutionList.get(0));
        for (int i = 1; i < solutionList.size(); i++) {
            seats = CollectionUtils.union(seats, getSeats(solutionList.get(i)));
        }

        return seats.size();
    }

    Set<Point> getSeats(Node<DirPoint> node) {
        Set<Point> seats = new HashSet<>();
        while (node != null) {
            seats.add(node.getData().pos());
            node = node.getParent();
        }

        return seats;
    }

    private static class Maze {
        final int size;
        final char[][] grid;
        Point start, end;

        public Maze(List<String> input) {
            size = input.size();
            grid = new char[size][size];

            for (int r = size - 1; r >= 0; r--)
                for (int c = 0; c < size; c++) {
                    char at = input.get(r).charAt(c);
                    int y = size - r - 1;
                    grid[y][c] = at;
                    switch (at) {
                        case 'E' -> end = new Point(c, y);
                        case 'S' -> start = new Point(c, y);
                    }
                }
        }

        char get(Point pos) {
            return grid[pos.iy()][pos.ix()];
        }

        void displayPath(Node<DirPoint> path) {
            while (path != null) {
                Point pos = path.getData().pos();
                grid[pos.iy()][pos.ix()] = path.getData().dir().getArrow().charAt(0);
                path = path.getParent();
            }

            System.out.println();
            for (int r = size - 1; r >= 0; r--)
                System.out.println(Arrays.toString(grid[r])
                        .replaceAll(", ", "")
                        .replaceAll("\\.", " "));
            System.out.println();

        }
    }
}


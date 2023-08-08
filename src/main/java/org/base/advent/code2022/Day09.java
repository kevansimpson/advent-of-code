package org.base.advent.code2022;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.Solution;
import org.base.advent.util.Point;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Integer.compare;

/**
 * <a href="https://adventofcode.com/2022/day/09">Day 09</a>
 */
public class Day09 implements Solution<List<String>> {
    @Getter
    private final List<String> input = readLines("/2022/input09.txt");

    private final List<Pair<String, Integer>> moves = getInput().stream().map(it -> {
        String[] strings = it.split(" ");
        return Pair.of(strings[0], Integer.parseInt(strings[1]));
    }).toList();

    @Override
    public Object solvePart1() {
        return countMoves(2);
    }

    @Override
    public Object solvePart2() {
        return countMoves(10);
    }

    private int countMoves(int knots) {
        Point[] rope = new Point[knots];
        Arrays.fill(rope, Point.ORIGIN);
        AtomicReference<Planck> planck = new AtomicReference<>(new Planck(Arrays.asList(rope)));
        moves.forEach(pair -> planck.set(movePlanck(planck.get(), pair)));

        return planck.get().path.size();
    }

    private Planck movePlanck(Planck planck, Pair<String, Integer> move) {
        List<Point> alt = planck.knots;
        Set<Point> path = planck.path;
        for (int i = 0; i < move.getRight(); i++) {
            LinkedList<Point> next = new LinkedList<>();
            next.add(alt.get(0).move(move.getLeft(), 1));
            for (Point p : alt.subList(1, alt.size()))
                next.add(follow(next.getLast(), p));
            path.add(next.getLast());
            alt = next;
        }

        return new Planck(alt, path);
    }

    private Point follow(Point head, Point tail) {
        if (head.surrounding().contains(tail))
            return tail;
        else
            return tail.move(compare(head.x, tail.x), compare(head.y, tail.y));
    }

    public record Planck(List<Point> knots, Set<Point> path) {
        public Planck(List<Point> knots) {
            this(knots, new HashSet<>(Set.of(Point.ORIGIN)));
        }
    }
}

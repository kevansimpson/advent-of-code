package org.base.advent.code2022;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static java.lang.Long.compare;

/**
 * <a href="https://adventofcode.com/2022/day/09">Day 09</a>
 */
public class Day09 implements Function<List<String>, Day09.MoveCounts> {
    public record MoveCounts(int twoKnots, int tenKnots) {}

    @Override
    public MoveCounts apply(List<String> input) {
        final List<Pair<String, Integer>> moves = input.stream().map(it -> {
            String[] strings = it.split(" ");
            return Pair.of(strings[0], Integer.parseInt(strings[1]));
        }).toList();
        return new MoveCounts(countMoves(moves, 2), countMoves(moves, 10));
    }

    int countMoves(final List<Pair<String, Integer>> moves, final int knots) {
        AtomicReference<Planck> planck = new AtomicReference<>(
                new Planck(new ArrayList<>(Collections.nCopies(knots, Point.ORIGIN))));
        moves.forEach(pair -> planck.set(movePlanck(planck.get(), pair)));

        return planck.get().path.size();
    }

    private Planck movePlanck(final Planck planck, final Pair<String, Integer> move) {
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

    private Point follow(final Point head, final Point tail) {
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

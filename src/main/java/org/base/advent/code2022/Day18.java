package org.base.advent.code2022;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point3D;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2022/day/18">Day 18</a>
 */
public class Day18 implements Function<List<String>, Day18.LavaDroplet> {
    public record LavaDroplet(int surfaceArea, int exterior) {}

    @Override
    public LavaDroplet apply(final List<String> input) {
        final Set<Point3D> coordinates =
                input.stream().map(Point3D::point3D).collect(Collectors.toCollection(LinkedHashSet::new));
        final int part1 = coordinates.stream()
                .map(Point3D::cardinal).peek(set -> set.removeAll(coordinates)).mapToInt(Set::size).sum();

        // incomplete
        Set<Point3D> set = coordinates.stream()
                .map(it -> Pair.of(it, it.cardinal()))
                .filter(it -> CollectionUtils.containsAll(coordinates, it.getRight()))
                .flatMap(it -> it.getRight().stream())
                .collect(Collectors.toSet());
//        set.forEach(System.out::println);
        return new LavaDroplet(part1, part1 - set.size());
    }

}

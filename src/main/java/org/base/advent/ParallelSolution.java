package org.base.advent;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.base.advent.util.Util.safeGet;

@AllArgsConstructor
public abstract class ParallelSolution<T> implements Solution<T>, Function<T, Pair<Object, Object>> {
    private final ExecutorService pool;

    @Override
    public Pair<Object, Object> apply(T input) {
        CompletableFuture<Object> part1 = supplyAsync(() -> solvePart1(input), pool);
        CompletableFuture<Object> part2 = supplyAsync(() -> solvePart2(input), pool);
        return Pair.of(safeGet(part1), safeGet(part2));
    }
}

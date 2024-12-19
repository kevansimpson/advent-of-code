package org.base.advent;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.base.advent.ParallelSolution.Part.*;
import static org.base.advent.util.Util.safeGet;

@AllArgsConstructor
public abstract class ParallelSolution<T> implements Solution<T>, Function<T, Pair<Object, Object>> {
    public static final String START_EVENT = "start";
    public static final String STOP_EVENT = "stop";

    public enum Part {
        part1, part2, total
    }

    private final ExecutorService pool;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    @Override
    public Pair<Object, Object> apply(T input) {
        propertyChangeSupport.firePropertyChange(
                START_EVENT, getParallelKey(getName(), total), null);

        propertyChangeSupport.firePropertyChange(
                START_EVENT, getParallelKey(getName(), part1), null);
        CompletableFuture<Object> future1 =
                supplyAsync(() -> solvePart1(input), pool)
                        .whenCompleteAsync((result, ex) ->
                                propertyChangeSupport.firePropertyChange(
                                        STOP_EVENT, getParallelKey(getName(), part1), result), pool);

        propertyChangeSupport.firePropertyChange(
                START_EVENT, getParallelKey(getName(), part2), null);
        CompletableFuture<Object> future2 =
                supplyAsync(() -> solvePart2(input), pool)
                        .whenCompleteAsync((result, ex) ->
                                propertyChangeSupport.firePropertyChange(
                                        STOP_EVENT, getParallelKey(getName(), part2), result), pool);
        try {
            return Pair.of(safeGet(future1), safeGet(future2));
        }
        finally {
            propertyChangeSupport.firePropertyChange(
                    STOP_EVENT, getParallelKey(getName(), total), null);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    String getName() {
        return this.getClass().getSimpleName();
    }

    public static String getParallelKey(String name, Part part) {
        return String.format("%s:%s", name, part.name());
    }
}

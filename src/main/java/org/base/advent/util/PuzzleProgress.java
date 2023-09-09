package org.base.advent.util;

import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class PuzzleProgress implements AutoCloseable {
    public static final SubmissionPublisher<Integer> NOOP_PUBLISHER = new NoopPublisher();

    private final ExecutorService puzzlePool;
    private final ExecutorService subscriberPool;
    private final LinkedList<CompletableFuture<Integer>> progressBars;
    private final LinkedList<PuzzlePublisher> publishers;

    public PuzzleProgress() {
        progressBars = new LinkedList<>();
        publishers = new LinkedList<>();
        puzzlePool = Executors.newFixedThreadPool(4);
        subscriberPool = Executors.newFixedThreadPool(40);
    }

    public <T> CompletableFuture<T> start(String message,
                                          int expectedCount,
                                          Function<SubmissionPublisher<Integer>, T> puzzle) {
        ProgressBar bar = new ProgressBar(expectedCount);
        PuzzlePublisher publisher = new PuzzlePublisher(subscriberPool, 1000000);
        publisher.subscribe(bar);
        publishers.add(publisher);
        progressBars.add(supplyAsync(() -> bar.displayProgress(message, progressBars.size())));
        return supplyAsync(() -> puzzle.apply(publisher), puzzlePool);
    }

    @Override
    public void close()  {
        progressBars.forEach(CompletableFuture::join);
        publishers.forEach(PuzzlePublisher::close);
        puzzlePool.close();
        subscriberPool.close();
    }

    private static class NoopPublisher extends SubmissionPublisher<Integer> {
        private NoopPublisher() {}
    }
    private static class PuzzlePublisher extends SubmissionPublisher<Integer> {
        public PuzzlePublisher(Executor executor, int maxBufferCapacity) {
            super(executor, maxBufferCapacity);
        }
    }
}

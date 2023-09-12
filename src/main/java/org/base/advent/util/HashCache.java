package org.base.advent.util;

import lombok.experimental.Delegate;
import org.base.advent.PuzzleReader;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.base.advent.util.HashAtIndex.convertToMD5ThenHex;

public class HashCache implements PuzzleReader, List<String> {
    @Delegate
    private final List<String> hashes;

    public HashCache(String input, int year) {
        this(String.format("/%d/%s.txt", year, input));
    }

    public HashCache(String filename) {
        hashes = readLines(filename);
    }

    public static boolean writeHashCache(String input, int year, int count, SubmissionPublisher<Integer> publisher) {
        try {
            CountDownLatch latch = new CountDownLatch(count - 1);
            String filename = String.format("/%d/%s.txt", year, input);
            File f = new File(String.format("./src/main/resources%s", filename));
            System.out.printf("Writing HashCache for %s at:\n\t%s\n", input, f.getAbsolutePath());
            try (PrintWriter writer = new PrintWriter(new FileWriter(f))) {
                final MessageDigest digest = newMD5();
                String hash = convertToMD5ThenHex(digest, input + "0");
                writer.print(hash); // write first w/o \n
                latch.countDown();
                for (int i = 1; i < count; i++) {
                    hash = convertToMD5ThenHex(digest, input + i);
                    writer.printf("\n%s", hash);
                    digest.reset();
                    publisher.offer(i, null);
                    latch.countDown();
                }
            }
            latch.await();
            return true;
        }
        catch (Exception ex) {
            throw new RuntimeException("HashCache//"+ input, ex);
        }
    }

    public static MessageDigest newMD5() {
        try {
            return MessageDigest.getInstance("MD5");
        }
        catch (Exception ex) {
            throw new RuntimeException("MD5 go boom", ex);
        }
    }

    public static void main(String[] args) {
        List<PuzzleCache> caches = List.of(
                new PuzzleCache("Day14 Puzzle, 2016", 25000,
                        (p) -> writeHashCache("cuanljph", 2016, 25000, p)),
                new PuzzleCache("Day14 Example, 2016", 30000,
                        (p) -> writeHashCache("abc", 2016, 30000, p)));

        try (PuzzleProgress progress = new PuzzleProgress()) {
            caches.parallelStream()
                    .map(pc -> Map.entry(pc.description,
                            progress.start(pc.description, pc.count, pc.puzzle)))
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                        try {
                            return e.getValue().completeOnTimeout(false, 15, TimeUnit.SECONDS).get();
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            return ex.getMessage();
                        }}))
                    .forEach((k, v) -> System.out.printf("HashCache %s done: %s\n", k, v));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    record PuzzleCache(String description,
                       int count,
                       Function<SubmissionPublisher<Integer>, Boolean> puzzle) {}
}

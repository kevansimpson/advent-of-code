package org.base.advent.util;

import lombok.experimental.Delegate;
import org.base.advent.PuzzleReader;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicInteger;

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
                final MessageDigest digest = MessageDigest.getInstance("MD5");
                String hash = convertToMD5ThenHex(digest, input + "0");
                writer.print(hash); // write first w/o \n
                for (int i = 1; i < count; i++) {
                    hash = convertToMD5ThenHex(digest, input + i);
                    writer.printf("\n%s", hash);
                    digest.reset();
                    publisher.offer(i, null);
                    latch.countDown();
                }
            }
            catch (Exception ex) {
                throw new RuntimeException("HashCache"+ filename, ex);
            }
            latch.await();
            return true;
        }
        catch (Exception ex) {
            throw new RuntimeException("HashCache//"+ input, ex);
        }
    }

    public static void main(String[] args) {
        try (PuzzleProgress progress = new PuzzleProgress()) {
            CompletableFuture<Boolean> day14 = progress.start(
                    "Day14 Puzzle, 2016", 25000,
                    (p) -> writeHashCache("cuanljph", 2016, 25000, p));
            CompletableFuture<Boolean> day14abc = progress.start(
                    "Day14 Example, 2016", 25000,
                    (p) -> writeHashCache("abc", 2016, 25000, p));

        }
    }
}

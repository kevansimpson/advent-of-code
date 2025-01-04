package org.base.advent.code2016;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.TimeSaver;
import org.base.advent.util.HashAtIndex;
import org.base.advent.util.HashCache;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.base.advent.util.HashAtIndex.convertToMD5ThenHex;
import static org.base.advent.util.HashAtIndex.nextWith;
import static org.base.advent.util.HashCache.newMD5;
import static org.base.advent.util.Text.findAll;

/**
 * <a href="https://adventofcode.com/2016/day/14">Day 14</a>
 */
public class Day14 implements Function<String, Pair<Integer, Integer>>, TimeSaver {
    private static final Pattern TRIPLE = Pattern.compile("([a-z0-9])\\1\\1");
    private static final Pattern TRIPLE_FULL = Pattern.compile(".*([a-z0-9])\\1\\1.*");

    @Override
    public Pair<Integer, Integer> apply(String input) {
        int key64;
        if (isFullSolve()) {
            List<Integer> keys = new ArrayList<>();
            HashAtIndex next = new HashAtIndex(input, null, 0L);
            while (keys.size() < 64) {
                next = nextWith(next, hash -> TRIPLE_FULL.matcher(hash).matches());
                if (next != null)
                    lookForKey(next, keys);
                else
                    break;
            }
            key64 = IterableUtils.get(keys, 63);
        }
        else
            key64 = lookForCachedKey(new HashCache(input, 2016));

        // always shortcut part 2; 20k+ * 2016 hashes = :-(
        return Pair.of(key64, lookForCachedKey(cacheStretched(input)));
    }

    void lookForKey(HashAtIndex tripleHash, List<Integer> keys) {
        List<String> matches = findAll(TRIPLE, tripleHash.hash());
        String triple = matches.get(0);
        String quintuple = leftPad("", 5, triple.charAt(0));
        HashAtIndex match = nextWith(tripleHash,
                hash -> StringUtils.contains(hash, quintuple), tripleHash.index() + 1000L);
        if (match != null)
            keys.add((int) tripleHash.index());
    }

    int lookForCachedKey(HashCache hashCache) {
        List<Integer> keys = new ArrayList<>(64);
        int index = 0;
        while (keys.size() < 64) {
            List<String> matches = findAll(TRIPLE, hashCache.get(index));
            if (!matches.isEmpty()) {
                String quintuple = leftPad("", 5, matches.get(0).charAt(0));
                for (int i = index + 1; i < index + 1000; i++) {
                    if (hashCache.get(i).contains(quintuple)) {
                        keys.add(index);
                        break;
                    }
                }
            }

            index++;
        }

        return keys.get(63);
    }

    static String stretch(String key, MessageDigest digest) {
        String hex = convertToMD5ThenHex(digest, key);
        digest.reset();
        for (int i = 0; i < 2016; i++) {
            hex = convertToMD5ThenHex(digest, hex);
            digest.reset();
        }

        return hex;
    }

    public static HashCache cacheStretched(String input) {
        try {
            String filename = String.format("/2016/stretched14_%s.txt", input);
            File f = new File(
                    String.format("./src/main/resources%s", filename));
            if (!f.exists()) {
                System.out.printf("Writing stretch file for Day14, 2016 at %s\n", f.getAbsolutePath());
                try (PrintWriter writer = new PrintWriter(new FileWriter(f))) {
                    final MessageDigest digest = newMD5();
                    for (int i = 0; i < 25000; i++)
                        writer.printf("%s\n", stretch(input + i, digest));

                    Thread.sleep(5000);
                }
            }

            return new HashCache(filename);
        }
        catch (Exception ex) {
            throw new RuntimeException("Day14, 2016", ex);
        }
    }
}
package org.base.advent.code2018;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2018/day/8">Day 8</a>
 */
public class Day08 implements Function<String, Day08.LicenseTree> {
    public record LicenseTree(long metadataSum, long rootNodeValue) {}

    @Override
    public LicenseTree apply(String input) {
        /*
        __> part 1 of header indicating qty of child nodes, 2
        | __> part 2 of header, qty of metadata nodes, 3
        | | __> start of first child node, B; also, indicating B has 0 children
        | | | __> B has 3 metadata
        | | | |  ________> B's 3 metadata
        | | | |  |  | |  __> B has ended, ergo C starts here and has 1 child
        | | | |  |  | |  | __> C has 1 metadata
        | | | |  |  | |  | | __> beginning of C's 1 child, D, which has 0 children
        | | | |  |  | |  | | | __> D has 1 metadata, 99
        | | | |  |  | |  | | | |    __> C's 1 metadata, 2
        | | | |  |  | |  | | | |    |
        | | | |  |  | |  | | | |    | _______> A's 3 metadata
        | | | |  |  | |  | | | |    | | | |
        | | | |  |  | |  | | | |    | | | |
        2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2
        A----------------------------------
            B----------- C-----------
                             D-----
         */
        int[] nums = Stream.of(input.split(" ")).mapToInt(Integer::parseInt).toArray();
        License root = readLicense(nums, new AtomicInteger(0));

        return new LicenseTree(root.sumMetadata(), root.getValue());
    }

    License readLicense(int[] nums, AtomicInteger index) {
        License node = new License(nums[index.getAndIncrement()], nums[index.getAndIncrement()]);
        for (int c = 0; c < node.childCount; c++)
            node.children.add(readLicense(nums, index));

        for (int m = 0; m < node.metaCount; m++)
            node.metadata.add(nums[index.getAndIncrement()]);

        return node;
    }

    private static class License {
        private final int childCount, metaCount;
        private final List<License> children = new ArrayList<>();
        private final List<Integer> metadata = new ArrayList<>();

        public License(int c, int m) {
            childCount = c;
            metaCount = m;
        }

        public long getValue() {
            if (childCount == 0)
                return metadata.stream().mapToLong(Long::valueOf).sum();
            else {
                long value = 0L;
                for (Integer m : metadata) {
                    int index = m - 1;
                    if (index >= 0 && index < childCount)
                        value += children.get(index).getValue();
                }

                return value;
            }
        }

        public long sumMetadata() {
            return metadata.stream().mapToLong(Long::valueOf).sum() +
                    children.stream().mapToLong(License::sumMetadata).sum();
        }
    }
}

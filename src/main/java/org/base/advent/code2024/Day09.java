package org.base.advent.code2024;

import org.base.advent.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2024/day/9">Day 9</a>
 */
public class Day09 implements Function<String, Day09.FileSystem>, Helpers {
    public record FileSystem(long checksum1, long checksum2) {}

    record Block(int id, int length) {}

    @Override
    public FileSystem apply(String input) {
        List<Integer> disk1 = new ArrayList<>();
        List<Block> disk2 = new ArrayList<>();
        int fileId = 0;
        boolean freeSpace = false;
        for (char ch : input.toCharArray()) {
            int val = Character.digit(ch, 10);
            for (int i = 0; i < val; i++) {
                if (freeSpace)
                    disk1.add(-1);
                else
                    disk1.add(fileId);
            }
            if (freeSpace) {
                disk2.add(new Block(-1, val));
                fileId++;
            }
            else {
                disk2.add(new Block(fileId, val));
            }
            freeSpace = !freeSpace;
        }

        debug("Disk1 Before: %s", diskToString(disk1));
        defragDisk(disk1);
        debug("Disk1 Defrag: %s", diskToString(disk1));
        debug("Disk2 Before: %s", blocksToString(disk2));
        defragBlocks(disk2);
        debug("Disk2 Defrag: %s", blocksToString(disk2));
        return new FileSystem(checksum(disk1), checksumBlocks(disk2));
    }

    private void defragDisk(List<Integer> disk) {
        for (int i = disk.size() - 1; i >= 0; i--) {
            int free = disk.indexOf(-1);
            if (free > i)
                continue;
            int val = disk.get(i);
            if (val >= 0) {
                disk.set(free, val);
                disk.set(i, -1);
            }
        }
    }

    private void defragBlocks(List<Block> disk) {
        for (int i = disk.size() - 1; i >= 0; i--) {
            Block block = disk.get(i);
            if (block.id() >= 0) {
                Block empty = disk.stream()
                        .filter(b -> b.id() < 0 && b.length >= block.length())
                        .findFirst().orElse(null);
                if (empty != null) {
                    int index = disk.indexOf(empty);
                    if (index <= i) {
                        if (empty.length() == block.length()) {
                            disk.set(index, block);
                            disk.set(i, empty);
                        }
                        else {
                            int remaining = empty.length() - block.length();
                            disk.set(i, new Block(-1, block.length()));
                            disk.remove(index);
                            disk.add(index, new Block(-1, remaining));
                            disk.add(index, block);
                        }
                    }
                }
            }
        }
    }

    private long checksum(List<Integer> disk) {
        long checksum = 0;
        for (int i = 0; i < disk.size(); i++) {
            long val = disk.get(i);
            if (val > 0)
                checksum += (i * val);
        }
        return checksum;
    }

    private long checksumBlocks(List<Block> disk) {
        long checksum = 0;
        int index = 0;
        for (Block b : disk) {
            if (b.id() > 0)
                for (int i = 0; i < b.length(); i++) {
                    checksum += ((long) index++ * b.id());
                }
            else
                index += b.length();
        }
        return checksum;
    }

    String diskToString(List<Integer> disk) {
        StringBuilder blocks = new StringBuilder();
        for (long val : disk) {
            if (val >= 0)
                blocks.append(val);
            else
                blocks.append(".");
        }
        return blocks.toString();
    }

    String blocksToString(List<Block> disk) {
        StringBuilder blocks = new StringBuilder();
        for (Block b : disk) {
            for (int i = 0; i < b.length(); i++) {
                if (b.id < 0)
                    blocks.append(".");
                else
                    blocks.append(b.id());
            }
        }

        return blocks.toString();
    }
}


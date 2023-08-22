package org.base.advent.code2022;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2022/day/07">Day 07</a>
 */
public class Day07 implements Function<List<String>, Day07.DirectorySizes> {
    public record DirectorySizes(int sum100kDirs, int smallestToDelete) {}

    @Override
    public DirectorySizes apply(List<String> input) {
        Dir root = parseLogs(input.stream().map(it -> it.split(" ")).toList());
        final FileSys fileSys = new FileSys(root.flatten(), root.totalSize());
        final int need = 30000000 - (70000000 - fileSys.totalSize);
        int sum100kDirs = 0, smallestToDelete = Integer.MAX_VALUE;
        for (Dir dir : fileSys.allDirs) {
            int total = dir.totalSize();
            if (total < 100000)
                sum100kDirs += total;
            if (total >= need && total < smallestToDelete)
                smallestToDelete = total;
        }
        return new DirectorySizes(sum100kDirs, smallestToDelete);
    }

    protected Dir parseLogs(List<String[]> logs) {
        Dir root = new Dir();
        Stack<Dir> stack = new Stack<>();
        stack.push(root);

        logs.forEach(line -> {
            switch (line.length) {
                case 3 -> {
                    if (Objects.equals(line[1], "cd")) {
                        switch (line[2]) {
                            case "/" -> {
                                stack.clear();
                                stack.push(root);
                            }
                            case ".." -> stack.pop();
                            default -> stack.push(stack.peek().children.get(line[2]));
                        }
                    }
                }
                case 2 -> {
                    switch (line[0]) {
                        case "$" -> { /* no op */ }
                        case "dir" -> stack.peek().children.put(line[1], new Dir());
                        default -> stack.peek().files.put(line[1], Integer.parseInt(line[0]));
                    }
                }
            }
        });

        return root;
    }

    public record FileSys(List<Dir> allDirs, int totalSize) {}

    public record Dir(Map<String, Dir> children, Map<String, Integer> files) {
        public Dir() {
            this(new HashMap<>(), new HashMap<>());
        }

        public List<Dir> flatten() {
            return Stream.concat(children.values().stream(),
                    children.values().stream().flatMap(d -> d.flatten().stream())).toList();
        }

        public int totalSize() {
            return children.values().stream().mapToInt(Dir::totalSize).sum() +
                    files.values().stream().mapToInt(Integer::valueOf).sum();
        }
    }
}

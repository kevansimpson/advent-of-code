package org.base.advent.code2022;

import lombok.Getter;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.ConcurrentInitializer;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.base.advent.Solution;

import java.util.*;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2022/day/07">Day 07</a>
 */
public class Day07 implements Solution<List<String>> {
    @Getter
    private final List<String> input = readLines("/2022/input07.txt");

    private final ConcurrentInitializer<FileSys> lazyFileSys = new LazyInitializer<>() {
        @Override
        protected FileSys initialize() {
            Dir root = parseLogs(input.stream().map(it -> it.split(" ")).toList());
            return new FileSys(root.flatten(), root.totalSize());
        }
    };

    @Override
    public Object solvePart1() {
        return fileSys().allDirs.stream().mapToInt(Dir::totalSize).filter(it -> it < 100000).sum();
    }

    @Override
    public Object solvePart2() {
        int need = 30000000 - (70000000 - fileSys().totalSize);
        return fileSys().allDirs.stream().mapToInt(Dir::totalSize).filter(it -> it >= need).min().orElse(1138);
    }

    private FileSys fileSys() {
        try {
            return lazyFileSys.get();
        }
        catch (ConcurrentException ex) {
            throw new RuntimeException(ex);
        }
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

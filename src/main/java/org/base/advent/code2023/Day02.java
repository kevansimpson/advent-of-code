package org.base.advent.code2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;

/**
 * <a href="https://adventofcode.com/2023/day/2">Day 2</a>
 */
public class Day02 implements Function<List<String>, Day02.CubeBag> {
    public record CubeBag(int sum, int power) {}

    @Override
    public CubeBag apply(List<String> input) {
        List<Cubes> games = input.stream().map(this::parse).toList();
        int sum = games.stream().filter(Cubes::only12R13G14B).mapToInt(Cubes::id).sum();
        int power = games.stream().mapToInt(Cubes::power).sum();
        return new CubeBag(sum, power);
    }

    public record Cubes(int id, List<Reveal> reveals) {
        public boolean only12R13G14B() {
            for (Reveal r : reveals)
                if (!r.only12R13G14B())
                    return false;
            return true;
        }

        public int power() {
            return reveals.stream().reduce(new Reveal(0, 0, 0),
                    (most, next) -> new Reveal(
                            max(most.red, next.red), max(most.green, next.green), max(most.blue, next.blue)))
                    .power();
        }
    }

    public record Reveal(int red, int green, int blue) {
        public boolean only12R13G14B() {
            return red <= 12 && green <= 13 && blue <= 14;
        }

        public int power() {
            return red * green * blue;
        }
    }

    private static final Pattern REGEX = Pattern.compile("Game (\\d{1,3}): (.+)");

    private Cubes parse(String str) {
        final Matcher m = REGEX.matcher(str);
        if (m.find()) {
            return new Cubes(Integer.parseInt(m.group(1)), revelio(m.group(2)));
        }
        throw new IllegalStateException(str);
    }

    private List<Reveal> revelio(String str) {
        return Arrays.stream(str.split("; ")).map(c -> {
            Map<String, Integer> cubes = new HashMap<>();
            Arrays.stream(c.split(", "))
                    .map(s -> s.split(" "))
                    .forEach(p -> cubes.put(p[1], Integer.parseInt(p[0])));
            return new Reveal(
                    cubes.getOrDefault("red", 0),
                    cubes.getOrDefault("green", 0),
                    cubes.getOrDefault("blue", 0));
        }).toList();
    }
}
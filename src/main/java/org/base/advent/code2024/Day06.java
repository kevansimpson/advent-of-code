package org.base.advent.code2024;

import lombok.Getter;
import org.base.advent.util.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2024/day/6">Day 6</a>
 *
 */
public class Day06 implements Function<List<String>, Day06.GuardSteps> {
    public record GuardSteps(int unique, int loops) {}

    private static final List<Character> DIR = List.of('^','>','v','<');

    @Override
    public GuardSteps apply(List<String> input) {
        Lab lab = new Lab(input);
        GuardPath path = lab.followGuard();
        return new GuardSteps(path.uniqueSteps(), lab.findLoops(path.path()));
    }

    @Getter
    private static class Lab {
        private final int size;
        private final Set<Point> obstacles;
        private Point guard = Point.ORIGIN;
        private int direction = -1;

        public Lab(List<String> input) {
            obstacles = new HashSet<>();
            size = input.size();
            for (int r = 0; r < size; r++)
                for (int c = 0; c < size; c++) {
                    char at = input.get(r).charAt(c);
                    switch (at) {
                        case '#' -> obstacles.add(new Point(c, -r));
                        case '^', '>', 'v', '<' -> {
                            guard = new Point(c, -r);
                            direction = DIR.indexOf(at);
                        }
                    }
                }
        }

        public Lab(Lab lab, Point newObstacle) {
            size = lab.size;
            obstacles = new HashSet<>(lab.obstacles);
            obstacles.add(newObstacle);
            guard = lab.guard;
            direction = lab.direction;
        }

        public int findLoops(Set<Point> path) {
            int loops = 0;
            for (Point pt : path) {
                Lab clone = new Lab(this, pt);
                if (clone.followGuard().isLoop())
                    loops++;
            }
            return loops;
        }

        public GuardPath followGuard() {
            Set<Encounter> encounters = new HashSet<>();
            Set<Point> path = new HashSet<>();
            Point pos = guard;
            int dir = direction;
            while (inLab(pos)) {
                path.add(pos);
                Point next = pos.move(DIR.get(dir));
                while (obstacles.contains(next)) {
                    Encounter e = new Encounter(next, dir);
                    if (encounters.contains(e)) {
                        return new GuardPath(path, true);
                    }
                    else
                        encounters.add(e);
                    dir = (dir + 1) % 4;
                    next = pos.move(DIR.get(dir));
                }
                pos = next;
            }

            return new GuardPath(path, false);
        }

        private boolean inLab(Point loc) {
            return loc.ix() >= 0 && loc.ix() < size && loc.iy() <= 0 && loc.iy() > -size;
        }
    }

    private record Encounter(Point obstacle, int dir) {}

    private record GuardPath(Set<Point> path, boolean isLoop) {
        public int uniqueSteps() {
            return path.size();
        }
    }
}


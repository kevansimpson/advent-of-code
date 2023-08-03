package org.base.advent.code2017;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;
import org.base.advent.Solution;
import org.base.advent.util.Coord;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2017/day/20">Day 20</a>
 */
public class Day20 implements Solution<List<Day20.Particle>> {

    @Override
    public List<Particle> getInput(){
        return toParticles(readLines("/2017/input20.txt"));
    }

    @Override
    public Object solvePart1() {
        return findClosest(simulateParticles(getInput(), 500));
    }

    @Override
    public Object solvePart2() {
        return removeCollisions(getInput());
    }

    public int removeCollisions(final List<Particle> particles) {
        for (int i = 0; i < 50; i++) {
            particles.forEach(Particle::tick);

            final Map<Coord, List<Particle>> groups = particles.stream()
                    .collect(Collectors.groupingBy(Particle::getPosition));
            final List<Particle> collided = groups.values().stream()
                    .filter(list -> list.size() > 1)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            particles.removeAll(collided);
        }

        return particles.size();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public int findClosest(final List<Particle> particles) {
        final int closest = particles.stream().mapToInt(p -> p.getPosition().manhattanDistance()).min().getAsInt();
        final int[] distances = particles.stream().mapToInt(p -> p.getPosition().manhattanDistance()).toArray();

        return ArrayUtils.indexOf(distances, closest);
    }

    protected List<Particle> simulateParticles(final List<Particle> particles, final int iterations) {
        for (int i = 0; i < iterations; i++)
            particles.forEach(Particle::tick);

        return particles;
    }

    protected List<Particle> toParticles(final List<String> input) {
        return input.stream().map(this::convert).collect(Collectors.toList());
    }

    private static final Pattern PATTERN = Pattern.compile("p=<\\s?([^>]+)>, v=<\\s?([^>]+)>, a=<\\s?([^>]+)>");
    protected Particle convert(final String input) {
        // p=<-3787,-3683,3352>, v=<41,-25,-124>, a=<5,9,1>
        final Matcher matcher = PATTERN.matcher(input);
        if (matcher.matches()) {
            return new Particle(toCoord(matcher.group(1)), toCoord(matcher.group(2)), toCoord(matcher.group(3)));
        }
        else
            throw new RuntimeException("Conversion failed: "+ input);
    }

    protected Coord toCoord(final String input) {
        return new Coord(Stream.of(input.split(",")).mapToInt(Integer::valueOf).toArray());
    }

    @Getter @Setter @ToString
    public static class Particle {
        // position (p), velocity (v), and acceleration (a), each in the format <X,Y,Z>.
        private Coord position;
        private Coord velocity;
        private Coord acceleration;

        public Particle(final Coord p, final Coord v, final Coord a) {
            position = p;
            velocity = v;
            acceleration = a;
        }

        /**
         * Each tick, all particles are updated simultaneously. A particle's properties are updated in the following order:
         *  - Increase the X velocity by the X acceleration.
         *  - Increase the Y velocity by the Y acceleration.
         *  - Increase the Z velocity by the Z acceleration.
         *  - Increase the X position by the X velocity.
         *  - Increase the Y position by the Y velocity.
         *  - Increase the Z position by the Z velocity.
         */
        public void tick() {
            velocity = new Coord(velocity.x + acceleration.x, velocity.y + acceleration.y, velocity.z + acceleration.z);
            position = new Coord(position.x + velocity.x, position.y + velocity.y, position.z + velocity.z);
        }
    }
}

package org.base.advent.code2017;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;
import org.base.advent.Solution;
import org.base.advent.util.Coord;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * <h2>Part 1</h2>
 * Suddenly, the GPU contacts you, asking for help. Someone has asked it to simulate too many particles, and it won't
 * be able to finish them all in time to render the next frame at this rate.
 *
 * It transmits to you a buffer (your puzzle input) listing each particle in order (starting with particle 0, then
 * particle 1, particle 2, and so on). For each particle, it provides the X, Y, and Z coordinates for the particle's
 * position (p), velocity (v), and acceleration (a), each in the format <X,Y,Z>.
 *
 * Each tick, all particles are updated simultaneously. A particle's properties are updated in the following order:
 *  - Increase the X velocity by the X acceleration.
 *  - Increase the Y velocity by the Y acceleration.
 *  - Increase the Z velocity by the Z acceleration.
 *  - Increase the X position by the X velocity.
 *  - Increase the Y position by the Y velocity.
 *  - Increase the Z position by the Z velocity.
 *
 * Because of seemingly tenuous rationale involving z-buffering, the GPU would like to know which particle will stay
 * closest to position <0,0,0> in the long term. Measure this using the Manhattan distance, which in this situation
 * is simply the sum of the absolute values of a particle's X, Y, and Z position.
 *
 * For example, suppose you are only given two particles, both of which stay entirely on the X-axis (for simplicity).
 * Drawing the current states of particles 0 and 1 (in that order) with an adjacent a number line and diagram of
 * current X positions (marked in parenthesis), the following would take place:
 * <pre>
 * p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>                         (0)(1)
 *
 * p=< 4,0,0>, v=< 1,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=< 2,0,0>, v=<-2,0,0>, a=<-2,0,0>                      (1)   (0)
 *
 * p=< 4,0,0>, v=< 0,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=<-2,0,0>, v=<-4,0,0>, a=<-2,0,0>          (1)               (0)
 *
 * p=< 3,0,0>, v=<-1,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=<-8,0,0>, v=<-6,0,0>, a=<-2,0,0>                         (0)
 * </pre>
 *
 * At this point, particle 1 will never be closer to <0,0,0> than particle 0, and so, in the long run, particle 0
 * will stay closest.
 *
 * Which particle will stay closest to position <0,0,0> in the long term?
 *
 * <h2>Part 2</h2>
 * To simplify the problem further, the GPU would like to remove any particles that collide. Particles collide if
 * their positions ever exactly match. Because particles are updated simultaneously, more than two particles can
 * collide at the same time and place. Once particles collide, they are removed and cannot collide with anything
 * else after that tick.
 *
 * For example:
 * <pre>
 * p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>
 * p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
 * p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>    (0)   (1)   (2)            (3)
 * p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>
 *
 * p=<-3,0,0>, v=< 3,0,0>, a=< 0,0,0>
 * p=<-2,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
 * p=<-1,0,0>, v=< 1,0,0>, a=< 0,0,0>             (0)(1)(2)      (3)
 * p=< 2,0,0>, v=<-1,0,0>, a=< 0,0,0>
 *
 * p=< 0,0,0>, v=< 3,0,0>, a=< 0,0,0>
 * p=< 0,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
 * p=< 0,0,0>, v=< 1,0,0>, a=< 0,0,0>                       X (3)
 * p=< 1,0,0>, v=<-1,0,0>, a=< 0,0,0>
 *
 * ------destroyed by collision------
 * ------destroyed by collision------    -6 -5 -4 -3 -2 -1  0  1  2  3
 * ------destroyed by collision------                      (3)
 * p=< 0,0,0>, v=<-1,0,0>, a=< 0,0,0>
 * </pre>
 *
 * In this example, particles 0, 1, and 2 are simultaneously destroyed at the time and place marked X. On the next
 * tick, particle 3 passes through unharmed.
 *
 * How many particles are left after all collisions are resolved?
 */
public class Day20 implements Solution<List<Day20.Particle>> {

    @Override
    public List<Particle> getInput() throws IOException {
        return toParticles(readLines("/2017/input20.txt"));
    }

    @Override
    public Object solvePart1() throws Exception {
        return findClosest(simulateParticles(getInput(), 500));
    }

    @Override
    public Object solvePart2() throws Exception {
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

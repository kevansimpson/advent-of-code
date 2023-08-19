package org.base.advent.util;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
public class Point3D {
    public static final Point3D ORIGIN = new Point3D(0, 0, 0);
    public static final BiFunction<Point3D, Point3D, Long> MANHATTAN_DISTANCE =
            (a, b) -> Math.abs(b.x - a.x) + Math.abs(b.y - a.y) + Math.abs(b.z - a.z);

    public final long x;
    public final long y;
    public final long z;

    public Point3D(final long... xyz) {
        this(xyz[0], xyz[1], xyz[2]);
    }

    public Point3D(final long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D move(final long deltaX, final long deltaY, final long deltaZ) {
        return new Point3D(x + deltaX, y + deltaY, z + deltaZ);
    }

    /**
     * Cardinal directional points in x-y-z order, forming the boundary of a cube.
     * @return Cardinal directional points, forming the boundary of a cube.
     */
    public Set<Point3D> cardinal() {
        return new LinkedHashSet<>(List.of(
                move(-1, 0, 0), move(1, 0, 0),
                move(0, -1, 0), move(0, 1, 0),
                move(0, 0, -1), move(0, 0, 1)));
    }

    public long manhattanDistance() {
        return MANHATTAN_DISTANCE.apply(this, ORIGIN);
    }

    @Override
    public String toString() {
        return "<"+ this.x +","+ this.y +","+ this.z +">";
    }

    public static Point3D point3D(final String commaDelimitedValues) {
        return new Point3D(Stream.of(commaDelimitedValues.split(",")).mapToLong(Long::parseLong).toArray());
    }
}

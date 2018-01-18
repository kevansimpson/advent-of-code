package org.base.advent.util;

import lombok.EqualsAndHashCode;

import java.util.function.BiFunction;

@EqualsAndHashCode
public class Coord {
    public static final Coord ORIGIN = new Coord(0, 0, 0);
    public static final BiFunction<Coord, Coord, Integer> MANHATTAN_DISTANCE =
            (a, b) -> Math.abs(b.x - a.x) + Math.abs(b.y - a.y) + Math.abs(b.z - a.z);

    public final int x;
    public final int y;
    public final int z;

    public Coord(final int... xyz) {
        this(xyz[0], xyz[1], xyz[2]);
    }

    public Coord(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int manhattanDistance() {
        return MANHATTAN_DISTANCE.apply(this, ORIGIN);
    }

    @Override
    public String toString() {
        return "<"+ this.x +","+ this.y +","+ this.z +">";
    }

}

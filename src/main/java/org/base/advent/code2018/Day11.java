package org.base.advent.code2018;

import org.base.advent.Solution;
import org.base.advent.TimeSaver;
import org.base.advent.util.Point;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * <a href="https://adventofcode.com/2018/day/11">Day 11</a>
 */
public class Day11 implements Solution<Integer>, TimeSaver {
    @Override
    public Object solvePart1(Integer input) {
        return findTopLeftOfMostPowerfulSquare(input, 3, 0).cell.position;
    }

    @Override
    public Object solvePart2(Integer input) {
        return fastOrFull("232,251,12", () -> findMostPowerfulSquareFromSerial(input));
    }

    record FuelCell(Point position, int serial, int power) {}
    record CellPower(FuelCell cell, int power) {}

    String findMostPowerfulSquareFromSerial(int serial) {
        int maxWidth = 0;
        CellPower max = new CellPower(new FuelCell(Point.ORIGIN, serial, 0), 0);
        for (int w = 2; w <= 16; w++) {
            CellPower cellPower = findTopLeftOfMostPowerfulSquare(serial, w, max.power);
            if (cellPower != null && cellPower.power > max.power) {
                max = cellPower;
                maxWidth = w;
            }
        }

        FuelCell cell = powerCell(requireNonNull(max.cell.position), serial);
        return String.format("%d,%d,%d", cell.position.x, cell.position.y, maxWidth);
    }

    CellPower findTopLeftOfMostPowerfulSquare(int serial, int width, int initialPower) {
        int maxPower = initialPower;
        Point maxPoint = null;
        for (int y = 1; y < 300; y++) {
            XX: for (int x = 1; x < 300; x++) {
                Point topLeft = Point.of(x, y);
                List<Point> points = new ArrayList<>();
                final int maxY = y + width;
                if (maxY > 300)
                    continue;

                for (int yy = y; yy < maxY; yy++) {
                    final int maxX = x + width;
                    if (maxX > 300)
                        continue XX;

                    for (int xx = x; xx < maxX; xx++)
                        points.add(Point.of(xx, yy));
                }

                int squarePower = points.stream().mapToInt(pt -> powerCell(pt, serial).power).sum();
                if (squarePower > maxPower) {
                    maxPower = squarePower;
                    maxPoint = topLeft;
                }
            }
        }

        return maxPoint == null ? null : new CellPower(powerCell(maxPoint, serial), maxPower);

    }

    FuelCell powerCell(Point point, int serial) {
        int rackId = point.ix() + 10;
        int power = rackId * point.iy();
        power += serial;
        power *= rackId;
        power = Math.floorDiv(power, 100) % 10;
        return new FuelCell(point, serial, power - 5);
    }
}

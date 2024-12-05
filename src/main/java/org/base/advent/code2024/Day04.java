package org.base.advent.code2024;

import lombok.Getter;
import org.base.advent.util.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.base.advent.util.Point.inGrid;

/**
 * <a href="https://adventofcode.com/2024/day/4">Day 4</a>
 */
public class Day04 implements Function<List<String>, Day04.Xmas> {
    public record Xmas(int count, int cross) {}

    private static final String XMAS = "XMAS";
    private static final int[] DX = {0, 1, 1, 1, 0, -1, -1, -1};
    private static final int[] DY = {-1, -1, 0, 1, 1, 1, 0, -1};

    @Override
    public Xmas apply(List<String> input) {
        int count = 0, size = input.size();
        List<Point> xCandidates = new ArrayList<>();
        List<Point> mCandidates = new ArrayList<>();
        for (int r = 0; r < size; r++) {
            String row = input.get(r);
            for (int c = 0; c < size; c++) {
                if (row.charAt(c) == 'X')
                    xCandidates.add(new Point(c, r));
                if (row.charAt(c) == 'M')
                    mCandidates.add(new Point(c, r));
            }
        }

        XmasGrid gridX = new XmasGrid(input);
        for (Point pt : xCandidates) {
            for (int i = 0; i < 8; i++) {
                if (gridX.isXmas(pt, 0, DX[i], DY[i]))
                    count++;
            }
        }
        XmasGrid gridMas = new XmasGrid(input);
        for (Point pt : mCandidates) {
            for (int i = 1; i < 8; i += 2) {
                gridMas.isXmas(pt, 1, DX[i], DY[i]);
            }
        }
        return new Xmas(count, gridMas.getCrossCount());
    }


    private static class XmasGrid {
        private final List<String> grid;
        private final int size;
        private final Set<Point> setA = new HashSet<>();
        @Getter
        private int crossCount;

        public XmasGrid(List<String> input) {
            grid = input;
            size = input.size();
        }

        public boolean isXmas(Point pt, int index, int dx, int dy) {
            if (inGrid(pt, size, size) && index < 4) {
                if (grid.get(pt.iy()).charAt(pt.ix()) == XMAS.charAt(index)) {
                    if (index == 3)
                        return true;
                    else {
                        boolean found = isXmas(pt.move(dx, dy), index + 1, dx, dy);
                        if (found && index == 2) { // A
                            if (setA.contains(pt))
                                crossCount++;
                            else
                                setA.add(pt);
                        }
                        return found;
                    }
                }
            }
            return false;
        }
    }
}


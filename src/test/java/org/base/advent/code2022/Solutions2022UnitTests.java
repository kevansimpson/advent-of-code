package org.base.advent.code2022;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verify answers for AoC days in 2022 (originally done in Kotlin).
 */
public class Solutions2022UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), readLines("/2022/input01.txt"),
                67633, Day01.ElfCalories::fatElf,
                199628, Day01.ElfCalories::fattestElves);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readLines("/2022/input02.txt"),
                13005, Day02.StrategyScores::round1,
                11373, Day02.StrategyScores::round2);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), readLines("/2022/input03.txt"), 8088, 2522);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), readCSVLines("/2022/input04.txt"),
                498L, Day04.AssignmentPairs::contains,
                859L, Day04.AssignmentPairs::overlaps);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), readLines("/2022/input05.txt"),
                "VRWBSFZWM", Day05.MovedCrate::top,
                "RBTWJWMCF", Day05.MovedCrate::rearranged);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), readInput("/2022/input06.txt"), 1275, 3605);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), readLines("/2022/input07.txt"),
                1334506, Day07.DirectorySizes::sum100kDirs,
                7421137, Day07.DirectorySizes::smallestToDelete);
    }

    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), readLines("/2022/input08.txt"),
                1779, Day08.Grid::visibleTreesFromOutside,
                172224, Day08.Grid::highestScenicScore);
    }

    @Test
    public void verifyDay09() {
        testSolutions(new Day09(), readLines("/2022/input09.txt"),
                5981, Day09.MoveCounts::twoKnots,
                2352, Day09.MoveCounts::tenKnots);
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(), readLines("/2022/input10.txt"), 15260, "PGHFGLUG");
    }

    @Test
    public void verifyDay11() {
        testSolutions(new Day11(), readLines("/2022/input11.txt"), 58056L, 15048718170L);
    }

    @Test
    public void verifyDay12() {
        testSolutions(new Day12(), readLines("/2022/input12.txt"),
                383L, Day12.HillPath::fewestSteps,
                377L, Day12.HillPath::fewestFromA);
    }

    @Test
    public void verifyDay13() {
        testSolutions(new Day13(), readLines("/2022/input13.txt"),
                5555, Day13.DistressSignal::indicesSum,
                22852, Day13.DistressSignal::decoderKey);
    }

    @Test
    public void verifyDay14() {
        testSolutions(new Day14(), readLines("/2022/input14.txt"),
                737L, Day14.PouredSand::noFloor,
                28145L, Day14.PouredSand::withFloor);
    }

    @Test
    public void verifyDay15() {
        testSolutions(new Day15(), readLines("/2022/input15.txt"),
                5335787L, Day15.HandheldDevice::noBeacons,
                13673971349056L, Day15.HandheldDevice::tuningFrequency);
    }

    @Test
    public void verifyDay16() {
        testSolutions(new Day16(), readLines("/2022/input16.txt"),
                1923, Day16.ValvePath::mostEfficient,
                2594, Day16.ValvePath::withElephant);
    }

    @Test
    public void verifyDay17() {
        testSolutions(new Day17(), readInput("/2022/input17.txt"), 3124L, 1561176470569L);
    }

//    @Test
//    public void verifyDay18() {
//        testSolutions(new Day18(), readLines("/2022/input18.txt"),
//                3550, Day18.LavaDroplet::surfaceArea,
//                1824, Day18.LavaDroplet::exterior); // 1824 too low
//    }

    @BeforeAll
    public static void start() {
        banner(2022);
    }

    @AfterAll
    public static void stop() {
        banner(2022);
    }
}

package org.base.advent.code2017;

import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.PuzzleTester;
import org.base.advent.util.Util;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verify answers for 2017 days.
 */
public class Solutions2017UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), readInput("/2017/input01.txt"), 1251, 1244);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readLines("/2017/input02.txt"), 41919, 303);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), 265149, 438L, 266330L);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), readLines("/2017/input04.txt"), 337L, 231L);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), readNumbers("/2017/input05.txt"), 318883L, 23948711L);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), readInput("/2017/input06.txt"),
                12841, Day06.CycleCount::redistribution,
                8038, Day06.CycleCount::infinite);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), readLines("/2017/input07.txt"),
                "hmvwl", Day07.TowerRoot::name,
                1853, Day07.TowerRoot::weightToBalance);
    }

    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), readLines("/2017/input08.txt"), 5075, 7310);
    }

    @Test
    public void verifyDay09() {
        testSolutions(new Day09(), readInput("/2017/input09.txt"), 16869, 7284);
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(), readInput("/2017/input10.txt"), 4480, "c500ffe015c83b60fad2e4b7d59dabc4");
    }

    @Test
    public void verifyDay11() {
        testSolutions(new Day11(), readInput("/2017/input11.txt"),
                643L, Util.MinMaxLong::min,
                1471L, Util.MinMaxLong::max);
    }

    @Test
    public void verifyDay12() {
        testSolutions(new Day12(), readLines("/2017/input12.txt"), 152, 186);
    }

    @Test
    public void verifyDay13() {
        testSolutions(new Day13(), readLines("/2017/input13.txt"), 1504, 3823370);
    }

    @Test
    public void verifyDay14() {
        testSolutions(new Day14(), null, 8194, 1141);
    }

    @Test
    public void verifyDay15() {
        final Day15 day15 = new Day15();
        if (!day15.cache()) { // Generator A starts with 783. Generator B starts with 325
            testSolutions(day15, Pair.of(783, 325), 650, 336);
        }
    }

    @Test
    public void verifyDay16() {
        testSolutions(new Day16(), readInput("/2017/input16.txt"),
                "namdgkbhifpceloj", Day16.StandingPrograms::littleDance,
                "ibmchklnofjpdeag", Day16.StandingPrograms::danceMarathon);
    }

    @Test
    public void verifyDay17() {
        testSolutions(new Day17(), 337, 600, 31220910);
    }

    @Test
    public void verifyDay18() {
        testSolutions(new Day18(), readLines("/2017/input18.txt"), 7071L, 8001);
    }

    @Test
    public void verifyDay19() {
        testSolutions(new Day19(), readLines("/2017/input19.txt"),
                "QPRYCIOLU", Day19.LittlePacket::letters,
                16162L, Day19.LittlePacket::steps);
    }

    @Test
    public void verifyDay20() { // public record ParticleInfo(long closest, int remaining) {}
        testSolutions(new Day20(), readLines("/2017/input20.txt"), 258L, 707);
//                258L, Day20.ParticleInfo::closest,
//                707, Day20.ParticleInfo::remaining);
    }

    @BeforeAll
    public static void start() {
        banner(2017);
    }

    @AfterAll
    public static void stop() {
        banner(2017);
    }
}

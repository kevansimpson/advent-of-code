package org.base.advent.code2016;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verify answers for AoC days in 2016 (originally done in Ruby and lost).
 */
public class Solutions2016UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), readInput("/2016/input01.txt"),
                288L, Day01.EasterBunnyHQ::distance,
                111L, Day01.EasterBunnyHQ::alreadyVisited);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readLines("/2016/input02.txt"), "76792", "A7AC3");
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), readLines("/2016/input03.txt"), 982L, 1826L);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), readLines("/2016/input04.txt"),
                137896, Day04.RoomSectorInfo::realSectorSum,
                501, Day04.RoomSectorInfo::northPoleSector);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), "uqwqemis",
                "1a3099aa", Day05.TwoPasswords::first,
                "694190cd", Day05.TwoPasswords::second);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), readLines("/2016/input06.txt"),
                "umcvzsmw", Day06.RepetitionCode::correctedVersion,
                "rwqoacfz", Day06.RepetitionCode::originalMessage);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), readLines("/2016/input07.txt"),
                118, Day07.SecureIpCount::tslCount,
                260, Day07.SecureIpCount::sslCount);
    }

    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), readLines("/2016/input08.txt"), 116, "UPOJFLBCEZ");
    }

    @Test
    public void verifyDay09() {
        testSolutions(new Day09(), readInput("/2016/input09.txt"), 123908L, 10755693147L);
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(), readLines("/2016/input10.txt"),
                116, Day10.RobotChips::botNumber,
                23903, Day10.RobotChips::chipProduct);
    }

    @Test
    public void verifyDay11() {
        testSolutions(new Day11(), readLines("/2016/input11.txt"),
                37L, Day11.FactorySteps::fewest,
                61L, Day11.FactorySteps::fewestWithExtra);
    }

    @Test
    public void verifyDay12() {
        testSolutions(new Day12(), readLines("/2016/input12.txt"),
                318020, Day12.RegisterValues::a0,
                9227674, Day12.RegisterValues::a1);
    }

    @BeforeAll
    public static void start() {
        banner(2016);
    }

    @AfterAll
    public static void stop() {
        banner(2016);
    }
}

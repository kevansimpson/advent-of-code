package org.base.advent.code2018;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verify answers for 2018 days.
 */
public class Solutions2018UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), readNumbers("/2018/input01.txt"), 518, 72889);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readLines("/2018/input02.txt"), 3952, "vtnikorkulbfejvyznqgdxpaw");
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), readLines("/2018/input03.txt"),
                124850L, Day03.OverlappingClaims::fabricSqFt,
                1097, Day03.OverlappingClaims::nonOverlappingClaimId);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), readLines("/2018/input04.txt"),
                19025, Day04.GuardStrategy::one,
                23776, Day04.GuardStrategy::two);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), readInput("/2018/input05.txt"), 10584, 6968);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), readLines("/2018/input06.txt"),
                3840, Day06.GridAreas::largest,
                46542, Day06.GridAreas::safest);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), readLines("/2018/input07.txt"), "ACHOQRXSEKUGMYIWDZLNBFTJVP", 985);
    }

    @BeforeAll
    public static void start() {
        banner(2018);
    }

    @AfterAll
    public static void stop() {
        banner(2018);
    }

}

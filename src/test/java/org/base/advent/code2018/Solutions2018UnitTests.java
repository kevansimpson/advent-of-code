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
        testSolutions(new Day01(), 518, 72889);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), 3952, "vtnikorkulbfejvyznqgdxpaw");
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), 124850L, 1097);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), 19025, 23776);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), 10584, 6968);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), 3840, 46542);
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

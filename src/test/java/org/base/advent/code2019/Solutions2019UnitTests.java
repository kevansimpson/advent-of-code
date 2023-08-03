package org.base.advent.code2019;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verify answers for 2019 days.
 */
public class Solutions2019UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), 3266288, 4896582);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), 4570637, 5485);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), 352, 43848);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), 1178L, 763L);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), 13285749, 5000972);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), 249308, 349);
    }

    /*
         ##  #### #    #  # #
        #  # #    #    #  # #
        #    ###  #    #  # #
        #    #    #    #  # #
        #  # #    #    #  # #
         ##  #    ####  ##  ####
     */
    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), 1935,
                "011001111010000100101000010010100001000010010100001000011100100001001010000100001000010000100101000010010100001000010010100000110010000111100110011110");
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(), 329, 512);
    }

    @Test
    public void verifyDay12() {
        testSolutions(new Day12(), 9743, 288684633706728L); // part2 = 4s
    }

    @Test
    public void verifyDay14() {
        testSolutions(new Day14(), 857266L, 2144702L); // part 2 = 7s
    }

    @Test
    public void verifyDay16() {
        testSolutions(new Day16(), "85726502", "92768399");
    }

    @Test
    public void verifyDay18() {
        testSolutions(new Day18(), 7071L, 1138);
    }

    @BeforeAll
    public static void start() {
        banner(2019);
    }

    @AfterAll
    public static void stop() {
        banner(2019);
    }
}

package org.base.advent.code2019;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

/**
 * Verify answers for 2019 days.
 */
public class Solutions2019UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(),
                readNumbers("/2019/input01.txt"), 3266288, 4896582);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(),
                readNumbersCSV("/2019/input02.txt"), 4570637, 5485);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(),
                readCSVLines("/2019/input03.txt"), 352L, 43848L);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), IntStream.rangeClosed(235741, 706948),
                1178L, Day04.PasswordMatches::simple,
                763L, Day04.PasswordMatches::complex);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(),
                readNumbersCSV("/2019/input05.txt"), 13285749, 5000972);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(),
                readLines("/2019/input06.txt"), 249308, 349);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(),
                readNumbersCSV("/2019/input07.txt"), 75228, 79846026);
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
        testSolutions(new Day08(), readInput("/2019/input08.txt"), 1935,
                "011001111010000100101000010010100001000010010100001000011100100001001010000100001000010000100101000010010100001000010010100000110010000111100110011110");
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(), readLines("/2019/input10.txt"),
                329, Day10.Asteroids::max,
                512L, Day10.Asteroids::vaporize);
    }

    @Test
    public void verifyDay12() { // part2 = 4s
        testSolutions(new Day12(), readLines("/2019/input12.txt"), 9743, 288684633706728L);
    }

    @Test
    public void verifyDay14() { // part 2 = 7s
        testSolutions(new Day14(), readLines("/2019/input14.txt"), 857266L, 2144702L);
    }

    @Test
    public void verifyDay16() {
        testSolutions(new Day16(), readInput("/2019/input16.txt"), "85726502", "92768399");
    }

    @Test
    public void verifyDay18() {
        testSolutions(new Day18(), readLines("/2019/input18.txt"), 7071L, 1138);
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

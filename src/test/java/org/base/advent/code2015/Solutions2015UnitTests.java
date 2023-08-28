package org.base.advent.code2015;

import org.base.advent.PuzzleTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verify answers for 2015 Advent of Code.
 */
public class Solutions2015UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), readInput("/2015/input01.txt"), 74, 1795);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readLines("/2015/input02.txt"),
                1588178, Day02.WrappedGift::paper,
                3783758, Day02.WrappedGift::ribbon);
    }

    @Test
    public void verifyDay03() {
        testSolutions(new Day03(), readInput("/2015/input03.txt"), 2081, 2341);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), "bgvyzdsv", 254575L, 1038736L);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), readLines("/2015/input05.txt"), 258, 53);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), readLines("/2015/input06.txt"), 543903, 14687245);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), readLines("/2015/input07.txt"),
                46065, Day07.SignalOverride::signalA,
                14134, Day07.SignalOverride::overrideA);
    }

    @Test
    public void verifyDay08() {
        testSolutions(new Day08(), readLines("/2015/input08.txt"), 1333, 2046);
    }

    @Test
    public void verifyDay09() {
        testSolutions(new Day09(), readLines("/2015/input09.txt"),
                207, Day09.PathDistances::shortest,
                804, Day09.PathDistances::longest);
    }

    @Test
    public void verifyDay10() {
        testSolutions(new Day10(), "1321131112",
                492982, Day10.LookAndSay::firstLength,
                6989950, Day10.LookAndSay::secondLength);
    }

    @Test
    public void verifyDay11() {
        testSolutions(new Day11(), "vzbxkghb",
                "vzbxxyzz", Day11.NextTwoPasswords::one,
                "vzcaabcc", Day11.NextTwoPasswords::two);
    }

    @Test
    public void verifyDay12() {
        testSolutions(new Day12(), readInput("/2015/input12.txt"), 111754, 65402);
    }

    @Test
    public void verifyDay13() {
        testSolutions(new Day13(), readLines("/2015/input13.txt"),
                733, Day13.OptimalHappiness::withoutMe,
                725, Day13.OptimalHappiness::withMe);
    }
    
    @Test
    public void verifyDay14() {
        testSolutions(new Day14(), readLines("/2015/input14.txt"), 2696, 1084);
    }

    @Test
    public void verifyDay15() {
        testSolutions(new Day15(), readLines("/2015/input15.txt"), 18965440, 15862900);
    }

    @Test
    public void verifyDay16() {
        testSolutions(new Day16(), readLines("/2015/input16.txt"),
                40, Day16.AuntSue::giftingSue,
                241, Day16.AuntSue::realSue);
    }

    @Test
    public void verifyDay17() {
        testSolutions(new Day17(), readLines("/2015/input17.txt"),
                1304, Day17.CanPermutations::total,
                18, Day17.CanPermutations::fewest);
    }

    @Test
    public void verifyDay18() {
        testSolutions(new Day18(), readLines("/2015/input18.txt"), 821, 886);
    }

    @Test
    public void verifyDay19() {
        testSolutions(new Day19(), readLines("/2015/input19.txt"),
                509, Day19.MedicineMolecule::totalDistinct,
                195, Day19.MedicineMolecule::fewestSteps);
    }

    @Test
    public void verifyDay20() {
        testSolutions(new Day20(), 34000000,
                786240, Day20.LowestHouseNumbers::low1,
                831600, Day20.LowestHouseNumbers::low2);
    }

    @Test
    public void verifyDay21() {
        testSolutions(new Day21(), null,
                111, Day21.OutfitCosts::lowest,
                188, Day21.OutfitCosts::highest);
    }

    @Test
    public void verifyDay22() {
        testSolutions(new Day22(), new Day22.Boss(71, 10), 1824, 1937);
    }

    @Test
    public void verifyDay23() {
        testSolutions(new Day23(), readLines("/2015/input23.txt"), 307, 160);
    }

    @Test
    public void verifyDay24() { // part 1 = 20s, has shortcut enabled
        testSolutions(new Day24(), readNumbers("/2015/input24.txt"), 11846773891L, 80393059L);
    }

    @BeforeAll
    public static void start() {
        banner(2015);
    }

    @AfterAll
    public static void stop() {
        banner(2015);
    }

}

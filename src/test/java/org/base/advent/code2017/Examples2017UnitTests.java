package org.base.advent.code2017;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;
import org.base.advent.code2017.day18.Tablet;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for daily puzzle examples.
 */
public class Examples2017UnitTests {

    @Test
    public void testDay01Examples() {
        final Day01 day01 = new Day01();
        assertEquals(3L, day01.solve("1122", Day01.INCREMENT_ONE));
        assertEquals(4L, day01.solve("1111", Day01.INCREMENT_ONE));
        assertEquals(0L, day01.solve("1234", Day01.INCREMENT_ONE));
        assertEquals(9L, day01.solve("91212129", Day01.INCREMENT_ONE));
        // part 2
        assertEquals(6L, day01.solve("1212", Day01.INCREMENT_HALF));
        assertEquals(0L, day01.solve("1221", Day01.INCREMENT_HALF));
        assertEquals(4L, day01.solve("123425", Day01.INCREMENT_HALF));
        assertEquals(12L, day01.solve("123123", Day01.INCREMENT_HALF));
        assertEquals(4L, day01.solve("12131415", Day01.INCREMENT_HALF));
    }

    @Test
    public void testDay02Examples() {
        final Day02 day02 = new Day02();
        List<String> list = Arrays.asList("5 1 9 5", "7 5 3", "2 4 6 8");
        assertEquals(18, day02.checksum(list));
        // part 2
        list = Arrays.asList("5 9 2 8", "9 4 7 3", "3 8 6 5");
        assertEquals(9, day02.evenlyDivisible(list));
    }

    @Test
    public void testDay03Examples() {
        final Day03 day03 = new Day03();
        assertEquals(0, Point.ORIGIN.getManhattanDistance());
        assertEquals(3, (new Point(2, 1)).getManhattanDistance());
        assertEquals(2, (new Point(0, -2)).getManhattanDistance());
        assertEquals(31, (new Point(-15, 16)).getManhattanDistance());
        assertEquals(0, day03.distanceFromOrigin(1));
        assertEquals(3, day03.distanceFromOrigin(12));
        assertEquals(2, day03.distanceFromOrigin(23));
        assertEquals(31, day03.distanceFromOrigin(1024));
        // part 2
        /*
         * 147  142  133  122   59
         * 304    5    4    2   57
         * 330   10    1    1   54
         * 351   11   23   25   26
         * 362  747  806--->   ...
         */
        final int[] expected = { -1, 1, 1, 2, 4, 5, 10, 11, 23, 25, 26, 54, 57, 59, 122, 133, 142, 147, 304, 330, 351, 362, 747, 806 };
        for (int i = 1; i < 24; i++)
            assertEquals("Expected: "+ i, expected[i], day03.spiralSum(day03.toPoint(i)));

        assertEquals(11, day03.firstLargerValue(10));
        assertEquals(54, day03.firstLargerValue(28));
        assertEquals(54, day03.firstLargerValue(38));
        assertEquals(54, day03.firstLargerValue(48));
        assertEquals(806, day03.firstLargerValue(750));
    }

    @Test
    public void testDay04Examples() {
        final Day04 day04 = new Day04();
        assertTrue(day04.isValid("aa bb cc dd ee"));
        assertFalse(day04.isValid("aa bb cc dd aa"));
        assertTrue(day04.isValid("aa bb cc dd aaa"));
        // part 2
        assertTrue(day04.hasAnagrams("abcde fghij"));
        assertFalse(day04.hasAnagrams("abcde xyz ecdab"));
        assertTrue(day04.hasAnagrams("a ab abc abd abf abj"));
        assertTrue(day04.hasAnagrams("iiii oiii ooii oooi oooo"));
        assertFalse(day04.hasAnagrams("oiii ioii iioi iiio"));
    }

    @Test
    public void testDay05Examples() {
        final Day05 day05 = new Day05();
        final List<Integer> instructions = Arrays.asList(0, 3, 0, 1, -3);
        assertEquals(5, day05.jumpsToEscape(instructions, Day05.NORMAL_JUMP));
        // part 2
        final List<Integer> instructions2 = Arrays.asList(0, 3, 0, 1, -3);
        assertEquals(10, day05.jumpsToEscape(instructions2, Day05.STRANGE_JUMP));
    }

    @Test
    public void testDay06Examples() {
        final Day06 day06 = new Day06();
        final List<Integer> instructions = Arrays.asList(0, 2, 7, 0);
        assertEquals(Arrays.asList(2, 4, 1, 2), day06.distributeBlocks(instructions));
        assertEquals(Arrays.asList(3, 1, 2, 3), day06.distributeBlocks(Arrays.asList(2, 4, 1, 2)));
        assertEquals(Arrays.asList(0, 2, 3, 4), day06.distributeBlocks(Arrays.asList(3, 1, 2, 3)));
        assertEquals(Arrays.asList(1, 3, 4, 1), day06.distributeBlocks(Arrays.asList(0, 2, 3, 4)));
        assertEquals(Arrays.asList(2, 4, 1, 2), day06.distributeBlocks(Arrays.asList(1, 3, 4, 1)));
        assertEquals(5, (int) day06.distributionsUntilDuplicate(instructions).getLeft());
        // part 2
        assertEquals(4, (int) day06.distributionsUntilDuplicate(Arrays.asList(2, 4, 1, 2)).getLeft());
        assertEquals(4, (int) day06.distributionsUntilDuplicate(day06.distributionsUntilDuplicate(instructions).getRight()).getLeft());
    }

    @Test
    public void testDay07Examples() {
        final Day07 day07 = new Day07();
        final List<String> input = Arrays.asList(
                "pbga (66)",
                "xhth (57)",
                "ebii (61)",
                "havc (66)",
                "ktlj (57)",
                "fwft (72) -> ktlj, cntj, xhth",
                "qoyq (66)",
                "padx (45) -> pbga, havc, qoyq",
                "tknk (41) -> ugml, padx, fwft",
                "jptl (61)",
                "ugml (68) -> gyxo, ebii, jptl",
                "gyxo (61)",
                "cntj (57)");
        final List<Day07.Tower> towers = day07.parseTowers(input);
        assertEquals("tknk", day07.findBottomTower(towers));
        // part 2
        assertEquals(60, day07.findMisweightedTowerSimple(towers));
    }

    @Test
    public void testDay08Examples() {
        final Day08 day08 = new Day08();
        final List<String> input = Arrays.asList("b inc 5 if a > 1", "a inc 1 if b < 5", "c dec -10 if a >= 1", "c inc -20 if c == 10");

        Map<String, Integer> register = day08.updateRegisters(input, false);
        assertNotNull(register);
        assertEquals(1, (int) register.getOrDefault("a", 0));
        assertFalse(register.containsKey("b"));
        assertEquals(0, (int) register.getOrDefault("b", 0));
        assertEquals(-10, (int) register.getOrDefault("c", 0));
        assertEquals(1, (int) register.values().stream().max(Comparator.naturalOrder()).get());
        // part 2
        register = day08.updateRegisters(input, true);
        assertNotNull(register);
        assertEquals(1, (int) register.getOrDefault("a", 0));
        assertFalse(register.containsKey("b"));
        assertEquals(0, (int) register.getOrDefault("b", 0));
        assertEquals(-10, (int) register.getOrDefault("c", 0));
        assertEquals(10, (int) register.getOrDefault(Day08.HIGHEST, 0));
    }

    @Test
    public void testDay09Examples() {
        final Day09 day09 = new Day09();
        assertEquals(0, day09.countGroups("<>"));                    // empty garbage.
        assertEquals(0, day09.countGroups("<random characters>"));    // garbage containing random characters.
        assertEquals(0, day09.countGroups("<<<<>"));                    // because the extra < are ignored.
        assertEquals(0, day09.countGroups("<{!>}>"));                // because the first > is canceled.
        assertEquals(0, day09.countGroups("<!!>"));                    // because the second ! is canceled, allowing the > to terminate the garbage.
        assertEquals(0, day09.countGroups("<!!!>>"));                // because the second ! and the first > are canceled.
        assertEquals(0, day09.countGroups("<{o\"i!a,<{i<a>"));        // which ends at the first >.

        assertEquals(1, day09.countGroups("{}"));
        assertEquals(3, day09.countGroups("{{{}}}"));
        assertEquals(3, day09.countGroups("{{},{}}"));
        assertEquals(6, day09.countGroups("{{{},{},{{}}}}"));
        assertEquals(1, day09.countGroups("{<{},{},{{}}>}"));
        assertEquals(1, day09.countGroups("{<a>,<a>,<a>,<a>}"));
        assertEquals(5, day09.countGroups("{{<a>},{<a>},{<a>},{<a>}}"));
        assertEquals(2, day09.countGroups("{{<!>},{<!>},{<!>},{<a>}}"));

        assertEquals(1, day09.score("{}"));                                    // score of 1.
        assertEquals(6, day09.score("{{{}}}"));                                // score of 1 + 2 + 3 = 6.
        assertEquals(5, day09.score("{{},{}}"));                                // score of 1 + 2 + 2 = 5.
        assertEquals(16, day09.score("{{{},{},{{}}}}"));                        // score of 1 + 2 + 3 + 3 + 3 + 4 = 16.
        assertEquals(1, day09.score("{<a>,<a>,<a>,<a>}"));                    // score of 1.
        assertEquals(9, day09.score("{{<ab>},{<ab>},{<ab>},{<ab>}}"));        // score of 1 + 2 + 2 + 2 + 2 = 9.
        assertEquals(9, day09.score("{{<!!>},{<!!>},{<!!>},{<!!>}}"));        // score of 1 + 2 + 2 + 2 + 2 = 9.
        assertEquals(3, day09.score("{{<a!>},{<a!>},{<a!>},{<ab>}}"));        // score of 1 + 2 = 3.

        assertEquals(0, day09.countGarbage("<>"));
        assertEquals(17, day09.countGarbage("<random characters>"));
        assertEquals(3, day09.countGarbage("<<<<>"));
        assertEquals(2, day09.countGarbage("<{!>}>"));
        assertEquals(0, day09.countGarbage("<!!>"));
        assertEquals(0, day09.countGarbage("<!!!>>"));
        assertEquals(10, day09.countGarbage("<{o\"i!a,<{i<a>"));
    }

    @Test
    public void testDay10Examples() {
        final Day10 day10 = new Day10();

        final Day10.Result result = day10.twist(day10.circularList(5), new int[] {3,4,1,5}, 5, 0, 0);
        assertEquals(3, (int) result.getCircularList().get(0));
        assertEquals(4, (int) result.getCircularList().get(1));
        assertEquals(4, result.getPosition());
        assertEquals(4, result.getSkipSize());
        // part 2
        assertEquals("49,44,50,44,51", day10.convertToASCII("1,2,3"));
        final int[] expected = {49,44,50,44,51,17,31,73,47,23};
        final int[] actual = day10.concoctMagicSequence(day10.convertToASCII("1,2,3"));
        for (int i = 0; i < expected.length; i++)
            assertEquals(expected[i], actual[i]);

        final int[] sparse = {65, 27, 9, 1, 4, 3, 40, 50, 91, 7, 6, 0, 2, 5, 68, 22};
        int xor = 0;
        for (final int val : sparse)
            xor ^= val;

        assertEquals(64, xor);
        assertEquals("a2582a3a0e66e6e86e3812dcb672a272", day10.toHexidecimal(day10.fullKnot("", 256)));
        assertEquals("33efeb34ea91902bb2f59c9920caa6cd", day10.toHexidecimal(day10.fullKnot("AoC 2017", 256)));
        assertEquals("3efbe78a8d82f29979031a4aa0b16a9d", day10.toHexidecimal(day10.fullKnot("1,2,3", 256)));
        assertEquals("63960835bcdc130f0b66d7ff4f6a5a8e", day10.toHexidecimal(day10.fullKnot("1,2,4", 256)));
    }

    @Test
    public void testDay11Examples() {
        final Day11 day11 = new Day11();
        assertEquals(3, (int) day11.countSteps("ne,ne,ne").getLeft());
        assertEquals(0, (int) day11.countSteps("ne,ne,sw,sw").getLeft());
        assertEquals(2, (int) day11.countSteps("ne,ne,s,s").getLeft());
        assertEquals(3, (int) day11.countSteps("se,sw,se,sw,sw").getLeft());
    }

    @Test
    public void testDay12Examples() {
        final Day12 day12 = new Day12();
        final List<String> pipes = Arrays.asList(
                "0 <-> 2", "1 <-> 1", "2 <-> 0, 3, 4", "3 <-> 2, 4", "4 <-> 2, 3, 6", "5 <-> 6", "6 <-> 4, 5");
        final Map<String, List<String>> map = day12.mapPrograms(pipes);
        assertEquals(6, day12.countPrograms(map, "0").size());
        assertEquals(2, day12.countProgramGroups(map));
    }

    @Test
    public void testDay13Examples() {
        final Day13 day13 = new Day13();
        final List<String> input = Arrays.asList("0: 3", "1: 2", "4: 4", "6: 4");
        assertEquals(24, day13.traverseNetwork(input));
        assertEquals(10, day13.shortestDelay(input));
    }

    @Test
    public void testDay14Examples() {
        final Day14 day14 = new Day14();
        assertEquals("0000", day14.highEndBits("0"));
        assertEquals("0001", day14.highEndBits("1"));
        assertEquals("1110", day14.highEndBits("e"));
        assertEquals("1111", day14.highEndBits("f"));
        assertTrue(StringUtils.startsWith(
                day14.highEndBits("a0c2017"), "1010000011000010000000010111"));

        if (!day14.cache()) {
            assertEquals(8108, day14.countUsedSquares(day14.listBits("flqrgnkx")));
            assertEquals(1242, day14.countRegions(day14.listBits("flqrgnkx")));
        }
    }

    @Test
    public void testDay15Examples() {
        final Day15 day15 = new Day15();
        Day15.Generator genA = Day15.Generator.A(65);
        Day15.Generator genB = Day15.Generator.B(8921);
        long[] expectedA = {    1092455L, 1181022009L,  245556042L, 1744312007L, 1352636452L };
        long[] expectedB = {  430625591L, 1233683848L, 1431495498L,  137874439L,  285222916L };
        String[] genAbits = { "00000000000100001010101101100111", "01000110011001001111011100111001",
                "00001110101000101110001101001010", "01100111111110000001011011000111", "01010000100111111001100000100100"};
        String[] genBbits = { "00011001101010101101001100110111", "01001001100010001000010110001000",
                "01010101010100101110001101001010", "00001000001101111100110000000111", "00010001000000000010100000000100"};
        final boolean[] expectedBitsMatch = { false, false, true, false, false };

        for (int i = 0; i < 5; i++) {
            final long nextA = genA.next();
            final long nextB = genB.next();
            assertEquals(expectedA[i], nextA);
            assertEquals(expectedB[i], nextB);
            assertEquals(genAbits[i], day15.toBits(nextA));
            assertEquals(genBbits[i], day15.toBits(nextB));
            assertEquals(expectedBitsMatch[i], day15.lowBitsMatch(nextA, nextB));
        }

        if (!day15.cache()) {
            genA = Day15.Generator.A(65);
            genB = Day15.Generator.B(8921);
            assertEquals(588, day15.countLowBitMatches(40000000, Pair.of(genA, genB)));
        }

        // part 2
        genA = Day15.Generator.A(65, i -> (i % 4) == 0);
        genB = Day15.Generator.B(8921, i -> (i % 8) == 0);
        expectedA = new long[] { 1352636452L, 1992081072L,  530830436L, 1980017072L, 740335192L };
        expectedB = new long[] { 1233683848L,  862516352L, 1159784568L, 1616057672L, 412269392L };
        genAbits = new String[] { "01010000100111111001100000100100", "01110110101111001011111010110000",
                "00011111101000111101010001100100", "01110110000001001010100110110000", "00101100001000001001111001011000"};
        genBbits = new String[] { "01001001100010001000010110001000", "00110011011010001111010010000000",
                "01000101001000001110100001111000", "01100000010100110001010101001000", "00011000100100101011101101010000"};

        for (int i = 0; i < 5; i++) {
            final long nextA = genA.next();
            final long nextB = genB.next();
            assertEquals(expectedA[i], nextA);
            assertEquals(expectedB[i], nextB);
            assertEquals(genAbits[i], day15.toBits(nextA));
            assertEquals(genBbits[i], day15.toBits(nextB));
            assertFalse(day15.lowBitsMatch(nextA, nextB));
        }

        if (!day15.cache()) {
            genA = Day15.Generator.A(65, i -> (i % 4) == 0);
            genB = Day15.Generator.B(8921, i -> (i % 8) == 0);
            assertEquals(309, day15.countLowBitMatches(5000000, Pair.of(genA, genB)));
        }
    }

    @Test
    public void testDay16Examples() {
        final Day16 day16 = new Day16();
        // s1, a spin of size 1: eabcd.
        // *  - x3/4, swapping the last two programs: eabdc.
        // *  - pe/b, swapping programs e and b: baedc.
        final String danced = day16.doALittleDance("abcde", Arrays.asList("s1", "x3/4", "pe/b"));
        assertEquals("baedc", danced);
        final String dancedAgain = day16.doALittleDance(danced, Arrays.asList("s1", "x3/4", "pe/b"));
        assertEquals("ceadb", dancedAgain);
    }

    @Test
    public void testDay17Examples() {
        final Day17 day17 = new Day17();
        final List<Integer> buffer = day17.newBuffer();

        int position = day17.stepForward(buffer, 3, 0, 1);
        assertEquals(1, position);
        position = day17.stepForward(buffer, 3, position, 2);
        assertEquals(1, position);
        position = day17.stepForward(buffer, 3, position, 3);
        assertEquals(2, position);
        final int value = day17.shortCircuitSpinLock(3);
        assertEquals(638, value);
    }

    @Test
    public void testDay18Examples() throws Exception {
        final Day18 day18 = new Day18();
        final List<String> instructions = Arrays.asList(
                "set a 1", "add a 2", "mul a a", "mod a 5", "snd a",
                "set a 0", "rcv a", "jgz a -1", "set a 1", "jgz a -2");
        assertEquals(4, (new Tablet()).applyInstructions(instructions));
        // part 2
        final List<String> deadlock = Arrays.asList(
                "snd 1", "snd 2", "snd p", "rcv a", "rcv b", "rcv c", "rcv d");
        assertEquals(3, day18.pairTablets(deadlock));
    }

    @Test
    public void testDay19Examples() {
        final Day19 day19 = new Day19();
        final List<String> points = Arrays.asList(
                "    |",
                "    |  +--+",
                "    A  |  C",
                "F---|----E|--+",
                "    |  |  |  D",
                "    +B-+  +--+");
        final Map<Point, String> grid = day19.buildGrid(points);
        assertEquals(new Point(4, 0), day19.findStart(grid));
        final List<Point> path = day19.followPath(grid);
        assertEquals("ABCDEF", day19.toLetters(path, grid));
        // part 2
        assertEquals(38, path.size());
    }

    @Test
    public void testDay20Examples() {
        final Day20 day20 = new Day20();
        final List<String> input = Arrays.asList(
                "p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>", "p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>");
        final List<Day20.Particle> list = day20.toParticles(input);

        assertEquals(0, day20.findClosest(day20.simulateParticles(list, 25)));
        // part 2
        final List<String> input2 = Arrays.asList(
                "p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>", "p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>",
                "p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>", "p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>");
        final List<Day20.Particle> list2 = day20.toParticles(input2);    // un-ticked
        assertEquals(1, day20.removeCollisions(list2));
    }
}

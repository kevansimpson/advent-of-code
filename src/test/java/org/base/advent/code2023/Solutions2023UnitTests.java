package org.base.advent.code2023;

import org.base.advent.PuzzleTester;
import org.base.advent.code2022.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Verify answers for AoC days in 2023.
 */
public class Solutions2023UnitTests extends PuzzleTester {
    @Test
    public void verifyDay01() {
        testSolutions(new Day01(), readLines("/2023/input01.txt"),
                54667, Day01.DocSum::sum1,
                54203, Day01.DocSum::sum2);
    }

    @Test
    public void verifyDay02() {
        testSolutions(new Day02(), readLines("/2023/input02.txt"),
                2593, Day02.CubeBag::sum,
                54699, Day02.CubeBag::power);
    }

    @Test
    public void verifyDay04() {
        testSolutions(new Day04(), readLines("/2023/input04.txt"),
                21088, Day04.Scratchcards::score,
                6874754, Day04.Scratchcards::count);
    }

    @Test
    public void verifyDay05() {
        testSolutions(new Day05(), readLines("/2023/input05.txt"),
                424490994L, Day05.Locations::lowest,
                15290096L, Day05.Locations::reallyLowest);
    }

    @Test
    public void verifyDay06() {
        testSolutions(new Day06(), List.of(
                new Day06.Race(41, 249),
                new Day06.Race(77, 1362),
                new Day06.Race(70, 1127),
                new Day06.Race(96, 1011)),
                771628L, Day06.Wins::wins1,
                27363861L, Day06.Wins::wins2);
    }

    @Test
    public void verifyDay07() {
        testSolutions(new Day07(), readLines("/2023/input07.txt"),
                249748283L, Day07.CardSums::sum,
                248029057L, Day07.CardSums::wildSum);
    }

    @BeforeAll
    public static void start() {
        banner(2023);
    }

    @AfterAll
    public static void stop() {
        banner(2023);
    }
}

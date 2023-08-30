package org.base.advent.code2016;

import org.base.advent.TimeSaver;
import org.base.advent.util.HashAtIndex;

import java.util.List;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2016/day/5">Day 5</a>
 */
public class Day05 implements Function<String, Day05.TwoPasswords>, TimeSaver {
    private static final List<Character> IN_RANGE = List.of('0', '1', '2', '3', '4', '5', '6', '7');
    public record TwoPasswords(String first, String second) {}

    @Override
    public TwoPasswords apply(String input) {
        // start and count are determined in hindsight from final solution.
        // calculating md5 hashes is slow, full stop.
        // shave a little time by starting at first matching hash OR
        // for fast solve, iterate through known hash indexes
        HashAtIndex start = new HashAtIndex(input, null, 4515058L);
        final List<HashAtIndex> list = fastOrFull(cached, () -> start.nextWith("00000", 17));

        StringBuilder first = new StringBuilder();
        char[] second = "        ".toCharArray();
        for (HashAtIndex hai : list) {
            System.out.println(hai);
            char five = hai.hash().charAt(5);
            if (first.length() < 8)
                first.append(five);

            if (IN_RANGE.contains(five)) {
                int digit = five - '0';
                if (digit < 8 && second[digit] == ' ')
                    second[digit] = hai.hash().charAt(6);
            }
        }

        return new TwoPasswords(first.toString(), new String(second));
    }

    // hashes with prefix and indices, from prior runs; only contains hits; cached for `TimeSaver`
    private static final List<HashAtIndex> cached = List.of(
            new HashAtIndex(null,"00000191970e97b86ecd2220e76d86b2", 4515059L),
            new HashAtIndex(null,"00000a1568b97dfc4736c4248df549b3", 6924074L),
            new HashAtIndex(null,"00000312234ca27718d52476a44c257c", 8038154L),
            new HashAtIndex(null,"00000064ec7123bedfc9ff00cc4f55f2", 13432968L),
            new HashAtIndex(null,"0000091c9c2cd243304328869af7bab2", 13540621L),
            new HashAtIndex(null,"0000096753dd21d352853f1d97e19d01", 14095580L),
            new HashAtIndex(null,"00000a220003ca08164ab5fbe0b7c08f", 14821988L),
            new HashAtIndex(null,"00000aaa1e7e216d6fb95a53fde7a594", 16734551L),
            new HashAtIndex(null,"000002457920bc00c2bd4d769a3da01c", 17743256L),
            new HashAtIndex(null,"000005074f875107f82b4ffb39a1fbf0", 19112977L),
            new HashAtIndex(null,"0000049d19713e17d7d93e9b1f02c856", 20616595L),
            new HashAtIndex(null,"000006c0b6e2bfeabd18eb400b3aecf7", 21658552L),
            new HashAtIndex(null,"000007d44ea65d0437b810035fec92f2", 26326685L));

}

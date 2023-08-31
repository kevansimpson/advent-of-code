package org.base.advent.code2016;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2016/day/7">Day 7</a>
 */
public class Day07 implements Function<List<String>, Day07.SecureIpCount> {
    public record SecureIpCount(int tslCount, int sslCount) {}

    @Override
    public SecureIpCount apply(List<String> input) {
        int tls = 0, ssl = 0;
        List<List<String>> ipv7List = input.stream()
                .map(it -> Arrays.asList(it.split("[\\[\\]]"))).toList();
        for (List<String> ipv7 : ipv7List) {
            if (supportsTLS(ipv7))
                tls += 1;

            if (supportsSSL(ipv7))
                ssl += 1;
        }
        return new SecureIpCount(tls, ssl);
    }

    boolean supportsTLS(List<String> ipv7) {
        boolean even = false;
        for (int i = 0; i < ipv7.size(); i++) {
            if ((i % 2) == 0 && abba(ipv7.get(i)))
                even = true;
            if ((i % 2) > 0 && abba(ipv7.get(i)))
                return false;
        }
        return even;
    }

    boolean supportsSSL(List<String> ipv7) {
        Set<String> abaSet = new HashSet<>();
        List<String> oddList = new ArrayList<>();
        for (int i = 0; i < ipv7.size(); i++) {
            if ((i % 2) == 0) {
                String str = ipv7.get(i);
                Matcher m = ABA_REGEX.matcher(str);
                while (m.matches()) {
                    String a = m.group(1), b = m.group(2);
                    String aba = String.format("%s%s%s", a, b, a);
                    String bab = String.format("%s%s%s", b, a, b);
                    str = str.substring(0, str.indexOf(aba) + 2);
                    m = ABA_REGEX.matcher(str);
                    if (!a.equals(b)) {
                        abaSet.add(bab);
                    }
                }
            }
            else
                oddList.add(ipv7.get(i));
        }

        if (!abaSet.isEmpty()) {
            for (String odd : oddList)
                if (StringUtils.containsAny(odd, abaSet.toArray(new String[0])))
                    return true;
        }
        return false;
    }

    private static final Pattern ABA_REGEX = Pattern.compile(".*(\\w)(\\w)\\1.*");
    private static final Pattern ABBA_REGEX = Pattern.compile(".*(\\w)(\\w)\\2\\1.*");

    private boolean abba(String text) {
        Matcher m = ABBA_REGEX.matcher(text);
        return m.matches() && !m.group(1).equals(m.group(2));
    }
}

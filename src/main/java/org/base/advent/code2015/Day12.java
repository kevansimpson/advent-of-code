package org.base.advent.code2015;

import org.base.advent.Solution;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>Part 1</h2>
 * Santa's Accounting-Elves need help balancing the books after a recent order. Unfortunately, their accounting
 * software uses a peculiar storage format. That's where you come in.
 *
 * They have a JSON document which contains a variety of things: arrays ([1,2,3]), objects ({"a":1, "b":2}), numbers,
 * and strings. Your first job is to simply find all of the numbers throughout the document and add them together.
 *
 * For example:
 *  - [1,2,3] and {"a":2,"b":4} both have a sum of 6.
 *  - [[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
 *  - {"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
 *  - [] and {} both have a sum of 0.
 *
 * You will not encounter any strings containing numbers.
 *
 * What is the sum of all numbers in the document?
 *
 * <h2>Part 2</h2>
 * Uh oh - the Accounting-Elves have realized that they double-counted everything red.
 *
 * Ignore any object (and all of its children) which has any property with the value "red". Do this only for
 * objects ({...}), not arrays ([...]).
 *
 *  - [1,2,3] still has a sum of 6.
 *  - [1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
 *  - {"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
 *  - [1,"red",5] has a sum of 6, because "red" in an array has no effect.
 *
 */
public class Day12 implements Solution<String> {

    private static Pattern numbers = Pattern.compile("([-\\d]+)");

    @Override
    public String getInput() throws IOException {
        return readInput("/2015/input12.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return sumNumbers(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return sumJson(getInput());
    }


    public int sumNumbers(final String input) {
        int sum = 0;
        final Matcher m = numbers.matcher(input);
        while (m.find()) {
            sum += Integer.parseInt(m.group());
        }

        return sum;
    }

    public int sumJson(final String input) throws Exception {
        final JSONParser parser = new JSONParser();
        return sum((JSONObject) parser.parse(input));
    }

    protected int sum(final JSONObject obj) {
        int sum = 0;
        
        for (final Object key : obj.keySet()) {
            final Object val = obj.get(key);
            if (!(key instanceof String))
                throw new RuntimeException(String.valueOf(key));

            if (val instanceof JSONObject) {
                sum += sum((JSONObject) val);
            }
            else if (val instanceof JSONArray) {
                sum += sum((JSONArray) val);
            }
            else if (val instanceof String) {
                final String str = (String) val;
                if ("red".equalsIgnoreCase(str))
                    return 0;
            }
            else
                sum += Integer.parseInt(String.valueOf(val));
        }

        return sum;
    }

    protected int sum(final JSONArray arr) {
        int sum = 0;
        
        for (final Object elem : arr) {
            if (elem instanceof JSONObject) {
                sum += sum((JSONObject) elem);
            }
            else if (elem instanceof JSONArray) {
                sum += sum((JSONArray) elem);
            }
            else if (!(elem instanceof String)) {
                sum += Integer.parseInt(String.valueOf(elem));
            }
        }

        return sum;
    }
}

package org.base.advent.code2015;

import org.base.advent.Solution;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/12">Day 12</a>
 */
public class Day12 implements Solution<String> {

    private static final Pattern numbers = Pattern.compile("([-\\d]+)");

    @Override
    public String getInput(){
        return readInput("/2015/input12.txt");
    }

    @Override
    public Object solvePart1() {
        return sumNumbers(getInput());
    }

    @Override
    public Object solvePart2() {
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

    public int sumJson(final String input) {
        try {
            final JSONParser parser = new JSONParser();
            return sum((JSONObject) parser.parse(input));
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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

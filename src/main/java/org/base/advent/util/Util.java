package org.base.advent.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility methods.
 */
public class Util {
    public static <T> int sum(final List<T> list, final ToIntFunction<T> function) {
        return list.stream().mapToInt(function).sum();
    }

    public static List<String[]> split(final String... input) {
        return Stream.of(input).map(str -> str.split(",")).collect(Collectors.toList());
    }

    public static <T> List<List<T>> permute(T... set) {
        List<List<T>> result = new ArrayList<>();

        //start from an empty list
        result.add(new ArrayList<>());

        for (T t : set) {
            //list of list in current iteration of the array num
            List<List<T>> current = new ArrayList<>();

            for (List<T> l : result) {
                // # of locations to insert is largest index + 1
                for (int j = 0; j < l.size() + 1; j++) {
                    // + add num[i] to different locations
                    l.add(j, t);

                    List<T> temp = new ArrayList<>(l);
                    current.add(temp);

                    //System.out.println(temp);

                    // - remove num[i] add
                    //noinspection SuspiciousListRemoveInLoop
                    l.remove(j);
                }
            }

            result = new ArrayList<>(current);
        }

        return result;
    }

//    public List<List<Integer>> permute(int[] num) {
//        List<List<Integer>> result = new ArrayList<>();
//
//        //start from an empty list
//        result.add(new ArrayList<>());
//
//        for (int i = 0; i < num.length; i++) {
//            //list of list in current iteration of the array num
//            List<List<Integer>> current = new ArrayList<>();
//
//            for (List<Integer> l : result) {
//                // # of locations to insert is largest index + 1
//                for (int j = 0; j < l.size()+1; j++) {
//                    // + add num[i] to different locations
//                    l.add(j, num[i]);
//
//                    ArrayList<Integer> temp = new ArrayList<>(l);
//                    current.add(temp);
//
//                    //System.out.println(temp);
//
//                    // - remove num[i] add
//                    l.remove(j);
//                }
//            }
//
//            result = new ArrayList<>(current);
//        }
//
//        return result;
//    }

}

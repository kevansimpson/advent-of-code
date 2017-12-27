package org.base.advent.code2015;

import org.base.advent.Solution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <h2>Part 1</h2>
 * Today, you set out on the task of perfecting your milk-dunking cookie recipe. All you have to do is find the
 * right balance of ingredients.
 *
 * Your recipe leaves room for exactly 100 teaspoons of ingredients. You make a list of the remaining ingredients
 * you could use to finish the recipe (your puzzle input) and their properties per teaspoon:
 *
 *  - capacity (how well it helps the cookie absorb milk)
 *  - durability (how well it keeps the cookie intact when full of milk)
 *  - flavor (how tasty it makes the cookie)
 *  - texture (how it improves the feel of the cookie)
 *  - calories (how many calories it adds to the cookie)
 *
 * You can only measure ingredients in whole-teaspoon amounts accurately, and you have to be accurate so you can
 * reproduce your results in the future. The total score of a cookie can be found by adding up each of the properties
 * (negative totals become 0) and then multiplying together everything except calories.
 *
 * For instance, suppose you have these two ingredients:
 *
 * Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
 * Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
 *
 * Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon (because the amounts of each
 * ingredient must add up to 100) would result in a cookie with the following properties:
 *
 *  - A capacity of 44*-1 + 56*2 = 68
 *  - A durability of 44*-2 + 56*3 = 80
 *  - A flavor of 44*6 + 56*-2 = 152
 *  - A texture of 44*3 + 56*-1 = 76
 *
 * Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now) results in a total score of 62842880,
 * which happens to be the best score possible given these ingredients. If any properties had produced a negative
 * total, it would have instead become zero, causing the whole score to multiply to zero.
 *
 * Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring cookie
 * you can make?
 *
 * <h2>Part 2</h2>
 * Your cookie recipe becomes wildly popular! Someone asks if you can make another recipe that has exactly 500 calories
 * per cookie (so they can use it as a meal replacement). Keep the rest of your award-winning process the same
 * (100 teaspoons, same ingredients, same scoring system).
 *
 * For example, given the ingredients above, if you had instead selected 40 teaspoons of butterscotch and 60 teaspoons
 * of cinnamon (which still adds to 100), the total calorie count would be 40*8 + 60*3 = 500. The total score would
 * go down, though: only 57600000, the best you can do in such trying circumstances.
 *
 * Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring cookie
 * you can make with a calorie total of 500?
 *
 */
public class Day15 implements Solution<List<String>> {
    private Map<String, Ingredient> ingredientMap;
    private int highestScore = Integer.MIN_VALUE;

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input15.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return findHighestScore(getInput());
    }

    @Override
    public Object solvePart2() throws Exception {
        return findHighestScoreFor500Calories(getInput());
    }
    
    public int findHighestScore(final List<String> cookbook) {
        ingredientMap = buildIngredientMap(cookbook);
        final Recipe recipe = new Recipe();
        final List<Ingredient> ingredientList = new ArrayList<>(this.ingredientMap.values());
        buildAllRecipes(ingredientList, 0, recipe, 100, -1);
        return highestScore;
    }

    public int findHighestScoreFor500Calories(final List<String> cookbook) {
        if (ingredientMap.isEmpty())
            ingredientMap = buildIngredientMap(cookbook);

        highestScore = Integer.MIN_VALUE;
        final Recipe recipe = new Recipe();
        final List<Ingredient> ingredientList = new ArrayList<>(this.ingredientMap.values());
        buildAllRecipes(ingredientList, 0, recipe, 100, 500);
        return highestScore;
    }

    protected void buildAllRecipes(final List<Ingredient> ingredientList, final int ingredientIndex,
                                   final Recipe recipe, final int total, final int calorieRequirement) {
        if (ingredientList.size() <= ingredientIndex) {    // recipe has all ingredients
            if (recipe.sumTeaspoons() == 100) {            // recipe has =100 teaspoons
                if (calorieRequirement <= 0 || recipe.caloricCount(ingredientMap) == calorieRequirement) {
                    final int score = recipe.calculateScore(ingredientMap);
                    if (score > highestScore) {
                        highestScore = score;
                    }
                }
            }

            return;
        }

        for (int i = 0; i <= total; i++) {
            final Ingredient current = ingredientList.get(ingredientIndex);
            recipe.setTeaspoons(current.name, i);
            buildAllRecipes(ingredientList, ingredientIndex + 1, recipe, total - i, calorieRequirement);
        }
    }

    private static Pattern parser = Pattern.compile(
            "([^:]+):\\s[a-z]+\\s([-\\d]+),\\s[a-z]+\\s([-\\d]+),\\s[a-z]+\\s([-\\d]+),\\s[a-z]+\\s([-\\d]+),\\s[a-z]+\\s([-\\d]+)",
            Pattern.DOTALL);

    protected Map<String, Ingredient> buildIngredientMap(final List<String> cookbook) {
        final Map<String, Ingredient> ingredientMap = new HashMap<>();
        for (final String ingredient : cookbook) {
            final Matcher matcher = parser.matcher(ingredient);
            if (matcher.matches()) {
                final String name = matcher.group(1);
                ingredientMap.put(name, new Ingredient(name, Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6))));
            }
            else {
                throw new RuntimeException("No match: "+ ingredient);
            }
        }

        return ingredientMap;
    }

    private enum Trait {
        capacity, durability, flavor, texture, calories
    }

    private static class Recipe {
        private final Map<String, Integer> ingredientMap = new HashMap<>();// # of tspn/ingredient

        public int caloricCount(final Map<String, Ingredient> cookbook) {
            int count = 0;
            for (final String iname : cookbook.keySet()) {
                final Ingredient ingredient = cookbook.get(iname);
                count += (ingredientMap.get(iname)) * ingredient.getValue(Trait.calories);
            }
            if (count <= 0)
                return 0;

            return count;
        }

        public void setTeaspoons(final String ingredient, final int teaspoons) {
            ingredientMap.put(ingredient, teaspoons);
        }

        public int sumTeaspoons() {
            return ingredientMap.values().stream().mapToInt(Integer::intValue).sum();
        }

        public int calculateScore(final Map<String, Ingredient> cookbook) {
            int score = 1;
            for (final Trait trait : Trait.values()) {
                if (trait == Trait.calories) continue;

                int value = 0;
                for (final String iname : cookbook.keySet()) {
                    final Ingredient ingredient = cookbook.get(iname);
                    value += (ingredientMap.get(iname)) * ingredient.getValue(trait);
                }

                if (value <= 0)
                    return 0;

                score *= value;
            }
            
            return score;
        }
    }

    private static class Ingredient {
        public final String name;
        public final Map<Trait, Integer> traits = new HashMap<>();
        public Ingredient(final String nm, final int cap, final int d, final int f, final int t, final int cal) {
            name = nm;
            traits.put(Trait.capacity, cap);
            traits.put(Trait.durability, d);
            traits.put(Trait.flavor, f);
            traits.put(Trait.texture, t);
            traits.put(Trait.calories, cal);
        }

        public int getValue(final Trait trait) {
            return traits.get(trait);
        }

        @Override
        public boolean equals(final Object obj) {
            return (obj instanceof Ingredient) && obj.hashCode() == this.hashCode();
        }

        @Override
        public int hashCode() {
            return toString().hashCode() * 7;
        }

        @Override
        public String toString() {
            return name + traits.toString();
        }
    }
}

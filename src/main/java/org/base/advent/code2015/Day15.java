package org.base.advent.code2015;

import org.base.advent.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2015/day/15">Day 15</a>
 */
public class Day15 implements Solution<List<String>> {
    @Override
    public Object solvePart1(final List<String> input) {
        return findHighestScore(input, -1);
    }

    @Override
    public Object solvePart2(final List<String> input) {
        return findHighestScore(input, 500);
    }

    int findHighestScore(final List<String> cookbook, final int calorieRequirement) {
        Map<String, Ingredient> ingredientMap = buildIngredientMap(cookbook);
        final Recipe recipe = new Recipe();
        final List<Ingredient> ingredientList = new ArrayList<>(ingredientMap.values());
        buildAllRecipes(ingredientMap, ingredientList, 0, recipe, 100, calorieRequirement);
        return recipe.highestScore;
    }

    protected void buildAllRecipes(final Map<String, Ingredient> ingredientMap,
                                   final List<Ingredient> ingredientList,
                                   final int ingredientIndex,
                                   final Recipe recipe,
                                   final int total,
                                   final int calorieRequirement) {
        if (ingredientList.size() <= ingredientIndex) {    // recipe has all ingredients
            if (recipe.sumTeaspoons() == 100) {            // recipe has =100 teaspoons
                if (calorieRequirement <= 0 || recipe.caloricCount(ingredientMap) == calorieRequirement) {
                    final int score = recipe.calculateScore(ingredientMap);
                    if (score > recipe.highestScore) {
                        recipe.highestScore = score;
                    }
                }
            }

            return;
        }

        // one {500 map[Butterscotch:31 Candy:29 Frosting:24 Sugar:16]}
        // two {500 map[Butterscotch:31 Candy:23 Frosting:21 Sugar:25]}
        // hindsight tells us this loop can be 16..32 or (2^numIngredients)..(2^numIngredients+1)
        for (int i = 16; i <= 32; i++) {
            final Ingredient current = ingredientList.get(ingredientIndex);
            recipe.setTeaspoons(current.name, i);
            buildAllRecipes(ingredientMap, ingredientList,
                    ingredientIndex + 1, recipe, total - i, calorieRequirement);
        }
    }

    private static final Pattern parser = Pattern.compile(
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
        int highestScore = Integer.MIN_VALUE;

        public int caloricCount(final Map<String, Ingredient> cookbook) {
            int count = 0;
            for (final String name : cookbook.keySet()) {
                final Ingredient ingredient = cookbook.get(name);
                count += (ingredientMap.get(name)) * ingredient.getValue(Trait.calories);
            }

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
            return name + traits;
        }
    }
}

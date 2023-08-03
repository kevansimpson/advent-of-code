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
    private Map<String, Ingredient> ingredientMap;
    private int highestScore = Integer.MIN_VALUE;

    @Override
    public List<String> getInput() {
        return readLines("/2015/input15.txt");
    }

    @Override
    public Object solvePart1() {
        return findHighestScore(getInput());
    }

    @Override
    public Object solvePart2() {
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

        public int caloricCount(final Map<String, Ingredient> cookbook) {
            int count = 0;
            for (final String name : cookbook.keySet()) {
                final Ingredient ingredient = cookbook.get(name);
                count += (ingredientMap.get(name)) * ingredient.getValue(Trait.calories);
            }

            return Math.max(count, 0);
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

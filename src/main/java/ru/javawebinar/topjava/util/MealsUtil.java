package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MealsUtil {

    private MealsUtil() {
    }

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return getFiltered(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, @Nullable LocalTime startTime, @Nullable LocalTime endTime) {
        return getFiltered(meals, caloriesPerDay, meal -> Util.isBetweenInclusive(meal.getTime(), startTime, endTime));
    }

    private static List<MealTo> getFiltered(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );
        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }


//    private static List<MealTo> getFilteredByCycle(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
//        meals.forEach(meal -> caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum));
//
//        final List<MealTo> mealsTo = new ArrayList<>();
//        meals.forEach(meal -> {
//            caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
//            if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
//                mealsTo.add(createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
//            }
//        });
//        return mealsTo;
//    }

//    private static List<MealTo> getFilteredByRecursion(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//        ArrayList<MealTo> result = new ArrayList<>();
//        filterWithRecursion(new LinkedList<>(meals), startTime, endTime, caloriesPerDay, new HashMap<>(), result);
//        return result;
//    }

//    private static void filterWithRecursion(LinkedList<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay,
//                                            Map<LocalDate, Integer> dailyCaloriesMap, List<MealTo> result) {
//        if (meals.isEmpty()) return;
//
//        Meal meal = meals.pop();
//        dailyCaloriesMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
//        filterWithRecursion(meals, startTime, endTime, caloriesPerDay, dailyCaloriesMap, result);
//        if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
//            result.add(createTo(meal, dailyCaloriesMap.get(meal.getDate()) > caloriesPerDay));
//        }
//    }

//        private static List<MealTo> getFilteredByAtomic(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//            Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
//            Map<LocalDate, AtomicBoolean> exceededMap = new HashMap<>();
//
//            List<MealTo> mealsTo = new ArrayList<>();
//            meals.forEach(meal -> {
//                AtomicBoolean wrapBoolean = exceededMap.computeIfAbsent(meal.getDate(), date -> new AtomicBoolean());
//                Integer dailyCalories = caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
//                if (dailyCalories > caloriesPerDay) {
//                    wrapBoolean.set(true);
//                }
//                if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
//                  mealsTo.add(createTo(meal, wrapBoolean.get()));  // also change createWithExcess and MealTo.excess
//                }
//            });
//            return mealsTo;
//        }

//    private static List<MealTo> getFilteredByClosure(List<Meal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
//        List<MealTo> mealsTo = new ArrayList<>();
//        mealList.forEach(meal -> {
//                    caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
//                    if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
//                        mealsTo.add(createTo(meal, (caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))); // also change createWithExcess and MealTo.excess
//                    }
//                }
//        );
//        return mealsTo;
//    }

//    private static List<MealTo> getFilteredByExecutor(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) throws InterruptedException, ExecutionException, ExecutionException {
//        Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
//        List<Callable<MealTo>> tasks = new ArrayList<>();
//
//        meals.forEach(meal -> {
//            caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum);
//            if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
//                tasks.add(() -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
//            }
//        });
//        ExecutorService executorService = Executors.newFixedThreadPool(4);
//        List<Future<MealTo>> futures = executorService.invokeAll(tasks);
//        executorService.shutdown();
//        final List<MealTo> mealsTo = new ArrayList<>();
//        for (Future<MealTo> future : futures) {
//            mealsTo.add(future.get());
//        }
//        return mealsTo;
//    }

//    private static List<MealTo> getFilteredByFlatMap(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//        Collection<List<Meal>> list = meals.stream()
//                .collect(Collectors.groupingBy(Meal::getDate)).values();
//
//        return list.stream()
//                .flatMap(dayMeals -> {
//                    boolean excess = dayMeals.stream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
//                    return dayMeals.stream().filter(meal ->
//                            DateTimeUtil.isBetween(meal.getTime(), startTime, endTime))
//                            .map(meal -> createTo(meal, excess));
//                }).collect(Collectors.toList());
//    }

//    private static List<MealTo> getFilteredByCollector(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//        final class Aggregate {
//            private final List<Meal> dailyMeals = new ArrayList<>();
//            private int dailySumOfCalories;
//
//            private void accumulate(Meal meal) {
//                dailySumOfCalories += meal.getCalories();
//                if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
//                    dailyMeals.add(meal);
//                }
//            }
//
//            // never invoked if the upstream is sequential
//            private Aggregate combine(Aggregate that) {
//                this.dailySumOfCalories += that.dailySumOfCalories;
//                this.dailyMeals.addAll(that.dailyMeals);
//                return this;
//            }
//
//            private Stream<MealTo> finisher() {
//                final boolean excess = dailySumOfCalories > caloriesPerDay;
//                return dailyMeals.stream().map(meal -> createTo(meal, excess));
//            }
//        }
//
//        Collection<Stream<MealTo>> values = meals.stream()
//                .collect(Collectors.groupingBy(Meal::getDate,
//                        Collector.of(Aggregate::new, Aggregate::accumulate, Aggregate::combine, Aggregate::finisher))
//                ).values();
//
//        return values.stream().flatMap(Function.identity()).collect(Collectors.toList());
//    }

    public static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
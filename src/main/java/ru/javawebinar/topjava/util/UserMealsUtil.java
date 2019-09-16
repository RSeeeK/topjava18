package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(UserMealsUtil::accept);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return getFilteredWithExceeded_for(mealList,
                startTime,
                endTime,
                caloriesPerDay);
    }

    private static List<UserMealWithExceed> getFilteredWithExceeded_for(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        //Получаем map с суммой калорий по дням
        Map<LocalDate, Integer> caloriesByDay = new HashMap<>();
        mealList.forEach(userMeal -> {
            LocalDate currentDate = userMeal.getDateTime().toLocalDate();
            caloriesByDay.put(currentDate, caloriesByDay.getOrDefault(currentDate, 0) + userMeal.getCalories());
        });

        //Проверяем, попадает ли прием пищи в интервал времени фильтрации,
        //если попадает - добавляем в результирующий список
        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        mealList.forEach(
                userMeal -> {
                    LocalTime currentTime = userMeal.getDateTime().toLocalTime();
                    if (currentTime.isAfter(startTime)
                            && currentTime.isBefore(endTime)) {
                        userMealWithExceeds.add(new UserMealWithExceed(userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                                caloriesByDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
                    }
                }
        );

        return userMealWithExceeds;
    }

    private static List<UserMealWithExceed> getFilteredWithExceeded_stream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        //Получаем map с суммой калорий по дням
        Map<LocalDate, Integer> caloriesByDay = mealList
                .stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        //Проверяем, попадает ли прием пищи в интервал времени фильтрации,
        //если попадает - добавляем в результирующий список
        return mealList
                .stream()
                .filter(userMeal -> {
                    LocalTime currentTime = userMeal.getDateTime().toLocalTime();
                    return currentTime.isAfter(startTime)
                            && currentTime.isBefore(endTime);
                })
                .map(meal ->
                        new UserMealWithExceed(meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                caloriesByDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static void accept(UserMealWithExceed userMealWithExceed) {
        System.out.println(userMealWithExceed.toString());
    }
}

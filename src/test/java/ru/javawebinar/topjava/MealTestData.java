package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final Meal USER_MEAL_BREAKFAST = new Meal(100002, LocalDateTime.of(2019,10,20,8,30,0), "Пользователь позавтракал", 500);
    public static final Meal USER_MEAL_LUNCH = new Meal(100003, LocalDateTime.of(2019,10,20, 14,30, 0), "Пользователь пообедал", 1000);
    public static final Meal USER_MEAL_DINNER = new Meal(100004, LocalDateTime.of(2019,10,20, 21,30,10), "Пользователь поужинал", 500);
    public static final Meal ADMIN_MEAL_BREAKFAST = new Meal(100005, LocalDateTime.of(2019,10,20, 12,0,0), "Админ позавтракал", 1500);
    public static final Meal ADMIN_MEAL_LUNCH = new Meal(100006, LocalDateTime.of(2019,10,20,18,0, 0), "Админ пообедал", 2000);
    public static final Meal ADMIN_MEAL_DINNER = new Meal(100007, LocalDateTime.of(2019,10,20,22,0, 0), "Админ поужинал", 2500);
    public static final Meal ADMIN_MEAL_DINNER_DUPLICATE = new Meal(null, LocalDateTime.of(2019,10,20,22,0, 0), "Админ поужинал", 2500);
    public static final Meal ADMIN_MEAL_LUNCH_UPDATED = new Meal(100006, LocalDateTime.of(2019,10,20,18,0, 0), "Админ пообедал", 2500);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator()
                .isEqualTo(expected);
    }

}

package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

public class MealTestData {
    public static final Meal USER_MEAL = new Meal(100002, LocalDateTime.of(2019,10,20,22, 40, 10), "Пользователь поужинал", 1500);
    public static final Meal ADMIN_MEAL = new Meal(100003, LocalDateTime.of(2019,10,20,22, 40, 25), "Админ пожрал", 1200);
}

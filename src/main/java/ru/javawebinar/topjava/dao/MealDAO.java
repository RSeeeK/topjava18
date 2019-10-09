package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDAO {
    Meal add(Meal meal);
    void remove(Long id);
    void update(Meal meal);
    List<Meal> getAll();
    Meal getById(Long id);
}

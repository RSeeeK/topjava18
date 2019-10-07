package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class MealDAOInMemoryImpl implements MealDAO {

    private static final ConcurrentNavigableMap<Long, Meal> meals;

    static {
        meals = new ConcurrentSkipListMap<>();
        meals.put(1L, new Meal(1L, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        meals.put(2L, new Meal(2L, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        meals.put(3L, new Meal(3L, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        meals.put(4L, new Meal(4L, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        meals.put(5L, new Meal(5L, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        meals.put(6L, new Meal(6L, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public void add(Meal meal) {
        Long id = 1L;
        if (meals.size() > 0) {
            id = meals.lastEntry().getValue().getId();
            meal.setId(++id);
        }
        meals.put(id, meal);
    }

    @Override
    public void remove(Meal meal) {
        meals.remove(meal.getId());
    }

    @Override
    public void update(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals
                .values());
    }

    @Override
    public Meal getById(Long id) {
        return meals.getOrDefault(id, null);
    }
}

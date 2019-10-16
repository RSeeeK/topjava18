package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private ConcurrentMap<Integer, ConcurrentMap<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            getByUserId(meal.getUserId()).put(meal.getId(), meal);
            return meal;
        }
        //TODO: Сделать проверку "анти Вася", убедиться что ид еды не принадлежит другому пользователю.
        // treat case: update, but not present in storage
        return getByUserId(meal.getUserId()).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, Integer userId) {
        return getByUserId(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        return getByUserId(userId).get(id);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return getByUserId(userId).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAllBetween(Integer userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return getByUserId(userId).values().stream()
                .filter(meal -> (startDate == null || startDate.compareTo(meal.getDate()) > 0) &
                        (endDate == null || endDate.compareTo(meal.getDate()) < 0) &
                        (startTime == null || startTime.compareTo(meal.getTime()) > 0) &
                        (startTime == null || startTime.compareTo(meal.getTime()) < 0))
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private ConcurrentMap<Integer, Meal> getByUserId(Integer userId) {
        repository.computeIfAbsent(userId, usersMeals -> new ConcurrentHashMap<>());
        return repository.get(userId);
    }

}


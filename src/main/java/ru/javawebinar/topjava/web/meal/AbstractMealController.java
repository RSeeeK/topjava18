package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public abstract class AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    @Autowired
    private ProfileRestController profile;

    public List<Meal> getAll() {
        log.info("getAll");
        return service.getAll(profile.get().getId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, profile.get().getId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, profile.get().getId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, profile.get().getId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, profile.get().getId());
    }
}

package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        assertMatch(service.get(USER_MEAL_BREAKFAST.getId(), USER_ID), USER_MEAL_BREAKFAST);
    }

    @Test
    public void delete() {
        service.delete(ADMIN_MEAL_BREAKFAST.getId(), ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL_DINNER, ADMIN_MEAL_LUNCH);
    }

    @Test
    public void update() {
        ADMIN_MEAL_LUNCH.setCalories(2500);
        service.update(ADMIN_MEAL_LUNCH, ADMIN_ID);
        assertMatch(service.get(ADMIN_MEAL_LUNCH.getId(), ADMIN_ID), ADMIN_MEAL_LUNCH_UPDATED);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(USER_MEAL_BREAKFAST.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(USER_MEAL_BREAKFAST.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(USER_MEAL_BREAKFAST, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> betweenDate = service.getBetweenDates(LocalDate.of(2019,10,20),LocalDate.of(2019,10,20), USER_ID);
        assertMatch(betweenDate, USER_MEAL_DINNER, USER_MEAL_LUNCH, USER_MEAL_BREAKFAST);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, ADMIN_MEAL_DINNER, ADMIN_MEAL_LUNCH, ADMIN_MEAL_BREAKFAST);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2019,10,23,23, 45, 0), "Пользователь еще поужинал", 2000);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), newMeal, USER_MEAL_DINNER, USER_MEAL_LUNCH, USER_MEAL_BREAKFAST);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createDuplicate() {
        service.create(ADMIN_MEAL_DINNER_DUPLICATE, ADMIN_ID);
    }
}
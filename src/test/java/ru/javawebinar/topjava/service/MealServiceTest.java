package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;


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

    @Test(expected = NotFoundException.class)
    public void get() {
        service.get(USER_MEAL.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(USER_MEAL.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void update() {
        service.update(USER_MEAL, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> betweenDate = service.getBetweenDates(LocalDate.of(2019,10,20),LocalDate.of(2019,10,20), USER_ID);
        assertThat(betweenDate).isEqualTo(Collections.singletonList(USER_MEAL));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertThat(all).isEqualTo(Collections.singletonList(ADMIN_MEAL));
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2019,10,21,22, 45), "Пользователь еще поужинал", 2000);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertThat(service.getAll(USER_ID)).isEqualTo(Arrays.asList(newMeal, USER_MEAL));
    }
}
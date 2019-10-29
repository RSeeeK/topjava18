package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestName;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final TestName description = new TestName();

    private static final Logger LOGGER = LoggerFactory.getLogger(MealServiceTest.class);

    private static final Map<String, Map<String, Long>> testResult = new ConcurrentHashMap<>();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void succeeded(long nanos, Description description) {
            ConcurrentHashMap<String, Long> currentTestResult = new ConcurrentHashMap<>();
            currentTestResult.put("succeeded", nanos);
            testResult.put(description.getMethodName(), currentTestResult);
        }

        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            ConcurrentHashMap<String, Long> currentTestResult = new ConcurrentHashMap<>();
            currentTestResult.put("failed", nanos);
            testResult.put(description.getMethodName(), currentTestResult);
        }

        @Override
        protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
            ConcurrentHashMap<String, Long> currentTestResult = new ConcurrentHashMap<>();
            currentTestResult.put("skipped", nanos);
            testResult.put(description.getMethodName(), currentTestResult);
        }

        @Override
        protected void finished(long nanos, Description description) {
            ConcurrentHashMap<String, Long> currentTestResult = new ConcurrentHashMap<>();
            currentTestResult.put("finished", nanos);
            testResult.put(description.getMethodName(), currentTestResult);
        }

    };

    @AfterClass
    public static void showResult() {
        LOGGER.info(" _________________________________________");
        LOGGER.info("|               TEST RESULT:              |");
        LOGGER.info("|_________________________________________|");
        String format = "| %1$-15s | %2$-10s | %3$-8s |";
        for (Map.Entry<String, Map<String, Long>> entry:
                testResult.entrySet()){
            for (Map.Entry<String, Long> innerEntry:
                    entry.getValue().entrySet()){
                LOGGER.info(String.format(format,
                        entry.getKey(),
                        innerEntry.getKey(),
                        "" + TimeUnit.NANOSECONDS.toMillis(innerEntry.getValue()) + " ms"));
            }
        }
        LOGGER.info("|_________________________________________|");
    }

    @Autowired
    private MealService service;

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(1, USER_ID);
    }

    @Test
    public void deleteNotOwn() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(1, USER_ID);
    }

    @Test
    public void getNotOwn() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}
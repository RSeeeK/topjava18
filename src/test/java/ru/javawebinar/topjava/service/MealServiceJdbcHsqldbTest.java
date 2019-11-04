package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.HSQL_DB;
import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles({HSQL_DB, JDBC})
public class MealServiceJdbcHsqldbTest extends MealServiceTest {
}
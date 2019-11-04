package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

@Repository
@Profile(Profiles.HSQL_DB)
public class JdbcHsqldbMealRepository extends JdbcCommonMealRepository {
    @Autowired
    public JdbcHsqldbMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public MapSqlParameterSource prepareSqlParameters(Meal meal, int userId) {
        return new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("date_time", convertToDate(meal.getDateTime()))
                .addValue("user_id", userId);
    }

    @Override
    public List<Meal> getBetweenInclusive(LocalDate startDate, LocalDate endDate, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id=? AND date_time >=? AND date_time < ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, getStartDateInclusive(startDate), getEndDateExclusive(endDate));
    }
}

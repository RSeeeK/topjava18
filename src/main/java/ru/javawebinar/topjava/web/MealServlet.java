package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MealDAOInMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final MealDAO mealDAO = new MealDAOInMemoryImpl();

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || Actions.valueOf(action) == Actions.list) {
            List<MealTo> mealsList = MealsUtil.getFiltered(mealDAO.getAll(),
                    LocalTime.MIN,
                    LocalTime.MAX,
                    MealsUtil.getDefaultCaloriesPerDay());
            request.setAttribute("mealsList", mealsList);
            request.setAttribute("df", dateFormatter);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (Actions.valueOf(action) == Actions.delete) {
            mealDAO.remove(mealDAO.getById(Long.parseLong(request.getParameter("id"))));
            response.sendRedirect("meals");
        } else if (Actions.valueOf(action) == Actions.edit) {
            Meal meal = mealDAO.getById(Long.parseLong(request.getParameter("id")));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/meal.jsp").forward(request, response);
        } else if (Actions.valueOf(action) == Actions.add) {
            request.setAttribute("meal", new Meal(LocalDateTime.now(), "", 0));
            request.getRequestDispatcher("/meal.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));

        Meal meal;
        String idParameter = request.getParameter("id");
        if (idParameter != null && !idParameter.isEmpty()) {
            Long id = Long.parseLong(idParameter);
            meal = new Meal(id, dateTime, description, calories);
            mealDAO.update(meal);
        } else {
            meal = new Meal(dateTime, description, calories);
            mealDAO.add(meal);
        }
        response.sendRedirect("meals");
    }
}

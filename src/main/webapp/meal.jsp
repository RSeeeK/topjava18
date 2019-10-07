<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
    <head>
        <title>Meal</title>
    </head>
    <body>
        <h3><a href="meals">Back</a></h3>
        <hr>
        <h2>Meal</h2>
        <br/>
        <form method="POST" action='meals' name="formSaveMeal">
            Meal ID:    <input type="text" readonly="readonly" name="id"
                             value="<c:out value="${meal.id}" />"/>
            <br/>
            Description:<input
                type="text" name="description"
                value="<c:out value="${meal.description}" />"/>
            <br/>
            Calories:    <input
                type="text" name="calories"
                value="<c:out value="${meal.calories}" />"/>
            <br/>
            Date:        <input
                type="datetime-local" name="dateTime"
                value="<c:out value="${meal.dateTime}" />"/>
            <br/>
            <input type="submit" value="Save" />
        </form>

    </body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Meals</title>
        <style><%@include file="/WEB-INF/css/style.css"%></style>
    </head>
    <body>
        <h3><a href="index.html">Home</a></h3>
        <hr>
        <h2>Meals</h2>
        <br/>
        <a href="meals?action=add">Add new...</a>
        <table border="1">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Calories</th>
                    <th colspan=2>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="entry" items="${mealsList}" >
                <tr class= ${entry.excess ? "excess" : "notExcess"}>
                    <td><c:out value="${df.format(entry.dateTime)}" /></td>
                    <td><c:out value="${entry.description}" /></td>
                    <td><c:out value="${entry.calories}" /></td>
                    <td><a href="meals?action=edit&id=<c:out value="${entry.id}"/>">Edit</a></td>
                    <td><a href="meals?action=delete&id=<c:out value="${entry.id}"/>">Delete</a></td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
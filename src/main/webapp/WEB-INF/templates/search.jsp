<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html>
  <head>
    <t:head/>
    <title>Project Search</title>
  </head>
  <body>
    <t:base/>
    <form method="GET" action="/search">
      <input name="q" value='${requestScope["previousQuery"]}' placeholder="Query goes here" required>
      <input type="submit">
    </form>
    <p>We found results NOT matching your query since we ignored your input:</p>
    <ul>
      <c:forEach items='${requestScope["results"]}' var="result">
      <li>${result}</li>
      </c:forEach>
    </ul>
  </body>
</html>

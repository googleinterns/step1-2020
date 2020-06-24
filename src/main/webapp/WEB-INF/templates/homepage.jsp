<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<!doctype html>
<html>
  <head>
    <t:head/>
    <title>Project Homepage</title>
  </head>
  <body>
    <t:base/>
    <p>Hello World!</p>
    <p>The current date is ${requestScope["date"]}</p>
    <form method="GET" action="/search">
      <input name="q" placeholder="Query goes here" required>
      <input type="submit">
    </form>
  </body>
</html>

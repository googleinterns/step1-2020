<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <div class="heading">
    <a class="logo" href="/">
      <h1>
        <span class="blue">S</span><span class="red">n</span><span class="yellow">i</span><span
          class="blue">p</span><span class="green">p</span><span class="red">e</span><span
          class="yellow">t</span>
      </h1>
    </a>
  </div>
  <meta charset="utf-8">
  <title>User Dashboard</title>
  <script src="data/languages.js"></script>
  <script src="js/user.js"></script>
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="css/user_style.css">
</head>
<body>
    <form class="setting" action="/user" method="POST">
      <div class="column websites">
        <label for="input-websites">Choose your preferred website:</label>
        <select class="input-websites" id="input-websites" name="input-websites">
          <option value="geeksforgeeks">GeeksForGeeks</option>
          <option value="stackoverflow">StackOverflow</option>
          <option value="w3schools">W3Schools</option>
        </select>
      </div>
      <div id="languages" class="column">
        <label for="input-languages">Choose your primary coding language:</label>
        <input class="input-languages" id="input-languages" type="text" name="input-languages"
               placeholder="Input your primary coding language">
      </div>
      <input class="setting-submit" type="submit" value="Submit">
    </form>
</body>
</html>

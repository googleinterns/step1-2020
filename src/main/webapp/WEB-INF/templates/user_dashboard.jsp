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
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="css/user_style.css">
</head>
<body>
    <a class="home-button" href="/">Home</a>
    <div class="main">
        <form class="setting" action="/user" method="POST">
            <div class="column websites">
                <label for="input-websites">Choose your preferred website:</label>
                <select class="input-websites" id="input-websites" name="website">
                    <c:choose>
                        <c:when test="${user.getWebsite() != null}">
                            <option value="${user.getWebsite()}" selected hidden>${user.getWebsite()}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="" selected disabled hidden>Select</option>
                        </c:otherwise>
                    </c:choose>
                    <option value="GeeksForGeeks">GeeksForGeeks</option>
                    <option value="StackOverflow">StackOverflow</option>
                    <option value="W3Schools">W3Schools</option>
                </select>
            </div>
            <div id="languages" class="column">
                <label for="input-languages">Choose your primary coding language:</label>
                <input class="input-languages" id="input-languages" type="text" name="language"
                    <c:choose>
                        <c:when test="${user.getLanguage() != null}">
                            value="${user.getLanguage()}"
                        </c:when>
                        <c:otherwise>
                            placeholder="Input a coding language"
                        </c:otherwise>
                    </c:choose>
                >
            </div>
            <input class="setting-submit" type="submit" value="Submit">
        </form>
    </div>
</body>
</html>

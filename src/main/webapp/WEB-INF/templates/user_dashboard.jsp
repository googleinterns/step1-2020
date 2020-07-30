<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>User Dashboard</title>
    <script src="data/languages.js"></script>
    <script src="js/user.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <link rel="stylesheet" type="text/css" href="/css/user_style.css">
</head>
<body>
    <a class="home-button" href="/">Home</a>
    <div class="main">
        <form class="setting" action="/user" method="POST">
            <div class="column websites">
                <label for="input-websites">Choose your preferred website:</label>
                <select class="input-websites" id="input-websites" name="website">
                    <option value="none" selected disabled hidden>
                        <c:if test="${user.getWebsite() != null}">
                            ${user.getWebsite()}
                        </c:if>
                        <c:if test="${user.getWebsite() == null}">
                            Select
                        </c:if>
                    </option>
                    <option value="GeeksForGeeks">GeeksForGeeks</option>
                    <option value="StackOverflow">StackOverflow</option>
                    <option value="W3Schools">W3Schools</option>
                </select>
            </div>
            <div id="languages" class="column">
                <label for="input-languages">Choose your primary coding language:</label>

                <input class="input-languages" id="input-languages" type="text" name="language"
                <c:if test="${user.getLanguage() != null}">
                       value="${user.getLanguage()}"
                </c:if>
                <c:if test="${user.getLanguage() == null || user.getLanguage().length() == 0}">
                       placeholder="Input a coding language"
                </c:if>
                >
                <script>autocomplete(document.getElementById('input-languages'), languages);</script>
            </div>
            <input class="setting-submit" type="submit" value="Submit">
        </form>
    </div>
</body>
</html>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>User Dashboard</title>
    <script src="../../js/user.js"></script>
    <script src="../../data/languages.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/user_style.css">
</head>

<body>
<!-- Side navigation -->


<!-- Page content -->
<div class="main">
    <form id="setting" action="/user" method="POST">
        <div id="websites" class="column">
            <label for="input-websites">Choose your preferred website:</label>
            <select id="input-websites" name="input-websites">
                <option value="geeksforgeeks">GeeksForGeeks</option>
                <option value="stackoverflow">StackOverflow</option>
                <option value="w3schools">W3Schools</option>
            </select>
        </div>
        <div id="languages" class="column">
            <label for="input-languages">Choose your primary coding language:</label>
            <input id="input-languages" type="text" name="input-languages"
                   placeholder="Input your primary coding language">
            <div><script>autocomplete(document.getElementById("input-languages"), languages);</script></div>
        </div>
        <input id="setting-submit" type="submit" value="Submit">
    </form>
</div>
</body>
</html>
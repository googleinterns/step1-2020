<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html>
  <head>
    <t:head/>
    <link href="css/srp_style.css" rel="stylesheet">
    <script defer src="js/srp_script.js"></script>
    <title>Snippet</title>
  </head>
  <body>
    <t:base/>
    <div class="srp-container">
      <div class="srp">
        <div class="gcse-searchresults-only"></div>
      </div>
      <div class="card-container">
        <c:forEach items='${requestScope["cardList"]}' var="knowledgeCard">
          <div class="card" id="card">
            <header id="card-header">
              <img class="icon" src="${knowledgeCard.getIcon()}" alt="${knowledgeCard.getSource()} site icon" width="25px" height="25px">
              <a href="${knowledgeCard.getLink()}" title="card-source"class="card-title">
                ${knowledgeCard.getTitle()}
              </a>
            </header>
            <div class="content">
              <pre><code>${knowledgeCard.getCode()}</code></pre>
              <p>${knowledgeCard.getDescription()}</p>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </body>
</html>

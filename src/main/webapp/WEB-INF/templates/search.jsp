<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html>
  <head>
    <t:head/>
    <link href="css/srp_style.css" rel="stylesheet">
    <title>Snippet</title>
    <script src="js/script.js"></script>
    <script src="https://kit.fontawesome.com/b99e675b6e.js"></script>
  </head>
  <body>
    <t:base/>
    <div class="srp-container">
      <div class="srp">
        <div class="gcse-searchresults-only"></div>
      </div>
      <div class="card-container">
        <c:forEach items='${requestScope["cardList"]}' var="knowledgeCard">
          <div class="card" url="${knowledgeCard.getLink()}">
            <div class="vote">
              <button class="upvote">
                <i class="fas fa-chevron-up"></i>
              </button>
              <span class="total-votes">${knowledgeCard.getVotes()}</span>
              <button class="downvote">
                <i class="fas fa-chevron-down"></i>
              </button>
            </div>
            <div class="content">
              <header>
                <img class="icon" src="${knowledgeCard.getIcon()}" alt="${knowledgeCard.getSource()} site icon" width="25px" height="25px">
                <a href="${knowledgeCard.getLink()}" class="card-title">
                  ${knowledgeCard.getTitle()}
                </a>
              </header>
              <c:if test="${knowledgeCard.getCode() != null}">
                <pre><code>${knowledgeCard.getCode()}</code></pre>
              </c:if>
              <p>${knowledgeCard.getDescription()}</p>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </body>
</html>

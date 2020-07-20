<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.google.step.snippet.data.Card"%> 
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html>
  <head>
    <t:head/>
    <link href="css/srp-style.css" rel="stylesheet">
    <title>Snippet</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}js/script.js"></script>
  </head>
  <body>
    <t:base/>
    <div class="gcse-searchbox-only" data-resultsUrl="/search"></div>
    <div class="srp-container">
      <div class="srp">
        <div class="gcse-searchresults-only"></div>
      </div>
      <div class="card-container">
        <c:forEach items='${requestScope["cardList"]}' var="knowledgeCard">
          <div class="card">
            <a href="${knowledgeCard.getLink()}" title="card-source"class="card-title">
              ${knowledgeCard.getTitle()}
            </a>
            <code>${knowledgeCard.getCode()}</code>
            <span class="card-description">Description:</span>
            <p>${knowledgeCard.getDescription()}</p>
            <button class="upvote" style="background-color: greenyellow;"value="${knowledgeCard.getLink()}">
              ${knowledgeCard.getUpvote()}
            </button>
            <button class="downvote" style="background-color: rgb(184, 51, 51); color:white;" value="${knowledgeCard.getLink()}">
              ${knowledgeCard.getDownvote()}
            </button>
          </div>
        </c:forEach>
      </div>
    </div>
  </body>
</html>

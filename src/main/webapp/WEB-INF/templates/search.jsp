<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.google.step.snippet.data.Card"%> 
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html>
  <head>
    <t:head/>
    <title>Snippet</title>
  </head>
  <body>
    <t:base/>
    <div class="gcse-searchbox-only" data-resultsUrl="/search"></div>
    <div class="srp-container">
      <div class="srp-child">
        <div class="gcse-searchresults-only"></div>
      </div>
      <div class="srp-child">
        <c:forEach items='${requestScope["cardList"]}' var="knowledgeCard">
          <div class="card">
            <div class="card-content">
              <a href="${knowledgeCard.getLink()}" title="card-source">
                <span class="card-title">${knowledgeCard.getTitle()}</span>
              </a>
              <p>${knowledgeCard.getCode()}</p>
              <span class="card-title">Description</span>
              <p>${knowledgeCard.getDescription()}</p>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </body>
</html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html>
  <head>
    <t:head/>
    <link href="css/srp_style.css" rel="stylesheet">
    <title>Snippet</title>
  </head>
  <body>
    <t:base/>
    <div class="srp-heading">
      <a class="logo" href="/">
        <h1>
          <span class="blue">S</span><span class="red">n</span><span class="yellow">i</span><span class="blue">p</span><span class="green">p</span><span class="red">e</span><span class="yellow">t</span>
        </h1>
      </a>
      <div class="gcse-searchbox-only" data-resultsUrl="/search"></div>
    </div>
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
          </div>
        </c:forEach>
      </div>
    </div>
  </body>
</html>

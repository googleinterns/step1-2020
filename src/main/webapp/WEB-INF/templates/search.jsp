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
            <c:if test="${knowledgeCard.getCode() != null}">
              <code>${knowledgeCard.getCode()}</code>
            </c:if>
            <div class="card-description">Description:</div>
            <p>${knowledgeCard.getDescription()}</p>
          </div>
        </c:forEach>
      </div>
    </div>
  </body>
</html>

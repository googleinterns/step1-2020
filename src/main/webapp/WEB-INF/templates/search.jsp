<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.google.step.snippet.data.Card"%> 
<%@page import="java.util.ArrayList"%> 
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
        <%ArrayList<Card> cards = (ArrayList<Card>)request.getAttribute("cardList");%>
          <div class="card">
            <div class="card-content">
              <a href="<%=cards.get(0).getLink()%>" title="card-source">
                <span class="card-title"><%=cards.get(0).getTitle()%></span>
              </a>
              <p><%=cards.get(0).getCode()%></p>
              <span class="card-title">Description</span>
              <p><%=cards.get(0).getDescription()%></p>
            </div>
          </div>
        <!--
        #TODO: Add two more cards to display.
        -->
      </div>
    </div>
  </body>
</html>

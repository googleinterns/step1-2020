<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.google.step.snippet.data.Card"%> 
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
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
      <div class="gcse-searchresults-only"></div>
    </div>
  </body>
</html>

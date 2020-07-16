<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<!doctype html>
<html>
  <head>
    <t:head/>
    <title>Snippet</title>
  </head>
  <body>
    <t:base/>
    <div class="gcse-searchbox-only" data-resultsUrl="/search"></div>
    <a href="${authUrl}">${authLabel}</a>
  </body>
</html>

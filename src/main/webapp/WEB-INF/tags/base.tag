<%@tag description="Common page content" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="heading">
  <a class="logo" href="/">
    <h1>
      <span class="blue">S</span><span class="red">n</span><span class="yellow">i</span><span class="blue">p</span><span class="green">p</span><span class="red">e</span><span class="yellow">t</span>
    </h1>
  </a>
  <div class="searchbox">
    <div class="gcse-searchbox-only" data-resultsUrl="/search"></div>
  </div>
  <div class="spacing"></div>
  <c:if test="${authLabel == 'Logout'}">
    <a class="setting-button" href="/user">Setting</a>
  </c:if>
  <a class="auth-button" href="${authUrl}">${authLabel}</a>
</div>

<%@include file="../layouts/include_taglib.jsp" %>
<div id="footer">
  <spring:url value="/" var="home"/>
  <span>
    <a href="${home}">
      <spring:message code="button_home"/>
    </a>
  </span>
</div>
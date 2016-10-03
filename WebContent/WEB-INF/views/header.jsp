<%@include file="../layouts/include_taglib.jsp" %>
<div id="header">

  <spring:url var="banner" value="/resources/images/logo.gif" />
  <spring:url var="home" value="/" />
  <spring:message code="button_home" var="home_label" htmlEscape="false" />
  <a href="${home}" name="${fn:escapeXml(home_label)}" title="${fn:escapeXml(home_label)}">
    <img src="${banner}" />
  </a>
</div> 
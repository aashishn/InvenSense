<%@include file="../layouts/include_taglib.jsp" %>
<div>
  <spring:message var="title" code="error_resourcenotfound_title" htmlEscape="false" />
    <h2>${fn:escapeXml(title)}</h2>
    <p>
      <spring:message code="error_resourcenotfound_problemdescription" />
    </p>
    <c:if test="${not empty exception}">
      <p>
        <h4>
          <spring:message code="exception_details" />
        </h4>
        <spring:message var="message" code="exception_message" htmlEscape="false" />
        <util:panel id="_message" title="${message}" openPane="false">
          <c:out value="${exception.localizedMessage}" />
        </util:panel>
        <spring:message var="stacktrace" code="exception_stacktrace" htmlEscape="false" />
        <util:panel id="_exception" title="${stacktrace}" openPane="false">
          <c:forEach items="${exception.stackTrace}" var="trace">
            <c:out value="${trace}" />
            <br />
          </c:forEach>
        </util:panel>
      </p>
    </c:if>
</div>
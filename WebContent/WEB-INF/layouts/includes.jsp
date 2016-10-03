<%@include file="include_taglib.jsp" %>
<c:set var="req" value="${pageContext.request}" />
<c:set scope="session" var="serverUrl" value="${fn:replace(req.requestURL, fn:substring(req.requestURI, 1, fn:length(req.requestURI)), req.contextPath)}" />

<c:set value="${serverUrl}/styles/superfish-1.4.8/css/superfish.css" var="super_fish" />
<c:set value="${serverUrl}/resources/images/favicon.ico" var="favicon" />
<c:set value="${serverUrl}/styles/standard.css" var="standard" />

<c:set value="${serverUrl}/styles/theme/redmond/jquery-ui-1.8.17.custom.css" var="jquery_ui" />
<c:set value="${serverUrl}/styles/cluetip/jquery.cluetip.css" var="cluetip" />
<c:set value="${serverUrl}/styles/uniform/css/uniform.default.css" var="uniform" />

<c:set value="${serverUrl}/js/jquery-1.7.1.min.js" var="jquery_js" />
<c:set value="${serverUrl}/styles/jquery-ui-1.8.18.custom.min.js" var="jquery_ui_js" />
<c:set value="${serverUrl}/styles/math/jquery.calculation.min.js" var="cal_js" />
<c:set value="${serverUrl}/styles/uniform/jquery.uniform.js" var="uniform_js" />

<c:set value="${serverUrl}/styles/cluetip/jquery.cluetip.all.min.js" var="cluetip_js" />

<link rel="stylesheet" type="text/css" media="screen"	href="${standard}" />
<link rel="SHORTCUT ICON" href="${favicon}" />
<link rel="stylesheet" media="screen"	href="${jquery_ui}" id="jqueryui_print" />
<link rel="stylesheet" media="screen"	href="${super_fish}" />
<link rel="stylesheet" href="${uniform}" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="${cluetip}" type="text/css" media="screen" charset="utf-8" />

<script	src="${jquery_js}"	type="text/javascript"></script>
<script	src="${jquery_ui_js}"	type="text/javascript"></script>
<script src="${uniform_js}" type="text/javascript"></script>
<script src="${cal_js}" type="text/javascript"></script>
<script	src="${cluetip_js}"	type="text/javascript"></script>


<script	src="${serverUrl}/styles/cluetip/jquery.cluetip.all.min.js"	type="text/javascript"></script>
<script src="${serverUrl}/styles/superfish-1.4.8/js/hoverIntent.js" type="text/javascript"><!-- required for FF3 and Opera --></script> 
<script src="${serverUrl}/styles/superfish-1.4.8/js/jquery.bgiframe.min.js" type="text/javascript"><!-- required for FF3 and Opera --></script>
<script src="${serverUrl}/styles/superfish-1.4.8/js/superfish.js" type="text/javascript"><!-- required for FF3 and Opera --></script> 
<script src="${serverUrl}/styles/superfish-1.4.8/js/supersubs.js" type="text/javascript"><!-- required for FF3 and Opera --></script>
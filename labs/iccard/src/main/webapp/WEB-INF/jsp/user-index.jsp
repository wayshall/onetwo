<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<c:set var="basePath" value="${pageContext.request.contextPath}"/>

<head>
<title>user</title>
</head>
<body>
	<table border="1">
		<tr>
			<td>id</td>
			<td>用户名</td>
			<td>email</td>
		</tr>
		<c:forEach items="${users }" var="user">
			<tr>
				<td>${user.id }</td>
				<td>${user.userName }</td>
				<td>${user.email }</td>
			</tr>
		</c:forEach>
	</table>
</body>


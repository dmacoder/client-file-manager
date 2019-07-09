<%@page import="org.springframework.security.access.AccessDeniedException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page isErrorPage="true"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %> 
<%@ page import="org.springframework.security.core.userdetails.UserDetails" %>
<%@ page import="org.springframework.security.core.userdetails.UserDetailsService" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<jsp:include page="/WEB-INF/views/common/include_common.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/common/include_js.jsp"></jsp:include>  
<jsp:useBean id="today" class="java.util.Date" scope="page" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>정의 되지 않은 에러 페이지</title>

<link rel="shortcut icon" type="image/x-icon" href="/resources/favicon.ico?v=<fmt:formatDate value='${today}' pattern='yyyyMMddHHmmss' />" />

<style type="text/css" media="screen, print">
.check-wrap {position:absolute; left:50%; top:50%; width:640px;  margin-left:-320px; background-repeat:no-repeat; font-size:12px; font-family:dotum, '돋움', sans-serif;}
.check-wrap.check-1{height:336px; margin-top:-168px;  background-image:url('/images/bg_popup_check_1.png');}
.check-wrap.check-2{height:320px; margin-top:-160px; background-image:url('/images/bg_popup_check_2.png');}
.check-wrap .hd,
.check-wrap .ft {display:none;}
.check-wrap .bd p {margin:147px 40px 0; padding:0; color:#999; line-height:17px;}
.check-wrap .bd p strong {color:#4e4e4e; font-weight:bold;}
</style>


</head>
<body>
<div class="page-content-wrapper">
    <!-- BEGIN CONTENT BODY -->
    <div class="page-content">
        <div class="page-head">
            <!-- BEGIN PAGE TITLE -->
            <div class="page-title">
                <h1>
                    <small>Error Page</small>
                </h1>
                <p>Application has encountered an error. Please contact support on ...</p>
            </div>
            <!-- END PAGE TITLE -->
        </div>
        <div class="col-lg-10 well">
            <table>
        <tr>
            <td>Date</td>
            <td>${timestamp}</td>
        </tr>
        <tr>
            <td>Error</td>
            <td>${error}</td>
        </tr>
        <tr>
            <td>Status</td>
            <td>${status}</td>
        </tr>
        <tr>
            <td>Message</td>
            <td>${message}</td>
        </tr>
        
        <tr>
            <td>Exception</td>
            <td>${exception}</td>
        </tr>
        <tr>
            <td>Trace</td>
            <td>
                <c:if test="${trace != null }">
                <pre>${trace}</pre>
                </c:if>
            </td>
        </tr>
    </table>
        </div>
        <!-- END container -->
    </div>
    <!-- END CONTENT BODY -->
</div>

   <div class="ft">
        <address>Copyright BELLSOFT Corp. All rights reserved.</address>
    </div>

</body>
</html>

<%@page import="org.springframework.security.access.AccessDeniedException"%>
<%@page import="org.springframework.security.core.userdetails.UserDetails"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page isErrorPage="true"%>
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
<title>접근 권한 거부 에러 페이지</title>

<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico?v=<fmt:formatDate value='${today}' pattern='yyyyMMddHHmmss' />" />

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


 <!-- <strong>방문 하시려는 페이지의 주소가 잘못 입력되었거나</strong><br />
            <strong>변경 또는 삭제되어 요청하신 페이지를 찾을 수 없습니다.</strong><br />
            <br />
            <strong>동일한 문제가 지속적으로 발생할 경우 <a href="/support/service">고객센터</a>로 문의해주세요. 감사합니다.</strong><br />
            항상 고객의 소리에 귀 기울이고,노력하는 벨소프트가 되겠습니다. -->
            
            
<div class="check-wrap check-2">
    <div class="hd">
        <h1>접근권한이 없습니다.</h1>
        <p>담당자에게 문의하여 주시기 바랍니다.</p>
    </div>
    
    <div class="bd">
        <p>
        <strong>방문 하시려는 페이지의 접근 권한이 없습니다.</strong><br/>
        <strong>담당자에게 문의하여 주시기 바랍니다.</strong>
        <br/>
        <br/>
        
        <%
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                String password = ((UserDetails) principal).getPassword();
                //out.println("Account ID : " + username.toString() + "<br>");
                //out.println("Account PW: " + password.toString() + "<br>");
         
        %>
        
        <strong>사용자[접근권한] : ${username} <a href="${pageContext.request.contextPath}/login/logout">로그아웃</a></strong>
        <br/>
        
        <%
        
            }            
        }
        %>
        
        <%
        
        AccessDeniedException errormsg = (AccessDeniedException)request.getAttribute("errormsg"); 
        if(errormsg != null){
            
            if(errormsg.getClass().toString().contains("InvalidCsrfTokenException")){
        
          
        %>
          접근 거부 사유 : csrf token이 만료 되었습니다. 해당 페이지가 두개 이상 열린 것은 아닌지 확인하세요.
          
            <%
        }else{
            
            %>
             <c:if test="${errormsg ne null}">
            에러 클래스 : ${errormsg.getClass() }
            <br/>
        에러 사유 : ${errormsg.getLocalizedMessage() }
        </c:if>
        
       <%
        }
       }%> 
       
        
        
        <br/>
        <%-- 에러 메시지 : ${errormsg}
        에러 사유 : ${errormsg.getCause()}
        에러 클래스 : ${errormsg.getClass() }
        에러 로컬 : ${errormsg.getLocalizedMessage() }
        에러 서프레스 : ${errormsg.getSuppressed() } --%> 
       
        
        <%-- 
        에러 메시지 : ${errormsg} 
        
        <c:out value="${SPRING_SECURITY_403_EXCEPTION.getMessage()}"/>
        
        --%>
        
        
           
        </p>
    </div>
    <div class="ft">
        <address>Copyright BELLSOFT Corp. All rights reserved.</address>
    </div>
</div>

</body>
</html>



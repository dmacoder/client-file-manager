<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page trimDirectiveWhitespaces="true"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>로그인 페이지</title>
<link rel="stylesheet" href="/webjars/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="/webjars/jquery/1.11.3/jquery.min.js"></script>
    <script src="/webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="/js/ilovecoinAdminCommon.js"></script>
    <script src="/js/is.min.js?v=1"></script>
</head>
<body>
<script type="text/javascript">
$( document ).ready(function() {
    
    document.addEventListener('keydown', function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
        }
    }, true);
    
    $("#alertArea").hide();
    $("#oneSummit").click(function() {
        goLogin();
    });
    var failResult = '${param.fail}';
    if( is.not.empty(failResult) ){
        debugger;
        $("#alertArea").attr('class','alert alert-danger');
        $("#alertArea").show();
        var reason = '${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}';
        $("#alertText").html(reason);
    }
    var logoutResult = '${param.logout}';
    if( is.not.empty(logoutResult) ){
        $("#alertArea").attr('class','alert alert-success');
        $("#alertArea").show();
        var reason = '로그아웃 되었습니다.';
        $("#alertText").html(reason);
    }

});


function loginFrmCheck(){
    if( is.empty( $("#loginId").val() ) ){
        popLayerMsg("로그인 아이디를 입력하세요.");
        $("#loginId").focus();
        return false;
    }else if( is.empty( $("#loginPwd").val() ) ){
        popLayerMsg("비밀번호를 입력해주세요.");
        $("#loginPwd").focus();
        return false;
    }
    return true;
}

function goLogin(){
    if(loginFrmCheck()){
        $("#frm").attr("action","<c:url value='/j_spring_security_check'/>").submit();
    }
}

function goLogin2(){
    if(loginFrmCheck()){
        $("#frm").attr("action","<c:url value='/j_spring_security_check'/>").submit();
    }
}
/*
 * 로그인 id,pwd 넘겨 패스워드 체크
 */
/* function checkLoginPwd(){
    $.ajax({
        type: "get",
        url: "/login/checkLoginPwd?loginId="+loginId+"&loginPwd="+loginPwd,
        dataType : "json",
        success: function(result) {
            if(result){
                alert("로그인 완료");
            }else{
                alert("잘못된 비밀번호 입니다.");
            }
        }, error: function(e) {
            alert(e.responseText);
        }
    });
}*/
</script>
<!-- BEGIN LOGIN -->
<div class="content">
    <!-- BEGIN LOGIN FORM -->
    <form class="login-form" id="frm" method="post" action="<c:url value='/j_spring_security_check'/>" onsubmit="return loginFrmCheck()">
        <sec:csrfInput />
        <h3 class="form-title font-green"></h3>
        <div class="alert alert-danger" id="alertArea">
            <button class="close" data-close="alert"></button>
            <span id="alertText"></span>
            <c:remove scope="session" var="SPRING_SECURITY_LAST_EXCEPTION"/>
        </div>
        <div class="form-group">
            <!--ie8, ie9 -->
            <label class="control-label visible-ie8 visible-ie9">관리자 ID</label>
            <input class="form-control form-control-solid placeholder-no-fix" id="loginId" name="loginId"
            type="text" autocomplete="off" placeholder="ID 입력해주세요"
            onkeydown="javascript:if(event.keyCode==13){goLogin();}"/>
        </div>
        <div class="form-group">
            <label class="control-label visible-ie8 visible-ie9">비밀번호</label>
            <input class="form-control form-control-solid placeholder-no-fix"
            type="password" autocomplete="off" placeholder="비밀번호를 입력해주세요" name="loginPwd" id="loginPwd"
            onkeydown="javascript:if(event.keyCode==13){goLogin();}"
            />
        </div>
        <div class="form-actions" style="margin-top:-20px;">
            <button type="button" id="oneSummit" class="btn green uppercase" style="width:100%; margin-right:3%; ">로그인</button>
        </div>
    </form>
</div>
<!-- END LOGIN -->
<jsp:include page="/WEB-INF/views/common/modal_msg.jsp"></jsp:include> 
</body>
</html>

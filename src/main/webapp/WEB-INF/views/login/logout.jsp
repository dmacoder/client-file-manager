<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page session="true"%>
<script type="text/javascript">
    $(document).ready(function() {

        var url = "${pageContext.request.contextPath}/j_spring_security_logout";
        $(location).attr('href', url);

    });
</script>
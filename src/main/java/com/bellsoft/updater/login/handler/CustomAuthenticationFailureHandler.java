package com.bellsoft.updater.login.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * 인증 실패 핸들러
 *
 */
@Getter
@Setter
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String loginIdName;             // 로그인 id값이 들어오는 input 태그 name
    private String loginPasswordName;         // 로그인 password 값이 들어오는 input 태그 name
    private String loginRedirectName;       // 로그인 성공시 redirect 할 URL이 지정되어 있는 input 태그 name
    private String exceptionMessageName;        // 예외 메시지를 request의 Attribute에 저장할 때 사용될 key 값
    private String defaultFailureUrl;       // 화면에 보여줄 URL(로그인 화면)
    
    public CustomAuthenticationFailureHandler(){
        this.loginIdName = "loginId";
        this.loginPasswordName = "loginPwd";
        this.loginRedirectName = "loginRedirect";
        this.exceptionMessageName = "securityExceptionMsg";
        this.defaultFailureUrl = "/login/loginForm?fail=true";
    }
    
    
   

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // TODO Auto-generated method stub
        
        // Request 객체의 Attribute에 사용자가 실패시 입력했던 로그인 ID와 비밀번호를 저장해두어 로그인 페이지에서 이를 접근하도록 한다
        String loginId = request.getParameter(loginIdName);
        String loginPwd = request.getParameter(loginPasswordName);
        String loginRedirect = request.getParameter(loginRedirectName);
        
        request.setAttribute(loginIdName, loginId);
        request.setAttribute(loginPasswordName, loginPwd);
        request.setAttribute(loginRedirectName, loginRedirect);
        
        
        // Request 객체의 Attribute에 예외 메시지 저장
        request.setAttribute(exceptionMessageName, exception.getMessage());
        // 로그인 실패한 request와 response를 그대로 넘기기 위해 forward 한다.
        request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
    }

}


package com.bellsoft.updater.common.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

private static final Logger logger = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

    private String errorPage;
    private String ajaxHeader;

    public void setAjaxHeader(String ajaxHeader) {
        this.ajaxHeader = ajaxHeader;
    }
    
    public void setErrorPage(String errorPage) {
        if ((errorPage != null) && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with '/'");
        }

        this.errorPage = errorPage;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException,
            ServletException {
       
        // Ajax를 통해 들어온것인지 파악한다
        
        String ajaxHeaderValue = request.getHeader(ajaxHeader);
        String result = "";
        
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        
        logger.info("엑세스 거부시 요청 주소 : "+request.getRequestURI());
        String reqURI = request.getRequestURI();
        
        
        
        if(accessDeniedException instanceof InvalidCsrfTokenException) {
            logger.info("InvalidCsrfTokenException 발생");
            
            request.setAttribute("errormsg", accessDeniedException);
            
            if(reqURI.equals("/j_spring_security_check")){
                
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if(auth != null) {
                    Object principal = auth.getPrincipal();
                    if (principal instanceof UserDetails) {
                        errorPage="/login/loginForm?layerMsg="+URLEncoder.encode("기존에 로그인된 창이 있어 로그아웃 하였습니다.<br/>다시 로그인하세요","UTF-8");
                        //request.setAttribute("layerMsg", "기존에 로그인된 창이 있습니다.<br/>다시 로그인하세요");                        
                        
                        response.sendRedirect(errorPage);
                    }
                }else {
                    //response.sendRedirect("/");
                    
                    errorPage="/login/loginForm?layerMsg="+URLEncoder.encode("만료 되거나 잘못된 토큰을 요청 하셨습니다.<br/>다시 로그인하세요","UTF-8");
                    response.sendRedirect(errorPage);
                }
                
                return;
            }else if(reqURI.equals("/login/logout")) {
                
                errorPage = "/login/logout";
                response.sendRedirect(errorPage);
                return;                
            }
            
            
        }
     // 추가 (서버가 재시작된 경우 csrfToken이 없어지게 되므로, 아래와 같은 Exception이 발생함
        else if(accessDeniedException instanceof MissingCsrfTokenException) {
            logger.info("MissingCsrfTokenException 발생");
            if(reqURI.equals("/j_spring_security_check")){
                errorPage="/login/loginForm?layerMsg="+URLEncoder.encode("CsrfToken이 없습니다. <br/>다시 로그인하세요","UTF-8");
                response.sendRedirect(errorPage);
                return;
            }
            
           
        }
        
       
        
        if(ajaxHeaderValue == null){                  // null로 받은 경우는 AJAX 헤더 변수가 없다는 의미이기 때문에 ajax가 아닌 일반적인 방법으로 접근했음을 의미한다
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth != null) {
                Object principal = auth.getPrincipal();
                if (principal instanceof UserDetails) {
                    String username = ((UserDetails) principal).getUsername();
                    Collection<? extends GrantedAuthority> role =  ((UserDetails) principal).getAuthorities();
                    request.setAttribute("username", username+role);
                }
            }
            
            request.setAttribute("errormsg", accessDeniedException);
            request.getRequestDispatcher(errorPage).forward(request, response);
        }else{
            
            if("true".equals(ajaxHeaderValue)){      // true로 값을 받았다는 것은 ajax로 접근했음을 의미한다
                result = "{\"result\" : \"fail\", \"message\" : \"" + accessDeniedException.getMessage() + "\"}";
            }else{                              // 헤더 변수는 있으나 값이 틀린 경우이므로 헤더값이 틀렸다는 의미로 돌려준다
                result = "{\"result\" : \"fail\", \"message\" : \"Access Denied(Header Value Mismatch)\"}";
            }
            response.getWriter().print(result);
            response.getWriter().flush();
        }
    }
    
}
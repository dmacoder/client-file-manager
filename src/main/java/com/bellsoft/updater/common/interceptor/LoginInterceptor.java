package com.bellsoft.updater.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle (HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler) throws Exception {
        RequestMapping rm = ((HandlerMethod) handler).getMethodAnnotation(
                            RequestMapping.class);

        boolean alreadyLoggedIn = request.getSession()
                                         .getAttribute("user") != null;
        
        boolean loginPageRequested = rm != null && rm.value().length > 0
                                              && "login".equals(rm.value()[0]);


        if (alreadyLoggedIn && loginPageRequested) {
            response.sendRedirect(request.getContextPath() + "/app/main-age");
            return false;
        } else if (!alreadyLoggedIn && !loginPageRequested) {
            response.sendRedirect(request.getContextPath() + "/login/loginForm");
            return false;
        }

        return true;
    }
}
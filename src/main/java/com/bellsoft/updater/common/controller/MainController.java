package com.bellsoft.updater.common.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bellsoft.updater.api.v1.exception.AccessForbiddenException;
import com.bellsoft.updater.common.CustomParamMap;


@Controller
public class MainController {
    
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    
    @RequestMapping(value = "/testException", method = RequestMethod.GET)
    public String customExceptionTest() {
        
        throw new AccessForbiddenException("Access Forbiden Exception","엑세스 접근 권한 금지 테스트");
        
    }
    
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String indexPage() {
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String initPage(Principal principal,Model model, CustomParamMap paramMap) {
        String forwardPage = "/login/loginForm";
        logger.info("초기화면 이동 페이지");
        // principal 객체가 존재하는 경우 로그인 페이지가 아닌 대쉬보드로 리다이렉트 처리
        if (principal != null) {
            forwardPage = "/index";
        }
        return "redirect:" + forwardPage;
    }
    
    
    
    //스프링 시큐리티 로그아웃 /j_spring_security_logout redirect
    @RequestMapping(value = "/j_spring_security_logout")
    public String logoutRedirect(Principal principal,Model model, CustomParamMap paramMap) {
        String forwardPage = "/login/logout";
        logger.info("스프링 로그아웃 페이지 리다이렉션");
        
        return "redirect:" + forwardPage;
    }
    
    
    @RequestMapping(value="/common/access-denied")
    public String access_denied(){
        return "access_denied";
    }
}

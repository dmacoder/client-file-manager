package com.bellsoft.updater.login.controller;



import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bellsoft.updater.login.domain.MemberInfo;
import com.bellsoft.updater.login.service.LoginService;

/**
 * @project : exchangeAdmin
 * <pre>
 * com.bellsoft.exchange.common.login.controller 
 *    |_ LoginController.java
 * 
 * </pre>
 * @date : 2018. 11. 30. 오후 5:47:37
 * @author :  [](psc)
 * @history : 
 * <pre>
 *	-----------------------------------------------------------------------
 *	변경일				작성자						변경내용  
 *	----------- ------------------- ---------------------------------------
 *	2018. 11. 30.		psc				최초 작성 
 *	-----------------------------------------------------------------------
 * </pre>
 * @description : 로그인 컨트롤러
 * <pre>
 * 1. 개요 : 
 * 2. 처리내용 : 
 * </pre>
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private LoginService loginService;

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/loginForm")
	public String login(HttpSession session,Principal principal,Model model,HttpServletRequest req, HttpServletResponse response) {
	    //String referer = req.getHeader("Referer");
        //req.getSession().setAttribute("prevPage", referer);
        
        logger.info("Welcome login! {}", session.getId());
        
        
        
        //logger.info("모델 : "+req.getAttribute("layerMsg"));
        
	    if(req.getParameter("layerMsg") != null) {
	        
	        //logger.info("레이어 메시지:"+req.getParameter("layerMsg"));
	        String layerMsg = req.getParameter("layerMsg");
            
            /*if(layerMsg.contains("기존에 로그인")) {
                if (principal != null) {
                    model.addAttribute("layerMsg", layerMsg);
                    //session.invalidate();
                }
            }*/
	        /*
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null) {
	            new SecurityContextLogoutHandler().logout(req, response, auth);
	        }
	        */
	        
	        model.addAttribute("layerMsg", layerMsg);
	    }else {
	        
	        if (principal != null) {
                return "redirect:/";
            }
	        
	    }
        
	    if(req.getAttribute("fail")!=null) {
	        logger.info("Welcome fail", req.getAttribute("fail"));
	        model.addAttribute("fail", req.getAttribute("fail"));
	    }
	    
	    if(req.getAttribute("logout") != null) {
	        model.addAttribute("logout", req.getAttribute("logout"));
	    }
		
		
		return "login/login";
	}

	//스프링 시큐리티 로그아웃 처리 (CSRF 적용후 SecurityContextLogoutHandler를 통해 로그아웃해줌) 로그아웃후에는 로그인 페이지로 리다이렉션
	@RequestMapping(value = "/logout", method = {RequestMethod.GET , RequestMethod.POST})
	public String logout(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
	    //session.invalidate();

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            logger.info("logout ok");
        }

		return "redirect:/login/loginForm?logout=true";
	}
	
	

	@RequestMapping(value = "/login_success", method = RequestMethod.GET)
	public void login_success(HttpSession session) {
		MemberInfo userDetails = (MemberInfo) SecurityContextHolder.getContext().getAuthentication()
				.getDetails();

		logger.info("Welcome login_success! {}, {}", session.getId(),
				userDetails.getUsername() + "/" + userDetails.getPassword());
		session.setAttribute("userLoginInfo", userDetails);
	}

	@RequestMapping(value = "logoutPage", method = RequestMethod.GET)
	public String page1() {
	    
	    return "login/logout";
	}

	@RequestMapping(value = "/login_duplicate", method = RequestMethod.GET)
	public void login_duplicate() {
		logger.info("Welcome login_duplicate!");
	}

}

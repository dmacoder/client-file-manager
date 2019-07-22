package com.bellsoft.updater.login.service;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bellsoft.updater.login.dao.LoginMapper;
import com.bellsoft.updater.login.domain.AdminUserVO;
import com.bellsoft.updater.login.domain.MemberInfo;
@Service
public class LoginService implements UserDetailsService{

	@Autowired
	private LoginMapper loginMapper;
	//private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Override
    public UserDetails loadUserByUsername(String loginId ) throws UsernameNotFoundException {
	  //로그인 아이디로 사용자 정보를 가지고 온다
        AdminUserVO adminUserVO = loginMapper.getMemberInfo(loginId);
        //"ROLE_USER"란 이름으로 권한을 설정
        if (adminUserVO == null) throw new UsernameNotFoundException("접속자 정보를 찾을 수 없습니다.");
        
        
        Collection<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();
        String[] roles=adminUserVO.getAuthority().split(",");
        for(String role:roles){
            
           
            list.add(new SimpleGrantedAuthority(role));
        }
        
        
        
        //커스텀 로그인 정보를 리턴
        MemberInfo user = new MemberInfo(adminUserVO.getAdminId(), loginId, adminUserVO.getLoginPwd()
                , adminUserVO.getAdminNm(),list);
        logger.info("사용자 ID : "+loginId +"사용자 명 : "+adminUserVO.getAdminNm());
        logger.info("role list : "+list);
        return user;

    }



}

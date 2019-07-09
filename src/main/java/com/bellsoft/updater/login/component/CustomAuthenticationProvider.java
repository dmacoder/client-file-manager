package com.bellsoft.updater.login.component;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Component;

import com.bellsoft.updater.login.domain.MemberInfo;
import com.bellsoft.updater.login.service.LoginService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider { // authenticationManager

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private LoginService customeUserDetailsService;

    private static DelegatingPasswordEncoder passwordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication; // 유저가 입력한 정보를 이이디비번으으로만든다.(로그인한 유저아이디비번정보를담는다)

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        MemberInfo user;
        Collection<? extends GrantedAuthority> authorities;
        try {
            user = (MemberInfo) customeUserDetailsService.loadUserByUsername(username);
            
            //logger.info("username : " + user.getUsername() + " / password : " + user.getPassword());
            
            passwordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());

            if (user.getPassword().contains("{bcrypt}")) {
                if (!matchPasswordByBCrypt(password, user.getPassword())) {
                    throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
                }
            } else {
                //암호화 되지 않은 비번 기존 DB 호환성 고려 -> 로그인후 자동 암호화할지는 나중에 고려해서 ..
                if (!matchPassword(password, user.getPassword())) {
                    throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
                }
            }
            authorities = user.getAuthorities();
        } catch (UsernameNotFoundException e) {
            logger.info(e.toString());
            throw new UsernameNotFoundException(e.getMessage());
        } catch (BadCredentialsException e) {
            logger.info(e.toString());
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            logger.info(e.toString());
            throw new RuntimeException(e.getMessage());
        }
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    private boolean matchPassword(String password, Object credentials) {
        return password.equals(credentials);
    }

    private boolean matchPasswordByBCrypt(String rawPassword, Object credentials) {
        return passwordEncoder.matches(rawPassword, (String) credentials);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

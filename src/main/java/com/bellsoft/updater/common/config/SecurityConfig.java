package com.bellsoft.updater.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bellsoft.updater.login.service.LoginService;

//시큐리티 버전5에서 Role prefix 없애는것 동작 안됨.. 그냥 hasAuthority 쓰자
//@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers("/", "/favicon.ico", "/resources/**", "/**")
        .permitAll()
        .antMatchers("/index")
        .hasAuthority("ADMIN")
        .anyRequest().authenticated()
        
        .and()
        .formLogin()
        .loginPage("/login/loginForm")
        .failureUrl("/login/loginForm?fail=true")
        .loginProcessingUrl("/j_spring_security_check")
        .usernameParameter("loginId")
        .passwordParameter("loginPwd")
        .permitAll()
        
   
        
        .and()
        .logout()
        .logoutUrl("/login/logout")
        .logoutSuccessUrl("/login/loginForm?logout=true")
        .permitAll()
        
        .and()
        .exceptionHandling().accessDeniedPage("/common/access-denied")
        
        .and().httpBasic();
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
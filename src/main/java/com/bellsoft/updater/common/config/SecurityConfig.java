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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.bellsoft.updater.common.filter.AjaxSessionCheckFilter;
import com.bellsoft.updater.common.filter.CorsFilter;
import com.bellsoft.updater.common.handler.AccessDeniedHandlerImpl;
import com.bellsoft.updater.login.component.CustomAuthenticationProvider;
import com.bellsoft.updater.login.service.LoginService;

//시큐리티 버전5에서 Role prefix 없애는것 동작 안됨.. 그냥 hasAuthority 쓰자
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService userDetailService;

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        AccessDeniedHandlerImpl handler = new AccessDeniedHandlerImpl();
        handler.setAjaxHeader("AJAX");
        handler.setErrorPage("/common/access-denied");
        return handler;
    }
    
    
    
    @Autowired
    private CustomAuthenticationProvider authProvider;
 
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers("/css/**","/js/**","/images/**","/resources/**","/webjars/**","/favicon.ico", "/")
        .permitAll()
        .antMatchers("/index")
        .hasAuthority("ADMIN")
        .anyRequest().authenticated();
        
        //iframe허용
        http.headers()
        .frameOptions().disable();
        
        
        http.formLogin()
        .loginPage("/login/loginForm")
        .loginProcessingUrl("/j_spring_security_check")
        .failureUrl("/login/loginForm?fail=true")
        .defaultSuccessUrl("/")
        .usernameParameter("loginId")
        .passwordParameter("loginPwd")
        .permitAll();
        
   
        
        http.logout()
        .logoutUrl("/login/logout")
        .logoutSuccessUrl("/login/loginForm?logout=true")
        .deleteCookies("JSESSIONID")
        .permitAll().invalidateHttpSession(true);
        
        AjaxSessionCheckFilter ajaxFilter = new AjaxSessionCheckFilter();
        ajaxFilter.setAjaxHeader("AJAX");
        
        http
        .addFilterBefore(ajaxFilter,BasicAuthenticationFilter.class)
        .addFilterAfter(new CorsFilter(),AjaxSessionCheckFilter.class);
        
        //.accessDeniedPage("/common/access-denied")
        http.exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler());
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
       
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
package com.bellsoft.updater;

import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.bellsoft.updater.common.resolver.CustomMapArgumentResolver;

@SpringBootApplication
@EnableWebSecurity
// @MapperScan(basePackages = "com.bellsoft.updater.api.v1.dao")
@ImportResource({ "classpath:/spring/context/context-spring-security.xml" })
public class ExchangeUpdaterApplication extends WebMvcConfigurerAdapter {

    private int maxUploadSizeInMb = 5 * 1024 * 1024; // 5 MB

    public static void main(String[] args) {
        SpringApplication.run(ExchangeUpdaterApplication.class, args);
    }

    @Autowired
    private CustomMapArgumentResolver customMapArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(customMapArgumentResolver);
    }

    /*
     * @Override public void addInterceptors (InterceptorRegistry registry) { registry.addInterceptor(new LoginInterceptor()); }
     */

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10); // reload messages every 10 seconds
        return messageSource;
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {

        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        cmr.setMaxUploadSize(maxUploadSizeInMb * 2);
        cmr.setMaxUploadSizePerFile(maxUploadSizeInMb); // bytes
        return cmr;

    }

    /*
     * @Bean(name = "multipartResolver") public CommonsMultipartResolver multipartResolver() { CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(); multipartResolver.setMaxUploadSize(-1); return multipartResolver; }
     */

    /*
     * @Bean public MultipartConfigElement multipartConfigElement() { MultipartConfigFactory factory = new MultipartConfigFactory(); factory.setMaxFileSize("512MB"); factory.setMaxRequestSize("512MB"); return factory.createMultipartConfig(); }
     * 
     * @Bean public MultipartResolver multipartResolver() { org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver(); multipartResolver.setMaxUploadSize(536870912); return multipartResolver; }
     */
}

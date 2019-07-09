package com.bellsoft.updater.common.resolver;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bellsoft.updater.common.CustomParamMap;


@Component
public class CustomMapArgumentResolver implements HandlerMethodArgumentResolver {

    @Value("${spring.profiles.active}")
    private String activeProfile;
    
    private static final Logger log = LoggerFactory.getLogger(CustomMapArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CustomParamMap.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        CustomParamMap paramMap = new CustomParamMap();

        HttpServletRequest httpServletRequest = (HttpServletRequest)webRequest.getNativeRequest();
        Enumeration<?> enumeration = httpServletRequest.getParameterNames();


        String key = null;
        String[] values = null;
        while(enumeration.hasMoreElements()){
            key = (String) enumeration.nextElement();
            values = httpServletRequest.getParameterValues(key);
            if(values != null){
                paramMap.put(key, (values.length > 1) ? values:values[0] );
                if(!activeProfile.equals("PROD")) {
                    log.info("Parameter Key : "+ key + "\nParameter Value:"+paramMap.get(key));
                }
            }
        }

        return paramMap;
    }

}

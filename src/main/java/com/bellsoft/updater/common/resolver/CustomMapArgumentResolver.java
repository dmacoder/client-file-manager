package com.bellsoft.updater.common.resolver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bellsoft.updater.common.CustomParamMap;
import com.google.gson.Gson;

public class CustomMapArgumentResolver implements HandlerMethodArgumentResolver {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private static final Logger log = LoggerFactory.getLogger(CustomMapArgumentResolver.class);
    private static final String JSON_BODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CustomParamMap.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        if (!activeProfile.equals("PROD")) {
            log.info("================CustomParamMap Resolver=================");
        }

        CustomParamMap paramMap = new CustomParamMap();

        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = httpServletRequest.getSession();
        Enumeration<?> enumeration = httpServletRequest.getParameterNames();

        String key = null;
        String[] values = null;
        while (enumeration.hasMoreElements()) {
            key = (String) enumeration.nextElement();
            values = httpServletRequest.getParameterValues(key);
            if (values != null) {
                paramMap.put(key, (values.length > 1) ? values : values[0]);
                if (!activeProfile.equals("PROD")) {
                    log.info("Parameter Key : " + key + "\nParameter Value:" + paramMap.get(key));
                }
            }
        }

        // AJAX JSON BODY PARAM
        String body = getRequestBody(webRequest);
        if (!StringUtils.isEmpty(body)) {
            Gson gson = new Gson();
            Map<String, Object> map = new HashMap<String, Object>();

            if (map != null) {
                map = gson.fromJson(body, map.getClass());
                paramMap.putAll(map);

                Set<String> set = paramMap.keySet();

                Iterator<String> iterator = set.iterator();

                while (iterator.hasNext()) {

                    String key2 = (String) iterator.next();
                    String value2 = paramMap.get(key2).toString();

                    if (!activeProfile.equals("PROD")) {
                        log.info("Request Body JSON PARAM : " + key2 + "/" + value2);
                    }

                }
            }

        }

        return paramMap;
    }

    /**
     * @method name : getRequestBody
     * @date : 2019. 1. 7. 오후 1:27:28
     * @author : [](psc)
     * @history:
     * 
     *           <pre>
     *	-----------------------------------------------------------------------
     *	변경일				작성자						변경내용  
     *	----------- ------------------- ---------------------------------------
     *	2019. 1. 7.		psc				최초 작성 
     *	-----------------------------------------------------------------------
     *           </pre>
     * 
     * @description: 웹에서 Ajax 호출시 메소드가 'put','patch','delete'인 경우에 따로 담아서 String 형태로 리턴
     * 
     *               <pre>
     * 1. 개요 : 
     * 2. 처리내용 :
     *               </pre>
     * 
     * @param webRequest(NativeWebRequest)
     * @return
     */
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String body = (String) webRequest.getAttribute(JSON_BODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);

        if (body == null) {
            try {
                body = IOUtils.toString(servletRequest.getInputStream());
                webRequest.setAttribute(JSON_BODY_ATTRIBUTE, body, NativeWebRequest.SCOPE_REQUEST);
                return body;
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        return body;
    }

    private static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

}

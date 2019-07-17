package com.bellsoft.updater.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.WebUtils;

public class AjaxRequestUtil {

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestHeader = request.getHeader("X-Requested-With");
        return requestHeader != null && ("XMLHttpRequest".equals(requestHeader) || "true".equalsIgnoreCase(requestHeader));       
    }
}
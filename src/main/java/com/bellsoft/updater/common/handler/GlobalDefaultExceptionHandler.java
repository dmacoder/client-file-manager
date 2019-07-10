package com.bellsoft.updater.common.handler;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.bellsoft.updater.common.ApiResponseMessage;

/**
 * @project : exchangeUpdater
 * 
 *          <pre>
 * com.bellsoft.updater.common.handler 
 *    |_ GlobalDefaultExceptionHandler.java
 * 
 *          </pre>
 * 
 * @date : 2018. 12. 12. 오후 2:35:51
 * @author : [](psc)
 * @history :
 * 
 *          <pre>
 *	-----------------------------------------------------------------------
 *	변경일				작성자						변경내용  
 *	----------- ------------------- ---------------------------------------
 *	2018. 12. 12.		psc				최초 작성 
 *	-----------------------------------------------------------------------
 *          </pre>
 * 
 * @description : @ControllerAdvice를 사용한 전역 처리 global exception handling
 * 
 *              <pre>
 * 1. 개요 : 
 * 2. 처리내용 :
 *              </pre>
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalDefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    public static final String DEFAULT_ERROR_VIEW = "error_common";

    @ExceptionHandler(Exception.class)
    private ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
       /* 
           if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        */
        

        logger.error("Request URL: " + req.getRequestURL() + " raised :" + e);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("name", e.getClass().getSimpleName());

        modelAndView.addObject("message", e.getMessage());

        modelAndView.addObject("exception", e);

        modelAndView.addObject("url", req.getRequestURL());

        modelAndView.setViewName(DEFAULT_ERROR_VIEW);

        ApiResponseMessage errorResp = new ApiResponseMessage();
        errorResp.setStatus(400);
        errorResp.setTimestamp(new Date());
        errorResp.setCode("Unknown");
        errorResp.setDetail(e.toString());
        errorResp.setMessage(e.getMessage());
        errorResp.setMoreInfo("/api/v1/errors/" + errorResp.getCode());

        modelAndView.addObject("errors", errorResp);

        return modelAndView;
    }

}

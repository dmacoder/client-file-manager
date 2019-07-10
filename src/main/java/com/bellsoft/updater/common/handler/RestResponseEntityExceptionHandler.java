package com.bellsoft.updater.common.handler;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.bellsoft.updater.api.v1.exception.AccessForbiddenException;
import com.bellsoft.updater.api.v1.exception.FileIdNotFoundException;
import com.bellsoft.updater.common.ApiResponseMessage;

//@ControllerAdvice
//@RequestMapping(produces = "application/vnd.error+json")
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestResponseEntityExceptionHandler {

    /*
     * invalidTokenException 401 //unauthorized
     * notAllowedException 429 (not permission)
     * accessRestrictedException 403 (api 요청 회수 초과 또는 기타 다른 사유의 접근 제한)
     */
    
    public static final String DEFAULT_ERROR_VIEW = "error_common";
    
    private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    //접근 권한 없음
    @ResponseStatus(value = HttpStatus.FORBIDDEN) //403 forbidden
    @ExceptionHandler(AccessForbiddenException.class)
    public Object handleForbiddenException(AccessForbiddenException e,HttpServletRequest req ){
        
        ApiResponseMessage errorResp = new ApiResponseMessage();
        errorResp.setStatus(403);
        errorResp.setTimestamp(new Date());
        errorResp.setCode("E1002");
        errorResp.setDetail(e.getDetail());
        errorResp.setMessage(e.getMessage());
        errorResp.setMoreInfo("/api/v1/errors/"+errorResp.getCode());
        
       
        
        String reqURI = req.getRequestURI();
        
        log.info("예외 요청 URL : "+reqURI);
        if (reqURI.endsWith(".json")) {            
           
            return new ResponseEntity<ApiResponseMessage>(errorResp, HttpStatus.FORBIDDEN);
        }else {
            ModelAndView model = new ModelAndView();
            
            model.addObject("name", e.getClass().getSimpleName());

            model.addObject("message", e.getMessage());

            model.addObject("exception", e);

            model.addObject("url", req.getRequestURL());
            
            model.addObject("errors", errorResp);
            
            model.setViewName(DEFAULT_ERROR_VIEW);
            return model;
        }
        
    }
    
  //파일 ID 없음
    @ResponseStatus(value = HttpStatus.NOT_FOUND) 
    @ExceptionHandler(FileIdNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> handleFileIdNotFoundExceptionException(FileIdNotFoundException e){
        ApiResponseMessage errorResp = new ApiResponseMessage();
        errorResp.setStatus(404);
        errorResp.setTimestamp(new Date());
        errorResp.setCode("E1005");
        errorResp.setDetail(e.getDetail());
        errorResp.setMessage(e.getMessage());
        errorResp.setMoreInfo("/api/v1/errors/"+errorResp.getCode());
        return new ResponseEntity<ApiResponseMessage>(errorResp, HttpStatus.NOT_FOUND);

    }
    
}

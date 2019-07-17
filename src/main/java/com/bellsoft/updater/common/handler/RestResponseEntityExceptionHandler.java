package com.bellsoft.updater.common.handler;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bellsoft.updater.api.v1.exception.AccessForbiddenException;
import com.bellsoft.updater.api.v1.exception.FileIdNotFoundException;
import com.bellsoft.updater.common.ApiResponseMessage;
import com.bellsoft.updater.common.exception.AbstractException;
import com.bellsoft.updater.common.exception.CommonBadRequestException;
import com.bellsoft.updater.common.exception.ErrorCode;
import com.bellsoft.updater.common.util.AjaxRequestUtil;

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
    
    @Value("${spring.profiles.active}")
    private String activeProfile;
    
    public static final String DEFAULT_ERROR_VIEW = "error_common";
    
    private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    // 그외 페이지로 에러 모델을 저장후 리다이렉트후 표시하기
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, HttpServletResponse resp, Exception e) throws Exception {

        log.info("공통 ExceptionHandler");
        
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) throw e;

        if(AjaxRequestUtil.isAjaxRequest(req)) {
            throw e;
        }
        // log.info("핸들 메시지 : "+e.getLocalizedMessage());
        ApiResponseMessage errorResp = new ApiResponseMessage();
        errorResp.setStatus(500);
        errorResp.setTimestamp(new Date());

        ModelAndView mav = new ModelAndView();

        if (resp.getStatus() == HttpStatus.BAD_REQUEST.value()) {
            errorResp.setStatus(400);
            mav.setViewName("error/error_400");
        } else if (resp.getStatus() == HttpStatus.FORBIDDEN.value()) {
            errorResp.setStatus(403);
            mav.setViewName("error/error_403");
        } else if (resp.getStatus() == HttpStatus.NOT_FOUND.value()) {
            errorResp.setStatus(404);
            mav.setViewName("error/error_404");
        } else if (resp.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            mav.setViewName("error/error_500");
        } else {
            mav.setViewName("error/error");
        }

        if (req.getAttribute("javax.servlet.error.status_code") != null) {
            errorResp.setCode("E" + req.getAttribute("javax.servlet.error.status_code"));
        } else {
            errorResp.setCode("E" + resp.getStatus());
        }

        errorResp.setMessage(e.getMessage());
        // errorResp.setDetail(e.getDetail());
        errorResp.setMoreInfo("/api/v1/errors/" + errorResp.getCode());

        if (!activeProfile.equals("PROD")) {
            log.info("에러 발생 ExceptionHandler!");
            e.printStackTrace();
        }

        mav.addObject("errors", errorResp);
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        // mav.setViewName("redirect:/error");

        return mav;
    }
    //접근 권한 없음
    @ResponseStatus(value = HttpStatus.FORBIDDEN) //403 forbidden
    @ExceptionHandler(AccessForbiddenException.class)
    public Object handleForbiddenException(AccessForbiddenException e,HttpServletRequest req ){
        
        ApiResponseMessage errorResp = new ApiResponseMessage();
        errorResp.setStatus(ErrorCode.ACCESS_FORBIDDEN_EXCEPTION.getStatus());
        
        errorResp.setCode(ErrorCode.ACCESS_FORBIDDEN_EXCEPTION.getCode());
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
    
    //공통 exception
    @ResponseStatus(value = HttpStatus.NOT_FOUND) // 404
    @ExceptionHandler(CommonBadRequestException.class)
    public ResponseEntity<?> handleCommonBadRequestException(AbstractException e, WebRequest request) {
        ApiResponseMessage errorResp = new ApiResponseMessage();
        errorResp.setStatus(e.getErrorCode().getStatus());
        errorResp.setCode(e.getErrorCode().getCode());
        errorResp.setMessage(e.getErrorCode().getMessage());
        errorResp.setDetail(e.getDetail());
        errorResp.setMoreInfo("/api/v1/errors/" + errorResp.getCode());

        return new ResponseEntity<>(errorResp, HttpStatus.valueOf(e.getErrorCode().getStatus()));

    }
    
    
    
  //파일 ID 없음
    @ResponseStatus(value = HttpStatus.NOT_FOUND) 
    @ExceptionHandler(FileIdNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> handleFileIdNotFoundExceptionException(FileIdNotFoundException e){
        ApiResponseMessage errorResp = new ApiResponseMessage();
        errorResp.setStatus(ErrorCode.FILE_ID_NOT_FOUND.getStatus());
        errorResp.setCode(ErrorCode.FILE_ID_NOT_FOUND.getCode());
        errorResp.setDetail(e.getDetail());
        errorResp.setMessage(e.getMessage());
        errorResp.setMoreInfo("/api/v1/errors/"+errorResp.getCode());
        return new ResponseEntity<ApiResponseMessage>(errorResp, HttpStatus.valueOf(ErrorCode.FILE_ID_NOT_FOUND.getStatus()));

    }
    
    
}

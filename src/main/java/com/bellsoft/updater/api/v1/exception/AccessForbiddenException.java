package com.bellsoft.updater.api.v1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.FORBIDDEN , reason = "권한 부족으로 인해 접근 제한 예외 발생")
public class AccessForbiddenException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    
    private String detail;
    
    public AccessForbiddenException() {
    }
    
    public AccessForbiddenException(String message) {
        super(message);
    }
    
    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }

    public AccessForbiddenException(String message, String detail) {
        super(message);
        this.setDetail(detail);
    }
    
}

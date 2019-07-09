package com.bellsoft.updater.api.v1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND , reason = "FileId 식별 불가로 인한 예외 발생")
public class FileIdNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    
    private String detail;
    
    public FileIdNotFoundException() {
        super();
    }
    
    public FileIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public FileIdNotFoundException(String message) {
        super(message);
    }
    
    public FileIdNotFoundException(Throwable cause) {
        super(cause);
    }

    public FileIdNotFoundException(String message, String detail) {
        super(message);
        this.setDetail(detail);
    }
    
    
    
}

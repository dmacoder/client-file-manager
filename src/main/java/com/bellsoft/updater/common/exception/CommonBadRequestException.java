package com.bellsoft.updater.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST , reason = "Common BAD_REQUEST")
@Getter
@Setter
@NoArgsConstructor
public class CommonBadRequestException extends AbstractException{


    /**
     * 
     */
    private static final long serialVersionUID = -4462057697906449020L;

    public CommonBadRequestException(ErrorCode errorCode, String...args) {
        super(errorCode, args);
    }

  
    
}

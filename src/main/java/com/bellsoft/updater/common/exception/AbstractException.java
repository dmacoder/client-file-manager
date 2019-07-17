package com.bellsoft.updater.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractException extends Exception {

    private String detail;
    private ErrorCode errorCode;

    private AbstractException(String message) {
        super(message);
    }

    private AbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    protected  AbstractException(String message, Throwable cause, String detail) {
        super(message, cause);
        this.setDetail(detail);
    }

    public AbstractException(Throwable cause) {
        super(cause);
    }

    protected  AbstractException(String message, String detail) {
        super(message);
        this.setDetail(detail);
    }
    
    protected  AbstractException(ErrorCode errorCode, String...args) {
        this(errorCode.getMessage(), errorCode.getDetail(args));
        this.setErrorCode(errorCode);
     }


}

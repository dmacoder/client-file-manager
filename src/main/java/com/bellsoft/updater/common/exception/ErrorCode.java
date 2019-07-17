package com.bellsoft.updater.common.exception;

import com.bellsoft.updater.common.util.ErrCodeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // E Common
    INVALID_INPUT_VALUE(400, "EC1001", "Invalid Input Value","%1은(는) 잘못된 입력값 입니다."),
    METHOD_NOT_ALLOWED(405, "EC1002", " Method Not Allowed","%1은(는) 허용되지 않은 Method 입니다."),
    HANDLE_ACCESS_DENIED(403, "EC1003", "Access is Denied"),

    // Member
    USER_ID_NOT_FOUND(404,"EM1001","UserId is invalid","%1은(는) 잘못된 아이디입니다."),
    EMAIL_DUPLICATION(400, "EM1002", "Email is Duplication"),

    //FRONT
    //키오스크 위치 이미지 찾을 수 없음
    FILE_ID_NOT_FOUND(404,"EF1001", "FileId Not Found", "파일 ID : %1 에 해당하는 파일이 없습니다."),
    
    //보기 권한 없음 UNAUTHORIZED
    INVALID_AUTHENTICATION_REQUEST(401,"EF1002","Unauthorised Request"),
    ACCESS_FORBIDDEN_EXCEPTION(403,"EF1003","Access Frobidden Exception")
    ;
    
    

    private final int status;
    private final String code;
    private final String message;
    private String detail;
    
    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
    
    ErrorCode(final int status, final String code, final String message, final String detail) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.detail = detail;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public String getDetail(String... args) {
         return ErrCodeUtil.parseMessage(this.detail, args);
    }
}
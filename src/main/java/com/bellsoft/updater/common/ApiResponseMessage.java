package com.bellsoft.updater.common;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Api 호출후 예외 발생시 응답 상태 정보를 저장하기 위한 클래스
 */
@Setter
@Getter
@ToString
public class ApiResponseMessage implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 4594951705192880286L;

    //response status 응답 상태
    private String status; //ok,fail
    
    /*
     * HttpStatus Code 200,400,404,500와는 다른 코드로 
     * 404 NOT FOUND의 경우에 API에 요청은 정상적으로 전달되었지만 
     * 데이터가 없는 경우 별도의 예외 코드(E1000,E1001,E1002 등)로 구분할수 있게 하기 위함  
     */
    //Error Code 에러코드
    private String code; 
    
    //timeStamp -> UTC + 9
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone ="Asia/Seoul")
    private Date timestamp;
    
    //Error Message 간단 에러 메시지
    private String message; //Bad Request, Autorization failed
    
    private String detail; // 상세 설명 (for user) User doesn't have permission to update user profile
    
    //Information URL (moreInfo)
    private String moreInfo; //http://admin.exchange.co.kr/api/v1/errors/E1001
    
    public ApiResponseMessage() {
        
        setTimestamp(new Date());
        
    }

    public ApiResponseMessage(String status, String code, String message, String detail, String moreInfo, Date timestamp) {
        
        this.status = status;
        this.code = code;
        this.message = message;
        this.detail = detail;
        this.moreInfo = moreInfo;
        this.timestamp = timestamp;
    }

    
    
    
}

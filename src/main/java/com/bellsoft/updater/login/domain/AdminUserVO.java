package com.bellsoft.updater.login.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@ToString
public class AdminUserVO {
	private String adminId;
	private String loginId;
	private String loginPwd;
	private String adminNm;
	
	private String authGrant; //사실 맵핑 필요 없음 function을 호출해서 코드번호에 맞는 권한값 ADMIN USER를  authority에저장
	private String authority; //사용자 권한
	
	private String phoneNo;
	private String notifyTp;

	private Date regDate;
	private String regId;
	private Date modDate;
    private String modId;
}

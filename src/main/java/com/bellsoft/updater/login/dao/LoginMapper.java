package com.bellsoft.updater.login.dao;


import org.apache.ibatis.annotations.Mapper;

import com.bellsoft.updater.login.domain.AdminUserVO;

@Mapper
public interface LoginMapper {

	AdminUserVO getMemberInfo(String loginId);

}

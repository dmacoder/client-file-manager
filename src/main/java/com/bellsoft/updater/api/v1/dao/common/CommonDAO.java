package com.bellsoft.updater.api.v1.dao.common;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonDAO {

    HashMap<String, Object> callProcInsertPk(HashMap<String, Object> pk);


}

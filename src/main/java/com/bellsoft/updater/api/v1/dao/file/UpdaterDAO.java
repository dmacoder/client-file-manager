package com.bellsoft.updater.api.v1.dao.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.bellsoft.updater.common.CustomParamMap;

@Mapper
public interface UpdaterDAO {

    List<HashMap<String, Object>> selectListClientFiles(CustomParamMap paramMap);

    int insertFile(Map<String, Object> fileMap);

    HashMap<String, Object> selectClientFileByFileId(String fileId);
   
}

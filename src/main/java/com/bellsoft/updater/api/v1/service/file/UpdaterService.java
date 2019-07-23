package com.bellsoft.updater.api.v1.service.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bellsoft.updater.api.v1.domain.file.FimClientFileTb;
import com.bellsoft.updater.common.CustomParamMap;

public interface UpdaterService {

    public List<HashMap<String, Object>> getListClientFiles(CustomParamMap paramMap);


    public int createFile(Map<String, Object> fileMap);

    public HashMap<String, Object> getClientFileByFileId(String fileId);
    
    
    public Page<FimClientFileTb> findClientFileList(Pageable pageable);
    
}

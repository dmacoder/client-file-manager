package com.bellsoft.updater.api.v1.service.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bellsoft.updater.api.v1.dao.file.UpdaterDAO;
import com.bellsoft.updater.common.CustomParamMap;

@Service
public class UpdaterService {
    
    @Autowired
    UpdaterDAO updaterDAO;

    public List<HashMap<String, Object>> getListClientFiles(CustomParamMap paramMap) {
        return updaterDAO.selectListClientFiles(paramMap);
    }

    public int createFile(Map<String, Object> fileMap) {
        return updaterDAO.insertFile(fileMap);
        
    }

    public HashMap<String, Object> getClientFileByFileId(String fileId) {
        return updaterDAO.selectClientFileByFileId(fileId);
    }
    
    
   
    
}
